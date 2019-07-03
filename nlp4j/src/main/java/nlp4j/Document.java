package nlp4j;

import java.util.ArrayList;

public class Document {

	String text;

	ArrayList<Keyword> keywords;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<Keyword> keywords) {
		this.keywords = keywords;
	}

}
