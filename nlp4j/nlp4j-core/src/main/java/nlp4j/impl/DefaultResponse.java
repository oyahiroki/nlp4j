package nlp4j.impl;

import nlp4j.Response;

/**
 * @author Hiroki Oya
 * @since 1.3
 */
public class DefaultResponse implements Response {

	String originalResponseBody;
	int responseCode;
	String message;

	/**
	 * 
	 */
	public DefaultResponse() {
		super();
	}

	/**
	 * @param responseCode
	 * @param message
	 * @param originalResponseBody
	 */
	public DefaultResponse(int responseCode, String message, String originalResponseBody) {
		super();
		this.responseCode = responseCode;
		this.message = message;
		this.originalResponseBody = originalResponseBody;
	}

	/**
	 * @param originalResponseBody
	 */
	public void setOriginalResponseBody(String originalResponseBody) {
		this.originalResponseBody = originalResponseBody;
	}

	/**
	 * @param responseCode
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getOriginalResponseBody() {
		return this.originalResponseBody;
	}

	@Override
	public int getResponseCode() {
		return this.responseCode;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
