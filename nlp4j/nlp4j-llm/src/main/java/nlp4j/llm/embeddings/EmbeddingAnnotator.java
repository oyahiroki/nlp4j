package nlp4j.llm.embeddings;

import com.google.gson.Gson;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.NlpServiceResponse;

/**
 * <pre>
 * String vector_name = "vector";
 * String endPoint = "http://localhost:8888/";
 * </pre>
 * 
 */
public class EmbeddingAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	private String vector_name = "vector";
	private String endPoint = "http://localhost:8888/";

	/**
	 * <pre>
	 * String vector_name = "vector";
	 * String endPoint = "http://localhost:8888/";
	 * </pre>
	 */
	public EmbeddingAnnotator() {
		super();
	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("vector_name".equals(key)) {
			this.vector_name = value;
		} //
		else if ("endPoint".equals(key)) {
			this.endPoint = value;
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {
		for (String target : targets) {
			String text = doc.getAttributeAsString(target);
			EmbeddingServiceViaHttp nlp = new EmbeddingServiceViaHttp(this.endPoint);
			NlpServiceResponse res = nlp.process(text);
			EmbeddingResponse r = (new Gson()).fromJson(res.getOriginalResponseBody(), EmbeddingResponse.class);
//			System.err.println(Arrays.toString(r.getEmbeddings()));
			doc.putAttribute(this.vector_name, r.getEmbeddings());
		}
	}

}
