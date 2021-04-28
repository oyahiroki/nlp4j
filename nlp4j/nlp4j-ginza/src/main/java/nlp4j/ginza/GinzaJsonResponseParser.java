package nlp4j.ginza;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeywordWithDependency;

public class GinzaJsonResponseParser {
	public void parseResponse(String json) {
		{
			Gson gson = new Gson();
			JsonArray responseObj = gson.fromJson(json, JsonArray.class);

			for (int n = 0; n < responseObj.size(); n++) {
				JsonObject p = responseObj.get(n).getAsJsonObject();
				JsonArray pg = p.get("paragraphs").getAsJsonArray();
				for (int nn = 0; nn < pg.size(); nn++) {
					JsonObject o = pg.get(nn).getAsJsonObject();
					System.err.println(o.toString());
					String raw = o.get("raw").getAsString();
					JsonArray ss = o.get("sentences").getAsJsonArray();

					ss.forEach(elm -> {
						HashMap<String, DefaultKeywordWithDependency> mapKwd = new HashMap<>();

						JsonArray tokens = elm.getAsJsonObject().get("tokens").getAsJsonArray();
						tokens.forEach(token -> {
							JsonObject tk = token.getAsJsonObject();
							String id = "" + tk.get("id").getAsInt();
							String orth = tk.get("orth").getAsString();
							String tag = tk.get("tag").getAsString();
							String pos = tk.get("pos").getAsString();
							String lemma = tk.get("lemma").getAsString();
							String head = "" + tk.get("head").getAsInt();
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
						});

//						for (DefaultKeywordWithDependency kwd : mapKwd.values()) {
//							String depKey = kwd.getDependencyKey();
//							if (mapKwd.containsKey(depKey)) {
//								kwd.setParent(mapKwd.get(depKey));
//							}
//						}
//
//						KeywordWithDependency root = mapKwd.get("1").getRoot();
//						System.err.println(root.toStringAsXml());

					});
				}
			}

		}

	}

}
