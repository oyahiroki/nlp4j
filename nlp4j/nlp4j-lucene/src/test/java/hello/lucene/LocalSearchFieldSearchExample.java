package hello.lucene;

import java.util.Map;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

/**
 * LocalSearch を使ったフィールド検索のサンプルプログラム。
 *
 * addJson() で登録したドキュメントの追加フィールド（category, country など）を
 * 使った絞り込み検索の使い方を示します。
 *
 * <pre>
 * 示すパターン:
 *   1. 全文検索のみ（body フィールド）
 *   2. フィールド指定の完全一致検索（category フィールド）
 *   3. 全文検索 + フィールド絞り込みの組み合わせ（search with filters）
 *   4. 複数フィールドによる絞り込み
 * </pre>
 */
public class LocalSearchFieldSearchExample {

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

			// --- 1. 全文検索のみ ---
			System.out.println("=== 1. 全文検索: \"Kyoto\" ===");
			SearchResult[] r1 = search.search("Kyoto", 10);
			printResults(r1);

			// --- 2. フィールド指定の完全一致検索 ---
			System.out.println("=== 2. フィールド検索: category=\"city\" ===");
			SearchResult[] r2 = search.search("category", "city", 10);
			printResults(r2);

			// --- 3. 全文検索 + フィールド絞り込み ---
			System.out.println("=== 3. 全文検索 + フィールド絞り込み: \"Kyoto\" + category=\"company\" ===");
			SearchResult[] r3 = search.search("Kyoto", 10, Map.of("category", "company"));
			printResults(r3);

			// --- 4. 複数フィールド絞り込み ---
			System.out.println("=== 4. 複数フィールド絞り込み: category=\"city\" + country=\"Japan\" ===");
			SearchResult[] r4 = search.search("", 10, Map.of("category", "city", "country", "Japan"));
			printResults(r4);
		}
	}

	private static void printResults(SearchResult[] results) {
		System.out.println("  hits: " + results.length);
		for (int n = 0; n < results.length; n++) {
			System.out.println("  [" + n + "] id=" + results[n].id
					+ "  score=" + String.format("%.4f", results[n].score)
					+ "  body=" + results[n].body);
		}
		System.out.println();
	}

}
