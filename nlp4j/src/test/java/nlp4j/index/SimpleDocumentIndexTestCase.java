package nlp4j.index;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Index;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

public class SimpleDocumentIndexTestCase extends TestCase {

	public void testAddDocument() {
		fail("Not yet implemented");
	}

	public void testAddDocuments() {
		DefaultDocument d1 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("facet");
			kw.setLex("lex");
			d1.addKeyword(kw);
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("facet");
			kw.setLex("lex");
			d2.addKeyword(kw);
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocument(d1);
			index.addDocument(d2);
		}
		{
			System.err.println("Facet keywords");
			List<Keyword> kwds = index.getKeywords("facet");
			for (Keyword kw : kwds) {
				System.err.println(kw);
			}
		}
		{
			System.err.println("All Keywords");
			List<Keyword> kwds = index.getKeywords();
			for (Keyword kw : kwds) {
				System.err.println(kw);
			}
		}

	}

	public void testGetKeywordsString() {
		fail("Not yet implemented");
	}

	public void testGetKeywordsStringString() {
		fail("Not yet implemented");
	}

	public void testToString() {
		fail("Not yet implemented");
	}

}
