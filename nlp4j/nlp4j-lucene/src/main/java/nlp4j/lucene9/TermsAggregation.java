package nlp4j.lucene9;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.SimpleCollector;
import org.apache.lucene.util.BytesRef;

import nlp4j.json.JsonNode;

/**
 * Aggregation for counting term occurrences in a field. Similar to OpenSearch's
 * terms aggregation, returns the top N terms by document count.
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
	 * Collector for gathering term counts from matching documents.
	 */
	private static class TermsCollector extends SimpleCollector {

		private final String field;
		private final Map<String, Long> counts = new HashMap<>();
		private SortedDocValues docValues;

		/**
		 * Constructs a new TermsCollector.
		 *
		 * @param field the field to collect terms from
		 */
		public TermsCollector(String field) {
			this.field = field;
		}

		/**
		 * Collects term data from a document.
		 *
		 * @param doc the document ID
		 * @throws IOException if an I/O error occurs
		 */
		@Override
		public void collect(int doc) throws IOException {
			if (docValues.advanceExact(doc)) {
				BytesRef term = docValues.lookupOrd(docValues.ordValue());
				String termString = term.utf8ToString();
				counts.merge(termString, 1L, Long::sum);
			}
		}

		@Override
		protected void doSetNextReader(LeafReaderContext context) throws IOException {
			docValues = context.reader().getSortedDocValues(field);

			if (docValues == null) {
				throw new IllegalArgumentException("Field [" + field + "] does not have SortedSetDocValues. "
						+ "Please add SortedSetDocValuesField when indexing.");
			}
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


