package nlp4j.w3c;

import java.io.IOException;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import junit.framework.TestCase;

public class NodeUtilsTestCase extends TestCase {

	public void testForEachNodes001() throws Exception {
		// クローンを返す
		boolean returnClone = true;
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		NodePrinter.print(w3Doc);
		for (Node node : NodeUtils.forEachNodes(w3Doc, returnClone)) {
			System.err.println("---foreachnodes");
			System.err.println(NodeUtils.toString(node));
		}
	}

	public void testForEachNodes002() throws Exception {
		// 参照を返す
		boolean returnClone = false;
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		NodePrinter.print(w3Doc);
		for (Node node : NodeUtils.forEachNodes(w3Doc, returnClone)) {
			System.err.println(Integer.toHexString(node.hashCode()));
		}
	}

	public void testForEachPatternNodes001() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		System.err.println("---[original]---");
		NodePrinter.print(w3Doc);
		List<Node> list = NodeUtils.forEachPatternNodes(w3Doc);
		for (Node node : list) {
			System.err.println("---patternnodes");
			NodePrinter.print(node);
		}
	}

	public void testForEachPatternNodes2_001() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		System.err.println("---[original]---");
		NodePrinter.print(w3Doc);
		List<Node> list = NodeUtils.forEachPatternNodes2(w3Doc);
		for (Node node : list) {
			System.err.println("---patternnodes");
			NodePrinter.print(node);
		}
	}

	public void testGetElementSize001() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		System.err.println("---[original]---");
		NodePrinter.print(w3Doc);
		int size = NodeUtils.getElementSize(w3Doc);
		assertEquals(5, size);
	}

	public void testGetElementSize002() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		System.err.println("---[original]---");
		NodePrinter.print(w3Doc);
		int size = NodeUtils.getElementSize(w3Doc);
		assertEquals(3, size);
	}

	public void testGetElementSize003() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		System.err.println("---[original]---");
		NodePrinter.print(w3Doc);
		int size = NodeUtils.getElementSize(w3Doc);
		assertEquals(1, size);
	}

	public void testDeepestUpper001() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		NodePrinter.print(w3Doc);
		Node ptr = NodeUtils.getDeepestUpperElement(w3Doc);
		NodePrinter.print(ptr);
	}

	public void testDeepestUpper002() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		NodePrinter.print(w3Doc);
		Node ptr = NodeUtils.getDeepestUpperElement(w3Doc);
		NodePrinter.print(ptr);
	}

	public void testDeepestUpper003() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		NodePrinter.print(w3Doc);
		Node ptr = NodeUtils.getDeepestUpperElement(w3Doc);
		NodePrinter.print(ptr);
	}

	public void testToTringAsSingleNode001() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		String s = NodeUtils.toStringAsSingleNode(w3Doc);
		System.err.println(s);
	}

	public void testToTring001() throws Exception {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"	<w id='5' relation=\"nsubj\" />\n" + //
				"</w>";//
		Document w3Doc = DocumentUtil.parse(xmlString);
		String s = NodeUtils.toString(w3Doc);
		System.err.println(s);
	}

	public void testGetFirstElementChild001() throws IOException {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//

		Document w3Doc = DocumentUtil.parse(xmlString);

		NodePrinter.print(w3Doc);

		System.err.println("---");

		Node node = w3Doc.getFirstChild();

		NodePrinter.print(node);

		// removeChild
		node.removeChild(NodeUtils.getFirstElementChild(node));

		System.err.println("---");

		NodePrinter.print(node);

	}

	public void testRemoveFirstElementChild001() throws IOException {
		String xmlString = "<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"	<w id='2' relation=\"nsubj\">\n" + //
				"		<w id='3' relation=\"nsubj\" />\n" + //
				"		<w id='4' relation=\"nsubj\" />\n" + //
				"	</w>\n" + //
				"</w>";//

		Document w3Doc = DocumentUtil.parse(xmlString);

		NodePrinter.print(w3Doc);

		System.err.println("---");

		Node node = w3Doc.getFirstChild();

		NodePrinter.print(node);

		// removeChild
		NodeUtils.removeFirstElementChild(node);

		System.err.println("---");

		NodePrinter.print(node);

		// removeChild
		NodeUtils.removeFirstElementChild(node);

		System.err.println("---");

		NodePrinter.print(node);
		// removeChild
		NodeUtils.removeFirstElementChild(node);

		System.err.println("---");

		NodePrinter.print(node);
		// removeChild
		NodeUtils.removeFirstElementChild(node);

		System.err.println("---");

		NodePrinter.print(node);

	}

}
