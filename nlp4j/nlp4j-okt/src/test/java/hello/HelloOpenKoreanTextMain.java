package hello;

import java.util.List;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import scala.collection.Seq;

public class HelloOpenKoreanTextMain {

	public static void main(String[] args) {
		// 今日は天気がよいので学校に歩いて行った
		String text = "오늘은 날씨가 좋아서 걸어서 학교에 갔다.";

		// Normalize
		CharSequence normalized = OpenKoreanTextProcessorJava.normalize(text);
		System.out.println(normalized);
		// 한국어를 처리하는 예시입니다ㅋㅋ #한국어

		// Tokenize
		Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
		System.out.println(OpenKoreanTextProcessorJava.tokensToJavaStringList(tokens));
		// [한국어, 를, 처리, 하는, 예시, 입니, 다, ㅋㅋ, #한국어]
		System.out.println(OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens));
		// [한국어(Noun: 0, 3), 를(Josa: 3, 1), 처리(Noun: 5, 2), 하는(Verb(하다): 7, 2), 예시(Noun:
		// 10, 2),
		// 입니다(Adjective(이다): 12, 3), ㅋㅋㅋ(KoreanParticle: 15, 3), #한국어(Hashtag: 19, 4)]

		// Phrase extraction
		List<KoreanPhraseExtractor.KoreanPhrase> phrases = OpenKoreanTextProcessorJava.extractPhrases(tokens, true,
				true);
		System.out.println(phrases);
		// [한국어(Noun: 0, 3), 처리(Noun: 5, 2), 처리하는 예시(Noun: 5, 7), 예시(Noun: 10, 2),
		// #한국어(Hashtag: 18, 4)]

	}

}
