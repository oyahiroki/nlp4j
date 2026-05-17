package nlp4j.wiki;

public interface WikiIndexItemHandler {

	public void read(WikiIndexItem item) throws BreakException;

	default public void begin() {
	}

	default public void end() {
	}
}
