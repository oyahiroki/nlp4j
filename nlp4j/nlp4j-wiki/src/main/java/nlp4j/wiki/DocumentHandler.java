package nlp4j.wiki;

import nlp4j.Document;

public interface DocumentHandler {

	public void read(Document document) throws BreakException;

}
