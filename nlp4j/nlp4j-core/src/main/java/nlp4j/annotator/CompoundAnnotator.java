/**
 * 
 */
package nlp4j.annotator;

import java.util.ArrayList;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public class CompoundAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	String facetTarget = "word.NN";
	String facet = "pattern.NN";

	ArrayList<Keyword> kwds = new ArrayList<>();

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if ("target".equals(key)) {
			this.facetTarget = value;
		}

		if ("facet".equals(key)) {
			this.facet = value;
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {

		ArrayList<Keyword> kwds2 = new ArrayList<>();

		for (Keyword kwd : doc.getKeywords()) {
//			System.err.println(kwd);

			if (facetTarget.equals(kwd.getFacet())) {
				kwds.add(kwd);
			} else {
				if (kwds.size() > 1) {
					extractKeyword(kwds2);
					kwds = new ArrayList<>();
				}
			}

		}

		if (kwds.size() > 1) {
			extractKeyword(kwds2);
			kwds = new ArrayList<>();
		}

		doc.addKeywords(kwds2);

	}

	private void extractKeyword(ArrayList<Keyword> kwds2) {
		for (int n = 0; n < kwds.size() - 1; n++) {
//						System.err.println(kwds.get(n).getLex());

			String s = "";
			for (int m = n; m < kwds.size(); m++) {
				s += kwds.get(m).getLex();
				if (m != n) {
//					System.err.println(s);
					Keyword k = new DefaultKeyword();
					k.setFacet(this.facet);
					k.setLex(s);
					k.setStr(s);
					kwds2.add(k);
				}
			}
//						System.err.println(s);
		}
	}

}
