package nlp4j.analytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * LocalAnalytics による1回の aggregation 分析結果を表すクラス。
 *
 * <p>
 * 分析条件や文書数など、分析全体に関する情報と、
 * {@link AnalyticsAggregationBucket} の一覧を保持します。
 * </p>
 *
 * <p>
 * relativeRate の例:
 * </p>
 *
 * <pre>
 * AnalyticsAggregationResult result =
 * 		analytics.relativeRate(
 * 				"maker",
 * 				"ニッサン",
 * 				"word.noun",
 * 				100);
 *
 * result.getQueryField(); // "maker"
 * result.getQueryValue(); // "ニッサン"
 * result.getField();      // "word.noun"
 * result.getCount();      // ニッサンの文書数
 * result.getTotalCount(); // 全文書数
 *
 * for (AnalyticsAggregationBucket bucket
 * 		: result.getBuckets()) {
 *
 * 	System.out.println(
 * 			bucket.getKey()
 * 			+ " count=" + bucket.getCount()
 * 			+ " relativeRate="
 * 			+ bucket.getRelativeRate());
 * }
 * </pre>
 */
public class AnalyticsResult {

	/**
	 * 分析の基準となるフィールド。
	 *
	 * 例:
	 * maker
	 */
	private final String queryField;

	/**
	 * 分析の基準となる値。
	 *
	 * 例:
	 * ニッサン
	 */
	private final String queryValue;

	/**
	 * aggregation 対象フィールド。
	 *
	 * 例:
	 * word.noun
	 */
	private final String field;

	/**
	 * queryField=queryValue に該当する文書数。
	 */
	private final long count;

	/**
	 * 分析対象となった全文書数。
	 */
	private final long totalCount;

	/**
	 * aggregation buckets。
	 */
	private final List<AnalyticsAggregationBucket> buckets =
			new ArrayList<>();

	/**
	 * @param queryField 基準フィールド
	 * @param queryValue 基準値
	 * @param field      aggregation 対象フィールド
	 * @param count      基準条件に該当する文書数
	 * @param totalCount 全文書数
	 */
	public AnalyticsResult(
			String queryField,
			String queryValue,
			String field,
			long count,
			long totalCount) {

		if (queryField == null || queryField.isBlank()) {
			throw new IllegalArgumentException(
					"queryField must not be empty");
		}

		if (queryValue == null) {
			throw new IllegalArgumentException(
					"queryValue must not be null");
		}

		if (field == null || field.isBlank()) {
			throw new IllegalArgumentException(
					"field must not be empty");
		}

		if (count < 0) {
			throw new IllegalArgumentException(
					"count must be >= 0");
		}

		if (totalCount < 0) {
			throw new IllegalArgumentException(
					"totalCount must be >= 0");
		}

		this.queryField = queryField;
		this.queryValue = queryValue;
		this.field = field;
		this.count = count;
		this.totalCount = totalCount;
	}

	public String getQueryField() {
		return queryField;
	}

	public String getQueryValue() {
		return queryValue;
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

	public AnalyticsResult addBucket(
			AnalyticsAggregationBucket bucket) {

		if (bucket == null) {
			throw new IllegalArgumentException(
					"bucket must not be null");
		}

		/*
		 * Result と Bucket の aggregation field が
		 * 一致していることを保証します。
		 */
		if (!field.equals(bucket.getField())) {
			throw new IllegalArgumentException(
					"bucket field does not match result field: "
							+ bucket.getField());
		}

		buckets.add(bucket);

		return this;
	}

	@Override
	public String toString() {
		return "AnalyticsAggregationResult [queryField="
				+ queryField
				+ ", queryValue=" + queryValue
				+ ", field=" + field
				+ ", count=" + count
				+ ", totalCount=" + totalCount
				+ ", buckets=" + buckets
				+ "]";
	}
}