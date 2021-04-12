package nlp4j;

import java.util.HashMap;

public class UPOS20 {

	static private HashMap<String, String> UPOS_JA = new HashMap<>();

	static {
		UPOS_JA.put("名詞", "NOUN");
		UPOS_JA.put("固有名詞", "PROPN");
		UPOS_JA.put("動詞", "VERB");
		UPOS_JA.put("動詞-接尾", "AUX");
		UPOS_JA.put("助動詞", "AUX");
		UPOS_JA.put("形容詞", "ADJ");
		UPOS_JA.put("助詞", "ADP");
		UPOS_JA.put("副詞", "ADV");
		UPOS_JA.put("間投詞", "INTJ");
		UPOS_JA.put("句読点", "PUNCT");
		UPOS_JA.put("句点", "PUNCT");
		UPOS_JA.put("読点", "PUNCT");
		UPOS_JA.put("記号", "SYM");
		UPOS_JA.put("その他", "X");
		UPOS_JA.put("代名詞", "PRON");
		UPOS_JA.put("数詞", "NUM");
		UPOS_JA.put("助数詞", "AUX");
		UPOS_JA.put("接続詞", "CONJ");
		UPOS_JA.put("従属接続詞", "SCONJ");
		UPOS_JA.put("限定詞", "DET");
		UPOS_JA.put("前置詞", "ADP");
		UPOS_JA.put("接辞", "PART");
	}

	static public String fromPOSJA(String posJa) {
		return UPOS_JA.get(posJa);
	}

}
