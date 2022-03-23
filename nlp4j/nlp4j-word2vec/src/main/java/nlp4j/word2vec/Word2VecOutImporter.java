package nlp4j.word2vec;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.EndingPreProcessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import nlp4j.Document;
import nlp4j.importer.AbstractDocumentImporter;

/**
 * Output Plain text file
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class Word2VecOutImporter extends AbstractDocumentImporter {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private File outFile = null;
	private String target = "text";

	ArrayList<String> ss = new ArrayList<>();

	@Override
	public void close() {
		logger.info("close");
	}

	@Override
	public void commit() throws IOException {
		logger.info("commit");

		// 2.
		CollectionSentenceIterator it = new CollectionSentenceIterator(ss);
		// 3.

		TokenizerFactory tokenizer = new DefaultTokenizerFactory();
//		final EndingPreProcessor preProcessor = new EndingPreProcessor();
//		tokenizer.setTokenPreProcessor(new TokenPreProcess() {
//			public String preProcess(String token) {
//				token = token.toLowerCase();
//				String base = preProcessor.preProcess(token);
//				base = base.replaceAll("/d", "d");
//				return base;
//			}
//		});

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
				.build(); // Exception in thread "main" java.lang.ExceptionInInitializerError
			// Caused by: java.lang.RuntimeException: org.nd4j.linalg.factory.Nd4jBackend$NoAvailableBackendException: Please ensure that you have an nd4j backend on your classpath. Please see: http://nd4j.org/getstarted.html

		// 5.
		vec.fit();

		// モデルを保存
		System.out.println("Save Model...");
		FileUtils.touch(this.outFile);
		WordVectorSerializer.writeWordVectors(vec, this.outFile);

	}

	@Override
	public void importDocument(Document doc) throws IOException {
		String s = doc.getAttributeAsString(target);
		if (s != null && s.trim().isEmpty() == false) {
			ss.add(s.trim());
		}
	}

	/**
	 * @param key   file | encoding
	 * @param value filepath | encoding name
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if (key == null) {
			return;
		} else {
			if (key.equals("outfile")) {
				this.outFile = new File(value);
			} //
			else if (key.equals("target")) {
				this.target = value;
			} //
		}
	}

}
