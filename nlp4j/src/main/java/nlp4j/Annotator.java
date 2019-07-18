package nlp4j;

import java.io.IOException;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface Annotator {

	public void annotate(DefaultDocument doc) throws IOException;

}
