package nlp4j.node;

import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeywordWithDependency;
import nlp4j.test.NLP4JTestCase;

public class NodeKeywordTestCase extends NLP4JTestCase {

	public NodeKeywordTestCase() {
		super();
		target = Node.class;
	}

	public void test001() throws Exception {

		DefaultKeywordWithDependency kw0 = new DefaultKeywordWithDependency();
		{
			kw0.setFacet("word.NN");
			kw0.setLex("AA");
		}
		{
			DefaultKeywordWithDependency kw1 = new DefaultKeywordWithDependency();
			{
				kw1.setFacet("word.NN");
				kw1.setLex("BB");
			}
			kw0.addChild(kw1);
		}

		DefaultKeywordWithDependency kw2 = new DefaultKeywordWithDependency();
		{
			kw2.setFacet("word.NN");
			kw2.setLex("AA");
		}
		{
			DefaultKeywordWithDependency kw3 = new DefaultKeywordWithDependency();
			{
				kw3.setFacet("word.NN");
				kw3.setLex("XX");
			}
			kw2.addChild(kw3);
		}

		Node<KeywordWithDependency> node1 = new Node<>(kw0);
		Node<KeywordWithDependency> node2 = new Node<>(kw2);

		System.err.println(node1.match(node2));

	}

}
