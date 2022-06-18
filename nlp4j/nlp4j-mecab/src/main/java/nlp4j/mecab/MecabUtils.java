package nlp4j.mecab;

import java.io.IOException;
import java.util.List;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;

public class MecabUtils {

	static public List<Keyword> getKeywords(String text) throws IOException {

		try {
			Document doc = new DefaultDocument();
			doc.putAttribute("text", text);
			MecabAnnotator annotator = new MecabAnnotator();
			annotator.setProperty("target", "text");
			annotator.annotate(doc); // throws Exception
			annotator.close();
			return doc.getKeywords();
		} catch (Exception e) {
			throw new IOException(e);
		}

	}

}
