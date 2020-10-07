package nlp4j.stanford;

import java.util.ArrayList;

public class SemanticGraphInfo {

	int id = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void idPP() {
		this.id++;
	}

	ArrayList<String> keys = new ArrayList<>();

	public void putKey(String key) {
		keys.add(key);
	}

	public boolean hasKey(String key) {
		return keys.contains(key);
	}

}
