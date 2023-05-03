package nlp4j.indexer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.Keyword;
import nlp4j.KeywordBuilder;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

public class SimpleDocumentIndexTestCase extends TestCase {

	/**
	 * <pre>
	 * すべての機能をテストする
	 * </pre>
	 */
	public void testAll001() {

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			// 月単位でカウントする
			index.setProperty(SimpleDocumentIndex.KEY_DATEFIELD, "date,yyyyMM");
		}
		{
			// document 001
			index.addDocument((new DocumentBuilder()).id("001") //
					.put("date", "2021-01-01") // date
					.kw("noun", "TEST1") // kw
					.build());
			// document 002
			index.addDocument((new DocumentBuilder()).id("002") //
					.put("date", "2021-01-01") // date
					.kw("noun", "TEST1") // kw
					.kw("noun", "TEST2") // kw
					.build());
			// document 003
			index.addDocument((new DocumentBuilder()).id("003") //
					.put("date", "2021-01-01") // date
					.kw("noun", "TEST1") // kw
					.kw("noun", "TEST2") // kw
					.kw("noun", "TEST3") // kw
					.build());
			// document 004
			index.addDocument((new DocumentBuilder()).id("004") //
					.put("date", "2021-02-01") // date
					.kw("noun", "TEST2") // kw
					.build());
			// document 005
			index.addDocument((new DocumentBuilder()).id("005") //
					.put("date", "2021-02-01") // date
					.kw("noun", "TEST2") // kw
					.build());
		}
		{ // 文書の数 NUMBER OF DOCUMENTS
			System.err.println("文書数: " + index.getDocumentCount());
		}
		{ // あるキーワードが含まれる文書の数
			long count = index.getDocumentCount(new DefaultKeyword("noun", "TEST1"));
			System.err.println(count);
		}
		{ // キーワードの数 NUMBER OF KEYWORDS
			System.err.println("キーワードの数: " + index.getKeywords().size());
			{ // キーワードの異なり数
				long expectedSize = 3;
				long keywordSize = index.getKeywords().size();
				System.err.println("キーワードの数: " + keywordSize);
				assertEquals(expectedSize, keywordSize);
			}
		}
		{ // キーワード毎のカウント COUNT OF KEYWORDS
			index.getKeywords().stream().forEach(kw -> {
				System.err.println(
						"keyword: " + "facet=" + kw.getFacet() + ",lex=" + kw.getLex() + ",count=" + kw.getCount());
			});
		}
		{ // 文書ID のリスト
			index.getDocumentIds().stream().forEach(id -> {
				System.err.println("document: id: " + id);
			});
		}
		{ // 文書IDを指定してドキュメント取得
			Document doc = index.getDocumentById("001");
			System.err.println(doc);
		}
		{ // キーワードの多い順
			index.getKeywords().stream().forEach(kw -> {
				System.err.println("facet:" + kw.getFacet() + ",lex:" + kw.getLex() + ",count:" + kw.getCount());
			});
		}
		{ // キーワードの数
			System.err.println("キーワード(noun,TEST1)の数:" + index.getKeywordCount(new DefaultKeyword("noun", "TEST1")));
			System.err.println("キーワード(noun,TEST2)の数:" + index.getKeywordCount(new DefaultKeyword("noun", "TEST2")));
			System.err.println("キーワード(noun,TEST3)の数:" + index.getKeywordCount(new DefaultKeyword("noun", "TEST3")));
		}
		{ // キーワードのIDF
			System.err.println("IDF(noun,TEST1):" + index.getkeywordIDF(new DefaultKeyword("noun", "TEST1")));
			System.err.println("IDF(noun,TEST2):" + index.getkeywordIDF(new DefaultKeyword("noun", "TEST2")));
			System.err.println("IDF(noun,TEST3):" + index.getkeywordIDF(new DefaultKeyword("noun", "TEST3")));
		}
		{ // キーワードのTF-IDF
			System.err.println("TF-IDF(noun,TEST1):" + index.getkeywordTFIDF(new DefaultKeyword("noun", "TEST1"), 1));
			System.err.println("TF-IDF(noun,TEST2):" + index.getkeywordTFIDF(new DefaultKeyword("noun", "TEST2"), 1));
			System.err.println("TF-IDF(noun,TEST3):" + index.getkeywordTFIDF(new DefaultKeyword("noun", "TEST3"), 1));
		}
		{ // 時系列
			for (Keyword kwd : index.getDateCountMonth()) {
				System.err.println(kwd.getFacet() + "," + kwd.getLex() + "," + kwd.getCount());
			}
		}
		{ // 共起キーワード
			List<Keyword> kwds = index.getRelevantKeywords(new DefaultKeyword("noun", "TEST1"), 0.0);
			for (Keyword kwd : kwds) {
				System.err.println(kwd.getLex() + "," + kwd.getCorrelation());
			}
		}
	}

	/**
	 * <pre>
	 * キーワードのカウントをテストする
	 * Test Keyword count
	 * </pre>
	 */
	public void testAddDocument001() {

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocument((new DocumentBuilder()).id("001").kw("noun", "TEST").build());
			index.addDocument((new DocumentBuilder()).id("002").kw("noun", "TEST").build());
			index.addDocument((new DocumentBuilder()).id("003").kw("noun", "TEST").build());
		}

		System.err.println(index.getKeywords().size());
		index.getKeywords().stream().forEach(kw -> {
			System.err.println("facet=" + kw.getFacet() + ",lex=" + kw.getLex() + ",count=" + kw.getCount());
		});
		long expectedCount = 3;
		long keywordCount = index.getKeywords().get(0).getCount();
		assertEquals(expectedCount, keywordCount);
	}

	/**
	 * <pre>
	 * キーワードのカウントをテストする
	 * Test Keyword count
	 * </pre>
	 */
	public void testAddDocument002() {
		DefaultDocument d1 = new DefaultDocument();
		{
			d1.addKeyword((new KeywordBuilder()).facet("facet1").lex("TEST").build());
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			d2.addKeyword((new KeywordBuilder()).facet("facet2").lex("TEST").build());
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocument(d1);
			index.addDocument(d2);
		}

		{
			int countExpected = 1;
			assertEquals(countExpected, index.getKeywords("facet1").size());
		}
		{
			int countExpected = 1;
			assertEquals(countExpected, index.getKeywords("facet2").size());
		}
		{
			int countExpected = 0;
			assertEquals(countExpected, index.getKeywords("facet0").size());
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

	public void testGetKeywordCount001() {
		DefaultDocument d1 = new DefaultDocument();
		{
			d1.addKeyword((new KeywordBuilder()).facet("noun").lex("TEST").build());
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			d2.addKeyword((new KeywordBuilder()).facet("noun").lex("TEST").build());
		}

		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocument(d1);
			index.addDocument(d2);
		}

		long expectedCount = 2;
		long keywordCount = index.getKeywordCount(new DefaultKeyword("noun", "TEST"));
		assertEquals(expectedCount, keywordCount);
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

	public void testGetDocumentsKeyword() {
		DefaultDocument d1 = new DefaultDocument();
		{
			d1.setText("Hi, OK");
			d1.addKeyword((new KeywordBuilder()).facet("noun").lex("TEST").build());
		}
		DefaultDocument d2 = new DefaultDocument();
		{
			d2.setText("Hi, NG");
			d2.addKeyword((new KeywordBuilder()).facet("noun").lex("TEST2").build());
		}
		SimpleDocumentIndex index = new SimpleDocumentIndex();
		{
			index.addDocument(d1);
			index.addDocument(d2);
		}
		List<Document> docs = index.getDocumentsByKeyword((new KeywordBuilder()).facet("noun").lex("TEST").build());
		System.err.println(docs.size());
		for (Document doc : docs) {
			System.err.println(doc.getText());
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
