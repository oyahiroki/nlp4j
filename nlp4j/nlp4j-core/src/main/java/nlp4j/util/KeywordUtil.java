package nlp4j.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;

/**
 * @author Hiroki Oya
 *
 */
public class KeywordUtil {

	/**
	 * @param kwd
	 * @return xml
	 */
	static public String toXml(KeywordWithDependency kwd) {
		return kwd.toStringAsXml();
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

}
