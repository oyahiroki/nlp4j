package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

/**
 * 例1: キーワード検索
 *
 * <p>
 * addJson() でドキュメントを登録し、search(query, limit) で全文検索を行うサンプルです。
 * </p>
 *
 * <pre>
 * 実行結果イメージ:
 *   === キーワード検索: "Kyoto" ===
 *   hits: 2
 *   [0] id=1  score=0.xxxx  body=Kyoto is a historic city in Japan.
 *   [1] id=2  score=0.xxxx  body=Nintendo is headquartered in Kyoto, Japan.
 * </pre>
 */
public class Example01_KeywordSearch {

    public static void main(String[] args) throws Exception {

        try (LocalSearch search = new LocalSearch("en")) {

            // ドキュメント登録
            search.addJson("""
                    {"id":"1","body":"Kyoto is a historic city in Japan."}
                    """);
            search.addJson("""
                    {"id":"2","body":"Nintendo is headquartered in Kyoto, Japan."}
                    """);
            search.addJson("""
                    {"id":"3","body":"Tokyo is the capital city of Japan."}
                    """);
            search.addJson("""
                    {"id":"4","body":"Paris is the capital city of France."}
                    """);
            search.addJson("""
                    {"id":"5","body":"Sony is a Japanese multinational company based in Tokyo."}
                    """);
            search.commit();

            // --- キーワード検索 ---
            System.out.println("=== キーワード検索: \"Kyoto\" ===");
            SearchResult[] r1 = search.search("Kyoto", 10);
            printResults(r1);

            System.out.println("=== キーワード検索: \"Japan\" ===");
            SearchResult[] r2 = search.search("Japan", 10);
            printResults(r2);

            System.out.println("=== キーワード検索: \"capital\" ===");
            SearchResult[] r3 = search.search("capital", 10);
            printResults(r3);
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
