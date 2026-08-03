package examples;

import java.util.Map;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

/**
 * 例4: キーワード検索 ＋ フィールド検索の組み合わせ
 *
 * <p>
 * search(query, limit, filters) で全文検索と keyword フィールドによる絞り込みを
 * 同時に行うサンプルです。
 * </p>
 *
 * <p>
 * filters は AND 条件です。複数フィールドを指定すると、すべての条件に一致する
 * ドキュメントのみが返ります。
 * </p>
 *
 * <pre>
 * 実行結果イメージ:
 *   === キーワード+フィールド: "Kyoto" + category="company" ===
 *   hits: 1
 *   [0] id=2  score=0.xxxx  body=Nintendo is headquartered in Kyoto, Japan.
 *
 *   === キーワード+フィールド: "Japan" + category="city" + country="Japan" ===
 *   hits: 2
 *   ...
 * </pre>
 */
public class Example04_KeywordAndFieldSearch {

    public static void main(String[] args) throws Exception {

        try (LocalSearch search = new LocalSearch("en")) {

            // ドキュメント登録
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
                    {"id":"5","body":"Sony is a Japanese multinational company based in Tokyo.",
                     "category":"company","country":"Japan"}
                    """);
            search.commit();

            // --- 全文検索 + 単一フィールド絞り込み ---
            System.out.println("=== キーワード+フィールド: \"Kyoto\" + category=\"company\" ===");
            SearchResult[] r1 = search.search("Kyoto", 10,
                    Map.of("category", "company"));
            printResults(r1);

            // --- 全文検索 + 単一フィールド絞り込み (絞り込み結果が複数) ---
            System.out.println("=== キーワード+フィールド: \"Japan\" + category=\"city\" ===");
            SearchResult[] r2 = search.search("Japan", 10,
                    Map.of("category", "city"));
            printResults(r2);

            // --- 全文検索 + 複数フィールド絞り込み ---
            System.out.println("=== キーワード+フィールド: \"city\" + category=\"city\" + country=\"Japan\" ===");
            SearchResult[] r3 = search.search("city", 10,
                    Map.of("category", "city", "country", "Japan"));
            printResults(r3);

            // --- query なし（match_all）+ フィールド絞り込み ---
            System.out.println("=== フィールドのみ絞り込み: category=\"city\" + country=\"France\" ===");
            SearchResult[] r4 = search.search("", 10,
                    Map.of("category", "city", "country", "France"));
            printResults(r4);

            // --- フィルターに一致なし ---
            System.out.println("=== キーワード+フィールド: \"Tokyo\" + country=\"France\" (該当なし) ===");
            SearchResult[] r5 = search.search("Tokyo", 10,
                    Map.of("country", "France"));
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
