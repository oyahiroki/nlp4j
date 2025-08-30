package nlp4j.wiki;

/**
 * Created at: 2022-06-11
 * 
 * @author Hiroki Oya
 *
 */
public interface WikiPageHandler {

	public void read(WikiPage page) throws BreakException;

	default void startDocument() {
	};

	default void endDocument() {
	};

}
