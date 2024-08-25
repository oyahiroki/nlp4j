package nlp4j.http;

public class HttpClientBuilder {

	private String user = null;
	private String password = null;
	private String protocol = null;
	private String hostname = null;
	int port = -1;

	public HttpClient build() {
		if (this.user != null && this.password != null && this.protocol != null && this.hostname != null
				&& this.port != -1) {
			return new HttpClient5(user, password, protocol, hostname, port);
		}
		return new HttpClient5();
	}

	public HttpClientBuilder hostname(String hostname) {
		this.hostname = hostname;
		return this;
	}

	public HttpClientBuilder password(String password) {
		this.password = password;
		return this;
	}

	public HttpClientBuilder port(int port) {
		this.port = port;
		return this;
	}

	public HttpClientBuilder protocol(String protocol) {
		this.protocol = protocol;
		return this;
	}

	public HttpClientBuilder user(String user) {
		this.user = user;
		return this;
	}

	public HttpClientBuilder user_password(String user, String password) {
		this.user = user;
		this.password = password;
		return this;
	}

}
