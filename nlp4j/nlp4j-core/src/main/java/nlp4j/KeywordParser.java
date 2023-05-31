package nlp4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * created on: 2023-05-30
 * 
 * @author Hiroki Oya
 * 
 */
public class KeywordParser implements Closeable {

	private DocumentAnnotator annotator;

	public KeywordParser(DocumentAnnotator annotator) {
		super();
		this.annotator = annotator;
	}

	public List<Keyword> parse(String text) throws Exception {

		Document doc = (new DocumentBuilder()).text(text).build();

		this.annotator.annotate(doc);

		return doc.getKeywords();

	}

	@Override
	public void close() throws IOException {
		if (annotator instanceof Closeable) {
			((Closeable) annotator).close();
		}

	}

}
