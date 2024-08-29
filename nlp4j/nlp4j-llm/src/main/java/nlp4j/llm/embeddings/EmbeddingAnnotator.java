package nlp4j.llm.embeddings;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.NlpServiceResponse;
import nlp4j.util.DoubleUtils;

/**
 * <pre>
 * String vector_name = "vector";
 * String endPoint = "http://localhost:8888/";
 * </pre>
 * 
 */
public class EmbeddingAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	private String vector_name = "vector";
	private String endPoint = "http://localhost:8888/";

	static public float[] embedding(String text) throws IOException {
		try {
			Document d = (new DocumentBuilder()).text(text).build();
			DocumentAnnotator ann = new EmbeddingAnnotator();
			ann.setProperty("target", "text");
			ann.setProperty("vector_name", "vector1024");
			ann.setProperty("endPoint", "http://localhost:8888/");
			ann.annotate(d); // vector size is 1024
			return DoubleUtils.toFloatArray(d.getAttributeAsListNumbers("vector1024"));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

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
			long time1 = System.currentTimeMillis();
			logger.info("embedding ... ");
			String text = doc.getAttributeAsString(target);
			EmbeddingServiceViaHttp nlp = new EmbeddingServiceViaHttp(this.endPoint);
			NlpServiceResponse res = nlp.process(text);
			EmbeddingResponse r = (new Gson()).fromJson(res.getOriginalResponseBody(), EmbeddingResponse.class);
//			System.err.println(Arrays.toString(r.getEmbeddings()));
			doc.putAttribute(this.vector_name, r.getEmbeddings());
			long time2 = System.currentTimeMillis();
			logger.info("embedding ... done " + (time2 - time1));
		}
	}

}
