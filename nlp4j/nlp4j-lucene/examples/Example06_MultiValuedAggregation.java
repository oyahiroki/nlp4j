package examples;

import nlp4j.lucene.LocalSearch;

/**
 * 例6: MultiValued フィールドの Aggregation（terms 集計）
 *
 * <p>
 * JSON 配列で登録したフィールド（MultiValued keyword field）に対して
 * {@code aggregateJson()} で terms aggregation を行うサンプルです。
 * </p>
 *
 * <p>
 * {@code addJson()} に渡す JSON の値が配列の場合、各要素が個別の keyword 値として
 * {@code SortedSetDocValuesField} でインデックスされます。
 * これにより 1 つのドキュメントが複数の集計バケットにカウントされます。
 * </p>
 *
 * <pre>
 * ドキュメントのイメージ:
 *   id=1  body="Kyoto is a historic city."   tags=["city","tourism","Japan"]
 *   id=2  body="Nintendo is in Kyoto."        tags=["company","Japan"]
 *   id=3  body="Tokyo is the capital city."   tags=["city","capital","Japan"]
 *   id=4  body="Paris is a beautiful city."   tags=["city","tourism","France"]
 *   id=5  body="Sony is based in Tokyo."      tags=["company","Japan"]
 *
 * tags フィールドの全件集計（期待結果）:
 *   Japan   = 4
 *   city    = 3
 *   company = 2
 *   tourism = 2
 *   capital = 1
 *   France  = 1
 * </pre>
 *
 * <pre>
 * 実行結果イメージ:
 *   === 1. tags 全件集計 ===
 *   {"aggregations":{"tags":{"buckets":[{"key":"Japan","doc_count":4},{"key":"city","doc_count":3},...]}}}
 *
 *   === 2. query="Kyoto" で絞り込んだ上での tags 集計 ===
 *   {"aggregations":{"tags":{"buckets":[{"key":"Japan","doc_count":2},{"key":"city","doc_count":1},...]}}}
 *
 *   === 3. size=3 で上位 3 バケットのみ取得 ===
 *   {"aggregations":{"tags":{"buckets":[{"key":"Japan","doc_count":4},{"key":"city","doc_count":3},{"key":"company","doc_count":2}]}}}
 * </pre>
 */
public class Example06_MultiValuedAggregation {

    public static void main(String[] args) throws Exception {

        try (LocalSearch search = new LocalSearch("en")) {

            // ドキュメント登録（tags フィールドを JSON 配列で指定 → MultiValued keyword field）
            search.addJson("""
                    {
                      "id": "1",
                      "body": "Kyoto is a historic city.",
                      "tags": ["city", "tourism", "Japan"]
                    }
                    """);
            search.addJson("""
                    {
                      "id": "2",
                      "body": "Nintendo is headquartered in Kyoto.",
                      "tags": ["company", "Japan"]
                    }
                    """);
            search.addJson("""
                    {
                      "id": "3",
                      "body": "Tokyo is the capital city of Japan.",
                      "tags": ["city", "capital", "Japan"]
                    }
                    """);
            search.addJson("""
                    {
                      "id": "4",
                      "body": "Paris is a beautiful city in France.",
                      "tags": ["city", "tourism", "France"]
                    }
                    """);
            search.addJson("""
                    {
                      "id": "5",
                      "body": "Sony is a Japanese company based in Tokyo.",
                      "tags": ["company", "Japan"]
                    }
                    """);
            search.commit();

            // --- 1. tags フィールドの全件集計 ---
            System.out.println("=== 1. tags 全件集計 ===");
            String r1 = search.aggregateJson("""
                    {"name": "tags", "field": "tags", "size": 10}
                    """);
            System.out.println(r1);
            System.out.println();

            // --- 2. 全文検索 "Kyoto" で絞り込んだ上での tags 集計 ---
            System.out.println("=== 2. query=\"Kyoto\" で絞り込んだ上での tags 集計 ===");
            String r2 = search.aggregateJson("""
                    {"name": "tags", "field": "tags", "query": "Kyoto", "size": 10}
                    """);
            System.out.println(r2);
            System.out.println();

            // --- 3. size=3 で上位 3 バケットのみ取得 ---
            System.out.println("=== 3. tags 集計（size=3、上位 3 件のみ）===");
            String r3 = search.aggregateJson("""
                    {"name": "tags", "field": "tags", "size": 3}
                    """);
            System.out.println(r3);
            System.out.println();
        }
    }
}
