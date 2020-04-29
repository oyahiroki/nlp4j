package nlp4j.azure.search.importer;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.azure.search.AzureSearchClient;
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

		JsonObject res = az.post(requestDocs);
		System.err.println(JsonUtils.prettyPrint(res));

	}

	@Override
	public void commit() throws IOException {

	}

	@Override
	public void close() {

	}

}
