package nlp4j.word2vec;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.EndingPreProcessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.crawler.Crawler;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.impl.DefaultDocument;
import nlp4j.importer.PlainTextOutImporter;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

public class HelloWord2VecJa1 {

	public static void main(String[] args) throws Exception {

		List<Document> docs = null;

		{
			Crawler crawler = new JsonLineSeparatedCrawler();
			String fileName = "C:/usr/local/nlp4j/collections/mlit/data/json1/milt_carinfo-20190101-20201231_json.txt";
			crawler.setProperty("file", fileName);

			docs = crawler.crawlDocuments();

			System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));
			System.err.println("docs: " + docs.size());
			System.err.println(docs.get(0).getAttribute("申告内容の要約"));

		}
		{
			Word2VecJaAnnotator ann = new Word2VecJaAnnotator();
			ann.setProperty("target", "申告内容の要約");
			ann.annotate(docs);
		}

		{
			PlainTextOutImporter importer = new PlainTextOutImporter();
			importer.setProperty("target", "text2");
			importer.setProperty("encoding", "UTF-8");
			importer.setProperty("outfile", "C:/usr/local/nlp4j/collections/mlit/models/data.txt");

			importer.importDocuments(docs);

		}

	}

}
