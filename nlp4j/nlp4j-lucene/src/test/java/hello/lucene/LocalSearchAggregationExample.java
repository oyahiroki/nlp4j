package hello.lucene;

import nlp4j.lucene.LocalSearch;

/**
 * LocalSearch を使った aggregation 検索のサンプルプログラム。
 *
 * addJson() で登録したドキュメントの keyword フィールドに対して
 * terms aggregation（値ごとのドキュメント件数集計）を行う使い方を示します。
 *
 * <pre>
 * 示すパターン:
 *   1. フィールド全件集計（query なし）
 *   2. 全文検索で絞り込んだ上での集計
 *   3. size パラメータによる上位 N 件制限
 *   4. 複数フィールドの集計
 * </pre>
 *
 * <p>戻り値 JSON 形式:</p>
 * <pre>
 * {
 *   "field": "category",
 *   "buckets": [
 *     {"key": "city",    "count": 3},
 *     {"key": "company", "count": 2}
 *   ]
 * }
 * </pre>
 */
public class LocalSearchAggregationExample {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = new LocalSearch("en")) {

			// --- ドキュメント登録 ---
			search.addJson("""
					{
					  "id": "1",
					  "body": "Kyoto is a historic city in Japan.",
					  "category": "city",
					  "country": "Japan"
					}
					""");
			search.addJson("""
					{
					  "id": "2",
					  "body": "Nintendo is a video game company headquartered in Kyoto.",
					  "category": "company",
					  "country": "Japan"
					}
					""");
			search.addJson("""
					{
					  "id": "3",
					  "body": "Tokyo is the capital city of Japan.",
					  "category": "city",
					  "country": "Japan"
					}
					""");
			search.addJson("""
					{
					  "id": "4",
					  "body": "Paris is the capital city of France.",
					  "category": "city",
					  "country": "France"
					}
					""");
			search.addJson("""
					{
					  "id": "5",
					  "body": "Sony is a Japanese multinational company.",
					  "category": "company",
					  "country": "Japan"
					}
					""");
			search.commit();

			// --- 1. category フィールドの全件集計 ---
			System.out.println("=== 1. category 全件集計 ===");
			String r1 = search.aggregateJson("""
					{"field": "category", "size": 10}
					""");
			System.out.println(r1);

			// --- 2. 全文検索 "Kyoto" で絞り込んだ上での category 集計 ---
			System.out.println("=== 2. query=\"Kyoto\" で絞り込んだ上での category 集計 ===");
			String r2 = search.aggregateJson("""
					{"field": "category", "query": "Kyoto", "size": 10}
					""");
			System.out.println(r2);

			// --- 3. size=1 で上位 1 バケットのみ取得 ---
			System.out.println("=== 3. category 集計（size=1、上位 1 件のみ） ===");
			String r3 = search.aggregateJson("""
					{"field": "category", "size": 1}
					""");
			System.out.println(r3);

			// --- 4. country フィールドの全件集計 ---
			System.out.println("=== 4. country 全件集計 ===");
			String r4 = search.aggregateJson("""
					{"field": "country", "size": 10}
					""");
			System.out.println(r4);
		}
	}

}
