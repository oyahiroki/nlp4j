package nlp4j.trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nlp4j.Keyword;
import nlp4j.KeywordBuilder;

/**
 * <pre>
 * Trie Tree (also known as a prefix tree) for substring search.
 *
 * Each node in the tree represents a single character. A path from the root
 * to a leaf node spells out a keyword registered in the dictionary.
 *
 * The {@link #search(String)} method scans the input string for all
 * occurrences of registered keywords. When {@code overwritten} is {@code true}
 * on a leaf node, any shorter matches found at the same start position are
 * discarded in favour of the longer match.
 * </pre>
 *
 * created on: 2022-04-06
 *
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public class Trie {

	/** True if this node marks the end of a registered keyword. */
	private boolean isLeaf = false;

	/** Child nodes keyed by character. */
	private Map<Character, Trie> children = new HashMap<>();

	/** Facet labels associated with this leaf node (may be empty). */
	private final List<String> facets = new ArrayList<>();

	/**
	 * When {@code true}, a match at this node replaces any shorter matches
	 * found at the same start position (longest-match-wins semantics).
	 */
	private boolean overwrittern = true;

	/** Cumulative total length of all strings passed to {@link #insert(String, boolean, String)}. */
	private int size_original = 0;

	/**
	 * Returns {@code true} if the given string exists as an exact entry in
	 * this trie (i.e. there is a path from the root matching every character
	 * of {@code s} and the terminal node is a leaf).
	 *
	 * @param s the string to look up; must not be {@code null}
	 * @return {@code true} if {@code s} is registered, {@code false} otherwise
	 */
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

	/**
	 * Returns the facet labels registered on this leaf node.
	 * The list is empty when no facet was specified at insert time.
	 *
	 * @return mutable list of facet label strings (never {@code null})
	 */
	public List<String> getFacets() {
		return facets;
	}

	/**
	 * Returns the total number of trie nodes in the subtree rooted at this
	 * node, including the node itself.
	 *
	 * @return node count (&ge; 1)
	 */
	public int getSize() {
		int n = 1;
		if (this.children != null) {
			for (Map.Entry<Character, Trie> e : this.children.entrySet()) {
				n += e.getValue().getSize();
			}
		}
		return n;
	}

	/**
	 * Returns the cumulative sum of the lengths of all strings inserted into
	 * this trie via {@link #insert(String, boolean, String)}.
	 *
	 * @return total inserted character count
	 */
	public int getSizeOriginal() {
		return size_original;
	}

	/**
	 * Inserts a string into the trie with {@code overwritten = false} and no
	 * facet label. Equivalent to {@code insert(s, false, null)}.
	 *
	 * @param s the string to insert; must not be {@code null}
	 */
	public void insert(String s) {
		insert(s, false, null);
	}

	/**
	 * Inserts a string into the trie and marks its terminal node as a leaf.
	 *
	 * <p>If {@code overwritten} is {@code true}, a {@link #search(String)} call
	 * will discard shorter matches at the same start position when this entry
	 * is found (longest-match-wins semantics).</p>
	 *
	 * <p>The same string may be inserted multiple times with different facet
	 * labels; each label is appended to the leaf node's facet list.</p>
	 *
	 * @param s            the string to insert; must not be {@code null}
	 * @param overwrittern {@code true} to enable longest-match-wins for this entry
	 * @param facet        optional facet label; {@code null} means no facet
	 */
	public void insert(String s, boolean overwrittern, String facet) {
		Trie ptr = this;
		this.size_original += s.length();

		// FOR_EACH(CHARACTER)
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			if (ptr.children.containsKey(c) == false) {
				// create new Trie node
				ptr.children.put(c, new Trie());
			}
			ptr = ptr.children.get(c);
		} // END_OF_FOR_EACH(CHARACTER)

		// 先端ノード
		ptr.isLeaf = true;
		ptr.overwrittern = overwrittern;

		if (facet != null) {
			ptr.facets.add(facet);
		}
	}

	/**
	 * Returns {@code true} if this leaf node is configured for
	 * longest-match-wins semantics (i.e. a longer match will replace this one
	 * at the same start position during {@link #search(String)}).
	 *
	 * @return {@code true} when overwrite mode is active
	 */
	public boolean isOverwrittern() {
		return overwrittern;
	}

	/**
	 * Prints a human-readable tree representation of this trie to
	 * {@link System#err}. Each node is indented proportionally to its depth.
	 */
	public void print() {
		this.print(0, null);
	}

	/**
	 * Recursive helper for {@link #print()}.
	 *
	 * @param depth current depth in the tree (root = 0)
	 * @param c     the character edge leading to this node, or {@code null} for
	 *              the root node
	 */
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

	/**
	 * Searches {@code s} for all substrings that match a keyword registered in
	 * this trie.
	 *
	 * <p>The algorithm slides a start position {@code begin} across every
	 * character of the input string. At each position a traversal follows the
	 * trie until no child node matches the next character. Every time a leaf
	 * node is reached a {@link Keyword} is built for the span
	 * {@code [begin, n+1)}.</p>
	 *
	 * <p>When the leaf node has {@code overwritten = true}, all shorter matches
	 * accumulated at the same start position are cleared before the new match
	 * is added (longest-match-wins semantics).</p>
	 *
	 * @param s the input text to search; must not be {@code null}
	 * @return a {@link TrieSearchResult} containing all matched
	 *         {@link Keyword} objects in the order they were discovered
	 */
	public TrieSearchResult search(String s) {
		TrieSearchResult result = new TrieSearchResult(s);

		for (int begin = 0; begin < s.length(); begin++) {

			Trie ptr = this;
//			if (ptr != null && ptr.children != null) {
//				System.err.println("children.size: " + ptr.children.size() + " " + ptr.children.keySet());
//			}

			Map<Keyword, Boolean> kk = new LinkedHashMap<>();

			// 開始位置
			for (int n = begin; n < s.length(); n++) {
				char c = s.charAt(n);
//				System.err.println("char: " + c);
				ptr = ptr.children.get(c);
				if (ptr == null) {
//					return false;
//					System.err.println("not found: " + c);
//					System.err.println("found: " + false);
//					return result;
					break;
				} else {
					//
//					System.err.println("continue: " + c);
//					System.err.println("isLeaf: " + ptr.isLeaf);

					if (ptr.isLeaf == true) {
	
						List<String> facets = ptr.getFacets();
	
						// ファセット定義なし
						if (facets == null || facets.size() == 0) {
							String lex = s.substring(begin, n + 1);
							Keyword kwd = (new KeywordBuilder()) //
									.lex(lex) //
									.begin(begin) //
									.end(n + 1) //
									.build();
							// 新しいマッチが overwritten=true の場合、既存の短いマッチを除去してから追加
							if (ptr.isOverwrittern()) {
								kk.clear();
							}
							kk.put(kwd, ptr.isOverwrittern());
						} //
							// ファセット定義あり
						else {
							// 新しいマッチが overwritten=true の場合、既存の短いマッチを除去してから追加
							if (ptr.isOverwrittern()) {
								kk.clear();
							}
							for (String facet : facets) {
								String lex = s.substring(begin, n + 1);
								Keyword kwd = (new KeywordBuilder()) //
										.lex(lex) //
										.begin(begin) //
										.end(n + 1) //
										.facet(facet) //
										.build();
								kk.put(kwd, ptr.isOverwrittern());
							}
						}
					}
					if (ptr != null && ptr.children != null) {
//						System.err.println("children.size: " + ptr.children.size() + " " + ptr.children.keySet());
					}
				} // END_OF_ELSE

			} // END_OF_FOR
			for (Keyword k : kk.keySet()) {
				result.addKeyword(k);
			}

		} // END_OF_FOR

//		Trie ptr = this;
////		if (ptr != null && ptr.children != null) {
////			System.err.println("children.size: " + ptr.children.size() + " " + ptr.children.keySet());
////		}
//		for (int n = 0; n < s.length(); n++) {
//			char c = s.charAt(n);
//			ptr = ptr.children.get(c);
//			if (ptr == null) {
////				return false;
////				System.err.println("not found: " + c);
////				System.err.println("found: " + false);
//				return result;
//			} else {
//				//
////				System.err.println("continue: " + c);
////				System.err.println("isLeaf: " + ptr.isLeaf);
//				if (ptr.isLeaf == true) {
////					System.err.println("c:" + c);
//					result.addIndex(n + 1);
//				}
//				if (ptr != null && ptr.children != null) {
////					System.err.println("children.size: " + ptr.children.size() + " " + ptr.children.keySet());
//				}
//			}
//		}

//		return ptr.isLeaf;

//		result.setFound(ptr.isLeaf);

//		System.err.println("found: " + ptr.isLeaf);

		return result;
	}

	/**
	 * Returns a concise string representation of this node showing its leaf
	 * status and the set of child characters.
	 *
	 * @return debug string, e.g. {@code "Trie [isLeaf=true, children=[a, b]]"}
	 */
	@Override
	public String toString() {
		return "Trie [isLeaf=" + isLeaf + ", children=" + children.keySet() + "]";
	}

	/**
	 * Returns a compact single-node label used by {@link #print()}.
	 * Format: {@code "[<char> *]"} for a leaf node, {@code "[<char> -]"} for
	 * an internal node. The root node uses {@code *} as the character symbol.
	 *
	 * @param c the character edge leading to this node, or {@code null} for
	 *          the root node
	 * @return formatted node string
	 */
	public String toString(Character c) {
//		JsonObject jo = new JsonObject();
//		if (c == null) {
//			jo.addProperty("c", "null");
//		} else {
//			jo.addProperty("c", c);
//		}
//		jo.addProperty("isLeaf", this.isLeaf);
//		return jo.toString();

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (c == null) {
			sb.append("* ");
		} else {
			sb.append(c + " ");
		}
		if (isLeaf) {
			sb.append("*");
		} else {
			sb.append("-");
		}
		sb.append("]");

		return sb.toString();
	}

}
