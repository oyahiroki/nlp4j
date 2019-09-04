package nlp4j;

import java.util.ArrayList;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface KeywordWithDependency extends Keyword {

	public void addChild(KeywordWithDependency keyword);

	public void addChildOnly(KeywordWithDependency keyword);

	public ArrayList<KeywordWithDependency> getChildren();

	public int getDepth();

	public KeywordWithDependency getParent();

	public KeywordWithDependency getRoot();

	public boolean hasChild();

	public boolean hasParent();

	public boolean isRoot();

	public void setParent(KeywordWithDependency parent);

	public void setParentOnly(KeywordWithDependency parent);

	public String toStringAsDependencyTree();

	public String toStringAsDependencyList();

	public String toStringAsXml();

	Object toStringAsXml(int i);
}

