package nlp4j.annotator;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.Keyword;

/**
 * Annotator for Numeral (数詞)
 * 
 * @author Hiroki Oya
 * @since 1.1
 *
 */
public class NumeralAnnotator extends AbstractDocumentAnnotator {

	String lexConditionRegex = "^[0-9]*$";

	String sourceFacet = "名詞";
	String targetFacet = "数詞";

	@Override
	public void annotate(Document doc) throws Exception {

		for (Keyword kwd : doc.getKeywords()) {
			if (kwd.getLex() != null && kwd.getLex().matches(lexConditionRegex) && kwd.getFacet() != null
					&& kwd.getFacet().equals(sourceFacet)) {
				kwd.setFacet(targetFacet);
			}
		}

	}

}
