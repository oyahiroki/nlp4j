package nlp4j;

/**
 * @author Hiroki Oya
 * @since 1.0
 */
public interface DocumentAnnotatorPipeline extends DocumentAnnotator {

	void add(DocumentAnnotator annotator) throws Exception;

}
