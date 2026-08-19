/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.analytics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import nlp4j.lucene.LocalSearch;

/**
 * LocalSearch 上のデータを利用して、 軽量な統計分析・テキスト分析を行うクラスです。
 *
 * <p>
 * LocalSearch が検索・count・aggregation などの基本操作を担当し、 LocalAnalytics
 * はそれらを組み合わせた分析処理を担当します。
 * </p>
 *
 * <p>
 * 例:
 * </p>
 *
 * <pre>
 * try (LocalSearch search = new LocalSearch("ja")) {
 *
 * 	search.add("1", "ニッサン ドアが破損した");
 * 	search.add("2", "ニッサン ドアが動かない");
 * 	search.add("3", "トヨタ ドアが外れた");
 * 	search.commit();
 *
 * 	LocalAnalytics analytics = new LocalAnalytics(search);
 *
 * 	AnalyticsAggregationResult result = analytics.relativeRate("word.noun", "ニッサン", "word.verb", 1000);
 *
 * 	System.out.println("count=" + result.getCount());
 * 	System.out.println("totalCount=" + result.getTotalCount());
 *
 * 	for (AnalyticsAggregationBucket bucket : result.getBuckets()) {
 *
 * 		System.out.println(bucket.getKey() + " count=" + bucket.getCount() + " allCount=" + bucket.getAllCount()
 * 				+ " relativeRate=" + bucket.getRelativeRate());
 * 	}
 * }
 * </pre>
 */
public class LocalAnalytics {

	private final LocalSearch search;

	/**
	 * LocalSearch を指定して LocalAnalytics を生成します。
	 *
	 * @param search 分析対象の LocalSearch
	 */
	public LocalAnalytics(LocalSearch search) {
		this.search = Objects.requireNonNull(search, "search must not be null");
	}

	/**
	 * 指定した条件に対する relativeRate を計算します。
	 *
	 * <p>
	 * relativeRate は次の式で計算します。
	 * </p>
	 *
	 * <pre>
	 * targetRate = targetCount / countQuery
	 *
	 * allRate = allCount / countAll
	 *
	 * relativeRate = targetRate / allRate
	 * </pre>
	 *
	 * <p>
	 * 例えば、 {@code queryField="maker"}, {@code queryValue="ニッサン"},
	 * {@code aggregationField="word.noun"} とした場合、ニッサンの文書に特徴的に出現する名詞を取得できます。
	 * </p>
	 *
	 * <p>
	 * 戻り値の {@link AnalyticsResult} は、 分析全体に関する以下の情報を保持します。
	 * </p>
	 *
	 * <pre>
	 * queryField
	 * queryValue
	 * field
	 * count
	 * totalCount
	 * buckets
	 * </pre>
	 *
	 * <p>
	 * 各 {@link AnalyticsAggregationBucket} は、 以下の情報を保持します。
	 * </p>
	 *
	 * <pre>
	 * keyword
	 * key
	 * count
	 * allCount
	 * relativeRate
	 * </pre>
	 *
	 * @param queryField       基準となるフィールド
	 * @param queryValue       基準となる値
	 * @param aggregationField 分析対象フィールド
	 * @param size             aggregation の最大バケット数
	 * @return relativeRate 分析結果
	 */
	public AnalyticsResult relativeRate(String queryField, String queryValue, String aggregationField,
			int size) {

		validateField(queryField, "queryField");

		validateField(aggregationField, "aggregationField");

		if (queryValue == null) {
			throw new IllegalArgumentException("queryValue must not be null");
		}

		if (size < 1) {
			throw new IllegalArgumentException("size must be greater than 0");
		}

		/*
		 * 全文書数。
		 */
		long countAll = search.count();

		if (countAll == 0) {

			return createResult(queryField, queryValue, aggregationField, 0, 0);
		}

		/*
		 * queryField=queryValue に該当する文書数。
		 */
		long countQuery = search.count(queryField, queryValue);

		if (countQuery == 0) {

			return createResult(queryField, queryValue, aggregationField, 0, countAll);
		}

		/*
		 * 全文書における aggregation。
		 *
		 * 各キーワードの allCount を取得するために使用します。
		 */
		Map<String, Long> aggregationAll = search.aggregate(aggregationField, size);

		/*
		 * queryField=queryValue に該当する文書のみを対象とした aggregation。
		 *
		 * bucket の count に相当します。
		 */
		Map<String, Long> aggregationQuery = search.aggregate(aggregationField, queryField, queryValue, size);

		return calculateRelativeRates(queryField, queryValue, aggregationField, countAll, countQuery, aggregationAll,
				aggregationQuery);
	}

	/**
	 * queryField に存在するすべての値について relativeRate を計算します。
	 *
	 * <p>
	 * 例えば、
	 * </p>
	 *
	 * <pre>
	 * Map&lt;String, AnalyticsAggregationResult&gt; result = analytics.relativeRates("maker", "word.noun", 100);
	 *
	 * AnalyticsAggregationResult nissan = result.get("ニッサン");
	 * </pre>
	 *
	 * <p>
	 * 全体の aggregation は queryValue ごとに再計算せず、 一度だけ実行します。
	 * </p>
	 *
	 * @param queryField       基準フィールド
	 * @param aggregationField 分析対象フィールド
	 * @param size             aggregation の最大バケット数
	 * @return queryValue → AnalyticsAggregationResult
	 */
	public Map<String, AnalyticsResult> relativeRates(String queryField, String aggregationField, int size) {

		validateField(queryField, "queryField");

		validateField(aggregationField, "aggregationField");

		if (size < 1) {
			throw new IllegalArgumentException("size must be greater than 0");
		}

		long countAll = search.count();

		if (countAll == 0) {
			return Map.of();
		}

		/*
		 * queryField に存在する各値と doc_count を取得します。
		 *
		 * 例:
		 *
		 * A -> 10 B -> 20
		 */
		Map<String, Long> queryValues = search.aggregate(queryField, size);

		/*
		 * aggregationField の全文書での集計。
		 *
		 * queryValue ごとに再計算する必要はありません。
		 */
		Map<String, Long> aggregationAll = search.aggregate(aggregationField, size);

		Map<String, AnalyticsResult> results = new LinkedHashMap<>();

		for (Map.Entry<String, Long> queryEntry : queryValues.entrySet()) {

			String queryValue = queryEntry.getKey();

			/*
			 * queryField の aggregation で取得した doc_count は、
			 *
			 * count(queryField, queryValue)
			 *
			 * と同じ意味なので、そのまま利用できます。
			 */
			long countQuery = queryEntry.getValue();

			if (countQuery == 0) {
				continue;
			}

			Map<String, Long> aggregationQuery = search.aggregate(aggregationField, queryField, queryValue, size);

			AnalyticsResult result = calculateRelativeRates(queryField, queryValue, aggregationField,
					countAll, countQuery, aggregationAll, aggregationQuery);

			results.put(queryValue, result);
		}

		return results;
	}

	/**
	 * relativeRate の実際の計算処理。
	 *
	 * @param queryField       基準フィールド
	 * @param queryValue       基準値
	 * @param aggregationField aggregation 対象フィールド
	 * @param countAll         全文書数
	 * @param countQuery       基準条件に該当する文書数
	 * @param aggregationAll   全文書に対する aggregation
	 * @param aggregationQuery 基準条件に該当する文書の aggregation
	 * @return AnalyticsAggregationResult
	 */
	private AnalyticsResult calculateRelativeRates(String queryField, String queryValue,
			String aggregationField, long countAll, long countQuery, Map<String, Long> aggregationAll,
			Map<String, Long> aggregationQuery) {

		AnalyticsResult result = createResult(queryField, queryValue, aggregationField, countQuery,
				countAll);

		List<AnalyticsAggregationBucket> buckets = new ArrayList<>();

		for (Map.Entry<String, Long> entry : aggregationQuery.entrySet()) {

			String key = entry.getKey();

			/*
			 * queryField=queryValue に該当する文書群のうち、 このキーワードを含む文書数。
			 */
			long targetCount = entry.getValue();

			/*
			 * 全文書のうち、 このキーワードを含む文書数。
			 */
			Long allCount = aggregationAll.get(key);

			/*
			 * aggregation の size 制限によって、 aggregationAll に対象の key が含まれていない場合があります。
			 *
			 * その場合は count(field, value) によって 正確な文書数を取得します。
			 */
			if (allCount == null) {

				allCount = search.count(aggregationField, key);
			}

			if (allCount == 0) {
				continue;
			}

			double targetRate = (double) targetCount / (double) countQuery;

			double allRate = (double) allCount / (double) countAll;

			if (allRate == 0.0) {
				continue;
			}

			double relativeRate = targetRate / allRate;

			/*
			 * aggregation の key を AnalyticsKeyword として表現します。
			 */
			AnalyticsKeyword keyword = new AnalyticsKeyword(aggregationField, key);

			AnalyticsAggregationBucket bucket = new AnalyticsAggregationBucket(keyword, targetCount, allCount,
					relativeRate);

			buckets.add(bucket);
		}

		/*
		 * relativeRate 降順。
		 */
		buckets.sort(Comparator.comparingDouble(AnalyticsAggregationBucket::getRelativeRate).reversed());

		for (AnalyticsAggregationBucket bucket : buckets) {

			result.addBucket(bucket);
		}

		return result;
	}

	/**
	 * AnalyticsAggregationResult を生成します。
	 *
	 * @param queryField       基準フィールド
	 * @param queryValue       基準値
	 * @param aggregationField aggregation 対象フィールド
	 * @param countQuery       基準条件に該当する文書数
	 * @param countAll         全文書数
	 * @return AnalyticsAggregationResult
	 */
	private AnalyticsResult createResult(String queryField, String queryValue, String aggregationField,
			long countQuery, long countAll) {

		return new AnalyticsResult(queryField, queryValue, aggregationField, countQuery, countAll);
	}

	/**
	 * フィールド名を検証します。
	 *
	 * @param field フィールド名
	 * @param name  引数名
	 */
	private void validateField(String field, String name) {

		if (field == null || field.isBlank()) {

			throw new IllegalArgumentException(name + " must not be empty");
		}
	}
}