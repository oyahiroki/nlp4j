package nlp4j.wiki;

import java.util.List;

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