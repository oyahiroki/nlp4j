package nlp4j;

import java.util.ArrayList;

public class Document implements DefaultDocument {

	String text;

	ArrayList<Keyword> keywords;

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public ArrayList<Keyword> getKeywords() {
		return keywords;
	}

	@Override
	public void setKeywords(ArrayList<Keyword> keywords) {
		this.keywords = keywords;
	}

}



