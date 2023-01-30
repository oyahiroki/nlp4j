package nlp4j.wiki;

import java.util.ArrayList;
import java.util.List;

public class WikiCategory {

	private String title;

	private boolean child;

	private List<WikiCategory> children = new ArrayList<WikiCategory>();

	public WikiCategory(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void addChild(WikiCategory cat) {
		children.add(cat);
	}

	public List<WikiCategory> getChildren() {
		return children;
	}

	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return "WikiCategory [title=" + title + ", children=" + children + "]";
	}

}
