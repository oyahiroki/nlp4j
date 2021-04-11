package nlp4j.word2vec;

import java.util.ArrayList;
import java.util.List;

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
import nlp4j.crawler.Crawler;
import nlp4j.crawler.JsonLineSeparatedCrawler;
import nlp4j.util.DocumentUtil;

public class HelloWord2Vec1 {

	public static void main(String[] args) throws Exception {
		// http://krr.blog.shinobi.jp/java_deeplearning/

		ArrayList<String> ss = new ArrayList<String>();
		String target = "CDESCR";
		{
			Crawler crawler = new JsonLineSeparatedCrawler();
			String fileName = "/usr/local/nlp4j/collections/nhtsa/data/nhtsa2020_NISSAN_20200315.json";
			crawler.setProperty("file", fileName);

			List<Document> docs = crawler.crawlDocuments();

			System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));

			System.err.println("docs: " + docs.size());

			for (Document doc : docs) {
				ss.add(doc.getAttributeAsString(target));
			}

		}

		SentenceIterator ite = new CollectionSentenceIterator(ss);
		ite.setPreProcessor(new SentencePreProcessor() {
			public String preProcess(String sentence) {
				if (sentence != null) {
					return sentence.toLowerCase();
				} else {
					return "";
				}
			}
		});

		// 2.
		CollectionSentenceIterator it = new CollectionSentenceIterator(ss);
		// 3.
		TokenizerFactory tokenizer = new DefaultTokenizerFactory();
		final EndingPreProcessor preProcessor = new EndingPreProcessor();
		tokenizer.setTokenPreProcessor(new TokenPreProcess() {
			public String preProcess(String token) {
				token = token.toLowerCase();
				String base = preProcessor.preProcess(token);
				base = base.replaceAll("\\d", "d");
				return base;
			}
		});
		int batchSize = 1000; // 1回のミニバッチで学習する単語数
		int iterations = 3;
		int layerSize = 150;
		int minWordFrequency = 5;

		batchSize = 3000;
		iterations = 10;
		minWordFrequency = 2;

		Word2Vec vec = new Word2Vec.Builder().batchSize(batchSize) // ミニバッチのサイズ
				.minWordFrequency(minWordFrequency) // 単語の最低出現回数。ここで指定した回数以下の出現回数の単語は学習から除外される
				.useAdaGrad(false) // AdaGradを利用するかどうか
				.layerSize(layerSize) // 単語ベクトルの次元数。
				.iterations(iterations) // 学習時の反復回数
				.learningRate(0.025) // 学習率
				.minLearningRate(1e-3) // 学習率の最低値
				.negativeSample(10) //
				.iterate(it) // 文章データクラス
				.tokenizerFactory(tokenizer) // 単語分解クラス
				.build();

		// 5.
		vec.fit();

		// モデルを保存
		System.out.println("Save Model...");
		WordVectorSerializer.writeWordVectors(vec,
				"C:\\usr\\local\\nlp4j\\collections\\nhtsa\\models\\word2vec_model_nissan.txt");

	}

}
