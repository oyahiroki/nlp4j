package nlp4j;

import java.util.ArrayList;

public interface Dependency {
	public void addChild(KeywordWithDependency keyword);

	public ArrayList<KeywordWithDependency> getChildren();

	public void setParent(KeywordWithDependency parent);

	public KeywordWithDependency getParent();

	public boolean hasChild();

	public boolean hasParent();

	public boolean isRoot();

	public int getDepth();

	public KeywordWithDependency getRoot();

}
