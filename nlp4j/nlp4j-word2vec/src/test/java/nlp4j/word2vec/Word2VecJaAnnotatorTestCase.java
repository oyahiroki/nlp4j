package nlp4j.word2vec;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;

public class Word2VecJaAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument101() throws Exception {

		String text = "今日はいい天気です。";
		String expected = "今日 は いい 天気 です";

		List<Document> docs = new ArrayList<>();

		{
			Document doc = new DefaultDocument();
			doc.putAttribute("text", text);

			docs.add(doc);

			System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));
			System.err.println("docs: " + docs.size());

		}

		{
			Word2VecJaAnnotator ann = new Word2VecJaAnnotator();
			ann.setProperty("target", "text");
			ann.setProperty("annotator", "nlp4j.krmj.annotator.KuromojiAnnotator");
			ann.annotate(docs);

			System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));
			System.err.println("docs: " + docs.size());
		}

		assertEquals(expected, docs.get(0).getAttributeAsString("text2"));

	}

}
