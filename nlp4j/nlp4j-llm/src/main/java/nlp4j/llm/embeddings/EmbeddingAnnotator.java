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
 * String vector_fieldname = "vector";
 * String endPoint = "http://localhost:8888/";
 * </pre>
 * 
 */
public class EmbeddingAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
//	static private String vector_name = "vector1024";
	static private String vector_fieldname = System.getenv("EMBEDDING_FIELD") == null ? "vector"
			: System.getenv("EMBEDDING_ENDPOINT");
	static private String EMBEDDING_ENDPOINT = System.getenv("EMBEDDING_ENDPOINT") == null ? "http://localhost:8888/"
			: System.getenv("EMBEDDING_ENDPOINT");

	static public float[] embedding(String text) throws IOException {
		try {
			Document d = (new DocumentBuilder()).text(text).build();
			EmbeddingAnnotator ann = new EmbeddingAnnotator();
			{
				ann.setProperty("target", "text");
				ann.setProperty("vector_name", vector_fieldname);
				ann.setProperty("endPoint", EMBEDDING_ENDPOINT);
				ann.annotate(d); // vector size is 1024
			}
			return DoubleUtils.toFloatArray(d.getAttributeAsListNumbers(vector_fieldname));
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
	public void annotate(Document doc) throws Exception {
		for (String target : targets) {
			long time1 = System.currentTimeMillis();
			logger.info("embedding ... ");
			String text = doc.getAttributeAsString(target);
			EmbeddingServiceViaHttp nlp = new EmbeddingServiceViaHttp(EMBEDDING_ENDPOINT);
			NlpServiceResponse res = nlp.process(text);
			EmbeddingResponse r = (new Gson()).fromJson(res.getOriginalResponseBody(), EmbeddingResponse.class);
//			System.err.println(Arrays.toString(r.getEmbeddings()));
			doc.putAttribute(vector_fieldname, r.getEmbeddings());
			long time2 = System.currentTimeMillis();
			logger.info("embedding ... done " + (time2 - time1));
		}
	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("vector_name".equals(key)) {
			vector_fieldname = value;
		} //
		else if ("endPoint".equals(key)) {
			EMBEDDING_ENDPOINT = value;
		}
	}

	@Override
	public String toString() {
		return "EmbeddingAnnotator ["

				+ "vector_fieldname=" + vector_fieldname + ", EMBEDDING_ENDPOINT=" + EMBEDDING_ENDPOINT
				+ ", super.toString()=" + super.toString() + "]";
	}

}
