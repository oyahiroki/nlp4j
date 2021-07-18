package nlp4j.azure.search;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.InvalidPropertyException;
import nlp4j.util.JsonUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Hiroki Oya
 * @since 1.3
 */
public class AzureSearchClient {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

//	private String endPoint;
	private String service_name;
	private String index_name;
	private String admin_key;
	static private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	/**
	 * https://%s.search.windows.net/indexes/%s/docs/search?api-version=2019-05-06
	 */
	static public final String baseURL = "https://%s.search.windows.net/indexes/%s/docs/%s?api-version=2019-05-06";

	/**
	 * @param props contains "service_name","index_name","admin_key"
	 */
	public AzureSearchClient(Properties props) {

//		endPoint = props.getProperty("endpoint");
		this.service_name = props.getProperty("service_name");
		this.index_name = props.getProperty("index_name");
		this.admin_key = props.getProperty("admin_key");

//		if (endPoint == null) {
//			throw new RuntimeException("ENDPOINT is not set");
//		}

		if (service_name == null) {
			throw new InvalidPropertyException("service_name", service_name);
		}
		if (index_name == null) {
			throw new InvalidPropertyException("index_name", index_name);
		}
		if (admin_key == null) {
			throw new InvalidPropertyException("admin_key", admin_key);
		}

	}

	/**
	 * @param action     "index" or "search"
	 * @param requestObj リクエストボディ
	 * @return 削除リクエスト結果
	 * @throws IOException 例外発生時
	 */
	public JsonObject post(String action, JsonObject requestObj) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String url = String.format(baseURL, this.service_name, this.index_name, action);

		String json = requestObj.toString();

		RequestBody body = RequestBody.create(JSON, json); // new

		Request request = new Request.Builder() //
				.url(url) //
				.addHeader("api-key", this.admin_key) // 忘れると403
				.post(body) //
				.build();

		Response response = client.newCall(request).execute();

		logger.info("Response");
		logger.info(response.code());
		logger.debug(response.message());

		String responsebody = response.body().string();

		logger.debug(JsonUtils.prettyPrint(responsebody));

		Gson gson = new Gson();

		JsonObject res = gson.fromJson(responsebody, JsonObject.class);

		res.addProperty("@code", response.code());
		res.addProperty("@message", response.message());

		return res;
	}

}
