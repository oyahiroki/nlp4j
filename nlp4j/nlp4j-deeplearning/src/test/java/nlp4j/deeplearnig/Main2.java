package nlp4j.deeplearnig;

import java.io.File;
import java.util.List;

import nlp4j.Document;
import nlp4j.annotator.WakachiAnnotator;
import nlp4j.util.DocumentUtil;

public class Main2 {

	/**
	 * 形態素解析済みのドキュメントを読み込む
	 * 
	 * @throws Exception 例外発生時
	 */
	public static void main(String[] args) throws Exception {

		// 形態素解析済みのドキュメントを読み込む
		File jsonFile = new File("src/test/resources/file/neko_annotated_json.txt");
		List<Document> docs = DocumentUtil.readFromLineSeparatedJson(jsonFile);

		WakachiAnnotator ann1 = new WakachiAnnotator();
		ann1.setProperty("target", "text");

		ann1.annotate(docs);

		for (int n = 0; n < 10; n++) {
			System.err.println(docs.get(n));
		}

		Word2VecIndexer indexer = new Word2VecIndexer();
		indexer.setProperty("outfile", "src/test/resources/model/model-wordvectors2temp.txt");
		indexer.setProperty("document_tokenized", "true");
		indexer.setProperty("batch_size", "1000");
		indexer.setProperty("min_word_frequency", "5");
		indexer.setProperty("use_ada_grad", "false");
		indexer.setProperty("layer_size", "150");
		indexer.setProperty("iterations", "5");
		indexer.setProperty("seed", "1");
		indexer.setProperty("window_size", "5");
		indexer.setProperty("learning_rate", "0.025");
		indexer.setProperty("min_learning_rate", "1e-3");
		indexer.setProperty("negative_sample", "10");
		indexer.setProperty("num_workers", "6");

		long time1 = System.currentTimeMillis();

		indexer.addDocuments(docs);

		indexer.commit();

		long time2 = System.currentTimeMillis();

		System.err.println(time2 - time1); // 21484 on ThinkPad X1 2018

	}

}
