package nlp4j.annotator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordAnnotator;

/**
 * ファセット名を変換する<br>
 * reference: Stanford CoreNLP POSタグまとめ
 * https://qiita.com/syunyo/items/2c1ce1d765f46a5c1d72
 * 
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public class KeywordFacetMappingAnnotator extends AbstractDocumentAnnotator
		implements DocumentAnnotator, KeywordAnnotator {

	/**
	 * デフォルトの変換マッピング<br>
	 * 名詞-&gt;word.NN,<br>
	 * 数詞-&gt;word.CD,<br>
	 * 動詞-&gt;word.VB,<br>
	 * 助詞-&gt;word.RP,<br>
	 * 助動詞-&gt;word.MD,<br>
	 * 記号-&gt;word.SYM,<br>
	 * 接続詞-&gt;word.CC,<br>
	 * 副詞-&gt;word.RB,<br>
	 * 形容詞-&gt;word.JJ,<br>
	 * 接頭詞-&gt;word_ja.RENTOU,<br>
	 * 連体詞-&gt;word_ja.RENTAI,<br>
	 * フィラー-&gt;word_ja.FILLER,<br>
	 * 感動詞-&gt;word.UH<br>
	 * 
	 */
	static public final String DEFAULT_MAPPING = "" + //
			"名詞->word.NN," + //
			"数詞->word.CD," + //
			"動詞->word.VB," + //
			"助詞->word.RP," + //
			"助動詞->word.MD," + //
			"記号->word.SYM," + //
			"接続詞->word.CC," + //
			"副詞->word.RB," + //
			"形容詞->word.JJ," + //
			"接頭詞->word_ja.RENTOU," + //
			"連体詞->word_ja.RENTAI," + //
			"フィラー->word_ja.FILLER," + //
			"感動詞->word.UH" + //
			"" //
	;

	HashMap<String, String> facetMap = new LinkedHashMap<String, String>();

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if (key.equals("mapping")) {
			String[] maps = value.split(",");

			for (String map : maps) {
				String[] ss = map.split("->");
				if (ss.length != 2) {
					continue;
				}
				String from = ss[0];
				if (from.isEmpty()) {
					continue;
				}
				String to = ss[1];
				facetMap.put(from, to);
			}
		}

	}

	@Override
	public void annotate(Document doc) throws Exception {

		List<Keyword> kwds = doc.getKeywords();

		for (Keyword kwd : kwds) {
			String facet = kwd.getFacet();

//			System.err.println(facet);

			if (facetMap.containsKey(facet)) {
				String facet2 = facetMap.get(facet);
				kwd.setFacet(facet2);
			}
		}

	}

}
