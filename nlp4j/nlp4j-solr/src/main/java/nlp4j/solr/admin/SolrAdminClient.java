package nlp4j.solr.admin;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.util.JsonUtils;

/**
 * Solr Admin Client
 * 
 * @author Hiroki Oya
 * @created_at 2021-10-21
 */
public class SolrAdminClient {

	String endPoint = "http://localhost:8983/";

	/**
	 * default endpoint "http://localhost:8983/"
	 */
	public SolrAdminClient() {
	}

	/**
	 * @param endPoint like "http://localhost:8983/"
	 */
	public SolrAdminClient(String endPoint) {
		this.endPoint = endPoint;
	}

	public void replicationBackup(String coreName) throws IOException {
		// https://solr.apache.org/guide/8_7/index-replication.html
		// http://_leader_host:port_/solr/_core_name_/replication?command=backup

		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url = this.endPoint + "solr/" + coreName + "/replication?command=backup";
		CloseableHttpResponse response = client.execute(new HttpGet(url));
		String responseBodyString = EntityUtils.toString(response.getEntity());
		Gson gson = new Gson();
		JsonObject responseObj = gson.fromJson(responseBodyString, JsonObject.class);
		System.err.println(responseObj);
	}

	/**
	 * List collections of Solr Cloud
	 * 
	 * @throws IOException
	 */
	public void listCollections() throws IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url = this.endPoint + "solr/admin/collections?action=LIST";
		CloseableHttpResponse response = client.execute(new HttpGet(url));
		String responseBodyString = EntityUtils.toString(response.getEntity());
		Gson gson = new Gson();
		JsonObject responseObj = gson.fromJson(responseBodyString, JsonObject.class);
		System.err.println(JsonUtils.prettyPrint(responseObj));
	}

	/**
	 * List cores of Stand alone Solr
	 * 
	 * @throws IOException
	 */
	public void listCores() throws IOException {
		// http://localhost:8983/solr/admin/cores
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url = this.endPoint + "solr/admin/cores";
		CloseableHttpResponse response = client.execute(new HttpGet(url));
		String responseBodyString = EntityUtils.toString(response.getEntity());
		Gson gson = new Gson();
		JsonObject responseObj = gson.fromJson(responseBodyString, JsonObject.class);
		System.err.println(JsonUtils.prettyPrint(responseObj));
	}

	public static void main(String[] args) throws Exception {
		SolrAdminClient adminClient = new SolrAdminClient();
		{
//			String coreName = "sandbox";
//			adminClient.replicationBackup(coreName);
		}
		{
			adminClient.listCollections();
		}

	}

}
