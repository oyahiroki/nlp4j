package nlp4j.wiki.category;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WikiCategory {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String title;
	private String id;

	private boolean child;

	private List<WikiCategory> children = new ArrayList<WikiCategory>();
	private List<WikiCategory> parents = new ArrayList<WikiCategory>();

	public WikiCategory(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public WikiCategory(String title) {
		this.title = title;
	}

	public void addChild(WikiCategory cat) {
		children.add(cat);
	}

	public void addParent(WikiCategory cat) {
		parents.add(cat);
	}

	public List<WikiCategory> getChildren() {
		return children;
	}

	public String getId() {
		return id;
	}

	public List<WikiCategory> getParents() {
		return parents;
	}

	public String getTitle() {
		return title;
	}

	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstRootCategories() {
		return String.join(",", getFirstRootCategoriesAsList());
	}

	public List<String> getFirstRootCategoriesAsList() {
		Set<String> roopCheck = new LinkedHashSet<String>();
		getFirstRoot(roopCheck);
		return new ArrayList<String>(roopCheck);
	}

	public String getRootCategories() {
		return String.join(",", getRootCategoriesAsList());
	}

	public List<String> getRootCategoriesAsList() {
		Set<String> roopCheck = new LinkedHashSet<String>();
		getRoot(roopCheck);
		return new ArrayList<String>(roopCheck);
	}

	public String getRoot() {
		Set<String> roopCheck = new LinkedHashSet<String>();
		return getRoot(roopCheck);
	}

	/**
	 * @return 親が複数あるときに最初のカテゴリだけをたどる
	 */
	public String getFirstRoot() {
		Set<String> roopCheck = new HashSet<String>();
		return getFirstRoot(roopCheck);
	}

	private String getFirstRoot(Set<String> roopCheck) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.title);
		roopCheck.add(this.title);

		if (this.parents != null) {
			if (this.parents.size() > 0) {
				if (this.parents.get(0) == null) {
					logger.debug("Parent is null: " + this.title + ",index: " + 0);
					return sb.toString();
				}
				String parentTitle = this.parents.get(0).getTitle();
				if (roopCheck.contains(parentTitle)) {
					sb.append("/*" + parentTitle);
					return sb.toString();
				} else {
					sb.append("/");
					sb.append(this.parents.get(0).getFirstRoot(roopCheck));
				}
			} //
			else {
				logger.debug("Root Category: " + this.title);
			}
		}
		return sb.toString();
	}

	private String getRoot(Set<String> roopCheck) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.title);
		roopCheck.add(this.title);

		if (this.parents != null) {
//			if (this.parents.size() == 1) {
//				if (this.parents.get(0) == null) {
//					logger.debug("Parent is null: " + this.title + ",index: " + 0);
//					return sb.toString();
//				}
//				String parentTitle = this.parents.get(0).getTitle();
//				// IF(既出)
//				if (roopCheck.contains(parentTitle)) {
//					sb.append("/*" + parentTitle);
//					return sb.toString();
//				}
//				// ELSE(既出ではない)
//				else {
//					sb.append("/");
//					sb.append(this.parents.get(0).getRoot(roopCheck));
//				}
//			} //
//			else 
			if (this.parents.size() >= 0) {
				sb.append("/");
				sb.append("[");

				for (int n = 0; n < this.parents.size(); n++) {
					if (this.parents.get(n) == null) {
						logger.debug("Parent is null: " + this.title + ",index: " + n);
						continue;
					}
					String parentTitle = this.parents.get(n).getTitle();
					System.err.println("子:" + this.title + " -> 親:" + parentTitle);

					// IF(既出)
					if (roopCheck.contains(parentTitle)) {
						sb.append("/*" + parentTitle);
						return sb.toString();
					}
					// ELSE(既出ではない)
					else {
						if (n > 0) {
							sb.append(",");
						}
						sb.append("[" + n + "]" + this.parents.get(n).getRoot(roopCheck));
					}

				}
				sb.append("]");
			}
			// ROOT CATEGORY
			else {

				logger.debug("Root Category: " + this.title);
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "WikiCategory [title=" + title + ", id=" + id + ", child=" + child + ", children=" + children
				+ ", parents=" + parents + "]";
	}

}
