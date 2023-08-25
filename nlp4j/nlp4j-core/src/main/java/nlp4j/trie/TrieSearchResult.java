package nlp4j.trie;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Keyword;

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

	List<Keyword> kwds = new ArrayList<>();

	private String s;

	public TrieSearchResult(String s) {
		this.s = s;
	}

	public void addKeyword(Keyword kwd) {
		this.kwds.add(kwd);
	}

	public List<Keyword> getKeywords() {
		return this.kwds;
	}

}
