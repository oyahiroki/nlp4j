package nlp4j.node;

import java.io.File;

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

	public void printTest001() {
		Node<Character> n1 = new Node<Character>('a');
		{
			n1.addChildNode(new Node<Character>('b'));
			n1.addChildNode(new Node<Character>('c'));
		}

		NodeUtils.print(System.out, n1, "");
	}

	public void printTest002() throws Exception {
		Node<Character> n1 = new Node<Character>('a');
		{
			n1.addChildNode(new Node<Character>('b'));
			n1.addChildNode(new Node<Character>('c'));
		}

		File tempFile = File.createTempFile("nlp4j", ".txt");
		System.err.println(tempFile.getAbsolutePath());

		NodeUtils.print(tempFile, n1);
	}
}
