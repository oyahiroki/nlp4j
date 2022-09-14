package nlp4j;

import java.util.HashMap;

public class UPOS20 {

	static private HashMap<String, String> UPOS_JA = new HashMap<>();

	/**
	 * 名詞
	 */
	static public String NOUN = "NOUN";
	/**
	 * 固有名詞
	 */
	static public String PROPN = "PROPN";
	/**
	 * 動詞
	 */
	static public String VERB = "VERB";
	/**
	 * 動詞-接尾, 助動詞, 助数詞
	 */
	static public String AUX = "AUX";
	/**
	 * 形容詞
	 */
	static public String ADJ = "ADJ";
	/**
	 * 助詞, 前置詞
	 */
	static public String ADP = "ADP";
	/**
	 * 副詞
	 */
	static public String ADV = "ADV";
	/**
	 * 間投詞
	 */
	static public String INTJ = "INTJ";
	/**
	 * 句読点, 句点, 読
	 */
	static public String PUNCT = "PUNCT";
	/**
	 * 記号
	 */
	static public String SYM = "SYM";
	/**
	 * その他
	 */
	static public String X = "X";
	/**
	 * 代名詞
	 */
	static public String PRON = "PRON";
	/**
	 * 数詞
	 */
	static public String NUM = "NUM";
	/**
	 * 接続詞
	 */
	static public String CONJ = "CONJ";
	/**
	 * 従属接続詞
	 */
	static public String SCONJ = "SCONJ";
	/**
	 * 限定詞
	 */
	static public String DET = "DET";
	/**
	 * 接辞
	 */
	static public String PART = "PART";

	static {
		UPOS_JA.put("名詞", NOUN);
		UPOS_JA.put("接頭詞", NOUN); // 2022-09-14
		UPOS_JA.put("固有名詞", PROPN);
		UPOS_JA.put("動詞", VERB);
		UPOS_JA.put("動詞-接尾", AUX);
		UPOS_JA.put("助動詞", AUX);
		UPOS_JA.put("形容詞", ADJ);
		UPOS_JA.put("助詞", ADP);
		UPOS_JA.put("副詞", ADV);
		UPOS_JA.put("間投詞", INTJ);
		UPOS_JA.put("句読点", PUNCT);
		UPOS_JA.put("句点", PUNCT);
		UPOS_JA.put("読点", PUNCT);
		UPOS_JA.put("記号", SYM);
		UPOS_JA.put("その他", X);
		UPOS_JA.put("代名詞", PRON);
		UPOS_JA.put("数詞", NUM);
		UPOS_JA.put("助数詞", AUX);
		UPOS_JA.put("接続詞", CONJ);
		UPOS_JA.put("従属接続詞", SCONJ);
		UPOS_JA.put("限定詞", DET);
		UPOS_JA.put("前置詞", ADP);
		UPOS_JA.put("接辞", PART);
	}

	/**
	 * <pre>
	 * Convert POS in Japanese to UPOS
	 * Example "名詞" -> "NOUN"
	 * </pre>
	 * 
	 * @param posJa
	 * @return
	 */
	static public String fromPOSJA(String posJa) {
		return UPOS_JA.get(posJa);
	}

}
