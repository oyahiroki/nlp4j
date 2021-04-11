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

public class HelloWord2VecJa_bak {

	public static void main(String[] args) throws Exception {
		// http://krr.blog.shinobi.jp/java_deeplearning/

//		ArrayList<String> ss = new ArrayList<String>();
		String target = "CDESCR";
//		{
//			Crawler crawler = new JsonLineSeparatedCrawler();
//			String fileName = "/usr/local/nlp4j/collections/nhtsa/data/nhtsa2020_NISSAN_20200315.json";
//			crawler.setProperty("file", fileName);
//
//			List<Document> docs = crawler.crawlDocuments();
//
//			System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));
//
//			System.err.println("docs: " + docs.size());
//
//			for (Document doc : docs) {
//				ss.add(doc.getAttributeAsString(target));
//			}
//
//		}

		List<Document> docs = null;

		{
			Crawler crawler = new JsonLineSeparatedCrawler();
			String fileName = "C:/usr/local/nlp4j/collections/mlit/data/json1/milt_carinfo-20190101-20201231_json.txt";
			crawler.setProperty("file", fileName);

			docs = crawler.crawlDocuments();

			System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));
			System.err.println("docs: " + docs.size());
			System.err.println(docs.get(0).getAttribute("申告内容の要約"));

			int idx = 0;

//			for (Document doc : docs) {
//
//				ArrayList<String> ss = new ArrayList<String>();
//
//				String text = doc.getAttributeAsString("申告内容の要約");
//				text = Normalizer.normalize(text, Normalizer.Form.NFKC);
//
//				// ドキュメントの用意（CSVを読み込むなどでも可）
//				Document doc2 = new DefaultDocument();
//				doc2.setText(text);
//
//				// 形態素解析アノテーター
//				DocumentAnnotator annotator = new KuromojiAnnotator(); // 形態素解析
//				annotator.setProperty("target", "text");
//				{
//					annotator.annotate(doc2);
//				}
//				{
////					System.err.println(doc2);
////					System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc2)));
//				}
//
////				ArrayList<String> ss = new ArrayList<>();
//				StringBuilder sb = new StringBuilder();
//
//				for (int n = 0; n < doc2.getKeywords().size(); n++) {
//					Keyword kwd = doc2.getKeywords().get(n);
//
//					if (kwd.getLex().equals("。") || kwd.getLex().equals("．")) {
//						ss.add(sb.toString());
//						sb = new StringBuilder();
//					} //
//					else if ((n == (doc.getKeywords().size() - 1))) {
//						if (sb.length() != 0) {
//							sb.append(" ");
//						}
//						String lex = kwd.getLex();
//						if (lex.equals("*")) {
//							sb.append(kwd.getStr());
//						} //
//						else {
//							sb.append(lex);
//						}
//						ss.add(sb.toString());
//						sb = new StringBuilder();
//					} //
//					else {
//						if (sb.length() != 0) {
//							sb.append(" ");
//						}
//						String lex = kwd.getLex();
//						if (lex.equals("*")) {
//							sb.append(kwd.getStr());
//						} //
//						else {
//							sb.append(lex);
//						}
//					}
//				}
//
//				doc.putAttribute("text", String.join("\n", ss));
//
//				if (idx > 10) {
//					break;
//				}
//				idx++;
//
//			}

		}
		{
//			ArrayList<Document> docs2 = new ArrayList<>();
//			for (int n = 0; n < 10; n++) {
//				docs2.add(docs.get(n));
//			}
//
//			docs = docs2;
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

//		SentenceIterator ite = new CollectionSentenceIterator(ss);

//		ite.setPreProcessor(new SentencePreProcessor() {
//			public String preProcess(String sentence) {
//				if (sentence != null) {
//					return sentence.toLowerCase();
//				} else {
//					return "";
//				}
//			}
//		});

		{
			Word2VecOutImporter out = new Word2VecOutImporter();
			out.importDocuments(docs);
			out.commit();
			out.close();
		}

		boolean b = true;
		if (b) {
			return;
		}

		ArrayList<String> ss = new ArrayList<>();

		// 2.
		CollectionSentenceIterator it = new CollectionSentenceIterator(ss);
		// 3.
		TokenizerFactory tokenizer = new DefaultTokenizerFactory();
		final EndingPreProcessor preProcessor = new EndingPreProcessor();
		tokenizer.setTokenPreProcessor(new TokenPreProcess() {
			public String preProcess(String token) {
				token = token.toLowerCase();
				String base = preProcessor.preProcess(token);
				base = base.replaceAll("/d", "d");
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
				"C:/usr/local/nlp4j/collections/milt/models/word2vec_model_milt.txt");

	}

}
