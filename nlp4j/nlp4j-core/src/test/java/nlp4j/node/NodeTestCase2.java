package nlp4j.node;

import junit.framework.TestCase;

public class NodeTestCase2 extends TestCase {

	public void testAddChildNode002() {
		Node<Character> n1 = new Node<Character>('a');
		{
			n1.addChildNode(new Node<Character>('b'));
			n1.addChildNode(new Node<Character>('c'));
		}

		String xml = NodeUtils.toXmlString(n1);
		System.err.println(xml);

	}

}
