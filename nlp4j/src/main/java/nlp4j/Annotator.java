package nlp4j;

import java.io.IOException;

public interface Annotator {

	public void annotate(Document doc) throws IOException;

}
