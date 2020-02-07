package nlp4j.deeplearnig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;

public class Main2 {

	public static void main(String[] args) throws IOException {

		{
			// 単語ベクトルの読込
			System.out.println("Load vectors...");
			File f = new File("file/model-wordvectors.txt");

			WordVectors vec = WordVectorSerializer.loadTxtVectors(f);

//			// 利用可能な単語を出力
			Collection<String> words = vec.vocab().words();
//			System.out.println("利用可能な単語");
//			for (String word : words) {
////				System.out.println(" " + word);
//				printTop10(vec, word);
//			}

			// 評価1(二つの単語の類似性)
			// コサイン距離
			{
				System.out.println("Evaluate model...");
				String word1 = "猫";
				String word2 = "犬";
				double similarity = vec.similarity(word1, word2);
				System.out
						.println(String.format("The similarity between 「%s」 and 「%s」 is %f", word1, word2, similarity));
			}
			{
				System.out.println("Evaluate model...");
				String word1 = "猫";
				String word2 = "ビール";
				double similarity = vec.similarity(word1, word2);
				System.out
						.println(String.format("The similarity between 「%s」 and 「%s」 is %f", word1, word2, similarity));
			}

			// 評価2(ある単語に最も意味が近い言葉)
			{
				String word = "東京";
				printTop10(vec, word);
			}
			{
				String word = "猫";
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
			{
				System.out.println("Positive : 単語の足し算");
				List<String> positiveList = Arrays.asList("コップ", "ビール");
				List<String> negativeList = new ArrayList<String>();
				Collection<String> nearestList = vec.wordsNearest(positiveList, negativeList, 10);
				System.out.println(String.format("%s = %s", String.join(" + ", positiveList), nearestList));
			}
			// 単語の足し算
			{
				System.out.println("Positive : 単語の足し算");
				List<String> positiveList = Arrays.asList("東京", "権力");
				List<String> negativeList = new ArrayList<String>();
				Collection<String> nearestList = vec.wordsNearest(positiveList, negativeList, 10);
				System.out.println(String.format("%s = %s", String.join(" + ", positiveList), nearestList));
			}

			// 単語の足し算・引き算
			{
				List<String> positiveList = Arrays.asList("先生");
				List<String> negativeList = Arrays.asList("酒");
				System.out.println("Negative : 単語の足し算・引き算");
				Collection<String> nearestList = vec.wordsNearest(positiveList, negativeList, 10);
				System.out.println(String.format("%s - %s = %s", String.join(" + ", positiveList),
						String.join(" + ", negativeList), nearestList));

			}

		}
	}

	private static void printTop10(WordVectors vec, String word1) {
		int ranking = 30;
		Collection<String> similarTop10 = vec.wordsNearest(word1, ranking);
//		System.out.println(String.format("Similar word to 「%s」 is %s", word1, similarTop10));

		String out = "";
		out += word1 + ",";

		for (String word2 : similarTop10) {
			double similarity = vec.similarity(word1, word2);
			out += String.format(" %s(%.2f),", word2, similarity);
		}

		System.out.println(out);
		try {
			FileUtils.write(new File("src/main/resources/scr_word2vec_dump.csv"), out + "\n", "UTF-8", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
