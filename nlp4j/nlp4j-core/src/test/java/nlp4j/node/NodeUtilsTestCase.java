package nlp4j.node;

import junit.framework.TestCase;

public class NodeUtilsTestCase extends TestCase {

	public void testToXmlString() {
		Node<Character> n1 = new Node<Character>('a');
		{
			n1.addChildNode(new Node<Character>('b'));
			n1.addChildNode(new Node<Character>('c'));
		}

		String xmlString = NodeUtils.toXmlString(n1);

		System.err.println(xmlString);

	}

}
