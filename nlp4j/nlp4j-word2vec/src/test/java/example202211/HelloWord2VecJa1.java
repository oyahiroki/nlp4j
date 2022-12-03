package example202211;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.importer.PlainTextOutImporter;
import nlp4j.util.DocumentUtil;
import nlp4j.word2vec.Word2VecJaAnnotator;

public class HelloWord2VecJa1 {

	public static void main(String[] args) throws Exception {

		List<Document> docs = null;

		{ // 改行区切りJSONを読み込む
			Crawler crawler = new JsonLineSeparatedCrawler();
			String fileName = "files/example_ja/mlit_carinfo-202112_json.txt";
			crawler.setProperty("file", fileName);
			docs = crawler.crawlDocuments();
			System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));
			System.err.println("docs: " + docs.size());
			System.err.println(docs.get(0).getAttribute("申告内容の要約"));
		}
		{ // Kuromojiで形態素解析
			Word2VecJaAnnotator ann = new Word2VecJaAnnotator();
			ann.setProperty("target", "申告内容の要約");
			ann.setProperty("annotator", "nlp4j.krmj.annotator.KuromojiAnnotator");
			ann.annotate(docs);
		}

		{ // 単語区切りのテキストファイルを出力
			PlainTextOutImporter importer = new PlainTextOutImporter();
			importer.setProperty("target", "text2");
			importer.setProperty("encoding", "UTF-8");
			importer.setProperty("outfile", "files/example_ja/out/mlit_carinfo-202112_json_word_splitted.txt");

			importer.importDocuments(docs);

		}

	}

}
