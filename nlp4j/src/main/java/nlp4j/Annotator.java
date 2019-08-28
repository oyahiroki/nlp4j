package nlp4j;

import java.io.IOException;
import java.util.List;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface Annotator {

	public void annotate(Document doc) throws IOException;

	public void annotate(List<Document> docs) throws IOException;

}
