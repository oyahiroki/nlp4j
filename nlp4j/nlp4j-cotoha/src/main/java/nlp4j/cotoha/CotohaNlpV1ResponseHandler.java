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

	// 構文のルートとして抽出されたキーワード
	ArrayList<DefaultKeywordWithDependency> roots = new ArrayList<>();

	// キーワードの並び
	ArrayList<Keyword> keywords = new ArrayList<>();

	ArrayList<String> chunkLinks = new ArrayList<>();

	// Map: token_id --> Keyword
	HashMap<String, DefaultKeywordWithDependency> mapKwd = new HashMap<>();

	// Map: id --> Keyword
	HashMap<String, DefaultKeywordWithDependency> idMapKwd = new HashMap<>();

	// token id --> sentence
	HashMap<Integer, Integer> idSentenceMap = new HashMap<>();

	/**
	 * @return 抽出された係り受けルートキーワード
	 */
	public ArrayList<DefaultKeywordWithDependency> getRoots() {
		return roots;
	}

	/**
	 * @return 語の並び
	 */
	public ArrayList<Keyword> getKeywords() {
		return keywords;
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
		// {"chunk_info":{...},"tokens"[{...},{...},{...}]},
		// {...},
		// {...}
		// ]
		// }
		// chunk_info と tokens を合わせたオブジェクト
		JsonArray chunk_tokens = result.getAsJsonArray("result");

		int idxBegin = 0;

		int idxSentence = 0;

		// FOR EACH(chunk_tokens)
		for (int idxChunkTokens = 0; idxChunkTokens < chunk_tokens.size(); idxChunkTokens++) {

			JsonObject chunk_token = chunk_tokens.get(idxChunkTokens).getAsJsonObject();

			// 1. chunk_info
			JsonObject chunk_info = chunk_token.get("chunk_info").getAsJsonObject();
			int chunk_head = -1;

			{
				logger.debug("chunk_info: " + chunk_info);
				chunk_head = chunk_info.get("head").getAsInt();

				String chunk_id = "" + chunk_info.get("id").getAsInt();
//				System.err.println("chunk_info: id: " + chunk_id);

				JsonArray links = chunk_info.get("links").getAsJsonArray();
				for (int n = 0; n < links.size(); n++) {
					JsonObject link = links.get(n).getAsJsonObject();

//					System.err.println(link.get("link").getAsInt());
//					System.err.println(link.get("label").getAsString());

					chunkLinks
							.add(chunk_id + "/" + link.get("label").getAsString() + "/" + link.get("link").getAsInt());
				}

			}

			// 2. tokens
			JsonArray tokens = chunk_token.get("tokens").getAsJsonArray();

			int idEndOfSentence = Integer.MAX_VALUE;

			// FOR EACH TOKENS
			for (int idxTokens = 0; idxTokens < tokens.size(); idxTokens++) {

				JsonObject token = tokens.get(idxTokens).getAsJsonObject();

				// X-Y 形式のID
				String token_id = idxChunkTokens + "-" + idxTokens;

				logger.debug("token_id: " + token_id);

				logger.debug("token: " + token);

				// token のうちの最後かどうか。trueであれば係り受けの先は次のtoken
				boolean isLastOfTokens = (idxTokens == tokens.size() - 1);

				if (isLastOfTokens) {
					logger.debug("最後のトークン: chunk_head:" + chunk_head);
				}

				// 文の最後かどうか
				boolean isLastOfSentence = (chunk_head == -1 && idxTokens == tokens.size() - 1) //
						|| (token.get("pos") != null && token.get("pos").getAsString().equals("句点"));

				// 係り受け形式のキーワード (nlp4j で定義)
				DefaultKeywordWithDependency kw = new DefaultKeywordWithDependency();

				// 文中で出てきた順の連番
				kw.setSequence(sequence);
				sequence++;

				// 開始位置
				kw.setBegin(idxBegin);

				// lemma:見出し語:原形
				if (token.get("lemma") != null) {
					kw.setLex(token.get("lemma").getAsString());
				} else {
					logger.warn("lemma is null");
				}

				int intId = token.get("id").getAsInt();
				String id = "" + token.get("id").getAsInt();

				idSentenceMap.put(intId, idxSentence);

				if (isLastOfSentence) {
					idxSentence++;
				}

//				// 次のtokenのID
//				String next_token = null;
//
//				// IF(最後のtokenでない)THEN
//				if (isLastOfTokens == false) {
//					next_token = idxChunkTokens + "-" + (idxTokens + 1);
//				}
//				// ELSE(最後のtoken)
//				else {
//					// IF(文の最後でない) THEN
//					if (isLastOfSentence == false) {
//						next_token = chunk_head + "-" + "0";
//					}
//					// ELSE(文の最後)
//					else {
//						logger.debug("文の区切り");
//						next_token = null;
//						sequence = 0;
//					}
//				}
//				logger.debug("next_token: " + next_token);

//				kw.setDependencyKey(next_token);

				// set facet 品詞
				kw.setFacet(token.get("pos").getAsString());

				// set str 表出形
				kw.setStr(token.get("form").getAsString());
				kw.setEnd(idxBegin + kw.getStr().length());

				if (isLastOfSentence) {
					idEndOfSentence = token.get("id").getAsInt();
				}

				idxBegin += kw.getStr().length();

				// set reading 読み
				kw.setReading(token.get("kana").getAsString());

				logger.debug("keyword : " + kw);

				mapKwd.put(token_id, kw);
				idMapKwd.put(id, kw);

				keywords.add(kw);

				// dependency labels
				if (token.get("dependency_labels") != null) {

					JsonArray arr = token.get("dependency_labels").getAsJsonArray();

					for (int n = 0; n < arr.size(); n++) {
						JsonObject obj = arr.get(n).getAsJsonObject();
						logger.debug(obj.get("token_id").getAsInt());
						logger.debug(obj.get("label").getAsString());

//						int intParentID = obj.get("token_id").getAsInt();
//						if (intParentID > idEndOfSentence) {
//							System.err.println("文をまたいでいるのでスキップ");
//							continue;
//						}

						String parentID = "" + obj.get("token_id").getAsInt();
						String label = obj.get("label").getAsString();

						kw.setDependencyKey(parentID);

						if (idMapKwd.get(parentID) != null) {
							System.err.println(
									idMapKwd.get(parentID).getLex() + " ... " + kw.getLex() + "(" + label + ")");
						}
					}
				} else {
					System.err.println("dependency_labels: null!");
				}

				// 最後のchunk で最後のtoken の場合、ルートになる

			} // END OF FOR EACH TOKENS

			logger.debug("---");

		} // END OF FOR EACH (chunk_tokens)

		logger.debug("ツリーの組み立て");

		///
		///
		///

		// FOR EACH(chunk_tokens)
		for (int idxChunkTokens = 0; idxChunkTokens < chunk_tokens.size(); idxChunkTokens++) {

			JsonObject chunk_token = chunk_tokens.get(idxChunkTokens).getAsJsonObject();

			// 1. chunk_info
			JsonObject chunk_info = chunk_token.get("chunk_info").getAsJsonObject();
			int chunk_head = -1;

			{
				logger.debug("chunk_info: " + chunk_info);
				chunk_head = chunk_info.get("head").getAsInt();
			}

			// 2. tokens
			JsonArray tokens = chunk_token.get("tokens").getAsJsonArray();

			// FOR EACH TOKENS
			for (int idxTokens = 0; idxTokens < tokens.size(); idxTokens++) {

				JsonObject token = tokens.get(idxTokens).getAsJsonObject();

				String id = "" + token.get("id").getAsInt();

				DefaultKeywordWithDependency kw = idMapKwd.get(id);

				// dependency labels
				if (token.get("dependency_labels") != null) {
					JsonArray arr = token.get("dependency_labels").getAsJsonArray();
					for (int n = 0; n < arr.size(); n++) {

						JsonObject obj = arr.get(n).getAsJsonObject();

						logger.debug(obj.get("token_id").getAsInt());
						logger.debug(obj.get("label").getAsString());

						String childID = "" + obj.get("token_id").getAsInt();
						String labelDependency = obj.get("label").getAsString();

						int sentence1 = idSentenceMap.get(token.get("id").getAsInt());
						int sentence2 = idSentenceMap.get(obj.get("token_id").getAsInt());

						System.err.println("debug:sentence1=" + sentence1 + ",sentence2=" + sentence2);

						if (idMapKwd.get(childID) != null && (sentence1 == sentence2)) {

							// 20200224 ParentとChildを逆にしていたのを修正(英語と日本語で異なる)
							DefaultKeywordWithDependency kw1Child = idMapKwd.get(childID);
							DefaultKeywordWithDependency kw2Parent = kw;

							kw2Parent.addChild(kw1Child);

							kw1Child.setRelation(labelDependency);

							System.err.println("[1] " + kw2Parent.getLex() + " ... " + kw1Child.getLex() + "("
									+ labelDependency + ")");

							if (kw1Child.getBegin() < kw2Parent.getBegin()) {
								System.err.println("[1a] " + kw1Child.getLex() + " ... " + kw2Parent.getLex() + "("
										+ labelDependency + ")");
							} else {
								System.err.println("[1b] " + kw2Parent.getLex() + " ... " + kw1Child.getLex() + "("
										+ labelDependency + ")");
							}

						} else {
							System.err.println("null ... null");
						}
					}
				}

				// 最後のchunk で最後のtoken の場合、ルートになる

			} // END OF FOR EACH TOKENS

			logger.debug("---");

		} // END OF FOR EACH (chunk_tokens)

		System.err.println("---");

		for (String link : chunkLinks) {
//			System.err.println(link);
			String id1 = link.split("/")[0];
			String relation = link.split("/")[1];
			String id2 = link.split("/")[2];

			String lex1 = mapKwd.get(id1 + "-0").getLex();
			String lex2 = mapKwd.get(id2 + "-0").getLex();

			System.err.println("[2] " + lex2 + " ... " + lex1 + " (" + relation + ")");

		}

		logger.debug("ツリーの組み立て");

		// 係り受けの情報をもとにNodeツリーを組み立てる
		for (String key : mapKwd.keySet()) {

			DefaultKeywordWithDependency kw = mapKwd.get(key);

			// 係り受け先のキー
			String depKey = kw.getDependencyKey();

			if (kw.getParent() == null) {
				roots.add(kw);
			}

//			// IF()
//			if (depKey != null) {
//				if (mapKwd.containsKey(depKey)) {
//
//					DefaultKeywordWithDependency parent = mapKwd.get(depKey);
//					kw.setParent(parent);
//
//				} else {
//					// ???
//					throw new RuntimeException("head not found");
//				}
//
//			}
//			//
//			else {
//				roots.add(kw);
//
//				logger.debug(kw.toStringAsXml());
//				logger.debug(kw.toStringAsDependencyList());
//				logger.debug(kw.toStringAsDependencyTree());
//			}

		} // END OF(係り受けの情報をもとにNodeツリーを組み立てる)

	}

}
