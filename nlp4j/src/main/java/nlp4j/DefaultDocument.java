package nlp4j;

import java.util.ArrayList;

public interface DefaultDocument {

	String getText();

	void setText(String text);

	ArrayList<Keyword> getKeywords();

	void setKeywords(ArrayList<Keyword> keywords);

}
