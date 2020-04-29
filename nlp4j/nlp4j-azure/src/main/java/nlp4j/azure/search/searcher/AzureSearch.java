package nlp4j.azure.search.searcher;

import java.io.IOException;

import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentSearcher;
import nlp4j.DocumentSearcher;
import nlp4j.azure.search.AzureSearchClient;
import nlp4j.util.JsonUtils;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public class AzureSearch extends AbstractDocumentSearcher implements DocumentSearcher {

	/**
	 * @param requestObj リクエストオブジェクト
	 * @throws IOException 例外発生時
	 */
	public JsonObject search(JsonObject requestObj) throws IOException {

		AzureSearchClient az = new AzureSearchClient(props);

		JsonObject res = az.post(requestObj);

		System.err.println(JsonUtils.prettyPrint(res));

		return res;

	}

}
