package nlp4j.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2024-08-04
 * 
 * @since 1.3.7.13
 * 
 */
public class MapBuilder<K, V> {

	private Map<K, V> map;

	public MapBuilder() {
		super();
		this.map = new HashMap<K, V>();
	}

	public MapBuilder<K, V> put(K key, V value) {
		this.map.put(key, value);
		return this;
	}

	public Map<K, V> build() {
		return this.map;
	}

}
