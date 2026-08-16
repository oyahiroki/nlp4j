package hello.kuromoji;

import java.util.List;

import com.atilika.kuromoji.TokenizerBase.Mode;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

public class HelloKuromojiMain2_Search {

	public static void main(String[] args) throws Exception {

		String text = "私はJALに乗って関西国際空港から旅行に行った。";

		Tokenizer tokenizer = (new Tokenizer.Builder()) //
				.mode(Mode.SEARCH) //
//				.isSplitOnNakaguro(false)
				.build();

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
// # expected output
//baseform: 私
//surface: 私
//reading: ワタシ
//begin: 0
//end: 1
//pos1: 名詞
//pos2: 代名詞
//pos3: 一般
//pos4: *
//---
//baseform: は
//surface: は
//reading: ハ
//begin: 1
//end: 2
//pos1: 助詞
//pos2: 係助詞
//pos3: *
//pos4: *
//---
//baseform: *
//surface: JAL
//reading: *
//begin: 2
//end: 5
//pos1: 名詞
//pos2: 一般
//pos3: *
//pos4: *
//---
//baseform: に
//surface: に
//reading: ニ
//begin: 5
//end: 6
//pos1: 助詞
//pos2: 格助詞
//pos3: 一般
//pos4: *
//---
//baseform: 乗る
//surface: 乗っ
//reading: ノッ
//begin: 6
//end: 8
//pos1: 動詞
//pos2: 自立
//pos3: *
//pos4: *
//---
//baseform: て
//surface: て
//reading: テ
//begin: 8
//end: 9
//pos1: 助詞
//pos2: 接続助詞
//pos3: *
//pos4: *
//---
//baseform: 関西
//surface: 関西
//reading: カンサイ
//begin: 9
//end: 11
//pos1: 名詞
//pos2: 固有名詞
//pos3: 地域
//pos4: 一般
//---
//baseform: 国際
//surface: 国際
//reading: コクサイ
//begin: 11
//end: 13
//pos1: 名詞
//pos2: 一般
//pos3: *
//pos4: *
//---
//baseform: 空港
//surface: 空港
//reading: クウコウ
//begin: 13
//end: 15
//pos1: 名詞
//pos2: 一般
//pos3: *
//pos4: *
//---
//baseform: から
//surface: から
//reading: カラ
//begin: 15
//end: 17
//pos1: 助詞
//pos2: 格助詞
//pos3: 一般
//pos4: *
//---
//baseform: 旅行
//surface: 旅行
//reading: リョコウ
//begin: 17
//end: 19
//pos1: 名詞
//pos2: サ変接続
//pos3: *
//pos4: *
//---
//baseform: に
//surface: に
//reading: ニ
//begin: 19
//end: 20
//pos1: 助詞
//pos2: 格助詞
//pos3: 一般
//pos4: *
//---
//baseform: 行く
//surface: 行っ
//reading: イッ
//begin: 20
//end: 22
//pos1: 動詞
//pos2: 自立
//pos3: *
//pos4: *
//---
//baseform: た
//surface: た
//reading: タ
//begin: 22
//end: 23
//pos1: 助動詞
//pos2: *
//pos3: *
//pos4: *
//---
//baseform: 。
//surface: 。
//reading: 。
//begin: 23
//end: 24
//pos1: 記号
//pos2: 句点
//pos3: *
//pos4: *
//---
