package nlp4j.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nlp4j.Document;
import nlp4j.Keyword;

/**
 * 自然言語処理対象のドキュメントクラスです。<br>
 * Document class for NLP
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class DefaultDocument implements Document {

	static private final String KEY_ID = "id";
	static private final String KEY_TEXT = "text";

	// 1.1.0 HashMap to LinkedHashMap
	Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	List<Keyword> keywords = Collections.synchronizedList(new ArrayList<Keyword>());

	/**
	 * Default Constructor
	 * 
	 * @since 1.1
	 */
	public DefaultDocument() {
		super();
	}

	@Override
	public void addKeyword(Keyword keyword) {
		keywords.add(keyword);
	}

	@Override
	public void addKeywords(List<Keyword> kwds) {
		this.keywords.addAll(kwds);
	}

	@Override
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	@Override
	public Date getAttributeAsDate(String key) {
		return (Date) this.attributes.get(key);
	}

	@Override
	public Number getAttributeAsNumber(String key) {
		return (Number) this.attributes.get(key);
	}

	@Override
	public List<String> getAttributeKeys() {
		if (attributes == null) {
			return new ArrayList<String>();
		} else {
			List<String> list = new ArrayList<>(attributes.keySet());
			return list;
		}
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
	public List<Keyword> getKeywords(String facet) {

		ArrayList<Keyword> ret = new ArrayList<Keyword>();

		for (Keyword kwd : this.keywords) {
			if (kwd.getFacet() != null && kwd.getFacet().equals(facet)) {
				ret.add(kwd);
			}
		}

		return ret;
	}

	@Override
	public String getText() {
		return (String) attributes.get(KEY_TEXT);
	}

	@Override
	public void putAttribute(String key, Date value) {
		this.attributes.put(key, value);
	}

	@Override
	public void putAttribute(String key, Number value) {
		this.attributes.put(key, value);
	}

	@Override
	public void putAttribute(String key, Object object) {
		this.attributes.put(key, object);
	}

	@Override
	public void putAttribute(String key, String value) {
		this.attributes.put(key, value);
	}

	@Override
	public void remove(String key) {
		this.attributes.remove(key);

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

	@Override
	public boolean removeKeyword(Keyword kwd) {
		return this.keywords.remove(kwd);

	}

	/**
	 * @return 削除されたキーワードが存在するか
	 */
	public boolean removeFlaggedKeyword() {
		boolean b = false;
		Keyword[] kwds = this.keywords.toArray(new Keyword[0]);

		for (Keyword kwd : kwds) {
			if (kwd.getFlag()) {
				this.removeKeyword(kwd);
				b = true;
			}
		}
		return b;
	}

}
