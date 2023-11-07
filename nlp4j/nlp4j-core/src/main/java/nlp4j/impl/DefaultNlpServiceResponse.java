package nlp4j.impl;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import nlp4j.Keyword;
import nlp4j.NlpServiceResponse;

/**
 * 自然言語処理サービスのレスポンスです。<br>
 * Response of NLP Service.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class DefaultNlpServiceResponse implements NlpServiceResponse {

	String originalResponseBody;
	int responseCode = -1;
	private List<Keyword> keywords;
	private Map<String, List<String>> headers;
	private String headersOriginal;

	/**
	 * コンストラクタ
	 */
	public DefaultNlpServiceResponse() {
		super();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param responseCode         HTTPレスポンスコード
	 * @param originalResponseBody オリジナルのレスポンスボディ
	 */
	public DefaultNlpServiceResponse(int responseCode, String originalResponseBody) {
		super();
		this.responseCode = responseCode;
		this.originalResponseBody = originalResponseBody;
	}

	/**
	 * since 2022-03-02
	 * 
	 * @since 1.3.4.0
	 */
	public JsonObject getAsJsonObject() {
//		List<String> values = this.headers.get("content-type");
//		System.err.println(values.toString().contains("json"));

		if (this.headers != null && this.headers.get("content-type").toString().contains("json")) {
			Gson gson = new Gson();
			JsonObject jo = gson.fromJson(this.originalResponseBody, JsonObject.class);
			return jo;
		} //
		else if (this.originalResponseBody != null) {
			try {
				Gson gson = new Gson();
				JsonObject jo = gson.fromJson(this.originalResponseBody, JsonObject.class);
				return jo;
			} catch (JsonSyntaxException e) {
				return null;
			}
		} //
		else {
			return null;
		}
	}

	/**
	 * @return
	 * @since 1.3.7.12 (2023-11-07)
	 */
	public JsonElement getAsJson() {
//		List<String> values = this.headers.get("content-type");
//		System.err.println(values.toString().contains("json"));

		if (this.originalResponseBody != null) {
			try {
				Gson gson = new Gson();
				JsonElement je = gson.fromJson(this.originalResponseBody, JsonElement.class);
				return je;
			} catch (JsonSyntaxException e) {
				return null;
			}
		} //
		else {
			return null;
		}
	}

	/**
	 * since 2022-03-02
	 * 
	 * @since 1.3.4.0
	 */
	public String getHeaders() {
		return this.headersOriginal;
	}

	/**
	 * since 2022-03-02
	 * 
	 * @since 1.3.4.0
	 */
	public Map<String, List<String>> getHeadersAsMultiMap() {
		return headers;
	}

	/**
	 *
	 */
	public List<Keyword> getKeywords() {
		return keywords;
	}

	/**
	 * @since 1.3
	 */
	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public String getOriginalResponseBody() {
		return this.originalResponseBody;
	}

	@Override
	public int getResponseCode() {
		return this.responseCode;
	}

	/**
	 * since 2022-03-02
	 * 
	 * @since 1.3.4.0
	 */
	public void setHeaders(Map<String, List<String>> multimap) {
		this.headers = multimap;
	}

	/**
	 * since 2022-03-02
	 * 
	 * @since 1.3.4.0
	 */
	public void setHeaders(String string) {
		this.headersOriginal = string;

	}

	/**
	 * @param keywords キーワード
	 */
	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;

	}

	/**
	 * @param originalResponseBody レスポンスボディ
	 * @since 1.2.1.0
	 */
	public void setOriginalResponseBody(String originalResponseBody) {
		this.originalResponseBody = originalResponseBody;
	}

	/**
	 * @param responseCode レスポンスコード
	 * @since 1.2.1.0
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @since 1.2.1.0
	 */
	public String toString() {
		return "DefaultNlpServiceResponse [responseCode=" + responseCode + ", originalResponseBody="
				+ originalResponseBody + "]";
	}

	@Override
	public boolean ok() {
		int code = getResponseCode();
		return (code >= 200 && code < 300);
	}

}
