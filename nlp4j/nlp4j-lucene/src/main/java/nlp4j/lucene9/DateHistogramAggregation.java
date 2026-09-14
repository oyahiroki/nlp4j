/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.SimpleCollector;

import nlp4j.json.JsonNode;

/**
 * Date histogram aggregation backed by NumericDocValues (DATE fields).
 *
 * <p>
 * Collects min/max epoch-millis from matching documents in a single pass, then
 * fills in zero-count buckets between them.
 * </p>
 */
public class DateHistogramAggregation {

	private static final int MAX_BUCKETS = 10_000;

	private final String field;
	private final DateHistogramInterval interval;
	private final ZoneId zoneId;

	private static final DateTimeFormatter FMT_YEAR = DateTimeFormatter.ofPattern("uuuu");
	private static final DateTimeFormatter FMT_MONTH = DateTimeFormatter.ofPattern("uuuu-MM");
	private static final DateTimeFormatter FMT_HOUR = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH");

	public DateHistogramAggregation(String field, DateHistogramInterval interval, ZoneId zoneId) {
		this.field = field;
		this.interval = interval;
		this.zoneId = (zoneId != null) ? zoneId : ZoneId.systemDefault();
	}

	/**
	 * Executes the aggregation and returns OpenSearch-compatible JSON buckets.
	 */
	public JsonNode execute(IndexSearcher searcher, Query query) throws IOException {
		DateHistogramCollector collector = new DateHistogramCollector(field);
		searcher.search(query, collector);

		List<DateHistogramBucket> buckets = buildBuckets(collector);

		return buildJsonResult(buckets);
	}

	/**
	 * Executes the aggregation and returns typed bucket list.
	 */
	public List<DateHistogramBucket> executeBuckets(IndexSearcher searcher, Query query) throws IOException {
		DateHistogramCollector collector = new DateHistogramCollector(field);
		searcher.search(query, collector);
		return buildBuckets(collector);
	}

	// -----------------------------------------------------------------------

	private List<DateHistogramBucket> buildBuckets(DateHistogramCollector collector) {

		if (collector.minEpochMillis == null) {
			// No documents matched
			return new ArrayList<>();
		}

		long minMs = collector.minEpochMillis;
		long maxMs = collector.maxEpochMillis;

		ZonedDateTime start = truncate(toZdt(minMs));
		ZonedDateTime end = truncate(toZdt(maxMs));

		List<DateHistogramBucket> buckets = new ArrayList<>();
		int bucketCount = 0;

		ZonedDateTime cursor = start;
		while (!cursor.isAfter(end)) {
			if (++bucketCount > MAX_BUCKETS) {
				throw new IllegalArgumentException("Too many date histogram buckets: " + field);
			}

			long bucketKey = cursor.toInstant().toEpochMilli();
			ZonedDateTime next = advance(cursor);

			// count docs whose epoch falls in [bucketKey, next)
			long count = 0;
			for (long epochMs : collector.epochValues) {
				if (epochMs >= bucketKey && epochMs < next.toInstant().toEpochMilli()) {
					count++;
				}
			}

			buckets.add(new DateHistogramBucket(bucketKey, format(cursor), count));
			cursor = next;
		}

		return buckets;
	}

	private ZonedDateTime toZdt(long epochMs) {
		return Instant.ofEpochMilli(epochMs).atZone(zoneId);
	}

	private ZonedDateTime truncate(ZonedDateTime dt) {
		switch (interval) {
		case YEAR:
			return ZonedDateTime.of(dt.getYear(), 1, 1, 0, 0, 0, 0, zoneId);
		case MONTH:
			return ZonedDateTime.of(dt.getYear(), dt.getMonthValue(), 1, 0, 0, 0, 0, zoneId);
		case HOUR:
			return dt.withMinute(0).withSecond(0).withNano(0);
		default:
			throw new IllegalStateException("Unknown interval: " + interval);
		}
	}

	private ZonedDateTime advance(ZonedDateTime dt) {
		switch (interval) {
		case YEAR:
			return dt.plusYears(1);
		case MONTH:
			return dt.plusMonths(1);
		case HOUR:
			return dt.plusHours(1);
		default:
			throw new IllegalStateException("Unknown interval: " + interval);
		}
	}

	private String format(ZonedDateTime dt) {
		switch (interval) {
		case YEAR:
			return dt.format(FMT_YEAR);
		case MONTH:
			return dt.format(FMT_MONTH);
		case HOUR:
			return dt.format(FMT_HOUR);
		default:
			throw new IllegalStateException("Unknown interval: " + interval);
		}
	}

	private JsonNode buildJsonResult(List<DateHistogramBucket> buckets) {
		JsonNode result = JsonNode.object();
		JsonNode bucketsNode = JsonNode.array();

		for (DateHistogramBucket bucket : buckets) {
			JsonNode b = JsonNode.object();
			b.put("key", bucket.getKey());
			b.put("key_as_string", bucket.getKeyAsString());
			b.put("doc_count", bucket.getDocCount());
			bucketsNode.add(b);
		}

		result.put("buckets", bucketsNode);
		return result;
	}

	// -----------------------------------------------------------------------
	// Collector
	// -----------------------------------------------------------------------

	private static class DateHistogramCollector extends SimpleCollector {

		private final String field;

		Long minEpochMillis;
		Long maxEpochMillis;

		/** All epoch-millis values from matched docs (used for bucket counting). */
		final List<Long> epochValues = new ArrayList<>();

		private NumericDocValues numericDocValues;

		DateHistogramCollector(String field) {
			this.field = field;
		}

		@Override
		public void collect(int doc) throws IOException {
			if (numericDocValues == null) {
				return;
			}
			if (!numericDocValues.advanceExact(doc)) {
				return;
			}
			long epochMs = numericDocValues.longValue();

			epochValues.add(epochMs);

			minEpochMillis = (minEpochMillis == null) ? epochMs : Math.min(minEpochMillis, epochMs);
			maxEpochMillis = (maxEpochMillis == null) ? epochMs : Math.max(maxEpochMillis, epochMs);
		}

		@Override
		protected void doSetNextReader(LeafReaderContext context) throws IOException {
			numericDocValues = context.reader().getNumericDocValues(field);
		}

		@Override
		public ScoreMode scoreMode() {
			return ScoreMode.COMPLETE_NO_SCORES;
		}
	}
}
