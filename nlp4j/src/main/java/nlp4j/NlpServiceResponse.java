package nlp4j;

import java.util.List;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface NlpServiceResponse {

	public String getOriginalResponseBody();

	public int getResponseCode();

	public List<Keyword> getKeywords();

}
