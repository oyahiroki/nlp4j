package nlp4j.search;

import java.io.IOException;

import com.google.gson.JsonObject;

public abstract class AbstractSearchClient implements SearchClient {

	@Override
	public abstract JsonObject search(String indexName, JsonObject request) throws IOException;

}
