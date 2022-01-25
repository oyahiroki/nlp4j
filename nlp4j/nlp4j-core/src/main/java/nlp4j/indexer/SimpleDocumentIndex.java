package nlp4j.indexer;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.counter.Count;
import nlp4j.counter.Counter;
import nlp4j.impl.DefaultKeyword;
import nlp4j.util.DateUtils;
import nlp4j.util.FacetUtils;

/**
 * シンプルなドキュメントインデックスのクラスです。<br>
 * Simple document index.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public class SimpleDocumentIndex extends AbstractDocumentIndexer implements DocumentIndexer {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	HashMap<String, Document> mapDocument = new HashMap<>();

	// <String facet, HashMap <String lex, Long count>>
	HashMap<String, HashMap<String, Long>> mapKeywordCount = new HashMap<>();

	// facet -> (value, count)
	HashMap<String, Counter<String>> mapItemCount = new HashMap<>();

	HashMap<Keyword, Long> keywordCount = new HashMap<Keyword, Long>();

	ArrayList<Keyword> keywords = new ArrayList<>();

	@Override
	public void addDocument(Document doc) {
		mapDocument.put(doc.getId(), doc);

		{ // COUNT ITEM
			// Facet count
			// FOR EACH (ITEM)
			for (String facet : doc.getAttributeKeys()) {
				String value = doc.getAttributeAsString(facet);
				if (value == null) {
					continue;
				}
				if (this.mapItemCount.containsKey(facet) == false) {
					Counter<String> counter = new Counter<>();
					counter.add(value);
					this.mapItemCount.put(facet, counter);
				} else {
					this.mapItemCount.get(facet).add(value);
				}
			} // END OF FOR EACH (ITEM)
		} // END OF COUNT ITEM

		{ // COUNT KEYWORD
			// Keyword count キーワードカウント
			Set<String> usedLex = new HashSet<String>();
			for (Keyword kwd : doc.getKeywords()) {
				// 同一文書に複数の同じキーワードが含まれる場合はカウントしない
				if (usedLex.contains(kwd.getLex()) == false) {
					countKeyword(kwd);
					usedLex.add(kwd.getLex());
				}
			}
		} // END OF COUNT KEYWORD

		{ // COUNT DATE
			// 日付カウント
			if (this.dateField != null) {
				boolean countYYYY = true;
				boolean countYYYYMM = true;
				boolean countYYYYMMDD = true;

				String value = doc.getAttributeAsString(dateField);
				if (value == null) {
				} //
				else {

					SimpleDateFormat sdf = new SimpleDateFormat(this.dateFieldFormat);
					SimpleDateFormat sdfYYYY = new SimpleDateFormat("yyyy");
					SimpleDateFormat sdfYYYYMM = new SimpleDateFormat("yyyyMM");
					SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("yyyyMMdd");

					try {
						Date valueDate = sdf.parse(value);

						if (countYYYY == true) {
							String v = sdfYYYY.format(valueDate);
							dateCounterYYYY.add(v);
						}

						if (countYYYYMM == true) {
							String v = sdfYYYYMM.format(valueDate);
							dateCounterYYYYMM.add(v);
						}
						if (countYYYYMMDD == true) {
							String v = sdfYYYYMMDD.format(valueDate);
							dateCounterYYYYMMDD.add(v);
						}

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		} // END OF COUNT DATE

	}

	Counter<String> dateCounterYYYY = new Counter<>();
	Counter<String> dateCounterYYYYMM = new Counter<>();
	Counter<String> dateCounterYYYYMMDD = new Counter<>();

	private void countKeyword(Keyword kwd) {
		{
			keywords.add(kwd);
		}
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

		String facet0 = kwd.getFacet();

		List<String> ff = FacetUtils.splitFacetPath(facet0);
		for (String facet : ff) {
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
	}

	/**
	 * @return
	 */
	public List<Keyword> getDateCountMonth() {
		return getDateCount("YYYYMM");
	}

	/**
	 * @return
	 */
	public List<Keyword> getDateCountDay() {
		return getDateCount("YYYYMMDD");
	}

	/**
	 * @return
	 */
	public List<Keyword> getDateCountYear() {
		return getDateCount("YYYY");
	}

	/**
	 * @param unit
	 * @return
	 */
	public List<Keyword> getDateCount(String unit) {
		// to be returned
		List<Keyword> kwds = new ArrayList<>();

		Counter<String> counter = null;
		String valueFormat = null;
		int unitField = -1;

		if ("YYYY".equals(unit.toUpperCase())) {
			counter = this.dateCounterYYYY;
			valueFormat = "yyyy";
			unitField = Calendar.YEAR;
		} //
		else if ("YYYYMM".equals(unit.toUpperCase())) {
			counter = this.dateCounterYYYYMM;
			valueFormat = "yyyyMM";
			unitField = Calendar.MONDAY;
		} //
		else if ("YYYYMMDD".equals(unit.toUpperCase())) {
			counter = this.dateCounterYYYYMMDD;
			valueFormat = "yyyyMMdd";
			unitField = Calendar.DAY_OF_MONTH;
		} //

		if (counter != null) {
			List<String> keys = counter.getObjectListSorted();

			if (keys != null && keys.size() > 0) {
				keys.sort(Comparator.naturalOrder());
				String min = keys.get(0);
				String max = keys.get(keys.size() - 1);

				List<String> dd = DateUtils.getCalendarValues(min, max, valueFormat, unitField);

				for (String key : dd) {
					Keyword kwd = new DefaultKeyword();
					kwd.setLex(key);
					kwd.setFacet(this.dateField);

					int count = counter.getCount(key);
					kwd.setCount(count);
					kwds.add(kwd);
				}
			}

		}

		return kwds;

	}

	/**
	 * @since 20220115
	 * @return count of documents
	 */
	public int getDocumentSize() {
		if (this.mapDocument == null) {
			return 0;
		} else {
			return mapDocument.size();
		}
	}

	public List<Keyword> getItemCount(String facet) {
		if (this.mapItemCount.containsKey(facet)) {
			List<Keyword> kwds = new ArrayList<>();
//			Map<String, Long> map = this.mapItemCount.get(facet);
			Counter<String> counter = this.mapItemCount.get(facet);
//			for (String key : map.keySet()) {
//				long count = map.get(key);
//				Keyword kwd = new DefaultKeyword();
//				kwd.setLex(key);
//				kwd.setCount(count);
//				kwds.add(kwd);
//			}
			for (String key : counter.getObjectList()) {
				long count = counter.getCount(key);
				Keyword kwd = new DefaultKeyword();
				kwd.setLex(key);
				kwd.setCount(count);
				kwds.add(kwd);
			}
			return kwds;
//			return map;
		} else {
			return new ArrayList<Keyword>();
		}

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

		if (condition == null) {
			return getKeywords(facet);
		}

		String[] cc = condition.split("=");
		String condKey = cc[0];
		String condValue = cc[1];

		List<Document> ddd = dd.stream().filter(d -> d.getAttribute(condKey).equals(condValue))
				.collect(Collectors.toList());
		DocumentIndexer smallIndex = new SimpleDocumentIndex();
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

	/**
	 * カウント無しキーワードのリストを返します。
	 * 
	 * @return Keywords
	 */
	public List<Keyword> getKeywordsWithoutCount() {
		return keywords;
	}

	// 日付フィールドの名前
	private String dateField = null;
	private String dateFieldFormat = null;

	static public final String KEY_DATEFIELD = "datefield";

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if (KEY_DATEFIELD.equals(key)) {
			if (value.split(",").length > 1) {
				this.dateField = value.split(",")[0];
				this.dateFieldFormat = value.split(",")[1];
			} else {

			}
		}
	}

	@Override
	public String toString() {
		return "SimpleDocumentIndex [mapDocument=" + mapDocument + ", mapKeywordCount=" + mapKeywordCount + "]";
	}

}
