package nlp4j.trie;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Trie Tree Search Result
 * </pre>
 * 
 * created on: 2022-04-06
 * 
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public class TrieSearchResult {

	List<Integer> indexes = new ArrayList<>();

	boolean found = false;

	public void addIndex(int idx) {
		indexes.add(idx);
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	@Override
	public String toString() {
		return "TrieSearchResult [found=" + found + ", indexes=" + indexes + "]";
	}

}
