package nlp4j.opensearch;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;

public class SimpleOpenSearchClient implements Closeable {
	private String endPoint;
	private HttpClient c;
	private String index;

	public SimpleOpenSearchClient(String endPoint, String user, String password, String index) throws IOException {
		this.endPoint = endPoint;
		String protocol = null;
		String host = null;
		int port = -1;
		try {
			URL ep = new URL(endPoint);
			protocol = ep.getProtocol();
			host = ep.getHost();
			port = ep.getPort();
		} catch (MalformedURLException e) {
			throw new IOException(e);
		}

		c = (new HttpClientBuilder()).user(user).password(password).protocol(protocol).hostname(host).port(port)
				.build();
		this.index = index;
	}

	public NlpServiceResponse analyze(String text) throws IOException {
		final String method = "_analyze";
		JsonObject jo = new JsonObject();
		{
			jo.addProperty("text", text);
		}
		return this.get(method, jo);

	}

	@Override
	public void close() throws IOException {
		if (c != null) {
			c.close();
		}
	}

	public NlpServiceResponse get(String method, JsonObject requestBody) throws IOException {
		String url = this.endPoint + "/" + this.index + "/" + method;
		NlpServiceResponse res = c.get(url, requestBody.toString());
//		System.err.println(res.getOriginalResponseBody());
		return res;
	}

	public NlpServiceResponse post(String method, JsonObject requestBody) throws IOException {
		String url = this.endPoint + "/" + this.index + "/" + method;
		NlpServiceResponse res = c.post(url, requestBody.toString());
		return res;
	}

	public NlpServiceResponse postDoc(JsonObject doc) throws IOException {
		String id = doc.get("id") != null ? doc.get("id").getAsString() : null;
		String method = "_doc";
		if (id != null) {
			method += "/" + id;
		}
		return post(method, doc);

	}

	public void search(JsonObject requestBody) throws IOException {
		String method = "_search";
		get(method, requestBody);
	}

	public NlpServiceResponse search(String field, String value) throws IOException {
		final String method = "_search";
		JsonObject jo = new JsonObject();
		{
			JsonObject query = new JsonObject();
			{
				JsonObject term = new JsonObject();
				term.addProperty(field, value);
				query.add("term", term);
			}
			jo.add("query", query);
		}
		return this.get(method, jo);
	}

	public NlpServiceResponse vector_search(String field_vector, float[] v) throws IOException {

		String method = "_search";
		JsonObject q = new JsonObject();
		{
//			JsonObject query = new JsonObject();
//			{
//				JsonObject script_score = new JsonObject();
//				{
//					{
//						JsonObject _query = (new Gson()).fromJson("{'match_all':{}}", JsonObject.class);
//						script_score.add("query", _query);
//					}
//					{
//						JsonObject script = new JsonObject();
//						{
//							script.addProperty("source",
//									"cosineSimilarity(params.query_vector, doc['" + field_vector + "']) + 1.0");
//							{
//								JsonObject params = new JsonObject();
//								{
//									JsonArray query_vector = new JsonArray();
//									for (int n = 0; n < v.length; n++) {
//										query_vector.add(v[n]);
//									}
//									params.add("query_vector", query_vector);
//								}
//								script.add("params", params);
//							}
//						}
//						script_score.add("script", script);
//					}
//
//				}
//				query.add("script_score", script_score);
//			}
		}
		{
			q.addProperty("size", 10);
			{
				JsonObject query = new JsonObject();
				{
					JsonObject knn = new JsonObject();
					{
						JsonObject v1 = new JsonObject();
						{
							{
								JsonArray vector = new JsonArray();
								for (int n = 0; n < v.length; n++) {
									vector.add(v[n]);
								}
								v1.add("vector", vector);
							}
							{
								v1.addProperty("k", 3);
							}
						}
						knn.add(field_vector, v1);
					}
					query.add("knn", knn);
				}
				q.add("query", query);
			}
		}

		NlpServiceResponse res = this.get(method, q);
		return res;

	}

}
