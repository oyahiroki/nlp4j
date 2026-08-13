package nlp4j.lucene9;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.index.SortedSetDocValues;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.SimpleCollector;
import org.apache.lucene.util.BytesRef;

import nlp4j.json.JsonNode;

/**
 * Aggregation for counting term occurrences in a field. Similar to OpenSearch's
 * terms aggregation, returns the top N terms by document count. Supports both
 * single-valued (SortedDocValues) and multi-valued (SortedSetDocValues) fields.
 */
public class TermsAggregation {

	private final String name;
	private final String field;
	private final int size;

	/**
	 * Constructs a new TermsAggregation.
	 *
	 * @param name  the aggregation name
	 * @param field the field to aggregate on
	 * @param size  the maximum number of terms to return
	 */
	public TermsAggregation(String name, String field, int size) {
		this.name = name;
		this.field = field;
		this.size = size;
	}

	/**
	 * Executes the terms aggregation.
	 *
	 * @param searcher the Lucene IndexSearcher to use
	 * @param query    the query to filter documents
	 * @return a JsonNode containing the aggregation results in OpenSearch format
	 * @throws IOException if an I/O error occurs during aggregation
	 */
	public JsonNode execute(IndexSearcher searcher, Query query) throws IOException {

		TermsCollector collector = new TermsCollector(field);
		searcher.search(query, collector);

		Map<String, Long> counts = collector.getCounts();

		// Sort by count descending and limit to size
		Map<String, Long> topTerms = counts.entrySet().stream()
				.sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())).limit(size)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		return buildResult(topTerms);
	}

	/**
	 * Builds the aggregation result in OpenSearch-compatible JSON format.
	 *
	 * @param topTerms the top terms with their document counts
	 * @return a JsonNode containing the formatted aggregation result
	 */
	private JsonNode buildResult(Map<String, Long> topTerms) {
		JsonNode result = JsonNode.object();

		long sumOtherDocCount = 0;
		long docCountErrorUpperBound = 0;

		JsonNode buckets = JsonNode.array();

		for (Map.Entry<String, Long> entry : topTerms.entrySet()) {
			JsonNode bucket = JsonNode.object();
			bucket.put("key", entry.getKey());
			bucket.put("doc_count", entry.getValue());
			buckets.add(bucket);
		}

		result.put("doc_count_error_upper_bound", docCountErrorUpperBound);
		result.put("sum_other_doc_count", sumOtherDocCount);
		result.put("buckets", buckets);

		return result;
	}

	/**
	 * Collector for gathering term counts from matching documents. Automatically
	 * detects whether the field uses SortedDocValues (single-valued) or
	 * SortedSetDocValues (multi-valued) and handles both.
	 */
	private static class TermsCollector extends SimpleCollector {

		private final String field;
		private final Map<String, Long> counts = new HashMap<>();
		private SortedDocValues sortedDocValues;
		private SortedSetDocValues sortedSetDocValues;

		/**
		 * Constructs a new TermsCollector.
		 *
		 * @param field the field to collect terms from
		 */
		public TermsCollector(String field) {
			this.field = field;
		}

		/**
		 * Collects term data from a document. Handles both single-valued and
		 * multi-valued fields.
		 *
		 * @param doc the document ID
		 * @throws IOException if an I/O error occurs
		 */
		@Override
		public void collect(int doc) throws IOException {
			if (sortedSetDocValues != null) {
				// Multi-valued: iterate over all values for this document
				if (sortedSetDocValues.advanceExact(doc)) {
					long ord;
					while ((ord = sortedSetDocValues.nextOrd()) != SortedSetDocValues.NO_MORE_ORDS) {
						BytesRef term = sortedSetDocValues.lookupOrd(ord);
						counts.merge(term.utf8ToString(), 1L, Long::sum);
					}
				}
			} else if (sortedDocValues != null) {
				// Single-valued
				if (sortedDocValues.advanceExact(doc)) {
					BytesRef term = sortedDocValues.lookupOrd(sortedDocValues.ordValue());
					counts.merge(term.utf8ToString(), 1L, Long::sum);
				}
			}
		}

		@Override
		protected void doSetNextReader(LeafReaderContext context) throws IOException {

			// セグメント切り替え時に前の参照をクリア
			sortedDocValues = null;
			sortedSetDocValues = null;

			FieldInfo fieldInfo = context.reader().getFieldInfos().fieldInfo(field);

			/*
			 * このセグメントに対象フィールドが存在しない場合。
			 *
			 * これはエラーではない。 このセグメントには集計対象の値がないものとしてスキップする。
			 */
			if (fieldInfo == null) {
				return;
			}

			DocValuesType docValuesType = fieldInfo.getDocValuesType();

			if (docValuesType == DocValuesType.SORTED_SET) {
				sortedSetDocValues = context.reader().getSortedSetDocValues(field);
				return;
			}

			if (docValuesType == DocValuesType.SORTED) {
				sortedDocValues = context.reader().getSortedDocValues(field);
				return;
			}

			/*
			 * フィールド自体は存在するが、 aggregation に必要な DocValues が設定されていない。
			 *
			 * これはスキーマ／インデックス設定上の問題なので例外にする。
			 */
			throw new IllegalArgumentException("Field [" + field + "] is not aggregatable. "
					+ "Expected SortedDocValues or SortedSetDocValues, but was " + docValuesType + ".");
		}

		@Override
		public ScoreMode scoreMode() {
			return ScoreMode.COMPLETE_NO_SCORES;
		}

		/**
		 * Returns the collected term counts.
		 *
		 * @return a map of terms to their document counts
		 */
		public Map<String, Long> getCounts() {
			return counts;
		}
	}
}
