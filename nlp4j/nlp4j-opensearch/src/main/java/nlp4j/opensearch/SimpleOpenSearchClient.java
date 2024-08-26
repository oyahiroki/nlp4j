package nlp4j.opensearch;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;

public class SimpleOpenSearchClient implements Closeable {
	private String endPoint;
	private HttpClient c;
	private String index;

	public SimpleOpenSearchClient(String endPoint, String user, String password, String index) throws Exception {
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
			throw new Exception(e);
		}

		c = (new HttpClientBuilder()).user(user).password(password).protocol(protocol).hostname(host).port(port)
				.build();
		this.index = index;
	}

	public void search(JsonObject requestBody) throws IOException {
		String method = "_search";
		get(method, requestBody);
	}

	public NlpServiceResponse get(String method, JsonObject requestBody) throws IOException {
		String url = this.endPoint + "/" + this.index + "/" + method;
		NlpServiceResponse res = c.get(url, requestBody.toString());
//		System.err.println(res.getOriginalResponseBody());
		return res;
	}

	@Override
	public void close() throws IOException {
		if (c != null) {
			c.close();
		}
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

}
