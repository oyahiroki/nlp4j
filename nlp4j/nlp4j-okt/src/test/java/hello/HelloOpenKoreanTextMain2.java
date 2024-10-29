package hello;

import java.util.List;

import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;

import scala.collection.Seq;

public class HelloOpenKoreanTextMain2 {

	public static void main(String[] args) {

		// 今日は天気がよいので学校に歩いて行った
		String text = "오늘은 날씨가 좋아서 걸어서 학교에 갔다.";

		// Normalize
		CharSequence normalized = OpenKoreanTextProcessorJava.normalize(text);

		// Tokenize
		Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);

		List<KoreanTokenJava> kk = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);

		for (KoreanTokenJava k : kk) {
			System.out.println("begin: " + k.getOffset());
			System.out.println("end: " + (k.getOffset() + k.getLength()));
			System.out.println("length: " + k.getLength());
			System.out.println("lex: " + k.getStem()); // LEX 原型
			System.out.println("str: " + k.getText()); // STR
			System.out.println("pos: " + k.getPos().name()); // JOSA 助詞 Noun 名詞 Adjective 形容詞 Verb 動詞 Punctation ピリオド
			System.out.println("isUnknown: " + k.isUnknown());
			System.out.println("---");
		}

	}

}
