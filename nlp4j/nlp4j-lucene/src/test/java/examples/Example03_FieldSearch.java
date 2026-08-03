package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

/**
 * 例3: フィールド検索
 *
 * <p>
 * addJson() で category / country などの追加フィールドを持つドキュメントを登録し、
 * search(field, value, limit) でフィールド完全一致検索を行うサンプルです。
 * </p>
 *
 * <p>
 * フィールド検索は keyword フィールドへの完全一致（term クエリ）です。
 * 全文検索（match クエリ）とは異なり、アナライザーを経由しません。
 * </p>
 *
 * <pre>
 * 実行結果イメージ:
 *   === フィールド検索: category="city" ===
 *   hits: 3
 *   [0] id=1  score=1.0000  body=Kyoto is a historic city in Japan.
 *   ...
 *
 *   === フィールド検索: country="Japan" ===
 *   hits: 4
 *   ...
 * </pre>
 */
public class Example03_FieldSearch {

    public static void main(String[] args) throws Exception {

        try (LocalSearch search = new LocalSearch("en")) {

            // ドキュメント登録（id, body, 追加フィールド）
            search.addJson("""
                    {"id":"1","body":"Kyoto is a historic city in Japan.",
                     "category":"city","country":"Japan"}
                    """);
            search.addJson("""
                    {"id":"2","body":"Nintendo is headquartered in Kyoto, Japan.",
                     "category":"company","country":"Japan"}
                    """);
            search.addJson("""
                    {"id":"3","body":"Tokyo is the capital city of Japan.",
                     "category":"city","country":"Japan"}
                    """);
            search.addJson("""
                    {"id":"4","body":"Paris is the capital city of France.",
                     "category":"city","country":"France"}
                    """);
            search.addJson("""
                    {"id":"5","body":"Sony is a Japanese multinational company.",
                     "category":"company","country":"Japan"}
                    """);
            search.commit();

            // --- category フィールドで検索 ---
            System.out.println("=== フィールド検索: category=\"city\" ===");
            SearchResult[] r1 = search.search("category", "city", 10);
            printResults(r1);

            System.out.println("=== フィールド検索: category=\"company\" ===");
            SearchResult[] r2 = search.search("category", "company", 10);
            printResults(r2);

            // --- country フィールドで検索 ---
            System.out.println("=== フィールド検索: country=\"Japan\" ===");
            SearchResult[] r3 = search.search("country", "Japan", 10);
            printResults(r3);

            System.out.println("=== フィールド検索: country=\"France\" ===");
            SearchResult[] r4 = search.search("country", "France", 10);
            printResults(r4);

            // --- 存在しない値 ---
            System.out.println("=== フィールド検索: category=\"sports\" (該当なし) ===");
            SearchResult[] r5 = search.search("category", "sports", 10);
            printResults(r5);
        }
    }

    private static void printResults(SearchResult[] results) {
        System.out.println("  hits: " + results.length);
        for (int n = 0; n < results.length; n++) {
            System.out.printf("  [%d] id=%-3s score=%.4f  body=%s%n",
                    n, results[n].id, results[n].score, results[n].body);
        }
        System.out.println();
    }
}
