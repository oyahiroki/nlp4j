package nlp4j.sudachi;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	Set<String> stopWords = new HashSet<>();

	public SudachiAnnotator() {
		super();
//		initTokenizer(DEFAULT_FULLDIC);
	}

	/**
	 * <pre>
	 * systemDict=system_core.dic
	 * systemDict=system_full.dic
	 * mode=A (short)
	 * mode=B (middle)
	 * mode=C (long)
	 * </pre>
	 * 
	 * @param key : systemDict
	 * @param
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		// 辞書のファイルパス
		if ("systemDict".equals(key)) {
			initTokenizer(value);
		} //
		else if ("pos".equals(key)) {
			this.posRegex = value;
		} //
			// 形態素解析モード
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
		} //
		else if ("stopwords".equals(key.toLowerCase())) {
			this.stopWords = new HashSet<>(Arrays.asList(value.split(",")));
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
			for (List<Morpheme> list :
//				Exception in thread "main" java.lang.IllegalStateException: EOS isn't connected to BOS
//				at com.worksap.nlp.sudachi.LatticeImpl.getBestPath(LatticeImpl.java:150)
//				at com.worksap.nlp.sudachi.JapaneseTokenizer.tokenizeSentence(JapaneseTokenizer.java:197)
//				at com.worksap.nlp.sudachi.JapaneseTokenizer.tokenizeSentences(JapaneseTokenizer.java:102)
			tokenizer.tokenizeSentences(mode, text) // throws IllegalStateException
			) {
				sentenceIndex++;
				// FOR_EACH(Morpheme)
				for (Morpheme morpheme : list) {
					logger.debug( //
							"" //
									+ morpheme.begin() + "\t" //
									+ morpheme.end() + "\t" //
									+ morpheme.surface() + "\t" // 表層形
									+ String.join("-", morpheme.partOfSpeech()) + "," // 品詞
									+ morpheme.dictionaryForm() + "," // 原形
									+ morpheme.readingForm() + "," // 読み
									+ morpheme.normalizedForm() // 正規形
					); // END_OF_DEBUG

					String facet = morpheme.partOfSpeech().get(0);
					String facet2 = String.join("-", morpheme.partOfSpeech());
					if (facet2.startsWith("名詞-固有名詞")) {
						facet = "固有名詞";
					}

					Keyword kwd = (new KeywordBuilder()) //
							.begin(morpheme.begin()) //
							.end(morpheme.end()) //
							.str(morpheme.surface()) //
							.facet(facet) //
							.facet2(facet2) //
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
					// 2023-09-24
					if (this.stopWords.size() > 0) {
						String lex = kwd.getLex();
						if (stopWords.contains(lex)) {
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
