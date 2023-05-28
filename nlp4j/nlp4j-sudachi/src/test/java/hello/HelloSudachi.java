package hello;

import java.io.IOException;
import java.util.List;

import com.worksap.nlp.sudachi.Dictionary;
import com.worksap.nlp.sudachi.DictionaryFactory;
import com.worksap.nlp.sudachi.Morpheme;
import com.worksap.nlp.sudachi.Tokenizer;

public class HelloSudachi {

	public static void main(String[] args) throws Exception {

		String dicFileName = "system_full.dic";
		String text = "Sudachi は日本語形態素解析器です。形態素解析はおもに以下の3つの処理を おこないます。";

		String settings = "{" + "\"systemDict\":\"" + dicFileName + "\"" + "}";

		Tokenizer.SplitMode mode = Tokenizer.SplitMode.C;

		// 形態素解析器の用意
		Dictionary dictionary = null;
		try {
			// 設定ファイル sudachi.json を用意した場合にはそれを読み込む
			// dictionary = new
			// DictionaryFactory().create(Files.readString(Paths.get("sudachi.json")));
			dictionary = new DictionaryFactory().create(null, settings, true);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		Tokenizer tokenizer = dictionary.create();

		// 形態素解析
		for (List<Morpheme> list : tokenizer.tokenizeSentences(mode, text)) {
			for (Morpheme morpheme : list) {
				System.out.println( //
						morpheme.begin() + "\t" //
								+ morpheme.end() + "\t" //
								+ morpheme.surface() + "\t" // 表層形
								+ String.join("-", morpheme.partOfSpeech()) + "," // 品詞
								+ morpheme.dictionaryForm() + "," // 原形
								+ morpheme.readingForm() + "," // 読み
								+ morpheme.normalizedForm()); // 正規形
			}
			System.out.println("EOS"); // 文の終端 (End of Sentence)
		}

	}

}
