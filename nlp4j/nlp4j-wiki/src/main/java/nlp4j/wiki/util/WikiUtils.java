package nlp4j.wiki.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.example.TextConverter;

import info.bliki.wiki.model.WikiModel;
import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;
import nlp4j.wiki.template.WikiTemplateNormalizer;

public class WikiUtils {

	/**
	 * <pre>
	 * セクションヘッダーの正規化に用いるマップ
	 * 例:「=={{L|ja}}==」→「=={{ja}}==」
	 * </pre>
	 */
	static private final Map<String, String> mapForNormalize = new HashMap<String, String>();

	static {
		// key: before normailze, value: after normalized
		mapForNormalize.put("=={{L|ja}}==", "=={{ja}}=="); // 日本語
		mapForNormalize.put("=={{jpn}}==", "=={{ja}}=="); // 日本語
		mapForNormalize.put("== {{jpn}} ==", "=={{ja}}=="); // 日本語
		mapForNormalize.put("== {{ja}} ==", "=={{ja}}=="); // 日本語
		mapForNormalize.put("==日本語==", "=={{ja}}=="); // 日本語
		mapForNormalize.put("== 日本語 ==", "=={{ja}}=="); // 日本語
		mapForNormalize.put("名詞", "{{noun}}"); // 名詞
		mapForNormalize.put("形容動詞", "{{adjectivenoun}}"); // 形容動詞
		mapForNormalize.put("動詞", "{{verb}}"); // 動詞

		mapForNormalize.put("成句", "{{idiom}}"); // 成句
		mapForNormalize.put("関連語", "{{rel}}"); // 関連語
		mapForNormalize.put("同義語", "{{syn}}"); // 同義語
		mapForNormalize.put("複合語", "{{comp}}"); // 複合語
		mapForNormalize.put("熟語", "{{prov}}"); // 複合語
	}

	static String splitter = "(:|・)";

	static private String REGEX_JA_WAGOKANJI = "\\{\\{ja-wagokanji\\|(.*?)\\}\\}";
	static private Pattern p_wagokanji = Pattern.compile(REGEX_JA_WAGOKANJI);

	static private String REGEX_WAGOKANJI_OF = "\\{\\{wagokanji of\\|(.*?)\\}\\}";
	static private Pattern p_wagokanji_of = Pattern.compile(REGEX_WAGOKANJI_OF);

	static private String REGEX_ALTERNATIVE_FORM_OF = "\\{\\{alternative form of\\|ja\\|(.*?)\\}\\}";
	static private Pattern p_alternative_form_of = Pattern.compile(REGEX_ALTERNATIVE_FORM_OF);

	/**
	 * HTMLからリンクを抽出し、表記をキーワードとして返す
	 * 
	 * @param html
	 * @return {facet:"wiki.link",lex:"title"}
	 */
	static public List<Keyword> extractKeywordsFromWikiHtml(String html, String keywordFacet) {

		ArrayList<Keyword> keywords = new ArrayList<>();

		org.jsoup.nodes.Document document = Jsoup.parse(html);

		// System.out.println(document.text());

		// ol > li
//		{
//			Elements elements = document.select("ol > li");
//
//			if (elements.size() > 0) {
//				String text = elements.get(0).text();
//				if (text.indexOf("。") != -1) {
//					String text0 = text;
//					text = text.substring(0, text.indexOf("。"));
//				}
//				Elements els = elements.get(0).select("a[title]");
//				for (int n = 0; n < els.size(); n++) {
//					String title = els.get(n).attr("title");
//					String t = els.get(n).text();
//					if (title.startsWith("Template") == false) {
//						// System.err.println("\t" + title + " (" + t + ")");
//						// System.err.println("\t" + t);
//						Keyword kwd = new DefaultKeyword();
//						kwd.setLex(title);
//						kwd.setStr(t);
//						kwd.setFacet(keywordFacet);
//						keywords.add(kwd);
//					}
//				}
//
//			}
//		}

		{
			Elements elements = document.select("a");
			for (int idx = 0; idx < elements.size(); idx++) {
				org.jsoup.nodes.Element el = elements.get(idx);
				String title = el.attr("title");
				if (title.startsWith("Template") == false) {
					Keyword kwd = new DefaultKeyword();
					kwd.setLex(title);
					kwd.setStr(title);
					kwd.setFacet(keywordFacet);
					keywords.add(kwd);
				}
			}
		}

		return keywords;

	}

	public static List<Keyword> extractKeywordsFromWikiText(String text, String keywordFacet) {
		List<Keyword> kwds = new ArrayList<>();
		{ // 「ja-wagokanji」テンプレート
			Matcher m = p_wagokanji.matcher(text);
			if (m.find()) {
				String lex = m.group(1);
				Keyword kwd = new DefaultKeyword(keywordFacet, lex);
				kwds.add(kwd);
			}

		}
		{ // 「ja-wagokanji of」テンプレート
			Matcher m = p_wagokanji_of.matcher(text);
			if (m.find()) {
				String lex = m.group(1);
				Keyword kwd = new DefaultKeyword(keywordFacet, lex);
				kwds.add(kwd);
			}

		}
		{ // p_alternative_form_of
			Matcher m = p_alternative_form_of.matcher(text);
			if (m.find()) {
				String lex = m.group(1);
				Keyword kwd = new DefaultKeyword(keywordFacet, lex);
				kwds.add(kwd);
			}
		}

		return kwds;
	}

	static public String[] extractSpells(String header) {

		// "=== {{verb}}・往 ===" -> "==={{verb}}・往==="
		header = header.replace(" ", "");

		// "==={{verb}}・往==="
		// ^idx1 ^idx2
		int idx1 = header.indexOf("}}");
		if (idx1 != -1) {
			int idx2 = header.indexOf("==", idx1);
			if (idx2 != -1 && (idx1 + 2) != idx2) {
				String spell = header.substring(idx1 + 2, idx2);

				ArrayList<String> list = new ArrayList<>();

				String[] ss = spell.split(splitter);
				for (String s : ss) {
					if (s.trim().isEmpty() == false) {
//						System.err.println(s);
						list.add(s);
					}
				}
				return list.toArray(new String[0]);
			} //
			else {
				return null;
			}
		} //
		else {
			return null;
		}

	}

	static public void getTags(String wikiText) {
		if (wikiText.contains("==[[漢字]]==")) {
			System.err.println("漢字");
		}
	}

	static public String normailzeHeader(String header) {

		// "=== {{verb}}・往 ===" -> "==={{verb}}・往==="
		header = header.replace(" ", "");

		{ // "==={{verb}}・往===" -> "==={{verb}}==="
			int idx2 = header.indexOf("}}");
			if (idx2 != -1) {
				int idx3 = header.indexOf("==", idx2);
				if (idx3 != -1) {
					header = header.substring(0, idx2 + 2) + header.substring(idx3);
				}
			}

		}

		if (mapForNormalize.containsKey(header)) {
			return mapForNormalize.get(header);
		}

		return header;
	}

	static public String normailzeHeaderPath(String headerPath) {
		if (headerPath == null) {
			return null;
		} else {
			for (String k : new ArrayList<>(mapForNormalize.keySet())) {
				headerPath = headerPath.replace(k, mapForNormalize.get(k));
			}
			return headerPath;
		}
	}

	/**
	 * @param wikiText in Wiki Format
	 * @return HTML converted from Wiki Text
	 * @throws Exception on Error
	 */
	static public String toHtml(String wikiText) throws Exception {
		// info.bliki.wiki.model.WikiModel
		WikiModel model = new WikiModel("", "");
		model.setUp();
		String html;
		try {
			html = model.render(wikiText);
			return html;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Wiki形式のテキストをPlain Textに変換する
	 * 
	 * <pre>
	 * [変換前]
	 * #なんらかの[[教育]]が体系的に行われる[[組織]]又はその[[設備]]。[[学舎]]、[[まなびや]]。
	 * [変換後]
	 * なんらかの教育が体系的に行われる組織又はその設備。学舎、まなびや。
	 * [pre-conversion]
	 * # {{lb|en|US|Canada}} An [[institution]] dedicated to [[teaching]] and [[learning]]; an [[educational]] institution.
	 * [post-conversion]
	 * An institution dedicated to teaching and learning; an educational institution.
	 * </pre>
	 * 
	 * @param wikiText
	 * @return
	 */
	static public String toPlainText(String wikiText) {

		// 20220618 テンプレートの正規化
		wikiText = WikiTemplateNormalizer.normalize(wikiText);

		// Set-up a simple wiki configuration
		WikiConfig config = DefaultConfigEnWp.generate();

		try {
			final int wrapCol = 1000;

			// Retrieve a page
			PageTitle pageTitle = PageTitle.make(config, wikiText);

			PageId pageId = new PageId(pageTitle, -1);

			// Instantiate a compiler for wiki pages
			WtEngineImpl engine = new WtEngineImpl(config);

			// Compile the retrieved page
			EngProcessedPage cp = engine.postprocess(pageId, wikiText, null);

			TextConverter p = new TextConverter(config, wrapCol);
			String text = (String) p.go(cp.getPage());

			return text;

		} catch (Exception e) {
			return null;
		}
	}

}