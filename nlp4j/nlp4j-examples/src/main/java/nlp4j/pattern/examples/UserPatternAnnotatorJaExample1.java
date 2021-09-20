package nlp4j.pattern.examples;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.cabocha.CabochaAnnotator;
import nlp4j.impl.DefaultDocument;
import nlp4j.pattern.UserPatternAnnotator;

@SuppressWarnings("javadoc")
public class UserPatternAnnotatorJaExample1 {

	public static void main(String[] args) throws Exception {

		List<Document> docs = new ArrayList<>();

		docs.add(new DefaultDocument("私は歩く。"));
		docs.add(new DefaultDocument("私が歩く。"));
		docs.add(new DefaultDocument("私は急いで歩く。"));
		docs.add(new DefaultDocument("私は急いで歩いた。"));
		docs.add(new DefaultDocument("私は急いで学校まで歩いた。"));
		docs.add(new DefaultDocument("昨日、私は急いで学校まで歩いた。"));

		CabochaAnnotator ann1 = new CabochaAnnotator();
		{ // 係り受け解析
			ann1.setProperty("encoding", "MS932");
			ann1.setProperty("target", "text");
		}

		UserPatternAnnotator ann2 = new UserPatternAnnotator();
		{ // 係り受けパターン抽出
			ann2.setProperty("file", "src/test/resources/nlp4j.pattern.examples/pattern-ja-sv.xml");
		}

		for (Document doc : docs) {
			System.err.println("<doc>");
			ann1.annotate(doc);
			ann2.annotate(doc);
			System.err.println(doc.getAttribute("text"));
			for (Keyword kwd : doc.getKeywords("pattern")) {
				System.err.println(kwd.getLex());
			}
			System.err.println("</doc>");
		}

	}
}
