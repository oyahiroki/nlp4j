package nlp4j.node.examples;

import nlp4j.node.Node;

@SuppressWarnings("javadoc")
public class NodeExample {

	public static void main(String[] args) {

		Node<String> n1 = new Node<String>("a");
		{
			n1.addChildNode(new Node<String>("b"));
			n1.addChildNode(new Node<String>("c"));
		}

		n1.print();
		// a[0]
		// --b[1]
		// --c[1]

		Node<String> n2 = (Node<String>) n1.clone();

		n2.print();
		// a[0]
		// --b[1]
		// --c[1]

		System.err.println(n1 == n2);
		// false

	}

}
