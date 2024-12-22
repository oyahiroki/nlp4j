package nlp4j.http;

import java.io.IOException;

/**
 * @since 1.3.7.16
 */
public class HttpException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int responseCode;
	private String url;
	private String message;

	public HttpException(String url, int responseCode) {
		super();
		this.url = url;
		this.responseCode = responseCode;
		this.message = "url: " + url + ", responseCode: " + responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
