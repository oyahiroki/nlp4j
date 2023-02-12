package nlp4j.pattern;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;

/**
 * created on 2021-05-03
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class Pattern implements Cloneable {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	String facet;

	KeywordRule keywordRule;

	String lang;

	String value;

	protected Pattern clone() {
		try {
			Pattern c = (Pattern) super.clone();

			c.facet = this.facet;
			c.keywordRule = this.keywordRule.clone();
			c.lang = this.lang;
			c.value = this.value;

			return c;

		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public String getFacet() {
		return facet;
	}

	public KeywordRule getKeywordRule() {
		return keywordRule;
	}

	public String getLang() {
		return lang;
	}

	public String getValue() {
		return value;
	}

	public void setFacet(String facet) {
		this.facet = facet;
	}

//	HashMap<String, Keyword> mapKw = new HashMap<>();

	public Keyword getKeyword(String id) {
		HashMap<String, Keyword> mapKw = new HashMap<>();
		for (KeywordWithDependency kw : keywordRule.asList()) {
			if (kw instanceof KeywordRule) {
				String idKey = ((KeywordRule) kw).getId();
				Keyword k = ((KeywordRule) kw).getHitKeyword();
				if (mapKw.containsKey(idKey)) {
//					throw new RuntimeException("id already exists: " + idKey);
				}
				mapKw.put(idKey, k);
			}
		}
//		return this.mapKw.get(id);
		return mapKw.get(id);
	}

	public void setKeywordRule(KeywordRule keywordRule) {
		this.keywordRule = keywordRule;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Pattern [lang=" + lang + ", facet=" + facet + ", value=" + value + ", keywordRule=" + keywordRule + "]";
	}

}
