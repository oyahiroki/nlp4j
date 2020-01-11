package nlp4j.util;

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

}
