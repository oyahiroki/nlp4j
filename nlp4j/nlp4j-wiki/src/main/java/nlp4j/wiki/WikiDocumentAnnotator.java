package nlp4j.wiki;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;
import nlp4j.Keyword;
import nlp4j.wiki.util.WikiUtils;

/**
 * <pre>
 * Document の "wikitext" から必要な部分を切り出して "text" にセットする。
 * </pre>
 * 
 * created on 2021-08-10
 * 
 * @author Hiroki Oya
 */
public class WikiDocumentAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

//	 String[] paths = { "=={{ja}}==/==={{noun}}===", "=={{ja}}==/==={{adj}}===", "=={{ja}}==/===和語の漢字表記===" };

	// 取得対象のヘッダー
	List<String> paths = new ArrayList<>();

	/**
	 * <pre>
	 * paths: 取得対象のヘッダーを指定する
	 * 
	 * Example
	 * paths 1: "=={{ja}}==/==={{noun}}===,=={{ja}}==/==={{adj}}==="
	 * paths 2: "=={{ja}}==/==={{noun}}===,=={{ja}}==/==={{adj}}===,=={{ja}}==/===和語の漢字表記==="
	 * </pre>
	 * 
	 * @param key   key of properties
	 * @param value value of properties
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if ("paths".equals(key)) {
			String[] pp = value.split(",");
			paths.addAll(Arrays.asList(pp));
		}

	}

	/**
	 * <pre>
	 * 処理1
	 * "wikitext" からWiki記事のテキストを取り出し、
	 * 特定の部分を切り出して "text" にセットする.
	 * 
	 * 処理2
	 * 
	 * </pre>
	 */
	public void annotate(Document doc) throws Exception {

		// Wiki形式のテキストを取得
		// GET "wikitext"
		String wikiText = doc.getAttributeAsString("wikitext");

		if (wikiText == null || wikiText.isEmpty()) {
			logger.debug("wikitext is empty");
			return;
			// error case
		} //

		{

//			System.err.println("debug: " + wikiText);

			// Wiki形式のテキストをパースする
			WikiItemTextParser parser = new WikiItemTextParser();
			parser.parse(wikiText);

			// Wiki形式のページからNodeを取得する
			WikiPageNode rootWikiPageNode = parser.getRoot();

			// 説明文
			List<String> ttPlainText = new ArrayList<>();

			// 例文
			List<String> ttPlainTextExample = new ArrayList<>();

			// FOR EACH(PATH)
			for (String path : paths) {

				logger.debug("path=" + path);

				List<WikiPageNode> pp = rootWikiPageNode.get(path);

				// FOR EACH WIKINODE
				for (WikiPageNode p : pp) {

					// IF(WIKINODE IS NOT NULL) THEN
					if (p != null) {
						{
							String[] ss = p.getSpells();
							if (ss != null && ss.length > 0) {
//								System.err.println("spells: " + String.join(",", ss));
								ttPlainText.add(String.join(",", ss));
							}
						}

						String[] lines = p.getText().split("\n");

						// FOR EACH(LINE)
						for (String line : lines) {

							logger.debug("line=" + line);

							// 「#」から始まる
							if (line.startsWith("#")) {
								// 例文ではない
								if (line.startsWith("#*") == false) {
									String html = WikiUtils.toHtml(line);
									logger.debug("html=" + html);
									{
										// Wikiリンク情報からリンク先アイテムをキーワードとしてセットする
										List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(html, "wiki.link");
										// ADD KEYWORD TO DOC
										doc.addKeywords(kwds);
									}
									{
										// Wikiリンク情報からリンク先アイテムをキーワードとしてセットする
										List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiText(line, "wiki.link");
										if (kwds.size() > 0) {
											doc.addKeywords(kwds);
										}
									}
									{
										String text = WikiUtils.toPlainText(line);
										logger.debug("text=" + text);
										if (ttPlainText.contains("# " + text) == false) {
											ttPlainText.add("# " + text);
										}
									}
								}
								// 例文
								else {
//									String html = WikiUtils.toHtml(line);
//									logger.debug("html=" + html);
//									{
//										// Wikiリンク情報からリンク先アイテムをキーワードとしてセットする
//										List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(html, "wiki.link");
//										// ADD KEYWORD TO DOC
//										doc.addKeywords(kwds);
//									}
//									{
//										// Wikiリンク情報からリンク先アイテムをキーワードとしてセットする
//										List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiText(line, "wiki.link");
//										if (kwds.size() > 0) {
//											doc.addKeywords(kwds);
//										}
//									}
									{
										String text = WikiUtils.toPlainText(line);
										logger.debug("text=" + text);
										if (ttPlainTextExample.contains("# " + text) == false) {
											ttPlainTextExample.add("# " + text);
										}
									}
								}
							}
							// 同義語・関連語
							else if (path.contains("{{rel}}") || path.contains("{{syn}}")) {
								logger.info("rel");
								String html = WikiUtils.toHtml(line);
								logger.debug("html=" + html);
								// Wikiリンク情報からリンク先アイテムをキーワードとしてセットする
								List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(html, "wiki.rel");
								if (kwds.size() > 0) {
									doc.addKeywords(kwds);
								}
							} //
							else if ("{{wikipedia}}".equals(line)) {
								doc.putAttribute("wikipedia", "true");
							}
							//
							else {
								String html = WikiUtils.toHtml(line);
								logger.debug("html=" + html);
								// Wikiリンク情報からリンク先アイテムをキーワードとしてセットする
								List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(html, "wiki.link");
								if (kwds.size() > 0) {
									doc.addKeywords(kwds);
								}

							}

						} // END OF FOR EACH(LINE)

					} // END OF IF

				} // END OF FOR EACH WIKINODE

			} // END OF FOR EACH(PATH)

			if (ttPlainText != null && ttPlainText.size() > 0) {
				String t = String.join("\n", ttPlainText);
//				System.err.println("<t>");
//				System.err.println(t);
//				System.err.println("</t>");
				doc.putAttribute("text", t);
			}
			if (ttPlainTextExample != null && ttPlainTextExample.size() > 0) {
				String t = String.join("\n", ttPlainTextExample);
//				System.err.println("<t>");
//				System.err.println(t);
//				System.err.println("</t>");
				doc.putAttribute("text_example", t);
			}

		}

	}

}
