package nlp4j.cotoha;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * COTOHA API 構文解析 V1 のレスポンスJSONをパースする
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 *
 */
public class CotohaNlpV1ResponseHandler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * 構文のルートとして抽出されたキーワード
	 */
	ArrayList<DefaultKeywordWithDependency> roots = new ArrayList<>();

	/**
	 * キーワードの並び
	 */
	ArrayList<Keyword> keywords = new ArrayList<>();

	/**
	 * 文節掛かり元キーワード
	 */
	ArrayList<Keyword> chunkLinkKeywords = new ArrayList<>();

	public ArrayList<Keyword> getChunkLinkKeywords() {
		return chunkLinkKeywords;
	}

	/**
	 * 掛かり元
	 */
	ArrayList<String> chunkLinks = new ArrayList<>();

	/**
	 * 掛かり元
	 */
	JsonArray arrChunkLinks = new JsonArray();

	/**
	 * Map: token_id --> Keyword
	 */
	HashMap<String, DefaultKeywordWithDependency> mapTokenidKwd = new HashMap<>();

	/**
	 * Map: id --> Keyword
	 */
	HashMap<String, DefaultKeywordWithDependency> mapIdKwd = new HashMap<>();

	/**
	 * token id --> sentence
	 */
	HashMap<Integer, Integer> idSentenceMap = new HashMap<>();

	/**
	 * 係り受けキーワード
	 */
	ArrayList<DefaultKeyword> patternKeywords = new ArrayList<>();

	/**
	 * @return 掛かり元
	 */
	public JsonArray getArrChunkLinks() {
		return arrChunkLinks;
	}

	/**
	 * @return 掛かり元
	 */
	public ArrayList<String> getChunkLinks() {
		return chunkLinks;
	}

	/**
	 * @return IDとキーワードのマップ
	 */
	public HashMap<String, DefaultKeywordWithDependency> getIdMapKwd() {
		return mapIdKwd;
	}

	/**
	 * @return 形態素のIDと文番号のマッピング
	 */
	public HashMap<Integer, Integer> getIdSentenceMap() {
		return idSentenceMap;
	}

	/**
	 * @return 語の並び
	 */
	public ArrayList<Keyword> getKeywords() {
		return keywords;
	}

	/**
	 * @return TOKEN ID とキーワードのマップ
	 */
	public HashMap<String, DefaultKeywordWithDependency> getMapKwd() {
		return mapTokenidKwd;
	}

	/**
	 * @return 掛かりうけキーワード
	 */
	public ArrayList<DefaultKeyword> getPatternKeywords() {
		return patternKeywords;
	}

	/**
	 * @return 抽出された係り受けルートキーワード
	 */
	public ArrayList<DefaultKeywordWithDependency> getRoots() {
		return roots;
	}

	/**
	 * @param json COTOHA API 構文解析のレスポンスJSON
	 */
	public void parse(String json) {

		// JSON Parser
		Gson gson = new Gson();

		// COTOHA API RESPONSE
		JsonObject result = gson.fromJson(json, JsonObject.class);

		// 文の中で出てきた順
		int sequence = 0;

		// {
		// "result":[
		// _{"chunk_info":{...},"tokens"[{...},{...},{...}]},
		// _{"chunk_info":{...},"tokens"[{...},{...},{...}]},
		// _{"chunk_info":{...},"tokens"[{...},{...},{...}]}
		// ]
		// }
		// chunk_info と tokens を合わせたオブジェクト
		JsonArray arrChunkTokens = result.getAsJsonArray("result");

		int idxBegin = 0;

		int idxSentence = 0;

		// FOR EACH(chunk_tokens)
		for (int idxChunkTokens = 0; idxChunkTokens < arrChunkTokens.size(); idxChunkTokens++) {

			JsonObject chunk_token = arrChunkTokens.get(idxChunkTokens).getAsJsonObject();

			// 1. chunk_info 文節情報オブジェクト
			// https://api.ce-cotoha.com/contents/reference/apireference.html#parsing_response_chunk
			JsonObject chunk_info = chunk_token.get("chunk_info").getAsJsonObject();
			logger.debug("chunk_info: " + chunk_info);

			int chunk_head = -1;

			{
				// 形態素番号（0オリジン）
				String chunk_id = "" + chunk_info.get("id").getAsInt();

				// 係り先の文節番号
				chunk_head = chunk_info.get("head").getAsInt();

				// 掛かり元情報の配列
				// https://api.ce-cotoha.com/contents/reference/apireference.html#parsing_response_links
				JsonArray links = chunk_info.get("links").getAsJsonArray();

				for (int n = 0; n < links.size(); n++) {
					JsonObject link = links.get(n).getAsJsonObject();

					int link_link = link.get("link").getAsInt();
					String link_label = link.get("label").getAsString();

					chunkLinks.add(chunk_id + "/" + link_label + "/" + link_link);

					arrChunkLinks.add(link);
				}

			}

			// 2. tokens 形態素情報オブジェクト
			// https://api.ce-cotoha.com/contents/reference/apireference.html#parsing_response_morpheme
			JsonArray tokens = chunk_token.get("tokens").getAsJsonArray();

			// FOR EACH TOKENS 形態素情報オブジェクト
			for (int idxTokens = 0; idxTokens < tokens.size(); idxTokens++) {

				JsonObject token = tokens.get(idxTokens).getAsJsonObject();
				logger.debug("token: " + token);

				// X-Y 形式のID ある文節の中で何番目の形態素であるか
				String token_id = idxChunkTokens + "-" + idxTokens;
				logger.debug("token_id: " + token_id);

				String token_pos = token.get("pos") != null ? token.get("pos").getAsString() : null;

				String token_lemma = token.get("lemma") != null ? token.get("lemma").getAsString() : null;

				String token_form = token.get("form") != null ? token.get("form").getAsString() : null;

				String token_kana = token.get("kana") != null ? token.get("kana").getAsString() : null;

				// token のうちの最後かどうか。trueであれば係り受けの先は次のtoken
				boolean isLastOfTokens = (idxTokens == tokens.size() - 1);

				if (isLastOfTokens) {
					logger.debug("最後のトークン: chunk_head:" + chunk_head);
				}

				// 係り受け形式のキーワード (nlp4j で定義)
				DefaultKeywordWithDependency kw = new DefaultKeywordWithDependency();

				// 文中で出てきた順の連番
				kw.setSequence(sequence);
				sequence++;

				// 開始位置
				kw.setBegin(idxBegin);

				// lemma:見出し語:原形
				if (token_lemma != null) {
					kw.setLex(token_lemma);
				} else {
					logger.warn("lemma is null");
				}

				int intId = token.get("id").getAsInt();
				String id = "" + token.get("id").getAsInt();

				idSentenceMap.put(intId, idxSentence);

				// 文の最後かどうか
				boolean isLastOfSentence = (chunk_head == -1 && idxTokens == tokens.size() - 1) //
						|| (token_pos != null && token_pos.equals("句点"));
				// IF(文の最後)
				if (isLastOfSentence) {
					// increment 文番号
					idxSentence++;
				}

				// set facet 品詞
				kw.setFacet(token_pos);

				// set str 表出形
				kw.setStr(token_form);
				kw.setEnd(idxBegin + kw.getStr().length());

				idxBegin += kw.getStr().length();

				// set reading 読み
				kw.setReading(token_kana);

				logger.debug("keyword : " + kw);

				mapTokenidKwd.put(token_id, kw);

				mapIdKwd.put(id, kw);

				keywords.add(kw);

				// dependency labels 依存先情報の配列
				if (token.get("dependency_labels") != null) {

					// 依存先情報の配列
					JsonArray arrDependency = token.get("dependency_labels").getAsJsonArray();

					for (int n = 0; n < arrDependency.size(); n++) {

						// 依存先情報
						JsonObject objDependency = arrDependency.get(n).getAsJsonObject();

						String dependency_token_id = "" + objDependency.get("token_id").getAsInt();
//						String dependency_label = objDependency.get("label").getAsString();

						// キーワードに依存先情報をセット
						kw.setDependencyKey(dependency_token_id);
					}
				}
				// ELSE(dependency labels == NULL)
				else {
					// do noting
				}

			} // END OF FOR EACH TOKENS

			logger.debug("---");

		} // END OF FOR EACH (chunk_tokens)

		// <ツリーの組み立て>

//		for (String key : idMapKwd.keySet()) {
//			DefaultKeywordWithDependency kwd = idMapKwd.get(key);
//			System.err.println("DependencyKey=" + kwd.getDependencyKey());
//		}

		// FOR EACH(chunk_tokens)
		for (int idxChunkTokens = 0; idxChunkTokens < arrChunkTokens.size(); idxChunkTokens++) {

			JsonObject chunk_token = arrChunkTokens.get(idxChunkTokens).getAsJsonObject();

			// 1. chunk_info
//			JsonObject chunk_info = chunk_token.get("chunk_info").getAsJsonObject();
//			int chunk_head = -1;
//
//			{
//				logger.debug("chunk_info: " + chunk_info);
//				chunk_head = chunk_info.get("head").getAsInt();
//			}

			// 2. tokens
			JsonArray tokens = chunk_token.get("tokens").getAsJsonArray();

			// FOR (EACH TOKEN)
			for (int idxTokens = 0; idxTokens < tokens.size(); idxTokens++) {

				JsonObject token = tokens.get(idxTokens).getAsJsonObject();

				String id = "" + token.get("id").getAsInt();

				DefaultKeywordWithDependency kw = mapIdKwd.get(id);

				// dependency labels
				if (token.get("dependency_labels") != null) {

					JsonArray arr_dependency_labels = token.get("dependency_labels").getAsJsonArray();

					for (int n = 0; n < arr_dependency_labels.size(); n++) {

						JsonObject dependency_label = arr_dependency_labels.get(n).getAsJsonObject();

//						logger.debug(dependency_label.get("token_id").getAsInt());
//						logger.debug(dependency_label.get("label").getAsString());

						String childID = "" + dependency_label.get("token_id").getAsInt();
						String labelDependency = dependency_label.get("label").getAsString();

						// 文をまたいでいないかをチェックする
						int sentence1 = idSentenceMap.get(token.get("id").getAsInt());
						int sentence2 = idSentenceMap.get(dependency_label.get("token_id").getAsInt());

//						System.err.println("debug:sentence1=" + sentence1 + ",sentence2=" + sentence2);

						// 文をまたいでない
						if (mapIdKwd.get(childID) != null && (sentence1 == sentence2)) {

							// 日本語と英語では ParentとChild が逆
							DefaultKeywordWithDependency kw1Child = mapIdKwd.get(childID);
							DefaultKeywordWithDependency kw2Parent = kw;

							kw2Parent.addChild(kw1Child);

							kw1Child.setRelation(labelDependency);

							if (kw1Child.getBegin() < kw2Parent.getBegin()) {
								DefaultKeyword kwd = new DefaultKeyword();
								kwd.setBegin(kw1Child.getBegin());
								kwd.setEnd(kw2Parent.getEnd());
								kwd.setLex(kw1Child.getLex() + " ... " + kw2Parent.getLex());
								kwd.setFacet(labelDependency);
								patternKeywords.add(kwd);

							} else {
								DefaultKeyword kwd = new DefaultKeyword();
								kwd.setBegin(kw2Parent.getBegin());
								kwd.setEnd(kw1Child.getEnd());
								kwd.setLex(kw2Parent.getLex() + " ... " + kw1Child.getLex());
								kwd.setFacet(labelDependency);
								patternKeywords.add(kwd);

							}

						} //
							// ELSE(文をまたいでいる)
						else if (sentence1 != sentence2) {
							//
						} //
						else {
							// no case
						}
					}
				}

			} // END OF FOR EACH TOKEN

		} // END OF FOR EACH (chunk_tokens)

		for (String link : chunkLinks) {

//			System.err.println("link:" + link);

			String id1 = link.split("/")[0];
			String relation = link.split("/")[1];
			String id2 = link.split("/")[2];

//			for (String key : mapTokenidKwd.keySet()) {
//				System.err.println("key=" + key + ",kw=" + mapTokenidKwd.get(key).getLex());
//			}
//			System.err.println("---");
//
//			System.err.println("id1=" + id1);
//			System.err.println("id2=" + id2);
//
//			System.err.println(mapIdKwd.get(id1).getLex() + " ??? " + mapIdKwd.get(id2).getLex());

			Keyword kwd1 = mapTokenidKwd.get(id1 + "-0");
			Keyword kwd2 = mapTokenidKwd.get(id2 + "-0");

			String lex1 = kwd1.getLex();
			String lex2 = kwd2.getLex();

			logger.debug("[2] " + lex2 + " ... " + lex1 + " (" + relation + ")");

			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setBegin(kwd1.getBegin());
			kwd.setEnd(kwd2.getEnd());
			kwd.setLex(lex2 + " ... " + lex1);
			kwd.setStr(kwd.getLex());
			kwd.setFacet(relation);

			chunkLinkKeywords.add(kwd);

		}

		// </ツリーの組み立て>

		for (String key : mapIdKwd.keySet()) {

			DefaultKeywordWithDependency kw = mapIdKwd.get(key);

			// 係り受け先のキー
			String depKey = kw.getDependencyKey();

			if (kw.getParent() == null) {
				roots.add(kw);
			}
		}

	} // end of parse()
}
