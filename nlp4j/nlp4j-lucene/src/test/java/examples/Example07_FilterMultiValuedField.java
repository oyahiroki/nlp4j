package examples;

import java.util.Map;

import nlp4j.json.JsonNode;
import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

/**
 * 例7: MultiValued フィールドをフィルター条件として絞り込み検索
 *
 * <p>
 * JSON 配列で登録した MultiValued keyword フィールド（tags）の値を
 * フィルター条件に指定して検索を行うサンプルです。
 * </p>
 *
 * <p>
 * MultiValued フィールドの各要素は {@code StringField} として個別にインデックスされるため、
 * 通常の単一値フィールドと同じく {@code search(field, value, limit)} や
 * {@code search(query, limit, filters)} でフィルター指定できます。
 * </p>
 *
 * <p>
 * 1 つのドキュメントが複数の tags 値を持つため、異なる値でフィルターしても
 * 同一ドキュメントがヒットし得る点が単一値フィールドとの違いです。
 * </p>
 *
 * <pre>
 * ドキュメントのイメージ:
 *   id=1  body="Kyoto is a historic city."       tags=["city","tourism","Japan"]
 *   id=2  body="Nintendo is headquartered in Kyoto."  tags=["company","Japan"]
 *   id=3  body="Tokyo is the capital city of Japan."  tags=["city","capital","Japan"]
 *   id=4  body="Paris is a beautiful city in France." tags=["city","tourism","France"]
 *   id=5  body="Sony is a Japanese company in Tokyo." tags=["company","Japan"]
 *
 * 期待結果:
 *   tags="Japan"   → id=1,2,3,5  (4件: Franceは含まれない)
 *   tags="city"    → id=1,3,4    (3件)
 *   tags="tourism" → id=1,4      (2件)
 *   tags="company" → id=2,5      (2件)
 *   tags="capital" → id=3        (1件)
 *   tags="France"  → id=4        (1件)
 *
 * query="Kyoto" + tags="Japan":
 *   → id=1,2  (Kyotoを含み かつ tags に Japan を持つドキュメント)
 *
 * tags="Japan" + tags="city" (AND条件, 2フィールドフィルター不可のため searchJson で実装):
 *   → id=1,3  (tags に Japan も city も持つドキュメント)
 * </pre>
 */
public class Example07_FilterMultiValuedField {

    public static void main(String[] args) throws Exception {

        try (LocalSearch search = new LocalSearch("en")) {

            // ドキュメント登録（tags を JSON 配列で指定 → MultiValued keyword field）
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

            // --- 1. MultiValued フィールド単体でフィルター検索 ---
            System.out.println("=== 1. フィールド検索: tags=\"Japan\" ===");
            SearchResult[] r1 = search.search("tags", "Japan", 10);
            printResults(r1);

            System.out.println("=== 2. フィールド検索: tags=\"city\" ===");
            SearchResult[] r2 = search.search("tags", "city", 10);
            printResults(r2);

            System.out.println("=== 3. フィールド検索: tags=\"tourism\" ===");
            SearchResult[] r3 = search.search("tags", "tourism", 10);
            printResults(r3);

            System.out.println("=== 4. フィールド検索: tags=\"capital\" ===");
            SearchResult[] r4 = search.search("tags", "capital", 10);
            printResults(r4);

            // --- 2. 全文検索 ＋ MultiValued フィールドフィルター ---
            System.out.println("=== 5. キーワード+フィールド: \"Kyoto\" + tags=\"Japan\" ===");
            SearchResult[] r5 = search.search("Kyoto", 10,
                    Map.of("tags", "Japan"));
            printResults(r5);

            System.out.println("=== 6. キーワード+フィールド: \"Japan\" + tags=\"city\" ===");
            SearchResult[] r6 = search.search("Japan", 10,
                    Map.of("tags", "city"));
            printResults(r6);

            System.out.println("=== 7. キーワード+フィールド: \"Tokyo\" + tags=\"company\" ===");
            SearchResult[] r7 = search.search("Tokyo", 10,
                    Map.of("tags", "company"));
            printResults(r7);

            // --- 3. MultiValued フィールドの値を AND で絞り込む（searchResponseJson で bool クエリを直接指定）---
            //        1 つのフィールドに対して複数の term 条件を AND で指定するには
            //        bool/filter に複数の term クエリを並べる必要があります。
            System.out.println("=== 8. tags に \"Japan\" かつ \"city\" を両方持つドキュメント ===");
            String r8 = search.searchResponseJson("""
                    {
                      "size": 10,
                      "query": {
                        "bool": {
                          "filter": [
                            {"term": {"tags": "Japan"}},
                            {"term": {"tags": "city"}}
                          ]
                        }
                      }
                    }
                    """);
            printHitsFromResponse(r8);

            System.out.println("=== 9. tags に \"Japan\" かつ \"tourism\" を両方持つドキュメント ===");
            String r9 = search.searchResponseJson("""
                    {
                      "size": 10,
                      "query": {
                        "bool": {
                          "filter": [
                            {"term": {"tags": "Japan"}},
                            {"term": {"tags": "tourism"}}
                          ]
                        }
                      }
                    }
                    """);
            printHitsFromResponse(r9);

            // --- 4. 該当なしのケース ---
            System.out.println("=== 10. フィールド検索: tags=\"sports\" (該当なし) ===");
            SearchResult[] r10 = search.search("tags", "sports", 10);
            printResults(r10);
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

    /**
     * searchResponseJson() の JSON レスポンスから hits を JsonNode で解析して表示します。
     * レスポンス構造: hits.total.value, hits.hits[n]._source.id
     */
    private static void printHitsFromResponse(String responseJson) {
        JsonNode response = JsonNode.parse(responseJson);

        int total = response.get("hits").get("total").get("value").asInt(0);
        System.out.println("  hits: " + total);

        JsonNode hitsArray = response.get("hits").get("hits");
        for (int n = 0; n < hitsArray.size(); n++) {
            JsonNode source = hitsArray.get(n).get("_source");
            String id = source.get("id").asString("-");
            System.out.printf("  [%d] id=%s%n", n, id);
        }
        System.out.println();
    }
}
