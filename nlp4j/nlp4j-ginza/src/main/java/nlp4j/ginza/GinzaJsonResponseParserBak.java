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
public class GinzaJsonResponseParserBak {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public List<Keyword> parseResponse(String json) {

		List<Keyword> kwds = new ArrayList<>();

		{
			Gson gson = new Gson();
			// GiNZA のレスポンスは JsonArray
			JsonArray responseJsonArray = gson.fromJson(json, JsonArray.class);
			if (logger.isDebugEnabled()) {
				logger.debug("Response array size: " + responseJsonArray.size());
			}

			for (int n = 0; n < responseJsonArray.size(); n++) {

				JsonObject responseObj = responseJsonArray.get(n).getAsJsonObject();
				JsonArray paragraphs = responseObj.get("paragraphs").getAsJsonArray();
				if (logger.isDebugEnabled()) {
					logger.debug("paragraphs.size(): " + paragraphs.size());
				}
				// FOR_EACH(パラグラフ)
				for (int idxParagraphs = 0; idxParagraphs < paragraphs.size(); idxParagraphs++) {

					JsonObject jsonObjParagraph = paragraphs.get(idxParagraphs).getAsJsonObject();
					if (logger.isDebugEnabled()) {
						if (jsonObjParagraph != null) {
							logger.debug(jsonObjParagraph.toString());
						}
					}

					// 原文
//					String raw = jsonObjParagraph.get("raw").getAsString();

					JsonArray sentences = jsonObjParagraph.get("sentences").getAsJsonArray();

					for (int idxSentence = 0; idxSentence < sentences.size(); idxSentence++) {
						JsonElement elm = sentences.get(idxSentence);

						HashMap<String, DefaultKeywordWithDependency> mapKwd = new HashMap<>();

						JsonArray tokens = elm.getAsJsonObject().get("tokens").getAsJsonArray();

						for (int idxToken = 0; idxToken < tokens.size(); idxToken++) {
							JsonElement token = tokens.get(idxToken);

							DefaultKeywordWithDependency kwd = new DefaultKeywordWithDependency();
							{
								kwd.setParagraphIndex(idxParagraphs);
								kwd.setSentenceIndex(idxSentence);
							}
							JsonObject tk = token.getAsJsonObject();
							String id = "" + tk.get("id").getAsInt();
							{
								// "orth": "今日",
								String orth = tk.get("orth").getAsString();
								kwd.setStr(orth);
							}
							{
								// "tag": "名詞-普通名詞-副詞可能",
								String tag = tk.get("tag").getAsString();
							}
							{
								// "pos": "NOUN",
								String pos = tk.get("pos").getAsString();
								kwd.setFacet(pos);
							}
							{
								// "lemma": "今日",
								String lemma = tk.get("lemma").getAsString();
								kwd.setLex(lemma);
							}
							// "head": 4,
							String head = "" + tk.get("head").getAsInt();
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
					}

//					// FOR_EACH(SENTENCE)
//					sentences.forEach(elm -> {
//					}); // END_OF FOR_EACH(SENTENCE)

				} // END_OF FOR_EACH(パラグラフ)
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
