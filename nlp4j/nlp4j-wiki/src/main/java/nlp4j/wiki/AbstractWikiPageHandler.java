package nlp4j.wiki;

public abstract class AbstractWikiPageHandler implements WikiPageHandler {

	abstract public void read(WikiPage page) throws BreakException;

	public void startDocument() {
	}

	public void endDocument() {
	}

}
