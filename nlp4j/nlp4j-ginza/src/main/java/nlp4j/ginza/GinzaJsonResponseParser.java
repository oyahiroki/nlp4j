package nlp4j.ginza;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nlp4j.Keyword;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * Parse GiNZA response
 * 
 * @author Hiroki Oya
 *
 */
public class GinzaJsonResponseParser {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public List<Keyword> parseResponse(String json) {

		List<Keyword> kwds = new ArrayList<>();

		{
			Gson gson = new Gson();

			// GiNZA のレスポンスは JsonObject
			JsonObject responseDoc = gson.fromJson(json, JsonObject.class);

			JsonArray sents = responseDoc.get("sents").getAsJsonArray();

			for (int idxSents = 0; idxSents < sents.size(); idxSents++) {

				JsonObject sent = sents.get(idxSents).getAsJsonObject();

				JsonArray tokens = sent.get("tokens").getAsJsonArray();

				HashMap<String, DefaultKeywordWithDependency> mapKwd = new HashMap<>();

				for (int idxToken = 0; idxToken < tokens.size(); idxToken++) {
					JsonElement token = tokens.get(idxToken);

					DefaultKeywordWithDependency kwd = new DefaultKeywordWithDependency();
					{
						kwd.setSentenceIndex(idxSents);
					}
					JsonObject tk = token.getAsJsonObject();
					String id = "" + tk.get("i").getAsInt();
					{
						// "orth": "今日",
						String orth = tk.get("orth").getAsString();
						kwd.setStr(orth);
					}
					{
						// "tag": "名詞-普通名詞-副詞可能",
						String tag = tk.get("tag").getAsString();
						kwd.setFacet(tag);
					}
					{
						// "pos": "NOUN",
						String pos = tk.get("pos").getAsString();
						kwd.setUPos(pos);
					}
					{
						// "lemma": "今日",
						String lemma = tk.get("lemma").getAsString();
						kwd.setLex(lemma);
					}
					// "head": 4,
					String head = "" + tk.get("head.i").getAsInt();
					// "dep": "obl"
					String dep = tk.get("dep").getAsString();
//							String ner = tk.get("ner").getAsString();
//							kwd.setFacet(tag.substring(0, tag.indexOf("-")));
					kwd.setRelation(dep);
					kwd.setDependencyKey(head);
					mapKwd.put(id, kwd);
				}

//						// FOR_EACH TOKEN
//						tokens.forEach(token -> {
//						}); // END_OF FOR_EACH TOKEN

				DefaultKeywordWithDependency root = null;

				// FOR_EACH ID_OF_MAP
				for (String id : mapKwd.keySet()) {

					DefaultKeywordWithDependency kwd = mapKwd.get(id);
					String depKey = mapKwd.get(id).getDependencyKey();
					if (depKey.equals(id) == false) {
						// 係り受け(Universal Dependency)
						kwd.setParent(mapKwd.get(depKey));
					}
					// root には ID と同じ head がセットされている
					else {
						root = kwd;
					}

				}
				// END_OF FOR_EACH ID_OF_MAP

				kwds.add(root);
//					}

//					// FOR_EACH(SENTENCE)
//					sentences.forEach(elm -> {
//					}); // END_OF FOR_EACH(SENTENCE)

//				} // END_OF FOR_EACH(パラグラフ)
			}

		}

		if (logger.isDebugEnabled()) {
			for (Keyword kwd : kwds) {
				logger.debug(((DefaultKeywordWithDependency) kwd).toStringAsXml());
			}
		}

		return kwds;

	}

}
