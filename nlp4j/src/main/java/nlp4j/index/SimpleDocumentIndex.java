package nlp4j.index;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.Index;
import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;

/**
 * シンプルなドキュメントインデックスのクラスです。<br/>
 * Simple document index.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public class SimpleDocumentIndex implements Index {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	HashMap<String, Document> mapDocument = new HashMap<>();

	// <String facet, HashMap <String lex, Long count>>
	HashMap<String, HashMap<String, Long>> mapKeywordCount = new HashMap<>();

	HashMap<Keyword, Long> keywordCount = new HashMap<Keyword, Long>();

	@Override
	public void addDocument(Document doc) {
		mapDocument.put(doc.getId(), doc);
		for (Keyword kwd : doc.getKeywords()) {
			addKeyword(kwd);
		}
	}

	@Override
	public void addDocuments(List<Document> docs) {
		for (Document doc : docs) {
			addDocument(doc);
		}
	}

	private void addKeyword(Keyword kwd) {
		{
			Long kwCount = keywordCount.get(kwd);
			if (kwCount == null) {
				keywordCount.put(kwd, (long) 1);
			} else {
				kwCount++;
				kwd.setCount(kwCount);
				keywordCount.put(kwd, kwCount);
			}
		}

		// facet , keyword, count

		String facet = kwd.getFacet();

		HashMap<String, Long> keywordCount = mapKeywordCount.get(facet);

		if (keywordCount == null) {

			keywordCount = new HashMap<>();
			keywordCount.put(kwd.getLex(), (long) 1);
			logger.debug("put: new keyword(1)");

			mapKeywordCount.put(facet, keywordCount);
			logger.debug("put: new facet count");
		} else {

			Long l = keywordCount.get(kwd.getLex());

			if (l == null) {
				keywordCount.put(kwd.getLex(), (long) 1);
				logger.debug("put: new keyword(2)");
			} else {
				l++;
				keywordCount.put(kwd.getLex(), l);
				logger.debug("increment: keyword");
			}

		}

	}

	@Override
	public List<Keyword> getKeywords(String facet) {

		HashMap<String, Long> kwCount = mapKeywordCount.get(facet);

		if (kwCount == null) {
			return new ArrayList<Keyword>();
		} else {
			ArrayList<Keyword> kwds = new ArrayList<Keyword>();
			for (String key : kwCount.keySet()) {
				String lex = key;
				long count = kwCount.get(key);
				Keyword kwd = new DefaultKeyword();
				kwd.setLex(lex);
				kwd.setStr(lex);
				kwd.setCount(count);
				kwd.setFacet(facet);
				kwds.add(kwd);
			}
			return kwds;
		}

	}

	@Override
	public List<Keyword> getKeywords(String facet, String condition) {

		ArrayList<Document> dd = new ArrayList<>();

		for (String key : mapDocument.keySet()) {
			Document doc = mapDocument.get(key);
			dd.add(doc);
		}

		String[] cc = condition.split("=");
		String condKey = cc[0];
		String condValue = cc[1];

		List<Document> ddd = dd.stream().filter(d -> d.getAttribute(condKey).equals(condValue))
				.collect(Collectors.toList());
		Index smallIndex = new SimpleDocumentIndex();
		smallIndex.addDocuments(ddd);

		List<Keyword> kwds = smallIndex.getKeywords(facet);

		// 相関値の計算
		int docCount0 = mapDocument.size();
		int docCount1 = ddd.size();

		for (int n = 0; n < kwds.size(); n++) {
			Keyword kwd = kwds.get(n);
			long keywordCount0 = mapKeywordCount.get(kwd.getFacet()).get(kwd.getLex());
			long keywordCount1 = kwd.getCount();

			double corr = ((double) keywordCount1 / (double) keywordCount0) / ((double) docCount1 / (double) docCount0);
			kwd.setCorrelation(corr);
		}

		List<Keyword> kwds2 = kwds.stream().sorted(Comparator.comparing(Keyword::getCorrelation).reversed())
				.collect(Collectors.toList());

		return kwds2;
	}

	@Override
	public String toString() {
		return "SimpleDocumentIndex [mapDocument=" + mapDocument + ", mapKeywordCount=" + mapKeywordCount + "]";
	}

	@Override
	public List<Keyword> getKeywords() {
		ArrayList<Keyword> kwds = new ArrayList<Keyword>(keywordCount.keySet());
		// clone を正しく実装する必要がある？
		for (int n = 0; n < kwds.size(); n++) {
			Long l = keywordCount.get(kwds.get(n));
			kwds.get(n).setCount(l);
		}
		List<Keyword> kwds2 = kwds.stream().sorted(Comparator.comparing(Keyword::getCount).reversed())
				.collect(Collectors.toList());
		return kwds2;
	}

}
