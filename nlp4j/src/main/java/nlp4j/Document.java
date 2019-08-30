package nlp4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface Document {

	void addKeyword(Keyword keyword);

	Object getAttribute(String key);

	String getId();

	List<Keyword> getKeywords();

	String getText();

	void putAttribute(String key, String value);

	void setId(String id);

	void setKeywords(List<Keyword> keywords);

	void setText(String text);

}
