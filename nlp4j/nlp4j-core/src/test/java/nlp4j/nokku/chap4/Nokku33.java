package nlp4j.nokku.chap4;

import java.util.List;
import java.util.stream.Collectors;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentAnnotatorPipeline;
import nlp4j.Keyword;
import nlp4j.crawler.Crawler;
import nlp4j.crawler.TextFileLineSeparatedCrawler;
import nlp4j.impl.DefaultDocumentAnnotatorPipeline;
import nlp4j.index.DocumentIndex;
import nlp4j.index.SimpleDocumentIndex;
import nlp4j.yhoo_jp.YJpMaAnnotator;

public class Nokku33 {

	public static void main(String[] args) throws Exception {

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
		// アノテーション処理の実行
		pipeline.annotate(docs);

		// キーワードをカウントするためにDocumentIndexを利用します。
		SimpleDocumentIndex index = new SimpleDocumentIndex();
		// ドキュメントの追加
		index.addDocuments(docs);

//		for (Keyword kwd : index.getKeywords("動詞")) {
//			System.err.println(kwd);
//		}

		List<Keyword> kwds = index.getKeywordsWithoutCount();

		String meishi = null;

		for (Keyword kwd : kwds) {
//			System.err.println(kwd.getLex());

			if (kwd.getFacet().equals("名詞")) {
				meishi = kwd.getLex();
			} //
			else if (meishi != null && kwd.getLex().equals("する")) {
				System.err.println(meishi + kwd.getLex());
				meishi = null;
			} //
			else {
				meishi = null;
			}

		}

	}

}