package nlp4j.sudachi;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksap.nlp.sudachi.Dictionary;
import com.worksap.nlp.sudachi.DictionaryFactory;
import com.worksap.nlp.sudachi.Morpheme;
import com.worksap.nlp.sudachi.Tokenizer;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordBuilder;

/**
 * <pre>
 * Sudachi Annotator
 * https://github.com/WorksApplications/Sudachi#sudachi-%E6%97%A5%E6%9C%AC%E8%AA%9Ereadme
 * </pre>
 * 
 * @author Hiroki Oya
 * @since 0.1.0.0
 *
 */
public class SudachiAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, Closeable {

	static private final Logger logger = LogManager.getLogger(SudachiAnnotator.class);

	private Tokenizer.SplitMode mode = Tokenizer.SplitMode.C;

	private Tokenizer tokenizer = null;

	private String posRegex;

	static public String DEFAULT_FULLDIC = "system_full.dic";

	public SudachiAnnotator() {
		super();
//		initTokenizer(DEFAULT_FULLDIC);
	}

	/**
	 * <pre>
	 * systemDict=system_core.dic
	 * systemDict=system_full.dic
	 * mode=A
	 * mode=B
	 * mode=C
	 * </pre>
	 * 
	 * @param key : systemDict
	 * @param
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("systemDict".equals(key)) {
			initTokenizer(value);
		} //
		else if ("pos".equals(key)) {
			this.posRegex = value;
		} //
		else if ("mode".equals(key)) {
			if ("A".equals(value)) {
				this.mode = Tokenizer.SplitMode.A; // short
			} //
			else if ("B".equals(value)) {
				this.mode = Tokenizer.SplitMode.B; // middle
			} //
			else if ("C".equals(value)) {
				this.mode = Tokenizer.SplitMode.C; // long
			}

		}

	}

	private void initTokenizer(String systemDict) {
		if ((new File(systemDict)).exists() == false) {
			logger.info("File not found: " + (new File(systemDict)).getAbsolutePath());
			return;
		}
		String settings = "{" + "\"systemDict\":\"" + systemDict + "\"" + "}";
		// 形態素解析器の用意
		Dictionary dictionary = null;
		try {
			// 設定ファイル sudachi.json を用意した場合にはそれを読み込む
			// dictionary = new
			// DictionaryFactory().create(Files.readString(Paths.get("sudachi.json")));
			dictionary = new DictionaryFactory().create(null, settings, true);
			tokenizer = dictionary.create();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {

		for (String target : super.targets) {
			String text = doc.getAttributeAsString(target);

			int sentenceIndex = -1;
			// FOR_EACH(Sentence)
			for (List<Morpheme> list : tokenizer.tokenizeSentences(mode, text)) {
				sentenceIndex++;
				// FOR_EACH(Morpheme)
				for (Morpheme morpheme : list) {
					logger.debug( //
							morpheme.begin() + "\t" //
									+ morpheme.end() + "\t" //
									+ morpheme.surface() + "\t" // 表層形
									+ String.join("-", morpheme.partOfSpeech()) + "," // 品詞
									+ morpheme.dictionaryForm() + "," // 原形
									+ morpheme.readingForm() + "," // 読み
									+ morpheme.normalizedForm()); // 正規形

					Keyword kwd = (new KeywordBuilder()).begin(morpheme.begin()) //
							.end(morpheme.end()) //
							.str(morpheme.surface()) //
							.facet(morpheme.partOfSpeech().get(0)) //
							.lex(morpheme.dictionaryForm()) //
							.reading(morpheme.readingForm()) //
							.sentenceIndex(sentenceIndex) //
							.build();

					if (this.posRegex != null) {
						String pos = kwd.getFacet();
						if (pos.matches(this.posRegex) == false) {
							continue;
						}
					}

					doc.addKeyword(kwd);
				} // END_OF_FOR_EACH(Morphoeme)
			}

		}
	}

	@Override
	public void close() throws IOException {
	}
}
