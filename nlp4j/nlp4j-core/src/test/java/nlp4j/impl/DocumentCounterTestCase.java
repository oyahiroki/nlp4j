package nlp4j.impl;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.counter.Count;
import nlp4j.counter.DocumentCounter;
import nlp4j.counter.DocumentCounterBuilder;

public class DocumentCounterTestCase extends TestCase {

	public void test001() throws Exception {

		DocumentCounter counter = DocumentCounterBuilder.create() //
				.idField("id").faceFilter("名詞").faceFilter("動詞").build();
		{
			DefaultDocument doc = new DefaultDocument();
			{
				doc.putAttribute("id", "001");
				doc.putAttribute("text", "私は学校に行きました。");
			}
			{
				doc.addKeyword(new DefaultKeyword(0, 1, "名詞", "私", "私"));
				doc.addKeyword(new DefaultKeyword(1, 2, "助詞", "は", "は"));
				doc.addKeyword(new DefaultKeyword(2, 4, "名詞", "学校", "学校"));
				doc.addKeyword(new DefaultKeyword(5, 6, "助詞", "に", "に"));
				doc.addKeyword(new DefaultKeyword(6, 8, "動詞", "行く", "行き"));
				doc.addKeyword(new DefaultKeyword(8, 10, "助動詞", "ます", "まし"));
				doc.addKeyword(new DefaultKeyword(10, 11, "助動詞", "た", "た"));
				doc.addKeyword(new DefaultKeyword(11, 12, "記号", "。", "。"));
			}
			counter.add(doc);
		}
		{
			DefaultDocument doc = new DefaultDocument();
			{
				doc.putAttribute("id", "002");
				doc.putAttribute("text", "私は学校に行く。学校から帰る。");
			}
			{
				doc.addKeyword(new DefaultKeyword(0, 2, "名詞", "学校", "学校"));
				doc.addKeyword(new DefaultKeyword(2, 3, "助詞", "に", "に"));
				doc.addKeyword(new DefaultKeyword(3, 5, "動詞", "行く", "行く"));
				doc.addKeyword(new DefaultKeyword(5, 6, "記号", "。", "。"));
				doc.addKeyword(new DefaultKeyword(6, 8, "名詞", "学校", "学校")); // 2nd in a single document
				doc.addKeyword(new DefaultKeyword(8, 10, "助詞", "から", "から"));
				doc.addKeyword(new DefaultKeyword(10, 12, "動詞", "帰る", "帰る"));
				doc.addKeyword(new DefaultKeyword(12, 13, "記号", "。", "。"));
			}
			counter.add(doc);
		}
		{
			DefaultDocument doc = new DefaultDocument();
			{
				doc.putAttribute("id", "003");
				doc.putAttribute("text", "彼は会社に行きました。");
			}
			{
				doc.addKeyword(new DefaultKeyword(0, 1, "名詞", "彼", "彼"));
				doc.addKeyword(new DefaultKeyword(1, 2, "助詞", "は", "は"));
				doc.addKeyword(new DefaultKeyword(2, 4, "名詞", "会社", "会社"));
				doc.addKeyword(new DefaultKeyword(5, 6, "助詞", "に", "に"));
				doc.addKeyword(new DefaultKeyword(6, 8, "動詞", "行く", "行き"));
				doc.addKeyword(new DefaultKeyword(8, 10, "助動詞", "ます", "まし"));
				doc.addKeyword(new DefaultKeyword(10, 11, "助動詞", "た", "た"));
				doc.addKeyword(new DefaultKeyword(11, 12, "記号", "。", "。"));
			}
			counter.add(doc);
		}

		System.err.println("Documents: " + counter.getCountDocument());

		{
			System.err.println("Keyword Count");
			List<Count<Keyword>> cc = counter.getCountKeywordSorted();

			for (Count<Keyword> c : cc) {
				System.err.println(c);
			}
		}
		{
			System.err.println("Keyword Count DF");
			List<Count<Keyword>> cc = counter.getCountKeywordSortedDF();

			for (Count<Keyword> c : cc) {
				System.err.println(c);
			}
		}
		{
			System.err.println("IDF");
			Keyword kw = new DefaultKeyword("名詞", "学校");
			System.err.println("idf(" + kw.getLex() + ") -> " + counter.getIDF(kw));
		}
		{
			System.err.println("IDF");
			Keyword kw = new DefaultKeyword("名詞", "会社");
			System.err.println("idf(" + kw.getLex() + ") -> " + counter.getIDF(kw));
		}
		{
			System.err.println("IDF for All Keywords");
			List<Count<Keyword>> cc = counter.getCountKeywordSortedDF();
			for (Count<Keyword> c : cc) {
				Keyword kw = c.getValue();
				System.err.println("idf(" + kw.getLex() + ") -> " + counter.getIDF(kw));
			}
		}
		{
			System.err.println("Get Document by ID");
			System.err.println(counter.getDocument("001"));
		}

	}

}
