package nlp4j.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nlp4j.tuple.Pair;

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

	/**
	 * Created on 2025-02-13
	 * 
	 * @param <K>
	 * @param <V>
	 * @param pairs
	 * @return
	 */
	@SafeVarargs
	static public <K, V> Map<K, V> of(Pair<K, V>... pairs) {
		HashMap<K, V> m = new HashMap<K, V>();
		for (Pair<K, V> p : pairs) {
			m.put(p.getKey(), p.getValue());
		}
		return Collections.unmodifiableMap(m);
	}

}
