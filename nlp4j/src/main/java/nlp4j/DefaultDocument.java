package nlp4j;

import java.util.ArrayList;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface DefaultDocument {

	String getText();

	void setText(String text);

	ArrayList<Keyword> getKeywords();

	void setKeywords(ArrayList<Keyword> keywords);

}
