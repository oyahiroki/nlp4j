package nlp4j.opensearch;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;
import nlp4j.util.JsonUtils;
import nlp4j.util.TempFileUtils;

public class SimpleOpenSearchClient implements Closeable {
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
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
		System.err.println(TempFileUtils.print(JsonUtils.prettyPrint(requestBody)).getAbsolutePath());
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
			jo.add("query", query);
			{
				JsonObject term = new JsonObject();
				query.add("term", term);
				term.addProperty(field, value);
			}
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
		{ // ベクトル検索
			q.addProperty("size", 10);
			{
				JsonObject query = new JsonObject();
				q.add("query", query);
				{
					JsonObject knn = new JsonObject();
					query.add("knn", knn);
					{
						JsonObject v1 = new JsonObject();
						knn.add(field_vector, v1);
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
					}
				}
			}
		}

		NlpServiceResponse res = this.get(method, q);
		return res;

	}

	public NlpServiceResponse vector_hybrid_search(String field_vector, float[] v, String field_txt, String value_text)
			throws IOException {

		String method = "_search" + "?" + "search_pipeline=nlp-search-pipeline";
		JsonObject q = new JsonObject();
		{
			// JsonObject query = new JsonObject();
			// {
			// JsonObject script_score = new JsonObject();
			// {
			// {
			// JsonObject _query = (new Gson()).fromJson("{'match_all':{}}",
			// JsonObject.class);
			// script_score.add("query", _query);
			// }
			// {
			// JsonObject script = new JsonObject();
			// {
			// script.addProperty("source",
			// "cosineSimilarity(params.query_vector, doc['" + field_vector + "']) + 1.0");
			// {
			// JsonObject params = new JsonObject();
			// {
			// JsonArray query_vector = new JsonArray();
			// for (int n = 0; n < v.length; n++) {
			// query_vector.add(v[n]);
			// }
			// params.add("query_vector", query_vector);
			// }
			// script.add("params", params);
			// }
			// }
			// script_score.add("script", script);
			// }
			//
			// }
			// query.add("script_score", script_score);
			// }
		}
		{ // ベクトル検索
			q.addProperty("size", 10);
			{
				JsonObject _source = new JsonObject();
				q.add("_source", _source);
				{
					JsonArray exclude = new JsonArray();
					_source.add("exclude", exclude);
					exclude.add("vector1024");
				}
			}
			{
				JsonObject highlight = new JsonObject();
				q.add("highlight", highlight);
				{
					highlight.add("fields", (new Gson()).fromJson("{'text_txt_ja':{}}", JsonObject.class));
				}
			}
			{
				JsonObject query = new JsonObject();
				q.add("query", query);
				{
					JsonObject hybrid = new JsonObject();
					query.add("hybrid", hybrid);
					{
						JsonArray queries = new JsonArray();
						hybrid.add("queries", queries);
						{
							JsonObject term = new JsonObject();
							queries.add(term);
							JsonObject termquery = new JsonObject();
							term.add("term", termquery);
							termquery.addProperty(field_txt
							// "text_txt_ja"
									, value_text
							// "好き"
							);
						}
						{
							JsonObject knnquery = new JsonObject();
							queries.add(knnquery);
							JsonObject knn = new JsonObject();

							knnquery.add("knn", knn);
							{
								JsonObject v1 = new JsonObject();
								knn.add(field_vector, v1);
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
							}

						}
					}

				}
			}
		}

		System.err.println(method);
		System.err.println(TempFileUtils.print(JsonUtils.prettyPrint(q)).getAbsolutePath());

		NlpServiceResponse res = this.get(method, q);
		return res;

	}

}
