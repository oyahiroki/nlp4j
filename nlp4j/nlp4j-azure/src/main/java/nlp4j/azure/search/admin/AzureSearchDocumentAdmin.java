package nlp4j.azure.search.admin;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentSearcher;
import nlp4j.DocumentSearcher;
import nlp4j.azure.search.AzureSearchClient;
import nlp4j.azure.search.searcher.AzureSearch;
import nlp4j.util.JsonUtils;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public class AzureSearchDocumentAdmin extends AbstractDocumentSearcher implements DocumentSearcher {

	/**
	 * 削除
	 * 
	 * @param requestObj リクエストオブジェクト
	 * @return response from Azure Search
	 * @throws IOException 例外発生時
	 */
	public JsonObject searchDelete(JsonObject requestObj) throws IOException {

		AzureSearch searcher = new AzureSearch();
		searcher.setProperties(super.props);

		JsonObject res = searcher.search(requestObj);
		JsonObject res2 = new JsonObject();

		System.err.println(JsonUtils.prettyPrint(res));

		{
			JsonArray value = res.get("value").getAsJsonArray();

			if (value.size() == 0) {
				System.err.println("Document Not found");
				return res;
			}

			JsonArray value2 = new JsonArray();

			for (int n = 0; n < value.size(); n++) {
				JsonObject v = value.get(n).getAsJsonObject();
				JsonObject v2 = v.deepCopy();
				value2.add(v2);
				for (String k : v.keySet()) {
					if (k.equals("id") == false) {
						v2.remove(k);
					}
				}
			}
			res2.add("value", value2);
		}

		System.err.println(JsonUtils.prettyPrint(res2));

		delete(res2);

		return null;

	}

	/**
	 * API document
	 * https://docs.microsoft.com/en-us/rest/api/searchservice/addupdate-or-delete-documents
	 * 
	 * @param requestObj リクエストボディ
	 * @throws IOException 例外発生時
	 */
	public void delete(JsonObject requestObj) throws IOException {

		JsonArray arr = requestObj.get("value").getAsJsonArray();

		for (int n = 0; n < arr.size(); n++) {
			JsonObject o = arr.get(n).getAsJsonObject();
			o.addProperty("@search.action", "delete");
		}

		System.err.println("request delete");
		System.err.println(JsonUtils.prettyPrint(requestObj));

		AzureSearchClient az = new AzureSearchClient(props);

		JsonObject res = az.post("index", requestObj);

		System.err.println("delete");
		System.err.println(JsonUtils.prettyPrint(res));

	}

}
