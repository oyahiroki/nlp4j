package nlp4j.annotator;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordAnnotator;

/**
 * <pre>
 * キーワードのファセットをフィールドにマップする
 * Map keyword facet to Document field
 * 
 * properties example: keyword_facet_field_mapping="wiki.link->word_ss,wiki.rel->word_ss"
 * </pre>
 * 
 * @author Hiroki Oya
 * 
 *
 */
public class KeywordFacetMapToFieldAnnotator extends AbstractDocumentAnnotator
		implements DocumentAnnotator, KeywordAnnotator {

	private String keyword_facet_field_mapping = null;

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		// キーワードのファセットをフィールドにマップする Map keyword facet to Document field
		if ("keyword_facet_field_mapping".equals(key)) {
			this.keyword_facet_field_mapping = value;
		} //
	}

	@Override
	public void annotate(Document doc) throws Exception {
		// キーワードのファセットをフィールドにマップする Map keyword facet to Document field
		if (this.keyword_facet_field_mapping != null) {
			String[] facet_field_maps = this.keyword_facet_field_mapping.split(",");
			for (String facet_field_map : facet_field_maps) {
				String[] map = facet_field_map.split("->");
				if (map.length != 2) {
					continue;
				} //
				else {
					String facet = map[0];
					String field = map[1];
					if (doc.getKeywords(facet) != null && doc.getKeywords(facet).size() > 0) {
						ArrayList<String> kwds = new ArrayList<>();
						for (Keyword kwd : doc.getKeywords(facet)) {
							kwds.add(kwd.getLex());
						}
						doc.putAttribute(field, StringUtils.join(kwds, ","));
					}
				} // END OF IF
			} // END OF FOR
		} // END OF IF

	}

}
