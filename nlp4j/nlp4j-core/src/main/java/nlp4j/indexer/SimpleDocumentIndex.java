package nlp4j.indexer;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.internal.bind.CollectionTypeAdapterFactory;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.counter.Counter;
import nlp4j.impl.DefaultKeyword;
import nlp4j.util.DateUtils;
import nlp4j.util.FacetUtils;
import nlp4j.util.KeywordUtil;

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

	static public final String KEY_DATEFIELD = "datefield";

	/**
	 * Map of (Document ID -> Document Object)
	 */
	private HashMap<String, Document> mapDocidDocument = new HashMap<>();

	// <String facet, HashMap <String lex, Long count>>
	private HashMap<String, HashMap<String, Long>> mapFacetKeywordCount = new HashMap<>();

	// facet -> (value, count)
	private HashMap<String, Counter<String>> mapItemCount = new HashMap<>();

	private HashMap<Keyword, Long> keywordCount = new HashMap<Keyword, Long>();

	private ArrayList<Keyword> keywords = new ArrayList<>();

	private List<String> documentids = new ArrayList<>();

	/**
	 * key (keyword facet + "." + keyword lex) -> Document IDs
	 */
	private HashMap<String, List<String>> mapKeywordDocumentids = new HashMap<String, List<String>>();

	Counter<String> dateCounterYYYY = new Counter<>();

	Counter<String> dateCounterYYYYMM = new Counter<>();

	Counter<String> dateCounterYYYYMMDD = new Counter<>();
	// 日付フィールドの名前
	private String dateField = null;
	private String dateFieldFormat = null;

	@Override
	public void addDocument(Document doc) {
		String docId = doc.getId();

		// DOCUMENT ID
		if (docId != null) {
			documentids.add(docId);
			mapDocidDocument.put(docId, doc);
		} else {
			logger.warn("ID is null: " + doc);
		}

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

				String key = kwd.getFacet() + "." + kwd.getLex();
				if (mapKeywordDocumentids.get(key) == null) {
					mapKeywordDocumentids.put(key, new ArrayList<String>());
				}
				if (mapKeywordDocumentids.get(key).contains(docId) == false) {
					mapKeywordDocumentids.get(key).add(docId);
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
			HashMap<String, Long> keywordCount = mapFacetKeywordCount.get(facet);
			if (keywordCount == null) {
				keywordCount = new HashMap<>();
				keywordCount.put(kwd.getLex(), (long) 1);
				logger.debug("put: new keyword(1)");
				mapFacetKeywordCount.put(facet, keywordCount);
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
	 * @return
	 */
	public List<Keyword> getDateCountDay() {
		return getDateCount("YYYYMMDD");
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
	public List<Keyword> getDateCountYear() {
		return getDateCount("YYYY");
	}

	public List<String> getDocumentIds() {
		return this.documentids;
	}

	/**
	 * created_at: 2022-11-19
	 * 
	 * @param kwd Keyword
	 * @return Document IDs that contains Keyword
	 */
	public List<String> getDocumentidsByKeyword(Keyword kwd) {
		String key = kwd.getFacet() + "." + kwd.getLex();
		if (this.mapKeywordDocumentids.get(key) != null) {
			return this.mapKeywordDocumentids.get(key);
		} else {
			return new ArrayList<String>();
		}
	}

	public Document getDocumentById(String id) {
		return this.mapDocidDocument.get(id);
	}

	/**
	 * @since 20220115
	 * @return count of documents
	 */
	public int getDocumentSize() {
		if (this.mapDocidDocument == null) {
			return 0;
		} else {
			return mapDocidDocument.size();
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

		HashMap<String, Long> kwCount = mapFacetKeywordCount.get(facet);

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

		for (String key : mapDocidDocument.keySet()) {
			Document doc = mapDocidDocument.get(key);
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
		int docCount0 = mapDocidDocument.size();
		int docCount1 = ddd.size();

		for (int n = 0; n < kwds.size(); n++) {
			Keyword kwd = kwds.get(n);
			long keywordCount0 = mapFacetKeywordCount.get(kwd.getFacet()).get(kwd.getLex());
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
		return "SimpleDocumentIndex [" //
				+ "mapDocument.size=" + mapDocidDocument.keySet().size() //
				+ ", mapKeywordCount.size=" + mapFacetKeywordCount.keySet().size()//
				+ "]";
	}

	public long getDocumentCount(Keyword kwd) {
		return getDocumentidsByKeyword(kwd).size();
	}

	public List<Keyword> getRelevantKeywords(Keyword kwd, double minimumCorrelation) {

		String facet = kwd.getFacet();

		// キーワードが含まれるドキュメント
		List<String> document_ids = getDocumentidsByKeyword(kwd);
		// キーワードが含まれるドキュメントに含まれるキーワードのカウンタ
		Counter<String> kwdCounter = new Counter<>();
		{
			for (String document_id : document_ids) {
				Document doc = getDocumentById(document_id);
				List<Keyword> kk = (facet != null) ? doc.getKeywords(facet) : doc.getKeywords();
				for (Keyword k : kk) {
					kwdCounter.add(k.getLex());
				}
			}
		}
		// キーワードが含まれるドキュメントに含まれるキーワード
		List<String> lexs = kwdCounter.getObjectList();
		// 全文書数
		long countDocAll = getDocumentSize();
		// 共起のキーワード
		List<Keyword> kk = new ArrayList<Keyword>();

		for (String lex : lexs) {
			Keyword k = new DefaultKeyword(facet, lex);
			// 文書セットの中で出てくるカウント
			long countRelevancy = kwdCounter.getCount(lex);
			k.setCount(countRelevancy);

			long countKeywordAll = getDocumentCount(k);
			if (countKeywordAll == 0) {
				countKeywordAll = 1;
			}

			double w = ((double) document_ids.size() / (double) countRelevancy) //
					/ //
					((double) countKeywordAll / (double) countDocAll);
			k.setCorrelation(w);
			kk.add(k);
		}

		kk = kk.stream().filter(k -> k.getCorrelation() > minimumCorrelation).collect(Collectors.toList());

		Collections.sort(kk, new Comparator<Keyword>() {
			@Override
			public int compare(Keyword o1, Keyword o2) {
				double d2 = o2.getCorrelation();
				double d1 = o1.getCorrelation();
				if (d2 == d1) {
					return 0;
				} //
				else if (d2 > d1) {
					return 1;
				} //
				else {
					return -1;
				}
			}
		});

		return kk;
	}

}
