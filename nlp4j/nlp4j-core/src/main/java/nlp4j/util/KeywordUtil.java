package nlp4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;

/**
 * @author Hiroki Oya
 *
 */
public class KeywordUtil {

	/**
	 * @param kwd to be converted to XML
	 * @return xml
	 */
	static public String toXml(KeywordWithDependency kwd) {
		return kwd.toStringAsXml();
	}

	/**
	 * Parse Keywords from XML file
	 * 
	 * @param xmlFile XML File
	 * @return Parsed Keywords
	 * @throws IOException on ERROR
	 */
	static public List<KeywordWithDependency> fromXml(File xmlFile) throws IOException {
		return fromXml(new FileInputStream(xmlFile));
	}

	/**
	 * Parse Keywords from XML file
	 * 
	 * @param xmlIs InputStream of XML File
	 * @return Parsed Keywords
	 * @throws IOException on ERROR
	 */
	static public List<KeywordWithDependency> fromXml(InputStream xmlIs) throws IOException {
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

			SAXParser saxParser = saxParserFactory.newSAXParser();

			KeywordHandler handler = new KeywordHandler();

			saxParser.parse(xmlIs, handler);

			List<KeywordWithDependency> kwds = handler.getKeywords();

			return kwds;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * TODO: implement Method
	 * 
	 * @param <T>
	 * @param xml
	 * @param classofT
	 * @return
	 */
	static public <T extends Keyword> List<T> fromXml(String xml, Class<T> classofT) {

		return null;
	}

	static public class Builder<T extends Keyword> {

		T kwd;

		public <T> T newKeyword(Class<T> classOfT) {
			Class<?>[] parameterTypes = {};
			try {
				Constructor<T> cst = classOfT.getConstructor(parameterTypes);

				Object[] args = {};

				return cst.newInstance(args);

			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		public Builder(Class<T> classOfT) {
			T kwd = this.newKeyword(classOfT);
			this.classOfT = classOfT;
			this.kwd = kwd;
		}

		Class classOfT;

		public Builder newKwd() {
			T kwd = (T) this.newKeyword(this.classOfT);
			this.kwd = kwd;
			return this;
		}

		public Builder(T kwd) {
			this.kwd = kwd;
		}

		public Builder<T> lex(String lex) {
			this.kwd.setLex(lex);
			return this;
		}

		public Builder<T> str(String str) {
			this.kwd.setStr(str);
			return this;
		}

		public Builder<T> facet(String facet) {
			this.kwd.setFacet(facet);
			return this;
		}

		public Builder<T> upos(String upos) {
			this.kwd.setUPos(upos);
			return this;
		}

		public Builder<T> begin(int begin) {
			this.kwd.setBegin(begin);
			return this;
		}

		public Builder<T> end(int end) {
			this.kwd.setEnd(end);
			return this;
		}

		public <T extends Keyword> T build() {
			return (T) kwd;
		}

	}

	/**
	 * Convert KeywordWithDependency to List of Keyword
	 * 
	 * @param kw to be Listed
	 * @return List of keyword
	 */
	static public List<Keyword> toKeywordList(KeywordWithDependency kw) {
		ArrayList<Keyword> ret = new ArrayList<>();
		ret.add(kw);
		for (KeywordWithDependency k : kw.getChildren()) {
			toKeywordList(k, ret);
		}
		ret.sort(new Comparator<Keyword>() {
			@Override
			public int compare(Keyword o1, Keyword o2) {
				return (o1.getBegin() - o2.getBegin());
			}
		});
		return ret;
	}

	static private void toKeywordList(KeywordWithDependency kw, ArrayList<Keyword> ret) {
		ret.add(kw);
		for (KeywordWithDependency k : kw.getChildren()) {
			toKeywordList(k, ret);
		}
	}

	/**
	 * @param kwds
	 * @return
	 * @since 1.3.3.0.20211227
	 */
	static public String toLexString(List<Keyword> kwds) {
		return String.join(",", toLexList(kwds));
	}

	/**
	 * @param kwds
	 * @return
	 * @since 1.3.6.1
	 */
	static public List<String> toLexList(List<Keyword> kwds) {
		List<String> ss = new ArrayList<String>();
		for (Keyword kwd : kwds) {
			String lex = kwd.getLex();
			ss.add("" + lex);
		}
		return ss;
	}

	/**
	 * @param doc
	 * @return
	 * @since 1.3.6.1
	 */
	static List<String> toLexList(Document doc) {
		if (doc == null || doc.getKeywords() == null) {
			return new ArrayList<>();
		} else {
			return KeywordUtil.toLexList(doc.getKeywords());
		}

	}

}
