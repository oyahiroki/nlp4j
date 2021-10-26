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

/**
 * Document の "wikitext" から必要な部分を切り出して "text" にセットする。
 * 
 * @author Hiroki Oya
 * @created_at 2021-08-10
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

			List<String> tt = new ArrayList<>();

			// FOR EACH(PATH)
			for (String path : paths) {

				List<WikiPageNode> pp = rootWikiPageNode.get(path);

				// FOR EACH WIKINODE
				for (WikiPageNode p : pp) {

					// IF(WIKINODE IS NOT NULL) THEN
					if (p != null) {
						{
							String[] ss = p.getSpells();
							if (ss != null && ss.length > 0) {
//								System.err.println("spells: " + String.join(",", ss));
								tt.add(String.join(",", ss));
							}
						}

						String[] lines = p.getText().split("\n");

						for (String line : lines) {
							if (line.startsWith("#") && (line.startsWith("#*") == false)) {

//								System.err.println("<line>");
//								System.err.println(line);
//								System.err.println("</line>");

								String html = WikiUtils.toHtml(line);

//								System.err.println("<html>");
//								System.err.println(html);
//								System.err.println("</html>");

								// Wikiリンク情報からリンク先アイテムをキーワードとしてセットする
								List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(html);

								// ADD KEYWORD TO DOC
								doc.addKeywords(kwds);

								{
									String text = WikiUtils.toPlainText(line);
//									System.err.println("line: " + line);
//									System.err.println("text: " + text);
									if (tt.contains("# " + text) == false) {
										tt.add("# " + text);
									}
								}

							}
							//
							else if (line.startsWith("#*") == false) {
//								System.err.println("例文?: " + line);
							} //
							else {

							}

						}
//						System.err.println("</page>");

					} // END OF IF

				} // END OF FOR EACH WIKINODE

			} // END OF FOR EACH(PATH)

			if (tt != null && tt.size() > 0) {
				String t = String.join("\n", tt);
//				System.err.println("<t>");
//				System.err.println(t);
//				System.err.println("</t>");
				doc.putAttribute("text", t);
			}

		}

	}

}
