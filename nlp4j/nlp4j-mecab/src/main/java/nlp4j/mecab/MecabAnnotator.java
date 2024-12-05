package nlp4j.mecab;

import java.io.Closeable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.impl.StandardTagger;
import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultKeyword;
import nlp4j.util.RegexUtils;

/**
 * Mecab Japanese Language Annotator
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 *
 */
public class MecabAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, Closeable {

	/**
	 * 固有名詞
	 */
	private static final String JA_KOYUU_MEISHI = "固有名詞";

	/**
	 * 名詞
	 */
	private static final String JA_MEISHI = "名詞";

	/**
	 * 動詞-接尾
	 */
	private static final String JA_DOUSHI_SETSUBI = "動詞-接尾";

	/**
	 * 接尾
	 */
	private static final String JA_SETSUBI = "接尾";

	/**
	 * 動詞
	 */
	private static final String JA_DOUSHI = "動詞";

	/**
	 * Logger
	 */
	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private ArrayList<String> facetfilter = null;

	private String lexregexfilter = null;

	private String option = "";

	StandardTagger tagger;

	public MecabAnnotator() {
		super();
	}

	@Override
	public void annotate(Document doc) throws Exception {

		if (this.tagger == null) {
			// StandardTagger: cmecab-2.0.jar
			tagger = new StandardTagger(this.option);
			logger.debug("tagger_initialized");
		}

		logger.debug("processing document");

		// Taggerを構築。
		// 引数には、MeCabのcreateTagger()関数に与える引数を与える。

		if (this.option == null) {
			this.option = "";
		}

		// Lattice（形態素解析に必要な実行時情報が格納されるオブジェクト）を構築
		Lattice lattice = tagger.createLattice();

		for (String target : targets) {

			Object obj = doc.getAttribute(target);

			if (obj == null || obj instanceof String == false) {
				continue;
			}

			String text = (String) obj;
			{
				// MS932に無い文字を取り除く
				text = nlp4j.util.StringUtils.filter(text, "MS932");
				// URLを除去
				text = text.replaceAll(RegexUtils.REGEX_URL, "");
			}

			// 解析対象文字列をセット
			lattice.setSentence(text);

			// tagger.parse()を呼び出して、文字列を形態素解析する。
			tagger.parse(lattice);

			// 一つずつ形態素をたどりながら、表層形と素性を出力
			Node node = lattice.bosNode();

			// 開始位置・終了位置
			int idxPosition = 0;
			// 連番
			int sequence = 1;

			int sentenceIndex = 0;

			// 表層形\t品詞,品詞細分類1,品詞細分類2,品詞細分類3,活用形,活用型,原形,読み,発音
			// features[0]: 品詞,
			// features[1]: 品詞細分類1,
			// features[2]: 品詞細分類2,
			// features[3]: 品詞細分類3,
			// features[4]: 活用形,
			// features[5]: 活用型,
			// features[6]: 原形,
			// features[7]: 読み,
			// features[8]: 発音
			while (node != null) {

				String surface = node.surface();
				String feature = node.feature();
				logger.debug("feature=" + feature);
				String[] features = feature.split(",");

				if (features[0].equals("BOS/EOS")) {
					node = node.next();
					continue;
				}

				if (features.length < 8 && logger.isDebugEnabled()) {
					logger.debug("invalid: features.length:" + features.length + "," + text);
				}

				logger.debug(node.surface() + "\t" + node.feature());

				DefaultKeyword kwd = new DefaultKeyword();

				{
					kwd.setNamespace("mecab");
				}

				// LEX
				{
					kwd.setLex(features[6]);
					kwd.setSentenceIndex(sentenceIndex);
				}
				{// sentenceIndex
					if (kwd.getLex() != null && kwd.getLex().equals("。")) {
						sentenceIndex++;
					}
				}

				// STR
				{
					kwd.setStr(surface);
				}
				// READING
				{
					if (features.length >= 8) {
						kwd.setReading(features[7]);
					} else {
						kwd.setReading("*");
					}
				}
				// FACET
				{
					// 動詞-接尾
					if (features[0].equals(JA_DOUSHI) && features[1].equals(JA_SETSUBI)) {
						kwd.setFacet(JA_DOUSHI_SETSUBI);
					} //
						// 固有名詞
					else if (features[0].equals(JA_MEISHI) && features[1].equals(JA_KOYUU_MEISHI)) {
						kwd.setFacet(JA_KOYUU_MEISHI);
					} //
					else {
						kwd.setFacet(features[0]);
					}
				}
				// 英字
				// @since 1.2.0.1
				if (kwd.getLex().equals("*") && kwd.getReading().equals("*")) {
					kwd.setLex(kwd.getStr());
				}
				// BEGIN
				{
					kwd.setBegin(idxPosition);
				}
				// END
				{
					kwd.setEnd(idxPosition + surface.length());
					idxPosition += surface.length();
				}
				// SEQUENCE
				{
					kwd.setSequence(sequence);
				}

				// facetfilter, lexregexfilter
				if ( //
						// facetFilter
				(this.facetfilter == null || this.facetfilter.contains(kwd.getFacet())) //
						&& //
							// lexregexfilter
						(this.lexregexfilter == null || kwd.getLex().matches(this.lexregexfilter)) //
				) {
					doc.addKeyword(kwd);
				}

				sequence++;
				node = node.next();
			}

		}

		// lattice, taggerを破壊
		lattice.destroy();

	}

	public void close() {
		if (tagger != null) {
			tagger.destroy();
		}
	}

	/**
	 * @param key : facetfilter|lexregexfilter|option
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if ("facetfilter".equals(key) && value != null) {
			if (this.facetfilter == null) {
				this.facetfilter = new ArrayList<>();
			}
			this.facetfilter.addAll(Arrays.asList(value.split(",")));
		} //
		else if ("lexregexfilter".equals(key) && value != null) {
			this.lexregexfilter = value;
		} //
		else if ("option".equals(key) && value != null) {
			this.option = value;
		}

	}
}
