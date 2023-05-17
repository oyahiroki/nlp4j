package nlp4j.annotator;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.Keyword;

/**
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public class SimpleAnnotatorPipeline {

	List<DocumentAnnotator> annotators = new ArrayList<>();

	public SimpleAnnotatorPipeline(DocumentAnnotator... annotators) {
		for (DocumentAnnotator ann : annotators) {
			this.annotators.add(ann);
		}
	}

	public void add(DocumentAnnotator ann) {
		this.annotators.add(ann);
	}

	public List<Keyword> getKeywords(String t) throws Exception {
		Document doc = (new DocumentBuilder()).text(t).build();
		for (DocumentAnnotator ann : this.annotators) {
			ann.annotate(doc);
		}
		return doc.getKeywords();
	}

}
