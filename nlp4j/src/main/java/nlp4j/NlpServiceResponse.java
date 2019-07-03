/**
 * 
 */
package nlp4j;

import java.util.List;

/**
 * @author oyahiroki
 *
 */
public interface NlpServiceResponse {

	public String getOriginalResponseBody();

	public int getResponseCode();

	public List<Keyword> getKeywords();

}
