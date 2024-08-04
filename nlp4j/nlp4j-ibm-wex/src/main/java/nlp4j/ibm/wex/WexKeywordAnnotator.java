package nlp4j.ibm.wex;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordAnnotator;
import nlp4j.KeywordBuilder;
import nlp4j.UPOS20;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClient5;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.MapBuilder;

public class WexKeywordAnnotator extends AbstractDocumentAnnotator implements KeywordAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private String collection = "sandbox";
	private String language = Locale.getDefault().toString();
	private String locale = Locale.getDefault().getLanguage();
	private String endPoint = "http://localhost:8393";

	@Override
	public void annotate(Document doc) throws Exception {
		String url = this.endPoint + "/api/v10/analysis/text";

		if (super.targets != null) {
			super.targets.forEach(target -> {

				String text = doc.getAttributeAsString(target);

				Map<String, String> params = (new MapBuilder<String, String>()) //
						.put("language", this.language)//
						.put("locale", this.locale)//
						.put("output", "application/json")// xmi
						.put("collection", this.collection)//
						.put("text", text)//
						.build();//

				System.err.println(params);

				try ( //
						HttpClient client = new HttpClient5(); //
						InputStream is = client.getInputStream(url, params); //
				) {
//					System.out.println(JsonUtils.prettyPrint(org.apache.commons.io.IOUtils.toString(is)));
//					System.out.println(org.apache.commons.io.IOUtils.toString(is));

					String responseBody = org.apache.commons.io.IOUtils.toString(is);

					JsonObject jo = JsonObjectUtils.fromJson(responseBody);

					String content = jo.get("content").getAsString();

					JsonArray textfacets = jo.get("metadata").getAsJsonObject().get("textfacets").getAsJsonArray();

					for (int n = 0; n < textfacets.size(); n++) {
						JsonObject kw = textfacets.get(n).getAsJsonObject();
						String path = null;
						{
							StringBuilder sb_path = new StringBuilder();
							{
								JsonArray _path = kw.get("path").getAsJsonArray();
								for (int x = 0; x < _path.size(); x++) {
									String p = _path.get(x).getAsString();
									if (sb_path.length() > 0) {
										sb_path.append(".");
									}
									sb_path.append(p);
								}
							}
							path = sb_path.toString();
						}
						String lex = kw.get("keyword").getAsString();
						int begin = kw.get("begin").getAsInt();
						int end = kw.get("end").getAsInt();
						String str = content.substring(begin, end);

						String upos = null;
						{
							if ("_word.noun.general".equals(path)) {
								upos = UPOS20.NOUN;
							} //
							else if ("_word.adj".equals(path)) {
								upos = UPOS20.ADJ;
							} //
							else if ("_word.verb".equals(path)) {
								upos = UPOS20.VERB;
							}
						}

						if (logger.isDebugEnabled()) {
							logger.debug(
									"path:" + path + ",lex:" + lex + ",begin:" + begin + ",end:" + end + ",str:" + str);
						}

//						System.out.println(
//								"path:" + path + ",lex:" + lex + ",begin:" + begin + ",end:" + end + ",str:" + str);

						if (upos != null) {
							Keyword k = (new KeywordBuilder()) //
									.facet(upos)//
									.upos(upos)//
									.lex(lex)//
									.str(str)//
									.begin(begin)//
									.end(end)//
									.build();
							doc.addKeyword(k);
						}

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
		}

	}

}
