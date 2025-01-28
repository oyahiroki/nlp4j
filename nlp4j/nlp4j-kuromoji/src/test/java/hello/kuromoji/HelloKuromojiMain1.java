package hello.kuromoji;

import java.util.List;

import com.atilika.kuromoji.TokenizerBase.Mode;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

public class HelloKuromojiMain1 {

	public static void main(String[] args) throws Exception {

		String text = "私はJALに乗って関西国際空港から旅行に行った。";

		Tokenizer tokenizer = (new Tokenizer.Builder()) //
				.mode(Mode.SEARCH) //
//				.isSplitOnNakaguro(false)
				.build();

		List<Token> tokens = tokenizer.tokenize(text);

		for (Token token : tokens) {
			System.err.println("baseform: " + token.getBaseForm());
			System.err.println("surface: " + token.getSurface());
			System.err.println("reading: " + token.getReading());
			System.err.println("begin: " + token.getPosition());
			System.err.println("end: " + (token.getPosition() + token.getSurface().length()));
			System.err.println("pos1: " + token.getPartOfSpeechLevel1());
			System.err.println("pos2: " + token.getPartOfSpeechLevel2());
			System.err.println("pos3: " + token.getPartOfSpeechLevel3());
			System.err.println("pos4: " + token.getPartOfSpeechLevel4());
			System.err.println("---");
		}

	}

}
