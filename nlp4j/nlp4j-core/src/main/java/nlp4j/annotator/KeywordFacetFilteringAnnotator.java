/**
 * 
 */
package nlp4j.annotator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordAnnotator;

/**
 * 
 * reference: Stanford CoreNLP POSタグまとめ
 * https://qiita.com/syunyo/items/2c1ce1d765f46a5c1d72
 * 
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public class KeywordFacetFilteringAnnotator extends AbstractDocumentAnnotator
		implements DocumentAnnotator, KeywordAnnotator {

	ArrayList<String> filter = new ArrayList<String>();

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if (key.equals("filter")) {
			String[] ff = value.split(",");
			filter.addAll(Arrays.asList(ff));
		}

	}

	@Override
	public void annotate(Document doc) throws Exception {

		List<Keyword> kwds = doc.getKeywords();
		ArrayList<Keyword> tobeRemoved = new ArrayList<>();

		for (Keyword kwd : kwds) {
			String facet = kwd.getFacet();

			if (filter.contains(facet) == false) {
				tobeRemoved.add(kwd);
			}
		}

		for (Keyword kwd : tobeRemoved) {
			doc.removeKeyword(kwd);
		}

	}

}
