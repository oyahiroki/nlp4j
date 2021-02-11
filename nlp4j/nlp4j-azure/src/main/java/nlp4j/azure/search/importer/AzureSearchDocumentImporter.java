package nlp4j.azure.search.importer;

import java.io.IOException;

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

	/**
	 * API Document :
	 * https://docs.microsoft.com/en-us/rest/api/searchservice/addupdate-or-delete-documents
	 */
	@Override
	public void importDocument(Document doc) throws IOException {

		if (super.debug == true) {
			super.debugPrint(doc);
			return;
		}

		AzureSearchClient az = new AzureSearchClient(super.props);

		JsonObject requestDocs = new JsonObject();
		{
			JsonArray requestValue = new JsonArray();
			{
				JsonObject requestDoc = DocumentUtil.toJsonObjectForIndex(doc);
				requestDoc.addProperty("@search.action", "upload");
				requestValue.add(requestDoc);
			}
			requestDocs.add("value", requestValue);
		}

		System.err.println("To be imported.");
		System.err.println(JsonUtils.prettyPrint(requestDocs));

		if (super.debug == true) {
			return;
		}

		JsonObject res = az.post("index", requestDocs);

		DefaultResponse r = new DefaultResponse();
		r.setMessage(res.get("@message").getAsString());
		r.setResponseCode(res.get("@code").getAsInt());
		r.setOriginalResponseBody(res.toString());

		if (r.getResponseCode() != 200) {
			throw new IOException("" + r.getResponseCode() + "," + r.getMessage());
		}

	}

	@Override
	public void commit() throws IOException {

	}

	@Override
	public void close() {

	}

}
