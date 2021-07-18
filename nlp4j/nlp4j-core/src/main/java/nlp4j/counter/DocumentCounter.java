package nlp4j.counter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-13
 * @since 1.3.2
 */
public class DocumentCounter {

	// Keyword Count : Duplicated in a single count allowed 文書内でのカウントを含む
	Counter<Keyword> countKeyword;
	private List<String> facetFilter;

	HashMap<Keyword, List<Document>> keyToDoc;
	List<Document> docs;

	HashMap<Object, Document> idToDoc;

	// DF : Document Frequency 文書数 文書内での重複カウントを含まない
	Counter<Keyword> countKeywordDF;

	int countDocument = 0;

	public DocumentCounter() {
		this.countKeyword = new Counter<>();
		this.countKeywordDF = new Counter<>();
		this.keyToDoc = new HashMap<Keyword, List<Document>>();
		this.idToDoc = new HashMap<Object, Document>();
		this.docs = new ArrayList<>();
	}

	public int getDF(Keyword kwd) {
		return this.countKeywordDF.getCount(kwd);
	}

	public Document getDocument(String id) {
		return idToDoc.get(id);
	}

	public void add(DefaultDocument doc) {
		this.docs.add(doc);
		{
			Object id = getID(doc);
			if (id != doc) {
				this.idToDoc.put(id, doc);
			}
		}
		countDocument++;
		Set<Keyword> used = new HashSet<>();
		for (Keyword kwd : doc.getKeywords()) {
			String facet = kwd.getFacet();

			if ((this.facetFilter == null) || (this.facetFilter.size() > 0 && this.facetFilter.contains(facet))) {
				countKeyword.add(kwd);
				if (used.contains(kwd) == false) {
					countKeywordDF.add(kwd);
					used.add(kwd);
				}
			}

//			if (this.facetFilter != null && this.facetFilter.size() > 0) {
//				if (this.facetFilter.contains(facet)) {
//					keywordCounter.add(kwd);
//					if (used.contains(kwd) == false) {
//						countDF.add(kwd);
//						used.add(kwd);
//					}
//				}
//			} //
//			else {
//				keywordCounter.add(kwd);
//				if (used.contains(kwd) == false) {
//					countDF.add(kwd);
//					used.add(kwd);
//				}
//			}
		}
	}

	public int getCountDocument() {
		return countDocument;
	}

	public List<Count<Keyword>> getCountKeywordSorted() {
		return this.countKeyword.getCountListSorted();
	}

	public List<Count<Keyword>> getCountKeywordSortedDF() {
		return this.countKeywordDF.getCountListSorted();
	}

	public void setFacetFilter(String facet) {
		if (this.facetFilter == null) {
			this.facetFilter = new ArrayList<>();
		}
		this.facetFilter.add(facet);
	}

	public double getIDF(Keyword kw) {

		int df = this.getDF(kw);
		int m = this.countDocument;

//		return Math.log((double) m / (double) df + 1.0);
		return MathUtil.log2((double) m / (double) df);

	}

	private String keyField;

	private Object getID(Document doc) {
		if (this.keyField != null) {
			Object k = doc.getAttribute(this.keyField);
			if (k != null) {
				return k;
			} else {
				return doc;
			}
		} else {
			return doc;
		}
	}

	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}

}
