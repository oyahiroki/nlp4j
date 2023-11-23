package nlp4j.tuple;

import junit.framework.TestCase;

public class TripleTestCase extends TestCase {

	public void testOfLMR() {
		Triple<String, String, String> t = Triple.of("aaa", "bbb", "ccc");
		System.err.println(t);
	}

	public void testTripleLMR() {
		Triple<String, String, String> t = new Triple<>("aaa", "bbb", "ccc");
		System.err.println(t);
	}

	public void testGetLeft() {
		Triple<String, String, String> t = new Triple<>("aaa", "bbb", "ccc");
		System.err.println("left: " + t.getLeft());
	}

	public void testGetMiddle() {
		Triple<String, String, String> t = new Triple<>("aaa", "bbb", "ccc");
		System.err.println("middle: " + t.getMiddle());
	}

	public void testGetRight() {
		Triple<String, String, String> t = new Triple<>("aaa", "bbb", "ccc");
		System.err.println("right: " + t.getRight());
	}

}
