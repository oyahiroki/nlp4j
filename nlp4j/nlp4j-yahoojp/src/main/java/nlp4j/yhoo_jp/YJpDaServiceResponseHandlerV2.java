package nlp4j.yhoo_jp;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * Yahoo! Japan Dependency AnalysisのレスポンスXMLをパースするクラスです。<br>
 * Yahoo! Japan Dependency Analysis Response XML Handler
 * 
 * @author Hiroki Oya
 * @version 2.0
 *
 */
public class YJpDaServiceResponseHandlerV2 /* extends AbstractXmlHandler */ {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	HashMap<String, DefaultKeywordWithDependency> map = new HashMap<String, DefaultKeywordWithDependency>();

	ArrayList<KeywordWithDependency> roots = new ArrayList<KeywordWithDependency>();;

	String sentence;

	int begin = 0;

	/**
	 * @param sentence 自然言語文字列
	 */
	public YJpDaServiceResponseHandlerV2(String sentence) {
		super();
		this.sentence = sentence;
	}

	/**
	 * @return キーワード
	 */
	public ArrayList<KeywordWithDependency> getRoots() {
		return this.roots;
	}

	/**
	 * 構文解析V2 のレスポンスをパースします.
	 * 
	 * @param json Yahoo 構文解析V2 のレスポンス
	 */
	public void parseJson(String json) {

		Gson gson = new Gson();

		JsonObject jsonObj = gson.fromJson(json, JsonObject.class);

//		String id = jsonObj.get("id").getAsString();

		JsonObject result = jsonObj.get("result").getAsJsonObject();

		JsonArray chunks = result.get("chunks").getAsJsonArray();

		int sequence = -1;

		// FOR EACH CHUNK
		for (int idxChunks = 0; idxChunks < chunks.size(); idxChunks++) {

			JsonObject chunk = chunks.get(idxChunks).getAsJsonObject();

//			result/chunks/tokens/0	string	形態素の表記
//			result/chunks/tokens/1	string	形態素の読みがな
//			result/chunks/tokens/2	string	形態素の基本形表記
//			result/chunks/tokens/3	string	形態素の品詞
//			result/chunks/tokens/4	string	形態素の品詞細分類
//			result/chunks/tokens/5	string	形態素の活用型
//			result/chunks/tokens/6	string	形態素の活用形

			int chunkHead = chunk.get("head").getAsInt();
			int chunkId = chunk.get("id").getAsInt();

			JsonArray tokens = chunk.get("tokens").getAsJsonArray();

			for (int idxToken = 0; idxToken < tokens.size(); idxToken++) {

				sequence++;

				DefaultKeywordWithDependency kwd = new DefaultKeywordWithDependency();

				JsonArray token = tokens.get(idxToken).getAsJsonArray();

				String tokenSurface = token.get(0).getAsString();
				String tokenReading = token.get(1).getAsString();
				String tokenBaseForm = token.get(2).getAsString();
				String tokenPos = token.get(3).getAsString();
				String tokenFeature = token.get(4).getAsString();

				String katsuyou1 = token.get(5).getAsString();
				String katsuyou2 = token.get(6).getAsString();

				kwd.setLex(tokenSurface);
				kwd.setReading(tokenReading);
				kwd.setStr(tokenBaseForm);
				kwd.setFacet(tokenPos);
				kwd.setSequence(sequence); // 連番
				kwd.setBegin(begin);
				kwd.setEnd(begin + tokenSurface.length());

				String fullMorphemId = "" + chunkId + "-" + idxToken;

				map.put(fullMorphemId, kwd);

				// MEMO: ヤフーの構文解析は文をまたいでも係り受けとみなすが、修正する
				boolean isEndOfSentence = tokenFeature.startsWith("句点");

				// 先頭の Token
				if (idxToken == 0) {
					if (isEndOfSentence == false) {
						kwd.setDependencyKey(chunkHead + "-" + "0");
					} else {
						kwd.setDependencyKey("-1");
					}
				}
				// ２つ目以降の Token
				else if (idxToken > 0) {
					// このMorphemが最後かもしれないのでこのようにセットしておく
					if (isEndOfSentence == false) {
						kwd.setDependencyKey(chunkHead + "-" + "0");
					} else {
						kwd.setDependencyKey("-1");
					}
					// ひとつ前の Morphem について DependencyKey を上書きする
					if (idxToken > 0) {
						map.get(chunkId + "-" + (idxToken - 1)).setDependencyKey(chunkId + "-" + (idxToken));
					}
				} //
				else {
				}

				begin += tokenSurface.length();

			} // END OF FOR2

		} // END OF FOR1

		for (String key : map.keySet()) {

			DefaultKeywordWithDependency kwd = map.get(key);

			if (map.get(kwd.getDependencyKey()) != null) {

				String dependencyKey = kwd.getDependencyKey();

				logger.debug("dep: " + dependencyKey);

				kwd.setParent(map.get(dependencyKey));

			}
		}

		for (String key : map.keySet()) {
			DefaultKeywordWithDependency kwd = map.get(key);
			logger.debug(key + ": isRoot: " + kwd.isRoot() + " " + kwd.toString());
			if (kwd.isRoot()) {
				roots.add(kwd);
			}
		}

	}

}
