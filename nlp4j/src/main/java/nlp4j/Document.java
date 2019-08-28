package nlp4j;

import java.util.ArrayList;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface Document {

	void addKeyword(Keyword keyword);

	Object getAttribute(String key);

	String getId();

	ArrayList<Keyword> getKeywords();

	String getText();

	void putAttribute(String key, String value);

	void setId(String id);

	void setKeywords(ArrayList<Keyword> keywords);

	void setText(String text);

}
