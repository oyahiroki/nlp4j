package nlp4j.mecab;

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
public class MecabAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private final Logger logger = LogManager.getLogger(MecabAnnotator.class);

	private ArrayList<String> facetfilter = null;

	private String lexregexfilter = null;

	private String option = "";

	StandardTagger tagger;

	public MecabAnnotator() {
		super();
		tagger = new StandardTagger(this.option);
	}

	@Override
	public void annotate(Document doc) throws Exception {

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

			int idx = 0;
			int sequence = 1;

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
				String[] features = node.feature().split(",");

				if (features[0].equals("BOS/EOS")) {
					node = node.next();
					continue;
				}

				if (features.length < 8) {
					logger.warn("invalid: features.length:" + features.length + "," + text);
				}

				logger.debug(node.surface() + "\t" + node.feature());

				DefaultKeyword kwd = new DefaultKeyword();

				kwd.setLex(features[6]);
				kwd.setStr(surface);
				if (features.length >= 8) {
					kwd.setReading(features[7]);
				} //
				else {
					kwd.setReading("*");
				}

				if (features[0].equals("動詞") && features[1].equals("接尾")) {
					kwd.setFacet("動詞-接尾");
				} else {
					kwd.setFacet(features[0]);

				}

				// 英字
				// @since 1.2.0.1
				if (kwd.getLex().equals("*") && kwd.getReading().equals("*")) {
					kwd.setLex(kwd.getStr());
				}

				kwd.setBegin(idx);
				kwd.setEnd(idx + surface.length());
				idx = idx + surface.length();
				kwd.setSequence(sequence);

				if ( //

				(this.facetfilter == null || this.facetfilter.contains(kwd.getFacet())) //
						&& //
						(this.lexregexfilter == null || kwd.getLex().matches(this.lexregexfilter)) //

				) //
				{
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
		tagger.destroy();
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
