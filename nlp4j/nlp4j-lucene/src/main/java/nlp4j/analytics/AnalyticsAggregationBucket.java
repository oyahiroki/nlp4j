/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.analytics;

import java.util.Objects;

/**
 * Analytics aggregation の1つの bucket を表すクラス。
 *
 * <p>
 * OpenSearch の aggregation bucket に相当する key / doc_count を基本とし、分析用の値として allCount
 * / relativeRate を保持します。
 * </p>
 *
 * <p>
 * 例:
 * </p>
 *
 * <pre>
 * AnalyticsAggregationBucket bucket = new AnalyticsAggregationBucket(new AnalyticsKeyword("word.noun", "ドア"), 10, 20,
 * 		2.5);
 *
 * bucket.getKey(); // "ドア"
 * bucket.getCount(); // 10
 * bucket.getAllCount(); // 20
 * bucket.getRelativeRate(); // 2.5
 * </pre>
 */
public class AnalyticsAggregationBucket {

	/**
	 * この bucket が表すキーワード。
	 */
	private final AnalyticsKeyword keyword;

	/**
	 * 分析対象文書群の中で、 このキーワードを含む文書数。
	 *
	 * OpenSearch の doc_count に相当します。
	 */
	private final long count;

	/**
	 * 全文書の中で、 このキーワードを含む文書数。
	 */
	private final long allCount;

	/**
	 * relativeRate。
	 */
	private final double relativeRate;

	/**
	 * @param keyword      キーワード
	 * @param count        分析対象文書群での文書数
	 * @param allCount     全文書での文書数
	 * @param relativeRate relativeRate
	 */
	public AnalyticsAggregationBucket(AnalyticsKeyword keyword, long count, long allCount, double relativeRate) {

		this.keyword = Objects.requireNonNull(keyword, "keyword must not be null");

		if (count < 0) {
			throw new IllegalArgumentException("count must be >= 0");
		}

		if (allCount < 0) {
			throw new IllegalArgumentException("allCount must be >= 0");
		}

		this.count = count;
		this.allCount = allCount;
		this.relativeRate = relativeRate;
	}

	/**
	 * キーワード情報を返します。
	 *
	 * @return AnalyticsKeyword
	 */
	public AnalyticsKeyword getKeyword() {
		return keyword;
	}

	/**
	 * aggregation の key を返します。
	 *
	 * <p>
	 * keyword.getLex() のショートカットです。
	 * </p>
	 *
	 * @return キーワードの正規形
	 */
	public String getKey() {
		return keyword.getLex();
	}

	/**
	 * aggregation field を返します。
	 *
	 * <p>
	 * keyword.getField() のショートカットです。
	 * </p>
	 *
	 * @return field
	 */
	public String getField() {
		return keyword.getField();
	}

	/**
	 * 分析対象文書群における文書数を返します。
	 *
	 * @return document count
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 全文書における、このキーワードの文書数を返します。
	 *
	 * @return document count in all documents
	 */
	public long getAllCount() {
		return allCount;
	}

	/**
	 * relativeRate を返します。
	 *
	 * @return relativeRate
	 */
	public double getRelativeRate() {
		return relativeRate;
	}

	@Override
	public String toString() {
		return "AnalyticsAggregationBucket [keyword=" + keyword + ", count=" + count + ", allCount=" + allCount
				+ ", relativeRate=" + relativeRate + "]";
	}
}