package nlp4j.opensearch.annotator;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordBuilder;
import nlp4j.NlpServiceResponse;
import nlp4j.opensearch.SimpleOpenSearchClient;

/**
 * <pre>
 * endpoint = "https://localhost:9200"
 * user = "admin"
 * password : must be set to SYSTEM_ENV or property
 * index: must be set
 * </pre>
 * 
 * created on: 2024-11-11
 */
public class OpenSearchAnalyzerAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String password;
	private String endpoint = "https://localhost:9200";
	private String user = "admin";
	private String index;

	public OpenSearchAnalyzerAnnotator() {
		super();
		{
			this.password = System.getProperty("OPENSEARCH_PASSWORD");
			if (this.password == null) {
				logger.warn("password is not set.");
			}
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {
		try (SimpleOpenSearchClient client = new SimpleOpenSearchClient(this.endpoint, this.user, this.password,
				this.index);) {
			for (String target : super.targets) {
				String text = doc.getAttributeAsString(target);
				NlpServiceResponse res = client.analyze(text); // analyze
				JsonObject jo = res.getAsJsonObject();
				JsonArray tokens = jo.getAsJsonArray("tokens").getAsJsonArray();
				for (int n = 0; n < tokens.size(); n++) {
					JsonObject token = tokens.get(n).getAsJsonObject();
					int start = token.get("start_offset").getAsInt();
					int end = token.get("end_offset").getAsInt();
					String t = token.get("token").getAsString();
					String t2 = text.substring(start, end);
					String type = token.get("type").getAsString();
					Keyword kwd = (new KeywordBuilder()).begin(start).end(end).facet(type).str(t2).lex(t).build();
					doc.addKeyword(kwd);
				}
			}
		}

	}

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if ("password".equals(key.toLowerCase())) {
			this.password = value;
		} //
		else if ("endpoint".equals(key.toLowerCase())) {
			this.endpoint = value;
		} //
		else if ("user".equals(key.toLowerCase())) {
			this.user = value;
		} //
		else if ("index".equals(key.toLowerCase())) {
			this.index = value;
		}
	}

}
