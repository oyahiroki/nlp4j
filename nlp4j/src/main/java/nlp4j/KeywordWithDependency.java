package nlp4j;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface KeywordWithDependency extends Keyword, Dependency {

	public String toStringAsDependencyTree();

	public String toStringAsDependencyList();

	public String toStringAsXml();

	Object toStringAsXml(int i);
}
