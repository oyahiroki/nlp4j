/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.analytics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import nlp4j.lucene.LocalSearch;
import nlp4j.lucene9.DateHistogramBucket;
import nlp4j.lucene9.DateHistogramInterval;

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
 * 	AnalyticsResult result = analytics.relativeRate("word.noun", "ニッサン", "word.verb", 1000);
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
	public AnalyticsResult relativeRate(String queryField, String queryValue, String aggregationField, int size) {

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

		AnalyticsQuery query = AnalyticsQuery.fieldValue(queryField, queryValue);

		if (countAll == 0) {
			return new AnalyticsResult(query, aggregationField, 0, 0);
		}

		/*
		 * queryField=queryValue に該当する文書数。
		 *
		 * field/value を構造化条件として渡し、Lucene Query 文字列への変換を避けます。
		 */
		Map<String, String> queryFilter = Map.of(queryField, queryValue);

		long countQuery = search.count(null, queryFilter);

		if (countQuery == 0) {
			return new AnalyticsResult(query, aggregationField, 0, countAll);
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
		Map<String, Long> aggregationQuery = search.aggregate(aggregationField, null, size, queryFilter);

		return calculateRelativeRates(query, aggregationField, countAll, countQuery, aggregationAll, aggregationQuery);
	}

	/**
	 * Lucene Query Parser syntax で指定した条件に対する relativeRate を計算します。
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
	 * 利用例:
	 * </p>
	 *
	 * <pre>
	 * AnalyticsResult result = analytics.relativeRateLucene("maker:Nissan", "word.noun", 1000);
	 * </pre>
	 *
	 * @param luceneQuery      Lucene Query Parser syntax のクエリ文字列
	 * @param aggregationField 分析対象フィールド
	 * @param size             aggregation の最大バケット数（candidateSize）
	 * @return relativeRate 分析結果
	 */
	public AnalyticsResult relativeRateLucene(String luceneQuery, String aggregationField, int size) {

		if (luceneQuery == null || luceneQuery.isBlank()) {
			throw new IllegalArgumentException("luceneQuery must not be empty");
		}

		validateField(aggregationField, "aggregationField");

		if (size < 1) {
			throw new IllegalArgumentException("size must be greater than 0");
		}

		/*
		 * 全文書数。
		 */
		long countAll = search.count();

		AnalyticsQuery query = AnalyticsQuery.lucene(luceneQuery);

		if (countAll == 0) {
			return new AnalyticsResult(query, aggregationField, 0, 0);
		}

		/*
		 * luceneQuery に該当する文書数。
		 */
		long countQuery = search.count(luceneQuery);

		if (countQuery == 0) {
			return new AnalyticsResult(query, aggregationField, 0, countAll);
		}

		/*
		 * 全文書における aggregation。
		 */
		Map<String, Long> aggregationAll = search.aggregate(aggregationField, size);

		/*
		 * luceneQuery に該当する文書のみを対象とした aggregation。
		 */
		Map<String, Long> aggregationQuery = search.aggregate(aggregationField, luceneQuery, size);

		return calculateRelativeRates(query, aggregationField, countAll, countQuery, aggregationAll, aggregationQuery);
	}

	/**
	 * queryField に存在する値のうち、doc_count 上位 {@code size} 件について relativeRate を計算します。
	 *
	 * <p>
	 * 例えば、
	 * </p>
	 *
	 * <pre>
	 * Map&lt;String, AnalyticsResult&gt; result = analytics.relativeRates("maker", "word.noun", 100);
	 *
	 * AnalyticsResult nissan = result.get("ニッサン");
	 * </pre>
	 *
	 * <p>
	 * 全体の aggregation は queryValue ごとに再計算せず、 一度だけ実行します。
	 * </p>
	 *
	 * <p>
	 * queryField は category / maker 等の比較的低カーディナリティなフィールドを想定します。
	 * 高カーディナリティなフィールド（user_id 等）を指定すると、queryValue の数だけ aggregation
	 * を実行するため非常に重くなります。
	 * </p>
	 *
	 * @param queryField       基準フィールド
	 * @param aggregationField 分析対象フィールド
	 * @param size             処理する queryField 値の最大種類数（candidateSize にも同じ値を適用）
	 * @return queryValue → AnalyticsResult
	 */
	public Map<String, AnalyticsResult> relativeRates(String queryField, String aggregationField, int size) {
		return relativeRates(queryField, aggregationField, size, size);
	}

	/**
	 * queryField に存在するすべての値について relativeRate を計算します。
	 *
	 * <p>
	 * {@code queryValueSize} は queryField の値を何種類まで処理するかを制御し、 {@code candidateSize}
	 * は aggregationField の候補バケット数を制御します。
	 * </p>
	 *
	 * <p>
	 * queryField は category / maker 等の比較的低カーディナリティなフィールドを想定します。
	 * </p>
	 *
	 * @param queryField       基準フィールド
	 * @param aggregationField 分析対象フィールド
	 * @param queryValueSize   処理する queryField 値の最大種類数
	 * @param candidateSize    aggregation の最大バケット数
	 * @return queryValue → AnalyticsResult
	 */
	public Map<String, AnalyticsResult> relativeRates(String queryField, String aggregationField, int queryValueSize,
			int candidateSize) {

		validateField(queryField, "queryField");

		validateField(aggregationField, "aggregationField");

		if (queryValueSize < 1) {
			throw new IllegalArgumentException("size must be greater than 0");
		}

		if (candidateSize < 1) {
			throw new IllegalArgumentException("candidateSize must be greater than 0");
		}

		long countAll = search.count();

		if (countAll == 0) {
			return new LinkedHashMap<>();
		}

		/*
		 * queryField に存在する各値と doc_count を取得します。
		 *
		 * 例:
		 *
		 * A -> 10 B -> 20
		 */
		Map<String, Long> queryValues = search.aggregate(queryField, queryValueSize);

		/*
		 * aggregationField の全文書での集計。
		 *
		 * queryValue ごとに再計算する必要はありません。
		 */
		Map<String, Long> aggregationAll = search.aggregate(aggregationField, candidateSize);

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

			/*
			 * field/value を構造化条件として渡し、Lucene Query 文字列への変換を避けます。
			 */
			Map<String, String> queryFilter = Map.of(queryField, queryValue);

			Map<String, Long> aggregationQuery = search.aggregate(aggregationField, null, candidateSize, queryFilter);

			AnalyticsQuery query = AnalyticsQuery.fieldValue(queryField, queryValue);

			AnalyticsResult result = calculateRelativeRates(query, aggregationField, countAll, countQuery,
					aggregationAll, aggregationQuery);

			results.put(queryValue, result);
		}

		return results;
	}

	/**
	 * Lucene Query で指定した条件に対する Date histogram relativeRate を計算します。
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
	 * 全文書と絞り込み後の両方で dateHistogram を取得し、両方のキー集合の和集合から 0件 bucket
	 * も含めてすべての時系列バケットを計算します。 結果は時系列昇順（keyAsString 昇順）で返します。
	 * </p>
	 *
	 * <p>
	 * 利用例:
	 * </p>
	 *
	 * <pre>
	 * AnalyticsResult result = analytics.relativeRateDateHistogram("text_ja:ニッサン", "date", DateHistogramInterval.YEAR);
	 * result.getInterval(); // "year"
	 * </pre>
	 *
	 * @param luceneQuery Lucene Query Parser syntax のクエリ文字列
	 * @param dateField   集計対象の DATE フィールド名
	 * @param interval    集計単位（YEAR / MONTH / HOUR）
	 * @return relativeRate 分析結果（時系列昇順）
	 */
	public AnalyticsResult relativeRateDateHistogram(String luceneQuery, String dateField,
			DateHistogramInterval interval) {

		if (luceneQuery == null || luceneQuery.isBlank()) {
			throw new IllegalArgumentException("luceneQuery must not be empty");
		}

		validateField(dateField, "dateField");

		if (interval == null) {
			throw new IllegalArgumentException("interval must not be null");
		}

		long countAll = search.count();

		AnalyticsQuery query = AnalyticsQuery.lucene(luceneQuery);

		if (countAll == 0) {
			return new AnalyticsResult(query, dateField, interval.value(), 0, 0);
		}

		long countQuery = search.count(luceneQuery);

		if (countQuery == 0) {
			return new AnalyticsResult(query, dateField, interval.value(), 0, countAll);
		}

		/*
		 * 全文書における dateHistogram。
		 */
		List<DateHistogramBucket> all = search.dateHistogram(dateField, interval);

		/*
		 * luceneQuery に該当する文書のみを対象とした dateHistogram。
		 */
		List<DateHistogramBucket> queryBuckets = search.dateHistogram(dateField, interval, luceneQuery);

		return calculateDateHistogramRelativeRates(query, dateField, interval.value(), countAll, countQuery, all,
				queryBuckets);
	}

	/**
	 * relativeRate の実際の計算処理（条件種別非依存）。
	 *
	 * @param query            分析条件（FIELD_VALUE または LUCENE）
	 * @param aggregationField aggregation 対象フィールド
	 * @param countAll         全文書数
	 * @param countQuery       分析条件に該当する文書数
	 * @param aggregationAll   全文書に対する aggregation
	 * @param aggregationQuery 分析条件に該当する文書の aggregation
	 * @return AnalyticsResult
	 */
	private AnalyticsResult calculateRelativeRates(AnalyticsQuery query, String aggregationField, long countAll,
			long countQuery, Map<String, Long> aggregationAll, Map<String, Long> aggregationQuery) {

		AnalyticsResult result = new AnalyticsResult(query, aggregationField, countQuery, countAll);

		List<AnalyticsAggregationBucket> buckets = new ArrayList<>();

		for (Map.Entry<String, Long> entry : aggregationQuery.entrySet()) {

			String key = entry.getKey();

			/*
			 * 分析条件に該当する文書群のうち、 このキーワードを含む文書数。
			 */
			long targetCount = entry.getValue();

			/*
			 * 全文書のうち、 このキーワードを含む文書数。
			 */
			Long allCount = aggregationAll.get(key);

			/*
			 * aggregation の size 制限によって、 aggregationAll に対象の key が含まれていない場合があります。
			 *
			 * その場合は count(field, value) によって 正確な文書数を取得します。 field/value を構造化条件として渡し、Lucene
			 * Query 文字列への変換を避けます。
			 */
			if (allCount == null) {
				allCount = search.count(null, Map.of(aggregationField, key));
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
		 * relativeRate 降順 → count 降順 → key 昇順 の順でソートします。 同じ relativeRate の bucket
		 * が複数ある場合の順序を安定化するためです。
		 */
		buckets.sort(Comparator.comparingDouble(AnalyticsAggregationBucket::getRelativeRate).reversed()
				.thenComparing(Comparator.comparingLong(AnalyticsAggregationBucket::getCount).reversed())
				.thenComparing(AnalyticsAggregationBucket::getKey));

		for (AnalyticsAggregationBucket bucket : buckets) {
			result.addBucket(bucket);
		}

		return result;
	}

	/**
	 * Date histogram relativeRate の計算処理。
	 *
	 * <p>
	 * 全文書と検索結果の両方のキー集合の和集合を使って bucket を構築します。 query 側に存在しない bucket は count=0,
	 * relativeRate=0 として含まれます。 結果は時系列昇順（keyAsString 昇順）で返します。
	 * </p>
	 *
	 * @param query        分析条件
	 * @param dateField    DATE フィールド名
	 * @param intervalStr  集計単位文字列（"year" 等）
	 * @param countAll     全文書数
	 * @param countQuery   分析条件に該当する文書数
	 * @param allBuckets   全文書に対する dateHistogram
	 * @param queryBuckets 分析条件に該当する文書の dateHistogram
	 * @return AnalyticsResult（時系列昇順）
	 */
	private AnalyticsResult calculateDateHistogramRelativeRates(AnalyticsQuery query, String dateField,
			String intervalStr, long countAll, long countQuery, List<DateHistogramBucket> allBuckets,
			List<DateHistogramBucket> queryBuckets) {

		AnalyticsResult result = new AnalyticsResult(query, dateField, intervalStr, countQuery, countAll);

		/*
		 * keyAsString → docCount のマップに変換。
		 */
		Map<String, Long> allMap = toKeyMap(allBuckets);
		Map<String, Long> queryMap = toKeyMap(queryBuckets);

		/*
		 * 両側のキー集合の和集合を時系列昇順で処理します。 query 側に存在しない bucket（count=0）も有効な bucket として含めます。
		 */
		Set<String> keys = new HashSet<>();
		keys.addAll(allMap.keySet());
		keys.addAll(queryMap.keySet());

		List<String> sortedKeys = new ArrayList<>(keys);
		sortedKeys.sort(Comparator.naturalOrder());

		for (String key : sortedKeys) {

			long targetCount = queryMap.getOrDefault(key, 0L);

			long allCount = allMap.getOrDefault(key, 0L);

			if (allCount == 0) {
				/*
				 * 全文書に該当バケットがない場合は relativeRate を計算できないためスキップします。
				 */
				continue;
			}

			double relativeRate = calculateRelativeRate(targetCount, countQuery, allCount, countAll);

			AnalyticsKeyword keyword = new AnalyticsKeyword(dateField, key);

			AnalyticsAggregationBucket bucket = new AnalyticsAggregationBucket(keyword, targetCount, allCount,
					relativeRate);

			result.addBucket(bucket);
		}

		return result;
	}

	/**
	 * List&lt;DateHistogramBucket&gt; を keyAsString → docCount の Map に変換します。
	 *
	 * @param buckets DateHistogramBucket リスト
	 * @return keyAsString → docCount
	 */
	private static Map<String, Long> toKeyMap(List<DateHistogramBucket> buckets) {
		Map<String, Long> map = new LinkedHashMap<>();
		for (DateHistogramBucket b : buckets) {
			map.put(b.getKeyAsString(), b.getDocCount());
		}
		return map;
	}

	/**
	 * 単一バケットの relativeRate を計算します（共通化）。
	 *
	 * <pre>
	 * relativeRate = (targetCount / countQuery) / (allCount / countAll)
	 * </pre>
	 *
	 * @param targetCount 分析条件に該当する文書群のうちこのバケットを含む文書数
	 * @param countQuery  分析条件に該当する文書数
	 * @param allCount    全文書のうちこのバケットを含む文書数
	 * @param countAll    全文書数
	 * @return relativeRate
	 */
	private static double calculateRelativeRate(long targetCount, long countQuery, long allCount, long countAll) {

		double targetRate = (double) targetCount / (double) countQuery;
		double allRate = (double) allCount / (double) countAll;

		if (allRate == 0.0) {
			return 0.0;
		}

		return targetRate / allRate;
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
