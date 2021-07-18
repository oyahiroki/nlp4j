package nlp4j.stanford;

import java.util.ArrayList;

/**
 * @author Hiroki Oya
 * @created_at 2021-02-12
 */
public class SemanticGraphInfo {

	int id = 0;

	/**
	 * @return ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id for ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * increment ID
	 */
	public void idPP() {
		this.id++;
	}

	ArrayList<String> keys = new ArrayList<>();

	/**
	 * @param key for KEY
	 */
	public void putKey(String key) {
		keys.add(key);
	}

	/**
	 * @param key for KEY
	 * @return keys contains key
	 */
	public boolean hasKey(String key) {
		return keys.contains(key);
	}

}
