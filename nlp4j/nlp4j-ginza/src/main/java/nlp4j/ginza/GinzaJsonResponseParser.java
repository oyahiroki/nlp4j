package nlp4j.ginza;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.impl.DefaultKeywordWithDependency;

public class GinzaJsonResponseParser {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public void parseResponse(String json) {

		List<DefaultKeywordWithDependency> kwds = new ArrayList<>();

		{
			Gson gson = new Gson();
			JsonArray responseJsonArray = gson.fromJson(json, JsonArray.class);
			logger.info("Response array size: " + responseJsonArray.size());
			for (int n = 0; n < responseJsonArray.size(); n++) {

				JsonObject responseObj = responseJsonArray.get(n).getAsJsonObject();
				JsonArray paragraphs = responseObj.get("paragraphs").getAsJsonArray();
				logger.info("paragraphs.size(): " + paragraphs.size());
				// FOR_EACH(パラグラフ)
				for (int idxParagraphs = 0; idxParagraphs < paragraphs.size(); idxParagraphs++) {

					JsonObject jsonObjParagraph = paragraphs.get(idxParagraphs).getAsJsonObject();
					if (logger.isDebugEnabled()) {
						if (jsonObjParagraph != null) {
							logger.debug(jsonObjParagraph.toString());
						}
					}

					// 原文
					String raw = jsonObjParagraph.get("raw").getAsString();

					JsonArray sentences = jsonObjParagraph.get("sentences").getAsJsonArray();

					// FOR_EACH(SENTENCE)
					sentences.forEach(elm -> {
						HashMap<String, DefaultKeywordWithDependency> mapKwd = new HashMap<>();

						JsonArray tokens = elm.getAsJsonObject().get("tokens").getAsJsonArray();

						// FOR_EACH TOKEN
						tokens.forEach(token -> {
							JsonObject tk = token.getAsJsonObject();
							String id = "" + tk.get("id").getAsInt();
							// "orth": "今日",
							String orth = tk.get("orth").getAsString();
							// "tag": "名詞-普通名詞-副詞可能",
							String tag = tk.get("tag").getAsString();
							// "pos": "NOUN",
							String pos = tk.get("pos").getAsString();
							// "lemma": "今日",
							String lemma = tk.get("lemma").getAsString();
							// "head": 4,
							String head = "" + tk.get("head").getAsInt();
							// "dep": "obl"
							String dep = tk.get("dep").getAsString();
//							String ner = tk.get("ner").getAsString();
							DefaultKeywordWithDependency kwd = new DefaultKeywordWithDependency();
							kwd.setLex(lemma);
							kwd.setStr(orth);
							kwd.setFacet(pos);
//							kwd.setFacet(tag.substring(0, tag.indexOf("-")));
							kwd.setRelation(dep);
							kwd.setDependencyKey(head);
							mapKwd.put(id, kwd);
						}); // END_OF FOR_EACH TOKEN

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

					}); // END_OF FOR_EACH(SENTENCE)
				} // END_OF FOR_EACH(パラグラフ)
			}

		}

		for (DefaultKeywordWithDependency kwd : kwds) {
			System.err.println(kwd.toStringAsXml());
		}

	}

}
