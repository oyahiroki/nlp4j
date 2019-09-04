package nlp4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface KeywordWithDependency extends Keyword {

	public void addChild(KeywordWithDependency keyword);

	public void addChildOnly(KeywordWithDependency keyword);

	public List<KeywordWithDependency> asList();

	public ArrayList<KeywordWithDependency> getChildren();

	public int getDepth();

	public KeywordWithDependency getParent();

	public KeywordWithDependency getParent(int depth);

	public KeywordWithDependency getRoot();

	public int getSequence();

	public boolean hasChild();

	public boolean hasParent();

	public boolean isRoot();

	public void setParent(KeywordWithDependency parent);

	public void setParentOnly(KeywordWithDependency parent);

	public void setSequence(int sequence);

	public String toStringAsDependencyList();

	public String toStringAsDependencyTree();

	public String toStringAsXml();

	Object toStringAsXml(int i);

}
