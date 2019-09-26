package nlp4j.impl;

import java.io.IOException;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.Keyword;

public class DefaultDocumentAnnotator extends AbstractDocumentAnnotator {

	@Override
	public void annotate(Document doc) throws IOException {
		doc.putAttribute("item", "value");

		{
			Keyword kwd = new DefaultKeyword();
			kwd.setBegin(0);
			kwd.setEnd(3);
			kwd.setLex("ABC");
			kwd.setFacet("facet");
			doc.addKeyword(kwd);
		}
	}

}
