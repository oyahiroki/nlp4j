package nlp4j.wiki;

import junit.framework.TestCase;

public class WikiPageNodeTestCase extends TestCase {

	public void test001() throws Exception {
		WikiPageNode pageNode = new WikiPageNode();
		pageNode.setHeader("=={{ja}}==");
	}

	public void testAddChild() {
	}

	public void testAddText() {
	}

	public void testGetChildren() {
	}

	public void testGetHeader() {
	}

	public void testGetLevel() {
	}

	public void testGetParent() {
	}

	public void testGetParentByLevel() {
	}

	public void testGetText() {
	}

	public void testSetHeader() {
	}

	public void testSetLevel() {
	}

	public void testSetParent() {
	}

	public void testMatchHeader() {
		WikiPageNode pageNode = new WikiPageNode();
		pageNode.setHeader("=={{ja}}==");

		boolean b = pageNode.matchHeader(".*\\{\\{ja\\}\\}.*");
		System.err.println(b);
	}

	public void testContainsHeader() {
		WikiPageNode pageNode = new WikiPageNode();
		pageNode.setHeader("=={{ja}}==");

		boolean b = pageNode.containsHeader("{{ja}}");
		System.err.println(b);
	}

	public void testToString() {
	}

}
