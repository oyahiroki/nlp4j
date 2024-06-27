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

	// ファセット(複数)
	private final List<String> facets = new ArrayList<>();

	// さらに長いキーワードがあればそちらを優先するか
	private boolean overwrittern = true;

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

	public List<String> getFacets() {
		return facets;
	}

	/**
	 * same as insert(s, false, null);
	 * 
	 * @param s
	 */
	public void insert(String s) {
		insert(s, false, null);
	}

	/**
	 * @param s
	 * @param overwrittern
	 * @param facet
	 */
	public void insert(String s, boolean overwrittern, String facet) {
		Trie ptr = this;

		// FOR_EACH(CHARACTER)
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			if (ptr.children.containsKey(c) == false) {
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

	public boolean isOverwrittern() {
		return overwrittern;
	}

	public void print() {
		this.print(0, null);
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

						{
							List<Keyword> toberemoved = new ArrayList<>();
							for (Keyword k : kk.keySet()) {
								boolean b = kk.get(k);
								if (b == true) {
									toberemoved.add(k);
								}
							}
							for (Keyword k : toberemoved) {
								kk.remove(k);
							}
						}

//						System.err.println("c:" + c);
//						System.err.println("HIT: " + s.subSequence(begin, n + 1));

						List<String> facets = ptr.getFacets();

						// ファセット定義なし
						if (facets == null || facets.size() == 0) {
							String lex = s.substring(begin, n + 1);
							Keyword kwd = (new KeywordBuilder()) //
									.lex(lex) //
									.begin(begin) //
									.end(n + 1) //
									.build();
							// 結果を追加
//							result.addKeyword(kwd);
							kk.put(kwd, ptr.isOverwrittern());
						} //
							// ファセット定義あり
						else {
							for (String facet : facets) {
								String lex = s.substring(begin, n + 1);
								Keyword kwd = (new KeywordBuilder()) //
										.lex(lex) //
										.begin(begin) //
										.end(n + 1) //
										.facet(facet) //
										.build();
								// 結果を追加
//								result.addKeyword(kwd);
								kk.put(kwd, ptr.isOverwrittern());
							}
						}

//						result.addIndex(n + 1);
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

	@Override
	public String toString() {
		return "Trie [isLeaf=" + isLeaf + ", children=" + children.keySet() + "]";
	}

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
