package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

/**
 * 例2: ベクトル検索
 *
 * <p>
 * add(id, vector) でベクトルドキュメントを登録し、search(vector, limit) で
 * 近傍ベクトル検索（KNN）を行うサンプルです。
 * </p>
 *
 * <p>
 * ここでは 2 次元ベクトルを使い、4 方位（East / North / West / South）を表す文書を登録します。
 * クエリベクトルに最も近い順（コサイン類似度）で結果が返ります。
 * </p>
 *
 * <pre>
 * ベクトルのイメージ（2次元）:
 *
 *        North (0, 1)
 *             |
 *  West (-1,0)---- East (1, 0)
 *             |
 *        South (-1,-1)
 *
 * クエリ (0.9, 0.1) → East に最も近い
 * </pre>
 *
 * <pre>
 * 実行結果イメージ:
 *   === ベクトル検索: queryVector=[0.9, 0.1] ===
 *   hits: 4
 *   [0] id=1_East   score=0.xxxx
 *   [1] id=2_North  score=0.xxxx
 *   ...
 * </pre>
 */
public class Example02_VectorSearch {

    public static void main(String[] args) throws Exception {

        // vectorDimension=2 を指定してベクトル検索を有効化
        try (LocalSearch search = new LocalSearch("en", 2)) {

            // ドキュメント登録（id, vector）
            search.add("1_East",  new float[]{  1.0f,  0.0f });
            search.add("2_North", new float[]{  0.0f,  1.0f });
            search.add("3_West",  new float[]{ -1.0f,  0.0f });
            search.add("4_South", new float[]{ -1.0f, -1.0f });
            search.commit();

            // --- ベクトル検索 ---
            System.out.println("=== ベクトル検索: queryVector=[0.9, 0.1] (East 寄り) ===");
            SearchResult[] r1 = search.search(new float[]{ 0.9f, 0.1f }, 4);
            printResults(r1);

            System.out.println("=== ベクトル検索: queryVector=[0.1, 0.9] (North 寄り) ===");
            SearchResult[] r2 = search.search(new float[]{ 0.1f, 0.9f }, 4);
            printResults(r2);

            System.out.println("=== ベクトル検索: limit=2 (上位2件のみ) ===");
            SearchResult[] r3 = search.search(new float[]{ 0.9f, 0.1f }, 2);
            printResults(r3);
        }
    }

    private static void printResults(SearchResult[] results) {
        System.out.println("  hits: " + results.length);
        for (int n = 0; n < results.length; n++) {
            System.out.printf("  [%d] id=%-10s score=%.4f%n",
                    n, results[n].id, results[n].score);
        }
        System.out.println();
    }
}
