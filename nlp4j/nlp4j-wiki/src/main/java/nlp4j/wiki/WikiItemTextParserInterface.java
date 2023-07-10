package nlp4j.wiki;

import java.util.List;

/**
 * Wiki形式のテキストについて見出しの親子関係を考慮してパースするクラス<br>
 * A class that parses text in Wiki format, taking into account the parent-child
 * relationship of the headings contained in the text.
 * 
 * @author Hiroki Oya
 */
public interface WikiItemTextParserInterface {

	WikiPageNode getRoot();

	List<WikiPageNode> getWikiPageNodesAsList();

	/**
	 * Wikipage を解析する
	 * 
	 * @param wikiItemText Wiki形式の文字列
	 * @return
	 */
	WikiPageNode parse(String wikiItemText);

	/**
	 * Wikipage を解析する
	 * 
	 * @param page
	 * @return
	 */
	WikiPageNode parse(WikiPage page);

	String toWikiPageNodeTree();

}