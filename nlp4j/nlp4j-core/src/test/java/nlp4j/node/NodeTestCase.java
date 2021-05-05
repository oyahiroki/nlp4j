package nlp4j.node;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.test.NLP4JTestCase;

/**
 * @author Hiroki Oya
 * @created_at 2021-01-17
 * @since 1.3.1.0
 */
public class NodeTestCase extends NLP4JTestCase {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@SuppressWarnings("javadoc")
	public NodeTestCase() {
		target = Node.class;
	}

	private void printNode(@SuppressWarnings("rawtypes") Node node, String id) {
		System.err.println("<node id='" + id + "'>");
		node.print();
		System.err.println("</node>");
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		logger.info("--- setup ---");
	}

	@SuppressWarnings("javadoc")
	public void testClone001() {
		Node<String> n1 = new Node<String>("a");
		{
			n1.addChildNode(new Node<String>("b"));
			n1.addChildNode(new Node<String>("c"));
		}

		printNode(n1, "original");

		Node<String> n2 = (Node<String>) n1.clone();
		printNode(n2, "clone");

		logger.info("n1 == n2: " + (n1 == n2));
		assertFalse(n1 == n2);

	}

	@SuppressWarnings("javadoc")
	public void testClone002() {
		Node<String> n1 = new Node<String>("a");
		{
			Node<String> n2 = new Node<String>("b");
			n1.addChildNode(n2);

			Node<String> n3 = new Node<String>("c");
			n1.addChildNode(n3);
			{
				Node<String> n4 = new Node<String>("d");
				Node<String> n5 = new Node<String>("e");
				n3.addChildNode(n4);
				n3.addChildNode(n5);

				logger.info("[n1]");
				printNode(n1, "original");

				logger.info("[n3]");
				Node<String> n3_clone = n3.clone();
				printNode(n3_clone, "clone_1");
				assertTrue(n3_clone != n3);

				logger.info("[n5]");
				Node<String> n5_clone = n5.clone();
				printNode(n5_clone, "clone_2");
				assertTrue(n5_clone != n5);
			}

		}

	}

	@SuppressWarnings("javadoc")
	public void testClonePatterns001() throws Exception {
		Node<String> n1 = new Node<String>("a");
		{
			n1.addChildNode(new Node<String>("b"));
			{
				Node<String> n2 = new Node<String>("c");
				n1.addChildNode(n2);
				n2.addChildNode(new Node<String>("d"));
				n2.addChildNode(new Node<String>("e"));
			}
		}
		printNode(n1, "original");
		
		List<Node<String>> list = n1.clonePatterns();

		int idx = 0;
		for (Node<String> o : list) {
			printNode(o, "code_" + idx);
			idx++;
		}

	}

	public void testHasNext001() {
		Node<String> n1 = new Node<String>("a");
		{
			Node<String> n2 = new Node<String>("b");
			n2.addChildNode(new Node<String>("c"));
			n2.addChildNode(new Node<String>("d"));
			n1.addChildNode(n2);
			Node<String> n3 = new Node<String>("e");
			n1.addChildNode(n3);
			Node<String> n4 = new Node<String>("f");
			n3.addChildNode(n4);
			Node<String> n5 = new Node<String>("g");
			n3.addChildNode(n5);
			n1.addChildNode(new Node<String>("h"));
		}
		n1.print();

		Node<String> ptr = n1;

		while (ptr.hasNext()) {
			ptr = ptr.next();
			System.err.println(ptr);
			System.err.println(ptr.hasNext());
		}

	}

	public void testMatch001() {
		Node<String> n1 = new Node<String>("a");
		Node<String> n2 = new Node<String>("b");
		System.err.println(n1.match(n2));
	}

	public void testMatch002() {
		Node<String> n1 = new Node<String>("a");
		Node<String> n2 = new Node<String>("a");
		System.err.println(n1.match(n2));
	}

	public void testMatchAll001() {
		Node<String> n1 = new Node<String>("a");
		{
			n1.addChildNode(new Node<String>("b"));
			n1.addChildNode(new Node<String>("c"));
			n1.print();
		}
		Node<String> cond = new Node<String>("a");
		{
			cond.addChildNode(new Node<String>("b"));
			cond.addChildNode(new Node<String>("c"));
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll0010() {
		Node<String> n1 = new Node<>("a");
		Node<String> cond = new Node<>("a");
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll0011() {
		Node<String> n1 = new Node<String>("a");
		Node<String> cond = new Node<String>("b");
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(false, b);
	}

	public void testMatchAll002() {
		Node<String> n1 = new Node<String>("a");
		{
			n1.addChildNode(new Node<String>("b"));
			n1.addChildNode(new Node<String>("x"));
			n1.print();
		}
		Node<String> cond = new Node<String>("a");
		{
			cond.addChildNode(new Node<String>("b"));
			cond.addChildNode(new Node<String>("c"));
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(false, b);
	}

	public void testMatchAll003() {
		Node<String> n1 = new Node<String>("a");
		{
			n1.addChildNode(new Node<String>("b"));
			n1.addChildNode(new Node<String>("x"));
			n1.addChildNode(new Node<String>("c"));
			n1.print();
		}
		Node<String> cond = new Node<String>("a");
		{
			cond.addChildNode(new Node<String>("b"));
			cond.addChildNode(new Node<String>("c"));
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll004() {
		Node<String> n1 = new Node<String>("a");
		{
			n1.addChildNode(new Node<String>("b")); // 0
			n1.addChildNode(new Node<String>("x")); // 1 <- NEED TO BE SKIPPED
			n1.addChildNode(new Node<String>("x")); // 2 <- NEED TO BE SKIPPED
			n1.addChildNode(new Node<String>("c")); // 3
			n1.addChildNode(new Node<String>("x")); // 4
			System.err.println("<object id='n1'>");
			n1.print();
			System.err.println("</object>");
		}
		Node<String> cond = new Node<String>("a");
		{
			cond.addChildNode(new Node<String>("b")); // 0
			cond.addChildNode(new Node<String>("c")); // 1
			System.err.println("<object id='cond'>");
			cond.print();
			System.err.println("</object>");
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll005() {
		Node<String> n1 = new Node<String>("a");
		{
			Node<String> n2 = new Node<String>("b");
			n1.addChildNode(n2);
			Node<String> n3 = new Node<String>("c");
			n1.addChildNode(n3);
			Node<String> n4 = new Node<String>("d");
			n3.addChildNode(n4);
			Node<String> n5 = new Node<String>("e");
			n3.addChildNode(n5);
			n1.print();
		}
		Node<String> cond = new Node<String>("a");
		{
			Node<String> n2 = new Node<String>("b");
			cond.addChildNode(n2);
			Node<String> n3 = new Node<String>("c");
			cond.addChildNode(n3);
			Node<String> n4 = new Node<String>("d");
			n3.addChildNode(n4);
			Node<String> n5 = new Node<String>("e");
			n3.addChildNode(n5);
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll006() {
		Node<String> n1 = new Node<String>("a");
		{
			Node<String> n2 = new Node<String>("b");
			n1.addChildNode(n2);
			Node<String> n3 = new Node<String>("c");
			n1.addChildNode(n3);
			{
				Node<String> n4 = new Node<String>("d");
				n3.addChildNode(n4);
				Node<String> n5 = new Node<String>("x"); // <- NEED TO BE SKIPPED
				n3.addChildNode(n5);
				Node<String> n6 = new Node<String>("e");
				n3.addChildNode(n6);
			}
		}
		Node<String> cond = new Node<String>("a");
		{
			Node<String> n2 = new Node<String>("b");
			cond.addChildNode(n2);
			Node<String> n3 = new Node<String>("c");
			cond.addChildNode(n3);
			{
				Node<String> n4 = new Node<String>("d");
				n3.addChildNode(n4);
				Node<String> n6 = new Node<String>("e");
				n3.addChildNode(n6);
			}
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll007() {
		Node<String> n1 = new Node<String>("a");
		{
			Node<String> n2 = new Node<String>("b");
			n1.addChildNode(n2);
			Node<String> n3 = new Node<String>("c");
			n1.addChildNode(n3);
			Node<String> n4 = new Node<String>("d");
			n3.addChildNode(n4);
			Node<String> n5 = new Node<String>("e");
			n3.addChildNode(n5);
			n1.addChildNode(new Node<String>("f"));
		}
		System.err.println("<object id='n1'>");
		n1.print();
		System.err.println("</object>");
		Node<String> cond = new Node<String>("a");
		{
			Node<String> n2 = new Node<String>("b");
			cond.addChildNode(n2);
			Node<String> n3 = new Node<String>("c");
			cond.addChildNode(n3);
			Node<String> n4 = new Node<String>("x");
			n3.addChildNode(n4);
			Node<String> n5 = new Node<String>("d");
			n3.addChildNode(n5);
			Node<String> n6 = new Node<String>("e");
			n3.addChildNode(n6);
		}
		System.err.println("<object id='cond'>");
		cond.print();
		System.err.println("</object>");

		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(false, b);
	}

	/**
	 * Test next node
	 * 
	 * <pre>
	 *  [a] 
	 *     [b/]
	 *     [c/]
	 *  [/a]
	 * </pre>
	 */
	public void testNext001() {
		Node<String> n1 = new Node<String>("a");
		{
			n1.addChildNode(new Node<String>("b"));
			{
				Node<String> n2 = new Node<String>("c");
				n1.addChildNode(n2);
				n2.addChildNode(new Node<String>("d"));
				n2.addChildNode(new Node<String>("e"));
			}
		}
		Node<String> ptr = n1;
		System.err.println(ptr);

		while ((ptr = ptr.next()) != null) {
			System.err.println(ptr);
		}

		System.err.println("------");

		ptr = n1;
		ptr.resetIndex();

		while (true) {
			System.err.println(ptr);
			ptr = ptr.next();
			if (ptr == null) {
				break;
			}
		}
	}

	/**
	 * Test next node
	 * 
	 * <pre>
	 *  [a] 
	 *     [b]
	 *         [c/]
	 *         [d/]
	 *     [/b]
	 *     [e]
	 *         [f/]
	 *         [g/]
	 *     [/e]
	 *     [h/]
	 *  [/a]
	 * </pre>
	 */
	public void testNext002() {
		Node<String> n1 = new Node<String>("a");
		{
			Node<String> n2 = new Node<String>("b");
			n2.addChildNode(new Node<String>("c"));
			n2.addChildNode(new Node<String>("d"));
			n1.addChildNode(n2);
			Node<String> n3 = new Node<String>("e");
			n1.addChildNode(n3);
			Node<String> n4 = new Node<String>("f");
			n3.addChildNode(n4);
			Node<String> n5 = new Node<String>("g");
			n3.addChildNode(n5);
			n1.addChildNode(new Node<String>("h"));
		}
		n1.print();

		Node<String> ptr = n1;
		System.err.println(ptr);
		while (true) {
			ptr = ptr.next();
			if (ptr != null) {
				System.err.println(ptr);
			} else {
				break;
			}
		}

	}

	/**
	 * <pre>
	 * [a]
	 *     [b]
	 *         [x/]
	 *         [y/]
	 *         [z/]
	 *     [/b]
	 *     [c/]
	 * [/a]
	 * </pre>
	 * 
	 */
	public void testNextChild001() {
		Node<String> n1 = new Node<>("a");
		{
			Node<String> n2 = new Node<>("b");
			n1.addChildNode(n2);
			{
				Node<String> n2a = new Node<>("x");
				n2.addChildNode(n2a);
				Node<String> n2b = new Node<>("y");
				n2.addChildNode(n2b);
				Node<String> n2c = new Node<>("z");
				n2.addChildNode(n2c);
			}
			Node<String> n3 = new Node<>("c");
			n1.addChildNode(n3);
		}
		{
			assertEquals(true, n1.getValue().equals("a"));
		}
		{
			Node<String> next = n1.nextChild();
			assertEquals(true, next.getValue().equals("b"));
		}
		{
			Node<String> next = n1.nextChild();
			assertEquals(true, next.getValue().equals("c"));
		}
		{
			Node<String> n = n1.nextChild();
			assertNull(n);
		}

	}

	/**
	 * Test Node
	 * 
	 * <pre>
	 * [a]
	 *     [b/]
	 * [/a]
	 * </pre>
	 * 
	 */
	public void testNode001() {
		Node<String> n1 = new Node<>("a");
		{
			Node<String> n2 = new Node<>("b");
			n1.addChildNode(n2);
		}
		printNode(n1, "n1");
		assertEquals("a", n1.getValue());
		assertEquals("b", n1.getChildNode(0).getValue());
	}

	public void testPrint001() {
		Node<String> n1 = new Node<String>("a");
		{
			n1.addChildNode(new Node<String>("b"));
			n1.addChildNode(new Node<String>("c"));
		}
		n1.print();
	}

	public void testRemove001() {
		Node<String> n1 = new Node<>("a");
		{
			Node<String> n2 = new Node<>("b");
			n1.addChildNode(n2);
			{
				Node<String> n2a = new Node<>("x");
				n2.addChildNode(n2a);
				Node<String> n2b = new Node<>("y");
				n2.addChildNode(n2b);
				Node<String> n2c = new Node<>("z");
				n2.addChildNode(n2c);
			}
		}
		{
			Node<String> clone = n1.clone();
			Node<String> removed = clone.removeChild(0);
			System.err.println("<removed>");
			removed.print();
			System.err.println("</removed>");
			System.err.println("<object>");
			clone.print();
			System.err.println("</object>");
		}
	}

	public void testRemove002() {
		Node<String> n1 = new Node<>("a");
		{
			Node<String> n2 = new Node<>("b");
			n1.addChildNode(n2);
			{
				Node<String> n2a = new Node<>("x");
				n2.addChildNode(n2a);
				Node<String> n2b = new Node<>("y");
				n2.addChildNode(n2b);
				Node<String> n2c = new Node<>("z");
				n2.addChildNode(n2c);
			}
		}
		{
			Node<String> clone = n1.clone();
			System.err.println("<object>");
			clone.print();
			System.err.println("</object>");
			Node<String> ptr = clone.getChildNode(0);
			Node<String> removed = ptr.removeChild(0);
			System.err.println("<removed>");
			removed.print();
			System.err.println("</removed>");
			System.err.println("<object>");
			clone.print();
			System.err.println("</object>");

			removed = ptr.removeChild(0);
			System.err.println("<removed>");
			removed.print();
			System.err.println("</removed>");
			System.err.println("<object>");
			clone.print();
			System.err.println("</object>");

			removed = ptr.removeChild(0);
			System.err.println("<removed>");
			removed.print();
			System.err.println("</removed>");
			System.err.println("<object>");
			clone.print();
			System.err.println("</object>");
		}

	}

	public void testToString001() {
		Node<String> n1 = new Node<String>("a");
		Node<String> n2 = new Node<String>("b");
		n1.addChildNode(n2);

		System.err.println(n1.toString());
		System.err.println(n2.toString());
	}

}
