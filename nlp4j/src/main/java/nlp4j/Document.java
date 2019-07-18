package nlp4j;

import java.util.ArrayList;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
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
