package nlp4j.azure.search.admin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Azure Search のインデックス定義をダウンロード、アップロード、削除する
 * https://docs.microsoft.com/en-us/rest/api/searchservice/index-operations
 *
 */
public class AzureSearchIndexAdmin {
	private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	/**
	 * Azure Search のインデックス定義を削除する
	 * 
	 * @param adminKey    admin-key
	 * @param serviceName service name
	 * @param indexName   index name
	 * @throws IOException 例外発生時
	 */
	public void delete(String adminKey, String serviceName, String indexName) throws IOException {

		String endPoint = "https://" + serviceName + ".search.windows.net/indexes/" + indexName
				+ "?api-version=2019-05-06";

		Request request = new Request.Builder() //
				.url(endPoint) //
				.delete() //
				.addHeader("api-key", adminKey) //
				.build();

		OkHttpClient client = new OkHttpClient();

		try (Response response = client.newCall(request).execute()) {
			int responseCode = response.code();
			String responseBody = response.body().string();

			if (responseCode == 200) {
				System.err.println(
						"200 is returned for a successful response, meaning that all items have been stored durably and will start to be indexed.");
			} //
			else if (responseCode == 201) {
				System.err.println("201 (for newly uploaded documents)");
			} //
				// 削除された
			else if (responseCode == 204) {
				System.err.println("204 (No Content)");
			} // すでに削除されている
			else if (responseCode == 404) {
				System.err.println("404 (Not Found)");
			} //
			else {
				System.err.println(responseCode);
			}

		}
	}

	/**
	 * Azure Search のインデックス定義をダウンロードする
	 * 
	 * @param adminKey
	 * @param serviceName
	 * @param indexName
	 * @param outFileName
	 * @throws IOException
	 */
	public void get(String adminKey, String serviceName, String indexName, String outFileName) throws IOException {

		File outFile = new File(outFileName);

		String outFileName1 = outFile.getName();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");

		String outFileName2 = outFileName1.substring(0, outFileName1.lastIndexOf(".")) + "_" + sdf.format(new Date())
				+ ".json";

		File outFile2 = new File(outFile.getParent(), outFileName2);

		String endPoint = "https://" + serviceName + ".search.windows.net/indexes/" + indexName
				+ "?api-version=2019-05-06";

		Request request = new Request.Builder() //
				.url(endPoint) //
				.get() //
				.addHeader("api-key", adminKey) //
				.build();

		OkHttpClient client = new OkHttpClient();

		try (Response response = client.newCall(request).execute()) {
			int responseCode = response.code();
			String responseBody = response.body().string();

			if (responseCode == 200) {
				System.err.println(
						"200 is returned for a successful response, meaning that all items have been stored durably and will start to be indexed.");

				FileUtils.write(outFile, responseBody, "UTF-8", false);
				System.err.println("out1: " + outFile.getAbsolutePath());

				FileUtils.write(outFile2, responseBody, "UTF-8", false);
				System.err.println("out2: " + outFile2.getAbsolutePath());

			} else if (responseCode == 201) {
				System.err.println("201 (for newly uploaded documents)");
			} else {
				System.err.println(responseCode);
			}

		}
	}

	/**
	 * Azure Search のインデックス定義をアップロードする
	 * 
	 * @param adminKey
	 * @param serviceName
	 * @param inFile
	 * @throws IOException
	 */
	public void post(String adminKey, String serviceName, File inFile) throws IOException {

		String json = FileUtils.readFileToString(inFile, "UTF-8");

		String endPoint = "https://" + serviceName + ".search.windows.net" + "/indexes" + "?api-version=2019-05-06";

		OkHttpClient client = new OkHttpClient();

		RequestBody body = RequestBody.create(JSON, json);

		Request request = new Request //
				.Builder() //
						.url(endPoint) //
						.post(body) //
						.addHeader("Content-Type", "application/json") //
						.addHeader("api-key", adminKey) //
						.build();

		try (Response response = client.newCall(request).execute()) {

			int responseCode = response.code();
			String originalResponseBody = response.body().string();

			if (responseCode == 200) {
				System.err.println(
						"200 is returned for a successful response, meaning that all items have been stored durably and will start to be indexed.");
			}
			// アップロードされた
			else if (responseCode == 201) {
				System.err.println("201 (for newly uploaded documents)");
			}
			// すでに存在している
			else if (responseCode == 400) {
				System.err.println("400 (Bad request)");
			} else {
				System.err.println(responseCode);
			}

		}
	}

}
