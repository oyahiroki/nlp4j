package nlp4j.dictionary;

import java.lang.invoke.MethodHandles;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Keyword;
import nlp4j.KeywordBuilder;

/**
 * 文字列マッチでユーザー辞書を適用する<br>
 * Extract Keywords by String Match based algorithm
 * 
 * @author Hiroki Oya
 * @since 1.3.7.5
 */
public class UserDictionaryStringMatcher {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	// MAP(正規形 → ファセット)
	Map<String, List<String>> mapLexFacets = new HashMap<>();
	// MAP(同義語 → 正規形)
	Map<String, List<String>> mapSynonymLex = new HashMap<>();

	public void put(String facet, String entry, String... synonyms) {

		entry = Normalizer.normalize(entry, Form.NFKC);

		if (this.mapLexFacets.get(entry) == null) {
			this.mapLexFacets.put(entry, new ArrayList<String>());
		}
		this.mapLexFacets.get(entry).add(facet);

//		String s = synonyms[0];
		if (synonyms != null) {
			for (String synonym : synonyms) {
				if (this.mapSynonymLex.get(synonym) == null) {
					this.mapSynonymLex.put(synonym, new ArrayList<String>());
				}
				this.mapSynonymLex.get(synonym).add(entry);
			}
		}

	}

	public List<Keyword> parse(String s) {

		List<Keyword> kwds = new ArrayList<Keyword>();

		for (int n = 0; n < s.length(); n++) {
			String s2 = s.substring(n);
			// 正規形にヒットするかをチェック
			for (String entry : this.mapLexFacets.keySet()) {
				if (s2.startsWith(entry)) {
					int begin = n;
					int end = n + entry.length();
					List<String> facets = this.mapLexFacets.get(entry);
					logger.debug(begin + "," + end + "," + entry + "," + facets + "," + entry);
					for (String facet : facets) {
						kwds.add((new KeywordBuilder()).begin(begin).end(end).lex(entry).facet(facet).str(entry)
								.build());
					}
				}
			}
			// 同義語にヒットするかをチェック
			for (String synonym : this.mapSynonymLex.keySet()) {
				if (s2.startsWith(synonym)) {
					int begin = n;
					int end = n + synonym.length();
					List<String> lexs = this.mapSynonymLex.get(synonym);
					for (String lex : lexs) {
						List<String> facets = this.mapLexFacets.get(lex);
						logger.debug(begin + "," + end + "," + lex + "," + facets + "," + synonym);
						for (String facet : facets) {
							kwds.add((new KeywordBuilder()).begin(begin).end(end).lex(lex).facet(facet).str(synonym)
									.build());
						}
					}

				}
			}
		}
		return kwds;

	}

	@Override
	public String toString() {
		int mapLexFacetsSize = 0;
		int mapSynonymLexSize = 0;
		if (mapLexFacets != null) {
			mapLexFacetsSize = mapLexFacets.size();
		}
		if (mapSynonymLex != null) {
			mapSynonymLexSize = mapSynonymLex.size();
		}
		return "UserDictionaryStringMatcher [" //
				+ "mapLexFacets.size=" + mapLexFacetsSize + ", " //
				+ "mapSynonymLex.size=" + mapSynonymLexSize //
				+ "]";
	}

}
