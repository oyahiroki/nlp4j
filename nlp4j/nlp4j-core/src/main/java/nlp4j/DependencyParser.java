package nlp4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class DependencyParser implements Closeable {

	private DocumentAnnotator annotator;

	public DependencyParser(DocumentAnnotator annotator) {
		super();
		this.annotator = annotator;
	}

	public List<Keyword> parse(String text) throws Exception {
		Document doc = (new DocumentBuilder()).text(text).build();
		this.annotator.annotate(doc);
		return doc.getKeywords();
	}

	public void printAsXml(String text) throws Exception {
		List<Keyword> kwds = parse(text);
		// KeywordWithDependency
		for (Keyword kwd : kwds) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}
	}

	@Override
	public void close() throws IOException {
		if (annotator instanceof Closeable) {
			((Closeable) annotator).close();
		}

	}

}
