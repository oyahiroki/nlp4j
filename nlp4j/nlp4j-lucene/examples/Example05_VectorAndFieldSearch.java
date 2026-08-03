package examples;

import java.util.Map;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

/**
 * 例5: ベクトル検索 ＋ フィールド検索の組み合わせ
 *
 * <p>
 * add(id, vector, fields) でベクトルと追加フィールドを同一ドキュメントに登録し、
 * search(vector, limit, filters) でフィールドフィルター付きの KNN 検索を行うサンプルです。
 * </p>
 *
 * <p>
 * フィルターは Lucene の {@code KnnFloatVectorQuery} 第4引数として渡されるため、
 * 後処理での除外ではなく、フィルター条件に一致する文書の中から真の上位 k 件を返します。
 * </p>
 *
 * <pre>
 * ベクトルのイメージ（2次元）:
 *
 *   id=1 tech  East  ( 1.0,  0.0)
 *   id=2 tech  North ( 0.0,  1.0)
 *   id=3 travel East ( 0.9,  0.2)   ← クエリに近いが travel なのでフィルター対象外
 *   id=4 travel West (-1.0,  0.0)
 *   id=5 tech  NE    ( 0.7,  0.7)
 *   id=6 travel NE   ( 0.6,  0.8)
 *
 * クエリ (0.9, 0.1):
 *   フィルターなし → id=3, id=1, id=5, ... の順
 *   category=tech  → id=1, id=5, id=2, ... の順（travel を除外した近傍）
 * </pre>
 *
 * <pre>
 * 実行結果イメージ:
 *   === ベクトル検索（フィルターなし）: queryVector=[0.9, 0.1] ===
 *   hits: 6
 *   [0] id=3  score=0.xxxx  (travel だが最近傍)
 *   ...
 *
 *   === ベクトル検索+フィールド: queryVector=[0.9, 0.1] + category="tech" ===
 *   hits: 3
 *   [0] id=1  score=0.xxxx  (tech の中の最近傍)
 *   ...
 * </pre>
 */
public class Example05_VectorAndFieldSearch {

    public static void main(String[] args) throws Exception {

        // vectorDimension=2 を指定
        try (LocalSearch search = new LocalSearch("en", 2)) {

            // ドキュメント登録（id, vector, fields）
            search.add("1_tech_East",   new float[]{  1.0f,  0.0f }, Map.of("category", "tech",   "country", "Japan"));
            search.add("2_tech_North",  new float[]{  0.0f,  1.0f }, Map.of("category", "tech",   "country", "Japan"));
            search.add("3_travel_East", new float[]{  0.9f,  0.2f }, Map.of("category", "travel", "country", "Japan"));
            search.add("4_travel_West", new float[]{ -1.0f,  0.0f }, Map.of("category", "travel", "country", "France"));
            search.add("5_tech_NE",     new float[]{  0.7f,  0.7f }, Map.of("category", "tech",   "country", "USA"));
            search.add("6_travel_NE",   new float[]{  0.6f,  0.8f }, Map.of("category", "travel", "country", "Japan"));
            search.commit();

            float[] queryVector = new float[]{ 0.9f, 0.1f };

            // --- フィルターなし ---
            System.out.println("=== ベクトル検索（フィルターなし）: queryVector=[0.9, 0.1] ===");
            SearchResult[] r1 = search.search(queryVector, 6);
            printResults(r1);

            // --- 単一フィールドフィルター ---
            System.out.println("=== ベクトル+フィールド: queryVector=[0.9, 0.1] + category=\"tech\" ===");
            SearchResult[] r2 = search.search(queryVector, 6,
                    Map.of("category", "tech"));
            printResults(r2);

            System.out.println("=== ベクトル+フィールド: queryVector=[0.9, 0.1] + category=\"travel\" ===");
            SearchResult[] r3 = search.search(queryVector, 6,
                    Map.of("category", "travel"));
            printResults(r3);

            // --- 複数フィールドフィルター ---
            System.out.println("=== ベクトル+フィールド: queryVector=[0.9, 0.1] + category=\"tech\" + country=\"Japan\" ===");
            SearchResult[] r4 = search.search(queryVector, 6,
                    Map.of("category", "tech", "country", "Japan"));
            printResults(r4);

            // --- フィルターに一致なし ---
            System.out.println("=== ベクトル+フィールド: queryVector=[0.9, 0.1] + country=\"France\" (該当なし相当) ===");
            SearchResult[] r5 = search.search(queryVector, 6,
                    Map.of("category", "tech", "country", "France"));
            printResults(r5);
        }
    }

    private static void printResults(SearchResult[] results) {
        System.out.println("  hits: " + results.length);
        for (int n = 0; n < results.length; n++) {
            System.out.printf("  [%d] id=%-18s score=%.4f%n",
                    n, results[n].id, results[n].score);
        }
        System.out.println();
    }
}
