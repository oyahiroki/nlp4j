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
import nlp4j.crawler.TextFileLineSeparatedCrawler;
import nlp4j.impl.DefaultDocument;
import nlp4j.importer.PlainTextOutImporter;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

public class HelloWord2VecJa3 {

	public static void main(String[] args) throws Exception {

		List<Document> docs = null;
		{
			String fileName = "C:/usr/local/nlp4j/collections/mlit/models/data.txt";
			TextFileLineSeparatedCrawler crawler = new TextFileLineSeparatedCrawler();
			crawler.setProperty("file", fileName);
			docs = crawler.crawlDocuments();
		}

		{
			Word2VecOutImporter out = new Word2VecOutImporter();
			out.setProperty("outfile", "C:/usr/local/nlp4j/collections/mlit/models/model.txt");
			out.importDocuments(docs);
			out.commit();
			out.close();
		}

		boolean b = true;
		if (b) {
			return;
		}

//		ArrayList<String> ss = new ArrayList<>();
//
//		// 2.
//		CollectionSentenceIterator it = new CollectionSentenceIterator(ss);
//		// 3.
//		TokenizerFactory tokenizer = new DefaultTokenizerFactory();
//		final EndingPreProcessor preProcessor = new EndingPreProcessor();
//		tokenizer.setTokenPreProcessor(new TokenPreProcess() {
//			public String preProcess(String token) {
//				token = token.toLowerCase();
//				String base = preProcessor.preProcess(token);
//				base = base.replaceAll("/d", "d");
//				return base;
//			}
//		});
//		int batchSize = 1000; // 1回のミニバッチで学習する単語数
//		int iterations = 3;
//		int layerSize = 150;
//		int minWordFrequency = 5;
//
//		batchSize = 3000;
//		iterations = 10;
//		minWordFrequency = 2;
//
//		Word2Vec vec = new Word2Vec.Builder().batchSize(batchSize) // ミニバッチのサイズ
//				.minWordFrequency(minWordFrequency) // 単語の最低出現回数。ここで指定した回数以下の出現回数の単語は学習から除外される
//				.useAdaGrad(false) // AdaGradを利用するかどうか
//				.layerSize(layerSize) // 単語ベクトルの次元数。
//				.iterations(iterations) // 学習時の反復回数
//				.learningRate(0.025) // 学習率
//				.minLearningRate(1e-3) // 学習率の最低値
//				.negativeSample(10) //
//				.iterate(it) // 文章データクラス
//				.tokenizerFactory(tokenizer) // 単語分解クラス
//				.build();
//
//		// 5.
//		vec.fit();
//
//		// モデルを保存
//		System.out.println("Save Model...");
//		WordVectorSerializer.writeWordVectors(vec,
//				"C:/usr/local/nlp4j/collections/milt/models/word2vec_model_milt.txt");

	}

}
