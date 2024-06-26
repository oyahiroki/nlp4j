package nlp4j.trie;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.KeywordAnnotator;

public class TrieDictionaryAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, KeywordAnnotator {

	Trie trie = new Trie();

	public TrieDictionaryAnnotator() {
		super();
	}

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if ("add1".equals(key)) {
			String s = value;
			boolean overwritten = false;
			String facet = "word";
			this.trie.insert(s, overwritten, facet);
		} //
		else if ("add2".equals(key)) {
			String s = value;
			boolean overwritten = true;
			String facet = "word";
			this.trie.insert(s, overwritten, facet);
		}

	}

	@Override
	public void annotate(Document doc) throws Exception {
		for (String target : super.targets) {
			String t = doc.getAttributeAsString(target);
			TrieSearchResult r = trie.search(t);

			doc.addKeywords(r.getKeywords());

		}

	}

}
