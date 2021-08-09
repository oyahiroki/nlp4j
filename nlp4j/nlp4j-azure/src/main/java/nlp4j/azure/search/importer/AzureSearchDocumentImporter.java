package nlp4j.azure.search.importer;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.azure.search.AzureSearchClient;
import nlp4j.impl.DefaultResponse;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public class AzureSearchDocumentImporter extends AbstractDocumentImporter implements DocumentImporter {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * API Document :
	 * https://docs.microsoft.com/en-us/rest/api/searchservice/addupdate-or-delete-documents
	 */
	@Override
	public void importDocument(Document doc) throws IOException {

//		if (super.debug == true) {
//			super.debugPrint(doc);
//			return;
//		}

		AzureSearchClient az = new AzureSearchClient(super.props);

		JsonObject requestDocs = new JsonObject();
		{
			JsonArray requestValue = new JsonArray();
			{
				JsonObject requestDoc = DocumentUtil.toJsonObjectForIndex(doc);

				{ // Convert ID to String
					Object idObj = requestDoc.get("id");
					if (idObj != null && (idObj instanceof String) == false) {
						requestDoc.addProperty("id", idObj.toString());
					}
				}

				requestDoc.addProperty("@search.action", "upload");
				requestValue.add(requestDoc);
			}
			requestDocs.add("value", requestValue);
		}

		System.err.println("<request>");
		System.err.println(JsonUtils.prettyPrint(requestDocs));
		System.err.println("</request>");

		if (super.debug == true) {
			return;
		}

		JsonObject res = az.post("index", requestDocs);

		DefaultResponse r = new DefaultResponse();
		r.setMessage(res.get("@message").getAsString());
		r.setResponseCode(res.get("@code").getAsInt());
		r.setOriginalResponseBody(res.toString());

		if (r.getResponseCode() != 200) {
			System.err.println("<response responsecode='" + r.getResponseCode() + "'>");
			System.err.println(res);
			System.err.println("</response>");
			throw new IOException("" + r.getResponseCode() + "," + r.getMessage());
		}

	}

	@Override
	public void commit() throws IOException {
		logger.info("commit");
	}

	@Override
	public void close() {
		logger.info("close");
	}

}
