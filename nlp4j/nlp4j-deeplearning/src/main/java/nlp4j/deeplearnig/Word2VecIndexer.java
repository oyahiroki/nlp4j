package nlp4j.deeplearnig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import nlp4j.Document;
import nlp4j.indexer.AbstractDocumentIndexer;

/**
 * Indexer for Word2Vec
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 */
public class Word2VecIndexer extends AbstractDocumentIndexer {

	String target = "text";
	String outfile = "model.txt";
	boolean document_tokenized = false;

	List<Document> docs = new ArrayList<>();

	// parameters
	int batch_size = 1000;
	int min_word_frequency = 5;
	boolean use_ada_grad = false;
	int layer_size = 150;
	int iterations = 5;
	long seed = 1;
	int window_size = 5;
	double learning_rate = 0.025;
	double min_learning_rate = 1e-3;
	double negative_sample = 10;
	int num_workers = 6;

	@Override
	public void addDocument(Document doc) {
		this.docs.add(doc);
	}

	/**
	 * set property of this class
	 * 
	 * <pre>
	 * "target" : field name of document
	 * "outfile" : out file of model file
	 * "document_tokenized": document is tokenized already: true | false
	 * </pre>
	 * 
	 * parameters
	 * 
	 * <pre>
	 * int batch_size = 1000;
	 * int min_word_frequency = 5;
	 * boolean use_ada_grad = false;
	 * int layer_size = 150;
	 * int iterations = 5;
	 * long seed = 1;
	 * int window_size = 5;
	 * double learning_rate = 0.025;
	 * double min_learning_rate = 1e-3;
	 * double negative_sample = 10;
	 * int num_workers = 6;
	 * </pre>
	 */
	@Override
	public void setProperty(String key, String value) {

		if ("target".equals(key)) {
			this.target = value;
		} //
		else if ("outfile".equals(key)) {
			this.outfile = value;
		} //
		else if ("document_tokenized".equals(key)) {
			this.document_tokenized = Boolean.parseBoolean(value);
		} //
		else if ("batch_size".equals(key)) {
			this.batch_size = Integer.parseInt(value);
		} //
		else if ("min_word_frequency".equals(key)) {
			this.min_word_frequency = Integer.parseInt(value);
		} //
		else if ("use_ada_grad".equals(key)) {
			this.use_ada_grad = Boolean.parseBoolean(value);
		} //
		else if ("layer_size".equals(key)) {
			this.layer_size = Integer.parseInt(value);
		} //
		else if ("iterations".equals(key)) {
			this.iterations = Integer.parseInt(value);
		} //
		else if ("seed".equals(key)) {
			this.seed = Long.parseLong(value);
		} //
		else if ("window_size".equals(key)) {
			this.window_size = Integer.parseInt(value);
		} //
		else if ("learning_rate".equals(key)) {
			this.learning_rate = Double.parseDouble(value);
		} //
		else if ("min_learning_rate".equals(key)) {
			this.min_learning_rate = Double.parseDouble(value);
		} //
		else if ("negative_sample".equals(key)) {
			this.negative_sample = Double.parseDouble(value);
		} //
		else if ("num_workers".equals(key)) {
			this.num_workers = Integer.parseInt(value);
		} //
		else {

		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void commit() throws IOException {

		try {

//			final EndingPreProcessor preProcessor = new EndingPreProcessor();

			TokenizerFactory tokenizer;

			if (this.document_tokenized) {
				tokenizer = new DefaultTokenizerFactory();
			} else {
				tokenizer = new KuromojiIpadicTokenizerFactory();
			}

//			tokenizer.setTokenPreProcessor(new TokenPreProcess() {
//
//				public String preProcess(String token) {
//					token = token.toLowerCase();
//					String base = preProcessor.preProcess(token);
//					base = base.replaceAll("\\d", "__NUMBER__");
//					return base;
//				}
//			});

//			File inFile = new File("file/neko.txt");

//			System.err.println("debug: inFile: " + inFile.getAbsolutePath());

//			List<String> lines = FileUtils.readLines(inFile, "UTF-8");

			ArrayList<String> ss = new ArrayList<String>();

//			for (String line : lines) {
//				if (line.length() > 8) {
//					docs.add(line);
//				} else {
//
//				}
//			}

			for (Document doc : docs) {
				String s = (String) doc.getAttribute(target);
				if (s != null) {
					ss.add(s);
				}
			}

			// 2.
			CollectionSentenceIterator it = new CollectionSentenceIterator(ss);

//			SentenceIterator iter = new BasicLineIterator(inFile);
			//

			Word2Vec vec = new Word2Vec //
					.Builder() //
							.batchSize(batch_size) //
							.minWordFrequency(min_word_frequency) //
							.useAdaGrad(use_ada_grad) //
							.layerSize(layer_size) //
							.iterations(iterations) //
							.seed(seed) //
							.windowSize(window_size) //
							.learningRate(learning_rate) //
							.minLearningRate(min_learning_rate) //
							.negativeSample(negative_sample) //
							.iterate(it) //
							.tokenizerFactory(tokenizer) //
							.workers(num_workers) //
							.build();

			// 4.
//			Word2Vec vec = new Word2Vec.Builder() //
//					.minWordFrequency(2) //
//					.iterations(5) //
//					.layerSize(100) //
////					.stopWords(stopwords) //
//					.iterate(it) //
//					.tokenizerFactory(tokenizer) //
//					.build();

			vec.fit();

			File out = new File(outfile);

			File directory = out.getParentFile();

			if (directory.exists() == false) {
				FileUtils.forceMkdir(directory);
			}

			System.err.println("writing: " + out.getAbsolutePath());

			WordVectorSerializer.writeWordVectors(vec, out);

			System.err.println("finished: " + out.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
