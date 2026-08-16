package hello.kuromoji;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.atilika.kuromoji.TokenizerBase.Mode;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

public class HelloKuromojiMain4_UserDicResource {

	public static void main(String[] args) throws Exception {

		String[] texts = { "令和の米騒動" };

		String userDic = "" //
				+ "# コメント行\r\n" //
				+ "# 単語,　形態素解析結果の単語, 読み, 品詞\r\n" //
				+ "令和,令和,レイワ,名詞\r\n" //
				+ "米騒動,米騒動,コメソウドウ,名詞\r\n" //
				+ "";

		Tokenizer tokenizer = (new Tokenizer.Builder()) //
				.mode(Mode.SEARCH) //
				.userDictionary(
						new ByteArrayInputStream(userDic.getBytes(StandardCharsets.UTF_8)))
//				.isSplitOnNakaguro(false)
				.build();

		for (String text : texts) {
			List<Token> tokens = tokenizer.tokenize(text);

			// 「Mode.SEARCH」だと「関西」「国際」「空港」の３つに切れる
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

}
// # expected output
//baseform: *
//surface: 令和
//reading: レイワ
//begin: 0
//end: 2
//pos1: 名詞
//pos2: *
//pos3: *
//pos4: *
//---
//baseform: の
//surface: の
//reading: ノ
//begin: 2
//end: 3
//pos1: 助詞
//pos2: 連体化
//pos3: *
//pos4: *
//---
//baseform: *
//surface: 米騒動
//reading: コメソウドウ
//begin: 3
//end: 6
//pos1: 名詞
//pos2: *
//pos3: *
//pos4: *
//---
