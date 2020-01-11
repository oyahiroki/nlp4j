package nlp4j.w3c;

import java.util.List;

import org.w3c.dom.Node;

import junit.framework.TestCase;

public class NodeMatcherTestCase extends TestCase {

	public void testGetHitNodes001() throws Exception {
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='C'>\n" + //
				"		<w id='3' lex=\"D\" />\n" + //
				"		<w id='4' lex=\"e\" />\n" + //
				"	</w>\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"</w>\n" + //
				"";
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		List<Node> hitNodes = NodeMatcher.getHitNodes(targetNode, conditionNode);
		assertNotNull(hitNodes);
		assertEquals(1, hitNodes.size());
		System.err.println("hitNodes: " + NodeUtils.toString(hitNodes));
		for (Node hitNode : hitNodes) {
			System.err.println("---");
			NodePrinter.print(hitNode);
		}
	}

	public void testGetHitNodes002() throws Exception {
		// ２箇所に登場し、ヒットする２つのルートは共通
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='B'>\n" + //
				"		<w id='3' lex=\"D\" />\n" + //
				"		<w id='4' lex=\"e\" />\n" + //
				"	</w>\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"</w>\n" + //
				"";
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		List<Node> hitNodes = NodeMatcher.getHitNodes(targetNode, conditionNode);
		System.err.println("hitNodes: " + NodeUtils.toString(hitNodes));
		for (Node hitNode : hitNodes) {
			System.err.println("---");
			NodePrinter.print(hitNode);
		}
		assertNotNull(hitNodes);
		assertEquals(3, hitNodes.size());
	}

	public void testGetHitNodes003() throws Exception {
		// ２箇所にヒットし、２つは独立している
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='A'>\n" + //
				"		<w id='3' lex=\"B\" />\n" + //
				"		<w id='4' lex=\"e\" />\n" + //
				"	</w>\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"</w>\n" + //
				"";
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		List<Node> hitNodes = NodeMatcher.getHitNodes(targetNode, conditionNode);
		System.err.println("target: " + NodeUtils.toString(targetNode));
		System.err.println("hitNodes: " + NodeUtils.toString(hitNodes));
		for (Node hitNode : hitNodes) {
			System.err.println("---");
			NodePrinter.print(hitNode);
		}
		assertNotNull(hitNodes);
		assertEquals(2, hitNodes.size());
	}

	public void testGetHitNodes004() throws Exception {
		// ２箇所にヒットし、２つめは最初の子ノードはヒットしない場合
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='A'>\n" + //
				"		<w id='3' lex=\"x\" />\n" + //
				"		<w id='4' lex=\"B\" />\n" + //
				"	</w>\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"</w>\n" + //
				"";
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		List<Node> hitNodes = NodeMatcher.getHitNodes(targetNode, conditionNode);
		assertNotNull(hitNodes);
		assertEquals(3, hitNodes.size());
		System.err.println("hitNodes: " + NodeUtils.toString(hitNodes));
		for (Node hitNode : hitNodes) {
			System.err.println("---");
			NodePrinter.print(hitNode);
		}
	}

	public void testGetHitNodes005() throws Exception {
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='A'>\n" + //
				"		<w id='3' lex=\"x\" />\n" + //
				"		<w id='4' lex=\"B\" />\n" + //
				"	</w>\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\"/>";
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		List<Node> hitNodes = NodeMatcher.getHitNodes(targetNode, conditionNode);
		for (Node hitNode : hitNodes) {
			System.err.println("---hitNodes");
			NodePrinter.print(hitNode);
		}
		assertNotNull(hitNodes);
		assertEquals(3, hitNodes.size());
	}

	public void testGetHitNodes006() throws Exception {
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='C' />\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">" + //
				"	<w id='1' lex='C' />\n" + //
				"</w>"; //
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		List<Node> hitNodes = NodeMatcher.getHitNodes(targetNode, conditionNode);
		for (Node hitNode : hitNodes) {
			System.err.println("---hitNodes");
			NodePrinter.print(hitNode);
		}
		assertNotNull(hitNodes);
		assertEquals(1, hitNodes.size());
	}

	public void testMatch001() throws Exception {
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='C' />\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">" + //
				"	<w id='1' lex='C' />\n" + //
				"</w>"; //
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		boolean b = NodeMatcher.match(targetNode, conditionNode);
		assertTrue(b);
	}

	public void testMatch002() throws Exception {
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='2' lex=\"C\" />\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">" + //
				"	<w id='1' lex='C' />\n" + //
				"</w>"; //
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		boolean b = NodeMatcher.match(targetNode, conditionNode);
		assertTrue(b);
	}

	public void testGetHitNodes000f() throws Exception {
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='A'>\n" + //
				"		<w id='3' lex=\"x\" />\n" + //
				"		<w id='4' lex=\"B\" />\n" + //
				"	</w>\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<w _hit=\"0\" id=\"2\" lex=\"A\" >\n" + "	<w id=\"4\" lex=\"B\" /></w>";
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);

		boolean b = NodeMatcher.match(targetNode, conditionNode);
		assertTrue(b == true);

	}

	public void testGetHitNodes000f2() throws Exception {
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='A'>\n" + //
				"		<w id='3' lex=\"x\" />\n" + //
				"		<w id='4' lex=\"B\" />\n" + //
				"	</w>\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<w id=\"2\" lex=\"A\" >\n" + //
				"	<w id=\"4\" lex=\"BX\" /></w>";
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);

		boolean b = NodeMatcher.match(targetNode, conditionNode);
		assertTrue(b == false);

	}

	public void testGetHitNodes000g() throws Exception {
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='A'>\n" + //
				"		<w id='3' lex=\"x\" />\n" + //
				"		<w id='4' lex=\"B\" />\n" + //
				"	</w>\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' />";
		Node targetNode = DocumentUtil.parse(targetXml);
		System.err.println(targetNode);
		NodePrinter.print(targetNode);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		System.err.println("conditionNode");
		NodePrinter.print(conditionNode);
		List<Node> hitNodes = NodeMatcher.getHitNodes(targetNode, conditionNode);
		for (Node hitNode : hitNodes) {
			System.err.println("---hitNodes");
			NodePrinter.print(hitNode);
		}
		assertNotNull(hitNodes);
		assertEquals(3, hitNodes.size());
	}

	public void testMatchAsSingleNode001() throws Exception {
		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' lex=\"A\">\n" + //
				"	<w id='1' lex=\"B\" />\n" + //
				"	<w id='2' lex='A'>\n" + //
				"		<w id='3' lex=\"x\" />\n" + //
				"		<w id='4' lex=\"B\" />\n" + //
				"	</w>\n" + //
				"</w>\n" + //
				"";
		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' />";
		Node targetNode = DocumentUtil.parse(targetXml);
		System.err.println(targetNode);
		NodePrinter.print(targetNode);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		System.err.println("conditionNode");
		NodePrinter.print(conditionNode);
		boolean b = NodeMatcher.matchAsSingleNode(targetNode, conditionNode, false);
		assertTrue(b);
		NodePrinter.print(targetNode);
		NodePrinter.print(conditionNode);
	}

	public void testGetHitNodes100() throws Exception {

		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- lock -->\n" + //
				"<w id=\"0\" lex=\"lock\" pos=\"VB\" relation=\"root\" relationLong=\"root\"\n" + //
				"	str=\"lock\">\n" + //
				"	<!-- user -->\n" + //
				"	<w id=\"1\" lex=\"user\" pos=\"NN\" relation=\"nsubj\"\n" + //
				"		relationLong=\"nominal subject\" str=\"user\">\n" + //
				"		<!-- the -->\n" + //
				"		<w id=\"2\" lex=\"the\" pos=\"DT\" relation=\"det\"\n" + //
				"			relationLong=\"determiner\" str=\"The\" />\n" + //
				"	</w>\n" + //
				"	<!-- shall -->\n" + //
				"	<w id=\"3\" lex=\"shall\" pos=\"MD\" relation=\"aux\"\n" + //
				"		relationLong=\"auxiliary\" str=\"shall\" />\n" + //
				"	<!-- remotely -->\n" + //
				"	<w id=\"4\" lex=\"remotely\" pos=\"RB\" relation=\"advmod\"\n" + //
				"		relationLong=\"adverbial modifier\" str=\"remotely\" />\n" + //
				"	<!-- door -->\n" + //
				"	<w id=\"5\" lex=\"door\" pos=\"NN\" relation=\"dobj\"\n" + //
				"		relationLong=\"direct object\" str=\"door\">\n" + //
				"		<!-- the -->\n" + //
				"		<w id=\"6\" lex=\"the\" pos=\"DT\" relation=\"det\"\n" + //
				"			relationLong=\"determiner\" str=\"the\" />\n" + //
				"		<!-- vehicle -->\n" + //
				"		<w id=\"7\" lex=\"vehicle\" pos=\"NN\" relation=\"nmod\"\n" + //
				"			relationLong=\"nominal modifier\" str=\"vehicle\">\n" + //
				"			<!-- of -->\n" + //
				"			<w id=\"8\" lex=\"of\" pos=\"IN\" relation=\"case\"\n" + //
				"				relationLong=\"case marker\" str=\"of\" />\n" + //
				"			<!-- the -->\n" + //
				"			<w id=\"9\" lex=\"the\" pos=\"DT\" relation=\"det\"\n" + //
				"				relationLong=\"determiner\" str=\"the\" />\n" + //
				"		</w>\n" + //
				"	</w>\n" + //
				"	<!-- . -->\n" + //
				"	<w id=\"10\" lex=\".\" pos=\".\" relation=\"punct\"\n" + //
				"		relationLong=\"punctuation\" str=\".\" />\n" + //
				"</w>";

		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"nsubj\" />\n" + //
				"</w>\n" + //
				"";
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		List<Node> hitNodes = NodeMatcher.getHitNodes(targetNode, conditionNode);

		assertNotNull(hitNodes);
		assertEquals(2, hitNodes.size());

		// System.err.println(hitNodes.size());
		System.err.println("hitNodes: " + NodeUtils.toString(hitNodes));
		for (Node hitNode : hitNodes) {
			System.err.println("---");
			NodePrinter.print(hitNode);
		}
	}

	public void testGetHitNodes101() throws Exception {

		String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- lock -->\n" + //
				"<w id=\"0\" lex=\"lock\" pos=\"VB\" relation=\"root\" relationLong=\"root\"\n" + //
				"	str=\"lock\">\n" + //
				"	<!-- user -->\n" + //
				"	<w id=\"1\" lex=\"user\" pos=\"NN\" relation=\"nsubj\"\n" + //
				"		relationLong=\"nominal subject\" str=\"user\">\n" + //
				"		<!-- the -->\n" + //
				"		<w id=\"2\" lex=\"the\" pos=\"DT\" relation=\"det\"\n" + //
				"			relationLong=\"determiner\" str=\"The\" />\n" + //
				"	</w>\n" + //
				"	<!-- shall -->\n" + //
				"	<w id=\"3\" lex=\"shall\" pos=\"MD\" relation=\"aux\"\n" + //
				"		relationLong=\"auxiliary\" str=\"shall\" />\n" + //
				"	<!-- remotely -->\n" + //
				"	<w id=\"4\" lex=\"remotely\" pos=\"RB\" relation=\"advmod\"\n" + //
				"		relationLong=\"adverbial modifier\" str=\"remotely\" />\n" + //
				"	<!-- door -->\n" + //
				"	<w id=\"5\" lex=\"door\" pos=\"NN\" relation=\"dobj\"\n" + //
				"		relationLong=\"direct object\" str=\"door\">\n" + //
				"		<!-- the -->\n" + //
				"		<w id=\"6\" lex=\"the\" pos=\"DT\" relation=\"det\"\n" + //
				"			relationLong=\"determiner\" str=\"the\" />\n" + //
				"		<!-- vehicle -->\n" + //
				"		<w id=\"7\" lex=\"vehicle\" pos=\"NN\" relation=\"nmod\"\n" + //
				"			relationLong=\"nominal modifier\" str=\"vehicle\">\n" + //
				"			<!-- of -->\n" + //
				"			<w id=\"8\" lex=\"of\" pos=\"IN\" relation=\"case\"\n" + //
				"				relationLong=\"case marker\" str=\"of\" />\n" + //
				"			<!-- the -->\n" + //
				"			<w id=\"9\" lex=\"the\" pos=\"DT\" relation=\"det\"\n" + //
				"				relationLong=\"determiner\" str=\"the\" />\n" + //
				"		</w>\n" + //
				"	</w>\n" + //
				"	<!-- . -->\n" + //
				"	<w id=\"10\" lex=\".\" pos=\".\" relation=\"punct\"\n" + //
				"		relationLong=\"punctuation\" str=\".\" />\n" + //
				"</w>";

		String conditionXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
				"<w id='0' pos=\"VB\">\n" + //
				"	<w id='1' relation=\"dobj\" />\n" + //
				"</w>\n" + //
				"";
		Node targetNode = DocumentUtil.parse(targetXml);
		Node conditionNode = DocumentUtil.parse(conditionXml);
		List<Node> hitNodes = NodeMatcher.getHitNodes(targetNode, conditionNode);

		// System.err.println(hitNodes.size());
		System.err.println("hitNodes: " + NodeUtils.toString(hitNodes));
		for (Node hitNode : hitNodes) {
			System.err.println("---");
			NodePrinter.print(hitNode);
		}

		assertNotNull(hitNodes);
		assertEquals(5, hitNodes.size());
	}

}
