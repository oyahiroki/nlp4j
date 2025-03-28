package nlp4j.impl;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.util.DateUtils;

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

	/**
	 * 
	 * @param text Text of this document
	 * @since 1.3.3.0
	 */
	public DefaultDocument(String text) {
		super();
		this.putAttribute("text", text);
	}

	@Override
	public void addKeyword(Keyword keyword) {
		keywords.add(keyword);
	}

	@Override
	public void addKeywords(List<Keyword> kwds) {
		if (kwds == null) {
			return;
		}
		this.keywords.addAll(kwds);
	}

	@Override
	public Object getAttribute(String key) {
		Object o = this.attributes.get(key);

		// Date
		if (o instanceof Date) {
			return DateUtils.toISO8601((Date) o);
		}
		// Number
		else if (o instanceof Number) {
			return o;
		}
		// String
		else if (o instanceof String) {
			return o;
		}
		//
		else {
			return o;
		}

	}

	/**
	 * @param key of attribute
	 * @return value
	 */
	public String getAttributeAsString(String key) {
		Object o = this.attributes.get(key);

		if (o == null) {
			return null;
		}
		// Date
		else if (o instanceof Date) {
			return DateUtils.toISO8601((Date) o);
		}
		// Number
		else if (o instanceof Number) {
			return o.toString();
		}
		// String
		else if (o instanceof String) {
			return (String) o;
		}
		//
		else {
			return o.toString();
		}

	}

	@Override
	public Date getAttributeAsDate(String key) {
		Object o = this.attributes.get(key);

		if (o instanceof Date) {
			return (Date) this.attributes.get(key);
		} //
		else if (o instanceof String) {
			String s = (String) o;
			return DateUtils.toDate(s);
		} //
		else {
			return (Date) this.attributes.get(key);
		}

	}

	@Override
	public Number getAttributeAsNumber(String key) {
		Object o = this.attributes.get(key);
		if (o instanceof Number) {
			return (Number) o;

		}
		//
		else if (o instanceof String) {
			String s = (String) o;

			try {
				return Integer.parseInt(s);
			} catch (Exception e) {
			}

			try {
				return Double.parseDouble(s);
			} catch (Exception e) {

			}
			try {
				return NumberFormat.getInstance().parse(s);
			} catch (ParseException e) {
			}
		} //

		throw new ClassCastException();
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

	/**
	 * <pre>
	 * "id"属性がセットされている場合はその値を返す
	 * "id"属性がセットされていない場合はクラス名+Hash値を返す
	 * CASE ID is set, return value of id
	 * CASE ID is not set, return Class name + Hash value (Object#toString())
	 * </pre>
	 */
	public String getId() {

		if (attributes.get(KEY_ID) != null) {
			Object o = attributes.get(KEY_ID);
			if (o == null) {
				return null;
			} //
			else if (o instanceof String) {
				return (String) o;
			} //
			else {
				return o.toString();
			}
//			return (String) ;
		} else {
			return super.toString();
		}

	}

	@Override
	public List<Keyword> getKeywords() {
		if (keywords != null) {
			return keywords;
		} else {
			// since 20210821
			return new ArrayList<Keyword>();
		}
	}

	@Override
	public List<Keyword> getKeywords(String facet) {

		ArrayList<Keyword> ret = new ArrayList<Keyword>();

		for (Keyword kwd : this.keywords) {
			if (kwd.getFacet() != null && (kwd.getFacet().equals(facet)
					|| (kwd.getFacet().startsWith(facet) && kwd.getFacet().length() > facet.length()
							&& kwd.getFacet().substring(facet.length()).startsWith(".")))) {
				ret.add(kwd);
			}
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Keyword> List<T> getKeywords(Class<T> classOfT) {
		List<T> ret = new ArrayList<T>();
		for (Keyword kwd : this.keywords) {
			if (classOfT.isInstance(kwd)) {
				ret.add((T) kwd);
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

	/**
	 * @param key     of Object
	 * @param object: Object[] will be converted to List<Object> internally
	 */
	public void putAttribute(String key, Object object) {

		// convert Object[] to List
		if (object instanceof Object[]) {
			List<Object> oo = new ArrayList<>();
			Object[] o1 = (Object[]) object;
			for (int n = 0; n < o1.length; n++) {
				oo.add(o1[n]);
			}
			this.attributes.put(key, oo);
		} //
			// 2024-07-15
		else if (object instanceof double[]) {
			List<Double> oo = new ArrayList<>();
			double[] o1 = (double[]) object;
			for (int n = 0; n < o1.length; n++) {
				oo.add(o1[n]);
			}
			this.attributes.put(key, oo);
		} //
		else {
			this.attributes.put(key, object);
		}
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

	@Override
	public List<Object> getAttributeAsList(String key) {
		Object o = attributes.get(key);
		if (o instanceof List<?>) {
			return (List<Object>) o;
		}
		if (o instanceof Object[]) {
			return Arrays.asList((Object[]) o);
		}
		return null;
	}

	@Override
	public List<Number> getAttributeAsListNumbers(String key) {
		Object o = attributes.get(key);
		if (o instanceof Number[]) {
			return Arrays.asList((Number[]) o);
		} //
			// 2024-07-15
		else if (o instanceof List) {
			List<Number> nn = new ArrayList<Number>();
			List<Object> oo = (List) o;
			for (Object obj : oo) {
				if (obj instanceof String) {
					Number n = Double.parseDouble((String) obj);
					nn.add(n);
				} else if (obj instanceof Number) {
					nn.add((Number) obj);
				} else {
					Number n = Double.parseDouble(obj.toString());
					nn.add(n);
				}
			}
			return nn;
		}
		return null;

	}

	@Override
	public void removeKeywords() {
		this.keywords = new ArrayList<Keyword>();
	}

	@Override
	public void changeAttributeKey(String from, String to) {
		Object obj = this.attributes.get(from);
		if (obj == null) {
			return;
		}
		this.attributes.remove(from);
		this.attributes.put(to, obj);
	}

}
