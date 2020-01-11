package nlp4j.index;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Index;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

public class SimpleDocumentIndexTestCase extends TestCase {

	public void testAddDocument() {
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

		System.err.println(index.getKeywords().size());
		System.err.println(index.getKeywords());
		assertEquals("lex", index.getKeywords().get(0).getLex());
		assertEquals(2, index.getKeywords().get(0).getCount());
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
			List<Keyword> kwds = index.getKeywords("facet");
			System.err.println(kwds);
			assertEquals(1, kwds.size());
		}
		{
			List<Keyword> kwds = index.getKeywords("xxx");
			System.err.println(kwds);
			System.err.println(kwds);
			assertEquals(0, kwds.size());
		}
	}

	public void testGetKeywordsStringString() {
		DefaultDocument d1 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("facet1");
			kw.setLex("lex");
			d1.addKeyword(kw);
			d1.putAttribute("item", "a");
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("facet2");
			kw.setLex("lex");
			d2.addKeyword(kw);
			d2.putAttribute("item", "b");
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocument(d1);
			index.addDocument(d2);
		}
		{
			List<Keyword> kwds = index.getKeywords("facet1", "item=a");
			assertEquals(1, kwds.size());
		}
	}

	public void testToString() {
		DefaultDocument d1 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("facet1");
			kw.setLex("lex");
			d1.addKeyword(kw);
			d1.putAttribute("item", "a");
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("facet2");
			kw.setLex("lex");
			d2.addKeyword(kw);
			d2.putAttribute("item", "b");
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocument(d1);
			index.addDocument(d2);
		}
		{
			System.err.println(index.toString());
		}
	}

}
