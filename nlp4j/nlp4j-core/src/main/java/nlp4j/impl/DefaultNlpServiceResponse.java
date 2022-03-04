package nlp4j.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
	private ArrayList<Keyword> keywords;
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

	public ArrayList<Keyword> getKeywords() {
		return keywords;
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
	 * @param keywords キーワード
	 */
	public void setKeywords(ArrayList<Keyword> keywords) {
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

	/**
	 * @since 1.3
	 */
	@Override
	public String getMessage() {
		return null;
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
	public Map<String, List<String>> getHeadersAsMultiMap() {
		return headers;
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
	public void setHeaders(String string) {
		this.headersOriginal = string;

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
		} else {
			return null;
		}

	}

}
