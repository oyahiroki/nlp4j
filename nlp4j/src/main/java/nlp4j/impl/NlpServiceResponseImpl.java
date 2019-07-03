package nlp4j.impl;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Keyword;
import nlp4j.NlpServiceResponse;

public class NlpServiceResponseImpl implements NlpServiceResponse {

	String originalResponseBody;
	int responseCode = -1;
	private ArrayList<Keyword> keywords;

	public NlpServiceResponseImpl(int responseCode, String originalResponseBody) {
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

	public void setKeywords(ArrayList<Keyword> keywords) {
		this.keywords = keywords;

	}

}
