package nlp4j.annotator;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordAnnotator;
import nlp4j.impl.DefaultKeyword;

/**
 * Pattern Annotator for Keyword Sequence (語の並び)
 * 
 * @author Hiroki Oya
 * @since 1.2
 *
 */
public class KeywordSequencePatternAnnotator extends AbstractDocumentAnnotator
		implements DocumentAnnotator, KeywordAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void annotate(Document doc) throws Exception {

		for (int n = 0; super.prop.getProperty("rule[" + n + "]") != null; n++) {

			String rule = super.prop.getProperty("rule[" + n + "]");
			String facet = super.prop.getProperty("facet[" + n + "]");
			String value = super.prop.getProperty("value[" + n + "]");

			if (facet == null) {
				logger.warn("facet[" + n + "] : null");
				return;
			}
			if (value == null) {
				logger.warn("value[" + n + "] : null");
				return;
			}

			// TODO value の Validation

			process(doc, facet, rule, value);
		}
	}

	private void process(Document doc, String facet, String rule, String value) {

		Gson gson = new Gson();

		JsonArray arr = gson.fromJson(rule, JsonArray.class);

		ArrayList<Keyword> newkwds = new ArrayList<>();

		List<Keyword> kwds = doc.getKeywords();

		int idxRule = 0;

		int hitBegin = -1;

		boolean hitPrevious = false;

		for (int n = 0; n < kwds.size(); n++) {

			Keyword kwd = kwds.get(n);

			// IF(HIT)
			if (match(kwd, arr.get(idxRule).getAsJsonObject()) == true) {

				if (hitPrevious == false) {
					hitBegin = n;
					hitPrevious = true;
				}

				newkwds.add(kwd);

				idxRule++;

				if (idxRule == arr.size()) {
					String v = getValue(newkwds, value);

					DefaultKeyword kw = new DefaultKeyword();
					kw.setLex(v);
					kw.setStr(v);
					kw.setBegin(newkwds.get(0).getBegin());
					kw.setEnd(newkwds.get(newkwds.size() - 1).getEnd());
					kw.setFacet(facet);
					doc.addKeyword(kw);

					newkwds.clear();
					idxRule = 0;
				}

			} //
				// ELSE(NOT HIT)
			else {
				newkwds.clear();
				idxRule = 0;
				hitPrevious = false;
				if (hitBegin != -1) {
					n = hitBegin + 1;
				}
				hitBegin = -1;
			} // END IF

		}

	}

	private String getValue(ArrayList<Keyword> kwds, String value) {

		Set<String> used = new HashSet<>();

		Pattern pattern = Pattern.compile("\\$\\{.+?}");
		Matcher matcher = pattern.matcher(value);

		while (matcher.find()) {
			// ${値の指定}
			String matched = matcher.group();
			// IF(チェック済みのValue)
			if (used.contains(matched) == false) {
				// like "0.lex"
				String valueElement = matched.substring("${".length(), matched.length() - "}".length());
				// like "0"
				String id = valueElement.split("\\.")[0];
				// like "lex"
				String att = valueElement.split("\\.")[1];

				int idIdx = -1;
				try {
					idIdx = Integer.parseInt(id);
				} catch (NumberFormatException e) {
					logger.error(e.getMessage(), e);
					continue;
				}

				Keyword kwd = kwds.get(idIdx);
				if (kwd == null) {
					logger.warn("value is null for: " + matched);
					continue;
				}

				String kwdValue = kwd.get(att);

				value = value.replace(matched, kwdValue);

				used.add(matched);

			} // END IF
		} // END WHILE

		return value;
	}

	/**
	 * Match<br>
	 * 正規表現に対応(1.3)
	 * 
	 * @param kwd     キーワード
	 * @param jsonObj JSONで指定した値
	 * @return true if matched
	 */
	public boolean match(Keyword kwd, JsonObject jsonObj) {
		for (String key : jsonObj.keySet()) {

			String value1 = jsonObj.get(key).getAsString();
			String value2 = kwd.get(key);

			// 正規表現として扱う
			if (value1 != null && value1.startsWith("/") && value1.endsWith("/") && value1.length() > 2) {
				String regex = value1.substring(1, value1.length() - 1);
				if (value2 == null || value2.matches(regex) == false) {
					return false;
				}
			}
			// 文字列として扱う
			else {
				if (value2 == null || value1.equals(value2) == false) {
					return false;
				}
			}

		}
		return true;
	}

}
