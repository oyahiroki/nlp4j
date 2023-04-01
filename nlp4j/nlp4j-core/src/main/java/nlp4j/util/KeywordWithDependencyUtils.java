package nlp4j.util;

import java.util.List;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.KeywordWithDependencyParser;

/**
 * <pre>
 * Extract Keyword with dependency
 * </pre>
 * 
 * created on 2023-04-02
 * 
 * @author Hiroki Oya
 *
 */
public class KeywordWithDependencyUtils {

	static public List<Keyword> extract(KeywordWithDependency kw) {
		return KeywordWithDependencyParser.parse(kw);
	}

}
