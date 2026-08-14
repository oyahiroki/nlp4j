package nlp4j.analytics;

import java.util.LinkedHashMap;
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
 * 	Map&lt;String, Double&gt; rates = analytics.relativeRate("word.noun", "ニッサン", "word.verb", 1000);
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
	 * 例:
	 * </p>
	 *
	 * <pre>
	 * // word.noun=ニッサン の文書について、
	 * // word.verb の relativeRate を計算
	 *
	 * Map&lt;String, Double&gt; result = analytics.relativeRate("word.noun", "ニッサン", "word.verb", 1000);
	 * </pre>
	 *
	 * @param queryField       基準となるフィールド
	 * @param queryValue       基準となる値
	 * @param aggregationField 分析対象フィールド
	 * @param size             aggregation の最大バケット数
	 * @return 値 → relativeRate のマップ（relativeRate 降順）
	 */
	public Map<String, Double> relativeRate(String queryField, String queryValue, String aggregationField, int size) {

		validateField(queryField, "queryField");
		validateField(aggregationField, "aggregationField");

		if (queryValue == null) {
			throw new IllegalArgumentException("queryValue must not be null");
		}

		if (size < 1) {
			throw new IllegalArgumentException("size must be greater than 0");
		}

		long countAll = search.count();

		if (countAll == 0) {
			return Map.of();
		}

		long countQuery = search.count(queryField, queryValue);

		if (countQuery == 0) {
			return Map.of();
		}

		/*
		 * 全文書における aggregation。
		 *
		 * relativeRate の分母として使用します。
		 */
		Map<String, Long> aggregationAll = search.aggregate(aggregationField, size);

		/*
		 * queryField=queryValue に該当する文書だけを対象とした aggregation。
		 */
		Map<String, Long> aggregationQuery = search.aggregate(aggregationField, queryField, queryValue, size);

		return calculateRelativeRates(aggregationField, countAll, countQuery, aggregationAll, aggregationQuery);
	}

	/**
	 * queryField に存在するすべての値について relativeRate を計算します。
	 *
	 * <p>
	 * 例:
	 * </p>
	 *
	 * <pre>
	 * Map&lt;String, Map&lt;String, Double&gt;&gt; result = analytics.relativeRates("word.noun", "word.verb", 1000);
	 *
	 * // result.get("ニッサン")
	 * // → ニッサンが出現する文書に特徴的な word.verb
	 * </pre>
	 *
	 * @param queryField       基準フィールド
	 * @param aggregationField 分析対象フィールド
	 * @param size             aggregation の最大バケット数
	 * @return queryValue → (aggregationValue → relativeRate)
	 */
	public Map<String, Map<String, Double>> relativeRates(String queryField, String aggregationField, int size) {

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
		 * queryField に存在する値を取得します。
		 */
		Map<String, Long> queryValues = search.aggregate(queryField, size);

		/*
		 * 全体の aggregation は queryValue ごとに 再計算する必要がないため、ここで一度だけ取得します。
		 */
		Map<String, Long> aggregationAll = search.aggregate(aggregationField, size);

		Map<String, Map<String, Double>> result = new LinkedHashMap<>();

		for (String queryValue : queryValues.keySet()) {

			long countQuery = search.count(queryField, queryValue);

			if (countQuery == 0) {
				continue;
			}

			Map<String, Long> aggregationQuery = search.aggregate(aggregationField, queryField, queryValue, size);

			Map<String, Double> rates = calculateRelativeRates(aggregationField, countAll, countQuery, aggregationAll,
					aggregationQuery);

			result.put(queryValue, rates);
		}

		return result;
	}

	/**
	 * relativeRate の実際の計算処理。
	 */
	private Map<String, Double> calculateRelativeRates(String aggregationField, long countAll, long countQuery,
			Map<String, Long> aggregationAll, Map<String, Long> aggregationQuery) {

		Map<String, Double> rates = new LinkedHashMap<>();

		for (Map.Entry<String, Long> entry : aggregationQuery.entrySet()) {

			String key = entry.getKey();
			long targetCount = entry.getValue();

			/*
			 * 通常はこちらから取得できます。
			 */
			Long allCount = aggregationAll.get(key);

			/*
			 * aggregation の size 制限によって aggregationAll に対象語が含まれていない可能性があります。
			 *
			 * その場合は count(field, value) で正確な文書数を取得します。
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

			rates.put(key, relativeRate);
		}

		/*
		 * relativeRate 降順で返します。
		 */
		Map<String, Double> sorted = new LinkedHashMap<>();

		rates.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.forEach(entry -> sorted.put(entry.getKey(), entry.getValue()));

		return sorted;
	}

	private void validateField(String field, String name) {

		if (field == null || field.isBlank()) {
			throw new IllegalArgumentException(name + " must not be empty");
		}
	}
}