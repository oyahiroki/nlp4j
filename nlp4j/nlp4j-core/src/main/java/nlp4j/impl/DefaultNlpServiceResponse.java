package nlp4j.impl;

import java.util.ArrayList;

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

	/**
	 * @param responseCode         HTTPレスポンスコード
	 * @param originalResponseBody オリジナルのレスポンスボディ
	 */
	public DefaultNlpServiceResponse(int responseCode, String originalResponseBody) {
		super();
		this.responseCode = responseCode;
		this.originalResponseBody = originalResponseBody;
	}

	@Override
	public String getOriginalResponseBody() {
		return this.originalResponseBody;
	}

	@Override
	public int getResponseCode() {
		return this.responseCode;
	}

	public ArrayList<Keyword> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords キーワード
	 */
	public void setKeywords(ArrayList<Keyword> keywords) {
		this.keywords = keywords;

	}

}
