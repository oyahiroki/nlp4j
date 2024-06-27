package example;

import java.io.File;

import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.impl.DefaultKeyword;
import nlp4j.indexer.SimpleDocumentIndex;
import nlp4j.trie.TrieDictionaryAnnotator;
import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class Wiktionary010ReadIndexExample1f_index {

	public static void main(String[] args) throws Exception {

//		Document doc = (new DocumentBuilder()).text("今日は東京ドームでイチローが引退試合を行った記念日である。大谷翔平、同志社大学、松井秀喜").build();

//		System.err.println(doc.toString());

		File indexFile = MediaWikiFileUtils.getIndexFile( //
				"/usr/local/wiki/jawiki/20230101", //
				"ja", //
				"wiki", //
				"20230101"); //

		// 1. Wikipedia見出しから辞書Annotatorを作成する

		TrieDictionaryAnnotator ann = new TrieDictionaryAnnotator();
		ann.setProperty("target", "text");
		{ // Wikipedia見出し読み込み
			System.err.println(indexFile.getAbsolutePath());
			// Read Wiki Index File
			WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
			{
				int count = 1;
				// FOR EACH(WikiItemTitles) THEN print info
				for (String title : wikiIndex.getWikiItemTitles()) {
					ann.setProperty("add1", title);
					count++;
				} // END OF FOR EACH
			}
		}

		// キーワードインデックス（統計処理）の用意
		SimpleDocumentIndex index = new SimpleDocumentIndex(1000 * 1000);
		{
			// Read Wiki Index File
			WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile); // throws IOException
			int count = 0;
			long time1 = System.currentTimeMillis();
			// ドキュメントの用意（CSVを読み込むなどでも可）
			for (String title : wikiIndex.getWikiItemTitles()) {
				count++;
				if (count % 10000 == 0) {
					long time2 = System.currentTimeMillis();
					System.err.println("Processing... " + count + " (" + (time2 - time1) + ")");
					time1 = System.currentTimeMillis();
				}
				Document d = (new DocumentBuilder()).text(title).build();
				ann.annotate(d);
//				index.addDocument(d, false, false, true, false, false);
			} // END OF FOR EACH
			System.err.println("Document size: " + index.getDocumentSize());
		}

		System.err.println(index.getkeywordTFIDF(new DefaultKeyword("word", "イチロー"), 1));
		System.err.println(index.getkeywordTFIDF(new DefaultKeyword("word", "イチ"), 1));
		System.err.println(index.getkeywordTFIDF(new DefaultKeyword("word", "イ"), 1));

//		ann.annotate(doc);
//
//		System.err.println(doc.toString());
//
//		{
//			System.err.println(doc.getText());
//			doc.getKeywords().forEach(kw -> {
//				System.err.println(kw.toString());
//			});
//		}

	}

}
