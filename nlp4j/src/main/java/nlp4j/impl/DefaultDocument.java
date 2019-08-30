package nlp4j.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nlp4j.Document;
import nlp4j.Keyword;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class DefaultDocument implements Document {

	static private final String KEY_ID = "id";
	static private final String KEY_TEXT = "text";

	Map<String, Object> attributes = new HashMap<String, Object>();

	List<Keyword> keywords = new ArrayList<Keyword>();

	@Override
	public void addKeyword(Keyword keyword) {
		keywords.add(keyword);
	}

	@Override
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	@Override
	public String getId() {

		if (attributes.get(KEY_ID) != null) {
			return (String) attributes.get(KEY_ID);
		} else {
			return super.toString();
		}

	}

	@Override
	public List<Keyword> getKeywords() {
		return keywords;
	}

	@Override
	public String getText() {
		return (String) attributes.get(KEY_TEXT);
	}

	@Override
	public void putAttribute(String key, String value) {
		this.attributes.put(key, value);
	}

	@Override
	public void setId(String id) {
		attributes.put(KEY_ID, id);
	}

	@Override
	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	@Override
	public void setText(String text) {
		attributes.put(KEY_TEXT, text);
	}

	@Override
	public String toString() {
		return "Document [attributes=" + attributes + ", keywords=" + keywords + "]";
	}

}
