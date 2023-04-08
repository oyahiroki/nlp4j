package nlp4j.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Trie Tree
 * </pre>
 * 
 * created on: 2022-04-06
 * 
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public class Trie {

	private boolean isLeaf = false;

	private Map<Character, Trie> children = new HashMap<>();

	public void insert(String s) {
		Trie ptr = this;
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			if (ptr.children.containsKey(c) == false) {
				ptr.children.put(c, new Trie());
			}
			ptr = ptr.children.get(c);
		}
		ptr.isLeaf = true;
	}

	public boolean contains(String s) {
		Trie ptr = this;
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			ptr = ptr.children.get(c);
			if (ptr == null) {
				return false;
			}
		}
		return ptr.isLeaf;
	}

	public TrieSearchResult search(String s) {
		TrieSearchResult result = new TrieSearchResult();

		Trie ptr = this;
//		if (ptr != null && ptr.children != null) {
//			System.err.println("children.size: " + ptr.children.size() + " " + ptr.children.keySet());
//		}
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			ptr = ptr.children.get(c);
			if (ptr == null) {
//				return false;
//				System.err.println("not found: " + c);
//				System.err.println("found: " + false);
				return result;
			} else {
				//
//				System.err.println("continue: " + c);
//				System.err.println("isLeaf: " + ptr.isLeaf);
				if (ptr.isLeaf) {
					result.addIndex(n);
				}
				if (ptr != null && ptr.children != null) {
//					System.err.println("children.size: " + ptr.children.size() + " " + ptr.children.keySet());
				}
			}
		}

//		return ptr.isLeaf;

		result.setFound(ptr.isLeaf);

		System.err.println("found: " + ptr.isLeaf);

		return result;
	}

	public void print() {
		this.print(0, null);
	}

	@Override
	public String toString() {
		return "Trie [isLeaf=" + isLeaf + ", children=" + children.keySet() + "]";
	}

	public String toString(Character c) {
		return
//				"Trie "
		"" //
				+ "[" + "" + ((c == null) ? "*" : c) + " " //
				// + "isLeaf="
				+ (isLeaf ? "*" : "-") //
//				+ ", " //
//				+ "children=" 
//				+ children.keySet() 
				+ "]";
	}

	private void print(int depth, Character c) {

		Trie ptr = this;

		String indent = "";
		for (int n = 0; n < depth; n++) {
			indent += " ";
		}

		System.err.println(indent + ptr.toString(c));

		for (Character cx : this.children.keySet()) {
			children.get(cx).print(depth + 1, cx);
		}

	}

}
