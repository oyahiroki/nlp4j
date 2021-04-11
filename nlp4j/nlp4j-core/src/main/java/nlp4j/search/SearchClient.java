package nlp4j.search;

import java.io.IOException;

import com.google.gson.JsonObject;

/**
 * Search Client
 * 
 * @author Hiroki Oya
 *
 */
public interface SearchClient {

	/**
	 * @param indexName as Index or Collection
	 * @param request   in JSON
	 * @return Search Response in JSON
	 * @throws IOException on Error
	 */
	public JsonObject search(String indexName, JsonObject request) throws IOException;

	/**
	 * @param indexName as Index or Collection
	 * @param request   in JSON String
	 * @return Search Response in JSON
	 * @throws IOException on Error
	 */
	public JsonObject search(String indexName, String request) throws IOException;

}
