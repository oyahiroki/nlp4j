package nlp4j;

import java.util.Date;

import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

/**
 * <pre>
 * Document builder class
 * </pre>
 * 
 * created on 2021-11-16
 * 
 * @author Hiroki Oya
 * @since 1.3.2
 */
public class DocumentBuilder implements Builder<Document> {

	Document doc;

	/**
	 * Document Builder of NLP4J
	 */
	public DocumentBuilder() {
		doc = new DefaultDocument();
	}

	/**
	 * Document Builder of NLP4J
	 * 
	 * @param classOfT target class of Created Document
	 */
	@SuppressWarnings("deprecation")
	public DocumentBuilder(Class<? extends Document> classOfT) {
		try {
			doc = classOfT.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * put value on the document.
	 * 
	 * @param key  of doc
	 * @param date value
	 * @return Builder
	 */
	public DocumentBuilder put(String key, Date date) {
		doc.putAttribute(key, date);
		return this;
	}

	/**
	 * put value on the document.
	 * 
	 * @param key    of doc
	 * @param number value
	 * @return Builder
	 */
	public DocumentBuilder put(String key, Number number) {
		doc.putAttribute(key, number);
		return this;
	}

	/**
	 * put value on the document.
	 * 
	 * @param key of doc
	 * @param obj value
	 * @return Builder
	 */
	public DocumentBuilder put(String key, Object obj) {
		doc.putAttribute(key, obj);
		return this;
	}

	/**
	 * put value on the document.
	 * 
	 * @param key of doc
	 * @param s   value
	 * @return Builder
	 */
	public DocumentBuilder put(String key, String s) {
		doc.putAttribute(key, s);
		return this;
	}

	/**
	 * <pre>
	 * put value on the document.
	 * "v" is short name of "put".
	 * </pre>
	 * 
	 * @param key  of doc
	 * @param date value
	 * @return Builder
	 */
	public DocumentBuilder v(String key, Date date) {
		put(key, date);
		return this;
	}

	/**
	 * <pre>
	 * put value on the document.
	 * "v" is short name of "put".
	 * </pre>
	 * 
	 * @param key    of doc
	 * @param number value
	 * @return Builder
	 */
	public DocumentBuilder v(String key, Number number) {
		put(key, number);
		return this;
	}

	/**
	 * <pre>
	 * put value on the document.
	 * "v" is short name of "put".
	 * </pre>
	 * 
	 * @param key of doc
	 * @param obj value
	 * @return Builder
	 */
	public DocumentBuilder v(String key, Object obj) {
		put(key, obj);
		return this;
	}

	/**
	 * <pre>
	 * put value on the document.
	 * "v" is short name of "put".
	 * </pre>
	 * 
	 * @param key of doc
	 * @param s   value
	 * @return Builder
	 */
	public DocumentBuilder v(String key, String s) {
		put(key, s);
		return this;
	}

	/**
	 * @param facet of keyword
	 * @param lex   of keyword
	 * @since 1.3.4.0.20200203
	 * @return this
	 */
	public DocumentBuilder kw(String facet, String lex) {
		Keyword kw = new DefaultKeyword(facet, lex);
		this.doc.addKeyword(kw);
		return this;
	}

	/**
	 * @return Created Document
	 */
	public Document create() {
		return this.doc;
	}

	/**
	 * Same as create()
	 * 
	 * @return Created Document
	 */
	public Document build() {
		return this.doc;
	}

	/**
	 * "c" is short name of "create"
	 * 
	 * @return Created Document
	 */
	public Document c() {
		return create();
	}

}
