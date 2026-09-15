/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.analytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * LocalAnalytics による1回の aggregation 分析結果を表すクラス。
 *
 * <p>
 * 分析条件や文書数など、分析全体に関する情報と、 {@link AnalyticsAggregationBucket} の一覧を保持します。
 * </p>
 *
 * <p>
 * relativeRate の例:
 * </p>
 *
 * <pre>
 * AnalyticsResult result = analytics.relativeRate("maker", "ニッサン", "word.noun", 100);
 *
 * result.getQueryField(); // "maker"
 * result.getQueryValue(); // "ニッサン"
 * result.getField(); // "word.noun"
 * result.getCount(); // ニッサンの文書数
 * result.getTotalCount(); // 全文書数
 *
 * for (AnalyticsAggregationBucket bucket : result.getBuckets()) {
 *
 * 	System.out
 * 			.println(bucket.getKey() + " count=" + bucket.getCount() + " relativeRate=" + bucket.getRelativeRate());
 * }
 * </pre>
 */
public class AnalyticsResult {

	/**
	 * 分析条件。FIELD_VALUE または LUCENE。
	 */
	private final AnalyticsQuery query;

	/**
	 * aggregation 対象フィールド。
	 *
	 * 例: word.noun
	 */
	private final String field;

	/**
	 * Date histogram の集計単位。
	 *
	 * <p>
	 * Terms aggregation の場合は null。 Date histogram の場合は "year" / "month" / "hour" 等。
	 * </p>
	 */
	private final String interval;

	/**
	 * 分析条件に該当する文書数（countQuery）。
	 */
	private final long count;

	/**
	 * 分析対象となった全文書数（countAll）。
	 */
	private final long totalCount;

	/**
	 * aggregation buckets。
	 */
	private final List<AnalyticsAggregationBucket> buckets = new ArrayList<>();

	// -----------------------------------------------------------------------
	// Constructors
	// -----------------------------------------------------------------------

	/**
	 * AnalyticsQuery を使った基本コンストラクタ。
	 *
	 * @param query      分析条件
	 * @param field      aggregation 対象フィールド
	 * @param count      分析条件に該当する文書数
	 * @param totalCount 全文書数
	 */
	public AnalyticsResult(AnalyticsQuery query, String field, long count, long totalCount) {
		this(query, field, null, count, totalCount);
	}

	/**
	 * Date histogram 用コンストラクタ（interval 付き）。
	 *
	 * @param query      分析条件
	 * @param field      aggregation 対象フィールド（Date フィールド名）
	 * @param interval   集計単位（"year" / "month" / "hour" 等）
	 * @param count      分析条件に該当する文書数
	 * @param totalCount 全文書数
	 */
	public AnalyticsResult(AnalyticsQuery query, String field, String interval, long count, long totalCount) {

		if (query == null) {
			throw new IllegalArgumentException("query must not be null");
		}

		if (field == null || field.isBlank()) {
			throw new IllegalArgumentException("field must not be empty");
		}

		if (count < 0) {
			throw new IllegalArgumentException("count must be >= 0");
		}

		if (totalCount < 0) {
			throw new IllegalArgumentException("totalCount must be >= 0");
		}

		if (count > totalCount) {
			throw new IllegalArgumentException(
					"count must be <= totalCount: count=" + count + ", totalCount=" + totalCount);
		}

		this.query = query;
		this.field = field;
		this.interval = interval;
		this.count = count;
		this.totalCount = totalCount;
	}

	/**
	 * フィールド値条件を使った後方互換コンストラクタ。
	 *
	 * @param queryField 基準フィールド
	 * @param queryValue 基準値
	 * @param field      aggregation 対象フィールド
	 * @param count      基準条件に該当する文書数
	 * @param totalCount 全文書数
	 */
	public AnalyticsResult(String queryField, String queryValue, String field, long count, long totalCount) {

		this(AnalyticsQuery.fieldValue(queryField, queryValue), field, count, totalCount);
	}

	// -----------------------------------------------------------------------
	// Getters
	// -----------------------------------------------------------------------

	/**
	 * 分析条件を返します。
	 *
	 * @return AnalyticsQuery
	 */
	public AnalyticsQuery getQuery() {
		return query;
	}

	/**
	 * 分析条件が FIELD_VALUE の場合は基準フィールド名を返します。 LUCENE の場合は null を返します。
	 *
	 * @return フィールド名または null
	 */
	public String getQueryField() {
		return query.getKind() == AnalyticsQuery.Kind.FIELD_VALUE ? query.getField() : null;
	}

	/**
	 * 分析条件が FIELD_VALUE の場合は基準値を返します。 LUCENE の場合は null を返します。
	 *
	 * @return 基準値または null
	 */
	public String getQueryValue() {
		return query.getKind() == AnalyticsQuery.Kind.FIELD_VALUE ? query.getValue() : null;
	}

	/**
	 * 分析条件が LUCENE の場合は Lucene クエリ文字列を返します。 FIELD_VALUE の場合は null を返します。
	 *
	 * @return Lucene クエリ文字列または null
	 */
	public String getLuceneQuery() {
		return query.getKind() == AnalyticsQuery.Kind.LUCENE ? query.getLuceneQuery() : null;
	}

	/**
	 * aggregation 対象フィールドを返します。
	 *
	 * @return aggregation field
	 */
	public String getField() {
		return field;
	}

	/**
	 * Date histogram の集計単位を返します。
	 *
	 * <p>
	 * Terms aggregation の場合は null を返します。
	 * </p>
	 *
	 * @return interval（"year" / "month" / "hour" 等）または null
	 */
	public String getInterval() {
		return interval;
	}

	/**
	 * 基準条件に該当する文書数を返します。
	 *
	 * <p>
	 * relativeRate の場合は countQuery に相当します。
	 * </p>
	 *
	 * @return document count
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 全文書数を返します。
	 *
	 * <p>
	 * relativeRate の場合は countAll に相当します。
	 * </p>
	 *
	 * @return total document count
	 */
	public long getTotalCount() {
		return totalCount;
	}

	public List<AnalyticsAggregationBucket> getBuckets() {
		return Collections.unmodifiableList(buckets);
	}

	public AnalyticsResult addBucket(AnalyticsAggregationBucket bucket) {

		if (bucket == null) {
			throw new IllegalArgumentException("bucket must not be null");
		}

		/*
		 * Result と Bucket の aggregation field が 一致していることを保証します。
		 */
		if (!field.equals(bucket.getField())) {
			throw new IllegalArgumentException("bucket field does not match result field: " + bucket.getField());
		}

		buckets.add(bucket);

		return this;
	}

	@Override
	public String toString() {
		return "AnalyticsResult [" + "query=" + query + ", field=" + field
				+ (interval != null ? ", interval=" + interval : "") + ", count=" + count + ", totalCount=" + totalCount
				+ ", buckets=" + buckets + "]";
	}
}
