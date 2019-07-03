/**
 * 
 */
package nlp4j;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author oyahiroki
 *
 */
public interface NlpService {

	public ArrayList<Keyword> getKeywords(String text) throws IOException;

}
