package nlp4j.mecab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.impl.StandardTagger;
import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultKeyword;

/**
 * Mecab Japanese Language Annotator
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 *
 */
public class MecabAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private final Logger logger = LogManager.getLogger(MecabAnnotator.class);

	@Override
	public void annotate(Document doc) throws Exception {

		logger.info("processing document");

		// Taggerを構築。
		// 引数には、MeCabのcreateTagger()関数に与える引数を与える。
		StandardTagger tagger = new StandardTagger("");
		// Lattice（形態素解析に必要な実行時情報が格納されるオブジェクト）を構築
		Lattice lattice = tagger.createLattice();

		for (String target : targets) {

			Object obj = doc.getAttribute(target);

			if (obj == null || obj instanceof String == false) {
				continue;
			}

			String text = (String) obj;

			// 解析対象文字列をセット
			lattice.setSentence(text);

			// tagger.parse()を呼び出して、文字列を形態素解析する。
			tagger.parse(lattice);

			// 一つずつ形態素をたどりながら、表層形と素性を出力
			Node node = lattice.bosNode();

			int idx = 0;
			int sequence = 1;

			while (node != null) {

				String surface = node.surface();
				String[] features = node.feature().split(",");

				if (features[0].equals("BOS/EOS")) {
					node = node.next();
					continue;
				}

				logger.info(node.surface() + "\t" + node.feature());

				DefaultKeyword kwd = new DefaultKeyword();

				kwd.setLex(features[6]);
				kwd.setStr(surface);
				kwd.setReading(features[7]);

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

				doc.addKeyword(kwd);

				sequence++;
				node = node.next();
			}

		}

		// lattice, taggerを破壊
		lattice.destroy();
		tagger.destroy();

	}
}
