package nlp4j;

import java.util.Properties;

import com.google.gson.JsonObject;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public abstract class AbstractDocumentSearcher implements DocumentSearcher {

	protected Properties props = new Properties();

	@Override
	public void setProperties(Properties prop) {
		for (Object key : prop.keySet()) {
			setProperty(key.toString(), prop.getProperty(key.toString()));
		}
	}

	@Override
	public void setProperty(String key, String value) {
		if (value == null) {
			throw new InvalidPropertyException(key, value);
		}
		props.put(key, value);
	}

	/**
	 * デフォルトの検索クエリー
	 * 
	 * @return {"count":true,"search":"*"}
	 */
	static public JsonObject createDefaultSearchRequest() {
		JsonObject requestObj = new JsonObject();
		{
			requestObj.addProperty("count", true);
			requestObj.addProperty("search", "*");
		}
		return requestObj;
	}

}
