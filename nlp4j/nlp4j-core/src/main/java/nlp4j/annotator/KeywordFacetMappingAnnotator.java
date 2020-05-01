/**
 * 
 */
package nlp4j.annotator;

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
public class KeywordFacetMappingAnnotator extends AbstractDocumentAnnotator
		implements DocumentAnnotator, KeywordAnnotator {

	HashMap<String, String> facetMap = new LinkedHashMap<String, String>();

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if (key.equals("mapping")) {
			String[] maps = value.split(",");

			for (String map : maps) {
				String[] ss = map.split("->");
				if (ss.length != 2) {
					continue;
				}
				String from = ss[0];
				if (from.isEmpty()) {
					continue;
				}
				String to = ss[1];
				facetMap.put(from, to);
			}
		}

	}

	@Override
	public void annotate(Document doc) throws Exception {

		List<Keyword> kwds = doc.getKeywords();

		for (Keyword kwd : kwds) {
			String facet = kwd.getFacet();

//			System.err.println(facet);

			if (facetMap.containsKey(facet)) {
				String facet2 = facetMap.get(facet);
				kwd.setFacet(facet2);
			}
		}

	}

}
