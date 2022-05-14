package nlp4j.pattern;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;

/**
 * created_at: 2022-05-12
 * 
 * @author Hiroki Oya
 *
 */
public class StandardPatternAnnotatorJa extends AbstractDocumentAnnotator implements DocumentAnnotator {

	UserPatternAnnotator ann;

	public StandardPatternAnnotatorJa() {
		super();
		ann = new UserPatternAnnotator();
		ann.setProperty("resource", "nlp4j.pattern/pattern-ja.xml");
		ann.setProperty("resource", "nlp4j.pattern/pattern-ja-sv.xml");
		ann.setProperty("resource", "nlp4j.pattern/pattern-ja-adjnoun.xml");
		ann.setProperty("resource", "nlp4j.pattern/word-ja.xml");
	}

	@Override
	public void annotate(Document doc) throws Exception {
		ann.annotate(doc);
	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if (this.ann != null) {
			ann.setProperty(key, value);
		}
	}

}
