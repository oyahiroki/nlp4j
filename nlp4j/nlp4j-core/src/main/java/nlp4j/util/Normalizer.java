package nlp4j.util;

/**
 * @author Hiroki Oya
 *
 */
public class Normalizer {

	/**
	 * @param s to normalize
	 * @return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFKC);
	 */
	static public String nfkc(String s) {
		return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFKC);
	}

}
