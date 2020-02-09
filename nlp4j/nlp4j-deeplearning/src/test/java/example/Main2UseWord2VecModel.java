package example;

import java.io.File;
import java.util.Collection;

import nlp4j.deeplearnig.Word2VecSearcher;
import nlp4j.deeplearnig.Words;

public class Main2UseWord2VecModel {

	public static void main(String[] args) throws Exception {

		// 「吾輩は猫である」から作った学習モデル
		File modelFile = new File("src/test/resources/model/model-wordvectors2.txt");

		Word2VecSearcher word2vec = new Word2VecSearcher(modelFile);

		Collection<String> words = word2vec.words();

		System.err.println("単語数: " + words.size());

		System.err.println("「猫」と「電車」の類似度: " + word2vec.similarity("猫", "電車"));

		System.err.println("「猫」と「犬」の類似度: " + word2vec.similarity("猫", "犬"));

		System.err.println("「猫」の類似語: " + word2vec.nearest("猫", 10));

		System.err.println("「猫-愛」: " + word2vec.nearest(Words.getNew().positive("猫").negative("愛"), 10));
	}

}
