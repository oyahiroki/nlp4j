package nlp4j.node;

import junit.framework.TestCase;

public class NodeTestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {

		super.setUp();

		System.err.println("--------");
	}

	public void testNode() {
		Node n1 = new Node("a");
		Node n2 = new Node("b");
		n1.addChildNode(n2);

		System.err.println(n1);
	}

	public void testNextChild001() {
		Node n1 = new Node("a");
		Node n2 = new Node("b");
		n1.addChildNode(n2);
		Node n3 = new Node("c");
		n1.addChildNode(n3);

		while (n1.hasNextChild()) {
			Node next = n1.nextChild();
			System.err.println(next);
		}

		Node n = n1.nextChild();
		assertNull(n);

	}

	public void testNext001() {
		Node n1 = new Node("a");
		{
			n1.addChildNode(new Node("b"));
			n1.addChildNode(new Node("c"));
		}

		Node n = n1;
		System.err.println(n);
		while (true) {
			n = n.next();
			if (n != null) {
				System.err.println(n);
			} else {
				break;
			}
		}

	}

	public void testNext002() {
		Node n1 = new Node("a");
		{
			Node n2 = new Node("b");
			n2.addChildNode(new Node("c"));
			n2.addChildNode(new Node("d"));
			n1.addChildNode(n2);
			Node n3 = new Node("e");
			n1.addChildNode(n3);
			Node n4 = new Node("f");
			n3.addChildNode(n4);
			Node n5 = new Node("g");
			n3.addChildNode(n5);
			n1.addChildNode(new Node("h"));
			n1.print();
		}

		Node n = n1;
		System.err.println(n);
		while (true) {
			n = n.next();
			if (n != null) {
				System.err.println(n);
			} else {
				break;
			}
		}

	}

	public void testMatchAll000a() {
		Node n1 = new Node("a");
		Node cond = new Node("a");
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll000b() {
		Node n1 = new Node("a");
		Node cond = new Node("b");
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(false, b);
	}

	public void testMatchAll001() {
		Node n1 = new Node("a");
		{
			n1.addChildNode(new Node("b"));
			n1.addChildNode(new Node("c"));
			n1.print();
		}
		Node cond = new Node("a");
		{
			cond.addChildNode(new Node("b"));
			cond.addChildNode(new Node("c"));
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll002() {
		Node n1 = new Node("a");
		{
			n1.addChildNode(new Node("b"));
			n1.addChildNode(new Node("x"));
			n1.print();
		}
		Node cond = new Node("a");
		{
			cond.addChildNode(new Node("b"));
			cond.addChildNode(new Node("c"));
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(false, b);
	}

	public void testMatchAll003() {
		Node n1 = new Node("a");
		{
			n1.addChildNode(new Node("b"));
			n1.addChildNode(new Node("x"));
			n1.addChildNode(new Node("c"));
			n1.print();
		}
		Node cond = new Node("a");
		{
			cond.addChildNode(new Node("b"));
			cond.addChildNode(new Node("c"));
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll004() {
		Node n1 = new Node("a");
		{
			n1.addChildNode(new Node("b"));
			n1.addChildNode(new Node("x"));
			n1.addChildNode(new Node("x"));
			n1.addChildNode(new Node("c"));
			n1.addChildNode(new Node("x"));
			n1.print();
		}
		Node cond = new Node("a");
		{
			cond.addChildNode(new Node("b"));
			cond.addChildNode(new Node("c"));
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll005() {
		Node n1 = new Node("a");
		{
			Node n2 = new Node("b");
			n1.addChildNode(n2);
			Node n3 = new Node("c");
			n1.addChildNode(n3);
			Node n4 = new Node("d");
			n3.addChildNode(n4);
			Node n5 = new Node("e");
			n3.addChildNode(n5);
			n1.print();
		}
		Node cond = new Node("a");
		{
			Node n2 = new Node("b");
			cond.addChildNode(n2);
			Node n3 = new Node("c");
			cond.addChildNode(n3);
			Node n4 = new Node("d");
			n3.addChildNode(n4);
			Node n5 = new Node("e");
			n3.addChildNode(n5);
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll006() {
		Node n1 = new Node("a");
		{
			Node n2 = new Node("b");
			n1.addChildNode(n2);
			Node n3 = new Node("c");
			n1.addChildNode(n3);
			Node n4 = new Node("d");
			n3.addChildNode(n4);
			Node n5 = new Node("x");
			n3.addChildNode(n5);
			Node n6 = new Node("e");
			n3.addChildNode(n6);
		}
		Node cond = new Node("a");
		{
			Node n2 = new Node("b");
			cond.addChildNode(n2);
			Node n3 = new Node("c");
			cond.addChildNode(n3);
			Node n4 = new Node("d");
			n3.addChildNode(n4);
			Node n6 = new Node("e");
			n3.addChildNode(n6);
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(true, b);
	}

	public void testMatchAll007() {
		Node n1 = new Node("a");
		{
			Node n2 = new Node("b");
			n1.addChildNode(n2);
			Node n3 = new Node("c");
			n1.addChildNode(n3);
			Node n4 = new Node("d");
			n3.addChildNode(n4);
			Node n5 = new Node("e");
			n3.addChildNode(n5);
			n1.addChildNode(new Node("f"));
			n1.print();
		}
		Node cond = new Node("a");
		{
			Node n2 = new Node("b");
			cond.addChildNode(n2);
			Node n3 = new Node("c");
			cond.addChildNode(n3);
			Node n4 = new Node("x");
			n3.addChildNode(n4);
			Node n5 = new Node("d");
			n3.addChildNode(n5);
			Node n6 = new Node("e");
			n3.addChildNode(n6);
			cond.print();
		}
		boolean b = n1.matchAll(cond);
		System.err.println(b);
		assertEquals(false, b);
	}

	public void testPrint001() {
		Node n1 = new Node("a");
		{
			n1.addChildNode(new Node("b"));
			n1.addChildNode(new Node("c"));
		}
		n1.print();
	}

	public void testClone001() {
		Node n1 = new Node("a");
		{
			n1.addChildNode(new Node("b"));
			n1.addChildNode(new Node("c"));
		}
		Node n2 = (Node) n1.clone();
		assertFalse(n1 == n2);
		n1.print();
		n2.print();

	}

	public void testClone002() {
		Node n1 = new Node("a");
		Node n2 = new Node("b");
		Node n3 = new Node("c");
		Node n4 = new Node("d");
		Node n5 = new Node("e");

		n1.addChildNode(n2);
		n1.addChildNode(n3);
		n3.addChildNode(n4);
		n3.addChildNode(n5);

		n1.print();

		Node n = n3.clone();
		n.print();

		Node nn = n5.clone();
		nn.print();

	}

	public void testMatch001() {
		Node n1 = new Node("a");
		Node n2 = new Node("b");
		System.err.println(n1.match(n2));
	}

	public void testMatch002() {
		Node n1 = new Node("a");
		Node n2 = new Node("a");
		System.err.println(n1.match(n2));
	}

	public void testToString() {
		Node n1 = new Node("a");
		Node n2 = new Node("b");
		n1.addChildNode(n2);

		System.err.println(n1.toString());
		System.err.println(n2.toString());
	}

}
