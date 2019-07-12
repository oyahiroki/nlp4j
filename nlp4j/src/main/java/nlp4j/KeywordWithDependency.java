package nlp4j;

public interface KeywordWithDependency extends Keyword, Dependency {

	public String toStringAsDependencyTree();

	public String toStringAsDependencyList();

	public String toStringAsXml();

	Object toStringAsXml(int i);
}
