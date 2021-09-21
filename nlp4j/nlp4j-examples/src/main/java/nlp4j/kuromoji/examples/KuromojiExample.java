package nlp4j.kuromoji.examples;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("javadoc")
public class KuromojiExample {
	public static void main(String[] args) {
		Tokenizer tokenizer = new Tokenizer();
		List<Token> tokens = tokenizer.tokenize("お寿司が食べたい。カレーも食べたい。");
		System.out.println("---");
		for (Token token : tokens) {
			System.err.println(token.getSurface() + "\t" + token.getAllFeatures());
		}
		System.out.println("---");
		for (Token token : tokens) {
			System.err.println(token);
			System.err.println(token.getBaseForm()); // 食べる
			System.err.println(token.getConjugationForm()); // 連用形
			System.err.println(token.getConjugationType()); // 一段
			System.err.println(token.getReading()); // タベ 読み
			System.err.println(token.getSurface()); // 食べ
			System.err.println(token.getPosition()); // 13
			System.err.println(token.getPartOfSpeechLevel1()); // 動詞
			System.err.println(token.getPartOfSpeechLevel2()); // 自立
			System.err.println(token.getPartOfSpeechLevel3()); // *
			System.err.println(token.getPartOfSpeechLevel4()); // *
			System.err.println(token.getPronunciation()); // タベ 発音
			System.err.println(Arrays.toString(token.getAllFeaturesArray()));
			System.err.println("---");
		}
	}
}
