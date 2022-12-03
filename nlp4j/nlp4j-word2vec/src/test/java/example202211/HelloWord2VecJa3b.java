package example202211;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

public class HelloWord2VecJa3b {

	public static void main(String[] args) throws IOException {

		String modelFileName = "files/example_ja/out/model.txt";

		{
			// 単語ベクトルの読込
			System.out.println("Load vectors...");
			File f = new File(modelFileName);
			WordVectors vec = WordVectorSerializer.loadTxtVectors(f);

			// 利用可能な単語を出力
			Collection<String> words = vec.vocab().words();
			System.out.println("利用可能な単語: " + words.size());

			// 評価1(二つの単語の類似性)
			// コサイン距離
			{
//				System.out.println("Evaluate model...");
//				String word1 = "lock";
//				String word2 = "unlock";
//				double similarity = vec.similarity(word1, word2);
//				System.out
//						.println(String.format("The similarity between 「%s」 and 「%s」 is %f", word1, word2, similarity));
			}
			{
//				System.out.println("Evaluate model...");
//				String word1 = "car";
//				String word2 = "vehicle";
//				double similarity = vec.similarity(word1, word2);
//				System.out
//						.println(String.format("The similarity between 「%s」 and 「%s」 is %f", word1, word2, similarity));
			}

			// 評価2(ある単語に最も意味が近い言葉)
			{
				String word = "タイヤ";
				printTop10(vec, word);
			}
			{
				String word = "ブレーキ";
				printTop10(vec, word);
			}
			{
				String word = "ゴールド";
				printTop10(vec, word);
			}
			{
				String word = "塗装";
				printTop10(vec, word);
			}
			{
				String word = "ドア";
				printTop10(vec, word);
			}
//			{
//				String word = "charging";
//				printTop10(vec, word);
//			}
//			{
//				String word = "user";
//				printTop10(vec, word);
//			}
//			{
//				String word = "driver";
//				printTop10(vec, word);
//			}
//			{
//				String word = "door";
//				printTop10(vec, word);
//			}
//			{
//				String word = "trip";
//				printTop10(vec, word);
//			}
//			{
//				String word = "phone";
//				printTop10(vec, word);
//			}
//			{
//				String word = "confirm";
//				printTop10(vec, word);
//			}
//			{
//				String word = "notify";
//				printTop10(vec, word);
//			}
//			{
//				String word = "detect";
//				printTop10(vec, word);
//			}
//			{
//				String word = "lock";
//				printTop10(vec, word);
//			}
//			
//			{
//				String word = "mobile";
//				printTop10(vec, word);
//			}
//			{
//				String word = "app";
//				printTop10(vec, word);
//			}

			// 単語の足し算
			System.out.println("単語の足し算");
			List<String> positiveList = Arrays.asList("車", "運転手");
			List<String> negativeList = new ArrayList<String>();
			Collection<String> nearestList = vec.wordsNearest(positiveList, negativeList, 10);
			System.out.println(String.format("%s = %s", String.join(" + ", positiveList), nearestList));

			// 単語の足し算・引き算
			System.out.println("単語の足し算・引き算");
			positiveList = Arrays.asList("車");
			negativeList = Arrays.asList("事故");
			nearestList = vec.wordsNearest(positiveList, negativeList, 10);
			System.out.println(String.format("%s - %s = %s", String.join(" + ", positiveList),
					String.join(" + ", negativeList), nearestList));

		}
	}

	private static void printTop10(WordVectors vec, String word1) {

		int ranking = 30;
		double minSimilarity = 0.3;

		Collection<String> similarTop10 = vec.wordsNearest(word1, ranking);
//		System.out.println(String.format("Similar word to 「%s」 is %s", word1, similarTop10));

		StringBuilder out = new StringBuilder();
//		String pos = getPos(word1, 0);
//		out.append(word1 + " " + pos + "\t,");
		boolean print = false;
		for (String word2 : similarTop10) {
			double similarity = vec.similarity(word1, word2);
			if (similarity > minSimilarity) {
				print = true;
//				String wordPos = getPos(word2 + " " + word1, 0);
				out.append(String.format(" %s(%.3f),", word2.replace("\uFFFD", ""), similarity));
			}
		}
		if (print) {
			System.out.println(word1 + " -> " + out);
		}
//		try {
//			FileUtils.write(new File("/usr/local/nissan/textanalytics/models/word2vec_model_2_out.txt"), out + "\n", "UTF-8", true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

//	static StanfordPosAnnotator posann;
//
//	static private String getPos(String word, int idx) {
//		if (posann == null) {
//			posann = new StanfordPosAnnotator();
//		}
//		Document doc = new Document();
//		doc.putAttribute("description", word);
//
//		posann.annotate(doc);
//
//		if (idx < doc.getKeywords().size()) {
//			return doc.getKeywords().get(idx).getFacet();
//		}
//
////		for (Keyword kwd : doc.getKeywords()) {
////			return kwd.getFacet();
////		}
//
//		return null;
//
//	}

}
