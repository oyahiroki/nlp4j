package nlp4j.indexer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.indexer.SimpleDocumentIndex;

public class SimpleDocumentIndexTestCase extends TestCase {

	public void testAddDocument001() {
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

	public void testAddDocument002() {
		DefaultDocument d1 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("facet.aa");
			kw.setLex("lex");
			d1.addKeyword(kw);
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("facet.bb");
			kw.setLex("lex");
			d2.addKeyword(kw);
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocument(d1);
			index.addDocument(d2);
		}

		for (Keyword kwd : index.getKeywords("facet")) {
			System.err.println(kwd.getFacet() + "," + kwd.getLex() + "," + kwd.getCount());
		}
		for (Keyword kwd : index.getKeywords("facet.aa")) {
			System.err.println(kwd.getFacet() + "," + kwd.getLex() + "," + kwd.getCount());
		}
		for (Keyword kwd : index.getKeywords("facet.bb")) {
			System.err.println(kwd.getFacet() + "," + kwd.getLex() + "," + kwd.getCount());
		}
	}

	/**
	 * キーワードのカウント
	 */
	public void testAddDocument003() {
		DefaultDocument d1 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("word.nn");
			kw.setLex("学校");
			d1.addKeyword(kw);
		}
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("word.nn");
			kw.setLex("学校");
			d1.addKeyword(kw);
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			Keyword kw = new DefaultKeyword();
			kw.setFacet("word.nn");
			kw.setLex("学校");
			d2.addKeyword(kw);
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocument(d1);
			index.addDocument(d2);
		}

		assertEquals(1, index.getKeywords().size());
		assertEquals("学校", index.getKeywords().get(0).getLex());
		assertEquals(2, index.getKeywords().get(0).getCount());

		for (Keyword kwd : index.getKeywords()) {
			System.err.println(kwd.getFacet() + "," + kwd.getLex() + "," + kwd.getCount());
		}
	}

	/**
	 * 日付のカウント（月単位）
	 */
	public void testAddDocument004() {
		DefaultDocument d1 = new DefaultDocument();
		{
			d1.putAttribute("date", "2020-01-01");
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			d2.putAttribute("date", "2020-03-01");
		}
		DefaultDocument d3 = new DefaultDocument();
		{
			d3.putAttribute("date", "2020-07-01");
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		index.setProperty("datefield", "date,yyyy-MM-dd");
		{
			index.addDocument(d1);
			index.addDocument(d2);
			index.addDocument(d3);
		}

		assertEquals(7, index.getDateCountMonth().size());
		assertEquals("202001", index.getDateCountMonth().get(0).getLex());
		assertEquals(1, index.getDateCountMonth().get(0).getCount());

		for (Keyword kwd : index.getDateCountMonth()) {
			System.err.println(kwd.getFacet() + "," + kwd.getLex() + "," + kwd.getCount());
		}
	}

	/**
	 * 日付のカウント（日単位）
	 */
	public void testAddDocument005() {
		DefaultDocument d1 = new DefaultDocument();
		{
			d1.putAttribute("date", "2020-01-01");
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			d2.putAttribute("date", "2020-03-01");
		}
		DefaultDocument d3 = new DefaultDocument();
		{
			d3.putAttribute("date", "2020-07-01");
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		index.setProperty("datefield", "date,yyyy-MM-dd");
		{
			index.addDocument(d1);
			index.addDocument(d2);
			index.addDocument(d3);
		}

		assertEquals(183, index.getDateCountDay().size());
		assertEquals("20200101", index.getDateCountDay().get(0).getLex());
		assertEquals(1, index.getDateCountDay().get(0).getCount());
//
		for (Keyword kwd : index.getDateCountDay()) {
			System.err.println(kwd.getFacet() + "," + kwd.getLex() + "," + kwd.getCount());
		}
	}

	/**
	 * 日付のカウント（年単位）
	 */
	public void testAddDocument006() {
		DefaultDocument d1 = new DefaultDocument();
		{
			d1.putAttribute("date", "2020-01-01");
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			d2.putAttribute("date", "2020-03-01");
		}
		DefaultDocument d3 = new DefaultDocument();
		{
			d3.putAttribute("date", "2020-07-01");
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		index.setProperty("datefield", "date,yyyy-MM-dd");
		{
			index.addDocument(d1);
			index.addDocument(d2);
			index.addDocument(d3);
		}

		assertEquals(1, index.getDateCountYear().size());
		assertEquals("2020", index.getDateCountYear().get(0).getLex());
		assertEquals(3, index.getDateCountYear().get(0).getCount());

		for (Keyword kwd : index.getDateCountYear()) {
			System.err.println(kwd.getFacet() + "," + kwd.getLex() + "," + kwd.getCount());
		}
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

	/**
	 * <pre>
	 * created_at : 2022-01-22
	 * </pre>
	 */
	public void testGetItemCount() {
		List<Document> docs = new ArrayList<Document>();
		{
			docs.add((new DocumentBuilder()).put("item1", "aaa").create());
			docs.add((new DocumentBuilder()).put("item1", "aaa").create());
			docs.add((new DocumentBuilder()).put("item1", "aaa").create());
		}
		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocuments(docs);
		}
		List<Keyword> kwds = index.getItemCount("item1");
		for (Keyword kwd : kwds) {
			System.err.println(kwd.getLex() + "," + kwd.getCount());
		}
	}

	public void testGetDateCount() {
		List<Document> docs = new ArrayList<Document>();
		{
			docs.add((new DocumentBuilder()).put("date", "202101").create());
			docs.add((new DocumentBuilder()).put("date", "202101").create());
			docs.add((new DocumentBuilder()).put("date", "202101").create());
			docs.add((new DocumentBuilder()).put("date", "202103").create());
			docs.add((new DocumentBuilder()).put("date", "202103").create());
			docs.add((new DocumentBuilder()).put("date", "202103").create());
			docs.add((new DocumentBuilder()).put("date", "202112").create());
			docs.add((new DocumentBuilder()).put("date", "202112").create());
			docs.add((new DocumentBuilder()).put("date", "202112").create());
		}
		SimpleDocumentIndex index = new SimpleDocumentIndex();
		index.setProperty(SimpleDocumentIndex.KEY_DATEFIELD, "date,yyyyMM");
		{
			index.addDocuments(docs);
		}
		List<Keyword> kwds = index.getDateCountMonth();
		assertTrue(kwds.size() > 0);
		for (Keyword kwd : kwds) {
			System.err.println(kwd.getLex() + "," + kwd.getCount());
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

	public void testFacetCount() {
		List<Document> docs = new ArrayList<Document>();
		{
			docs.add((new DocumentBuilder()).put("item1", "aaa").create());
			docs.add((new DocumentBuilder()).put("item1", "aaa").create());
			docs.add((new DocumentBuilder()).put("item1", "aaa").create());
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocuments(docs);
		}
		{
			List<Keyword> kwds = index.getItemCount("item1");
			for (Keyword kwd : kwds) {
				System.err.println(kwd.getLex() + "," + kwd.getCount());
			}
			assertEquals("aaa", kwds.get(0).getLex());
			assertEquals(3, kwds.get(0).getCount());
		}
		{
			assertEquals(0, index.getItemCount("itemXXX").size());
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
