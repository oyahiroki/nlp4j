package nlp4j;

public interface DocumentAnnotatorPipeline extends DocumentAnnotator {

	void add(DocumentAnnotator annotator) throws Exception;

}
