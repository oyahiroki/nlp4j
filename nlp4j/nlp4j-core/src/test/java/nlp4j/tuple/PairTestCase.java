package nlp4j.tuple;

import junit.framework.TestCase;

/**
 * @author Hiroki Oya
 * @since 1.3.7.12
 */
public class PairTestCase extends TestCase {

	public void testOf() {
		Pair<String, String> p = Pair.of("aaa", "xxx");
		System.err.println(p.toString());
	}

	public void testPairLR() {
		Pair<String, String> p = new Pair<>("aaa", "xxx");
		System.err.println(p.toString());
	}

	public void testGetLeft() {
		Pair<String, String> p = new Pair<>("aaa", "xxx");
		System.err.println("left: " + p.getLeft());
	}

	public void testGetRight() {
		Pair<String, String> p = new Pair<>("aaa", "xxx");
		System.err.println("right: " + p.getRight());
	}

	public void testSetValue() {
		Pair<String, String> p = new Pair<>("aaa", "xxx");
		try {
			p.setValue("aaa");
		} catch (UnsupportedOperationException e) {
			return;
		}
		fail();
	}

}
