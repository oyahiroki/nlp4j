package nlp4j.nokku.chap4;

import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentAnnotatorPipeline;
import nlp4j.Keyword;
import nlp4j.annotator.KeywordSequencePatternAnnotator;
import nlp4j.crawler.Crawler;
import nlp4j.crawler.TextFileLineSeparatedCrawler;
import nlp4j.impl.DefaultDocumentAnnotatorPipeline;
import nlp4j.yhoo_jp.YJpMaAnnotator;

public class Nokku34c {

	public static void main(String[] args) throws Exception {
		String rule = "[{facet:'名詞'},{lex:'の'},{facet:'名詞'}]";
		String facet = "word_nn_no_nn";
		String value = "${0.lex}-${1.lex}-${2.lex}";

		// NLP4Jが提供するテキストファイルのクローラーを利用する
		Crawler crawler = new TextFileLineSeparatedCrawler();
		crawler.setProperty("file", "src/test/resources/nlp4j.crawler/neko_short_utf8.txt");
		crawler.setProperty("encoding", "UTF-8");
		crawler.setProperty("target", "text");

		// ドキュメントのクロール
		List<Document> docs = crawler.crawlDocuments();

		// NLPパイプライン（複数の処理をパイプラインとして連結することで処理する）の定義
		DocumentAnnotatorPipeline pipeline = new DefaultDocumentAnnotatorPipeline();
		{
			// Yahoo! Japan の形態素解析APIを利用するアノテーター
			DocumentAnnotator annotator = new YJpMaAnnotator();
			pipeline.add(annotator);
		}
		{
			KeywordSequencePatternAnnotator annotator = new KeywordSequencePatternAnnotator();
			annotator.setProperty("rule[0]", rule);
			annotator.setProperty("facet[0]", facet);
			annotator.setProperty("value[0]", value);
			pipeline.add(annotator);
		}
		// アノテーション処理の実行
		pipeline.annotate(docs);

		System.err.println("<抽出されたキーワード>");
		for (Document doc : docs) {
			for (Keyword kwd : doc.getKeywords(facet)) {
				System.err.println(kwd);
			}
		}
		System.err.println("</抽出されたキーワード>");

	}

}
