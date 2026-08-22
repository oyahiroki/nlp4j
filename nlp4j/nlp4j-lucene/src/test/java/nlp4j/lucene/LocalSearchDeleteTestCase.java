package nlp4j.lucene;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import junit.framework.TestCase;

/**
 * delete(id) 機能の統合テスト。
 *
 * <p>
 * CRUD/ライフサイクル機能として {@link LocalSearchTestCase} から独立して管理します。
 * </p>
 */
public class LocalSearchDeleteTestCase extends TestCase {

	/**
	 * 基本削除: 3件登録 → 1件削除 → count=2 になることを確認する。
	 */
	public void testDelete001_basic() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京");
			search.add("2", "京都");
			search.add("3", "大阪");
			search.commit();

			assertEquals(3L, search.count());

			search.delete("2");
			search.commit();

			long count = search.count();
			System.out.println("testDelete001_basic count: " + count);
			assertEquals(2L, count);
		}
	}

	/**
	 * 削除後の全文検索: 削除した文書が検索結果に出ないことを確認する。
	 */
	public void testDelete002_bySearch() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京タワー");
			search.add("2", "京都タワー");
			search.add("3", "大阪タワー");
			search.commit();

			search.delete("2");
			search.commit();

			// 全件取得
			SearchResult[] results = search.search("タワー", 10);
			System.out.println("testDelete002_bySearch size: " + results.length);
			for (SearchResult r : results) {
				System.out.println("  id: " + r.id);
				assertFalse("削除した id=2 が検索結果に含まれてはいけない", "2".equals(r.id));
			}

			assertEquals(2, results.length);
		}
	}

	/**
	 * 存在しないID削除: 例外が発生しないことを確認する（idempotent）。
	 */
	public void testDelete003_notExists() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京");
			search.commit();

			// 存在しないIDを削除しても例外なし
			search.delete("999");
			search.commit();

			// 元の文書は残っている
			assertEquals(1L, search.count());
		}
	}

	/**
	 * 同じIDを2回削除しても正常に動作することを確認する（idempotent）。
	 */
	public void testDelete004_twice() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京");
			search.add("2", "京都");
			search.commit();

			search.delete("1");
			search.delete("1"); // 2回目
			search.commit();

			long count = search.count();
			System.out.println("testDelete004_twice count: " + count);
			assertEquals(1L, count);
		}
	}

	/**
	 * delete後に同じIDを再addできることを確認する（add/delete/re-add ライフサイクル）。
	 */
	public void testDelete005_addAgain() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "old");
			search.commit();

			search.delete("1");
			search.commit();

			search.add("1", "new");
			search.commit();

			// id=1 が "new" で1件だけ存在すること
			SearchResult[] results = search.search("id", "1", 10);
			System.out.println("testDelete005_addAgain size: " + results.length);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
			assertEquals("new", results[0].body);
		}
	}

	/**
	 * addJson() で登録した文書を delete(id) で削除できることを確認する。
	 */
	public void testDelete006_json() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("{\"id\":\"1\",\"body\":\"Kyoto is a historic city.\",\"category\":\"city\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"Nintendo is headquartered in Kyoto.\",\"category\":\"company\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"Paris is the capital city of France.\",\"category\":\"city\"}");
			search.commit();

			search.delete("2");
			search.commit();

			// category=company の文書が削除されていること
			SearchResult[] byCompany = search.search("category", "company", 10);
			System.out.println("testDelete006_json category=company size: " + byCompany.length);
			assertEquals(0, byCompany.length);

			// category=city の2件は残っていること
			SearchResult[] byCity = search.search("category", "city", 10);
			System.out.println("testDelete006_json category=city size: " + byCity.length);
			assertEquals(2, byCity.length);
		}
	}

	/**
	 * ベクトル文書を delete(id) で削除できることを確認する。
	 * KNN vector も Lucene Document の1フィールドなので通常削除と同じ経路で削除できる。
	 */
	public void testDelete007_vector() throws Exception {
		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.add("1", new float[] { 1.0f, 0.0f });
			search.add("2", new float[] { 0.0f, 1.0f });
			search.commit();

			search.delete("1");
			search.commit();

			// クエリ (1.0, 0.0) に最近傍は id=1 だったが削除されているので id=2 のみ返る
			SearchResult[] results = search.search(new float[] { 1.0f, 0.0f }, 10);
			System.out.println("testDelete007_vector size: " + results.length);
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	/**
	 * 削除後の aggregation: 削除した文書が集計から除外されることを確認する。
	 */
	public void testDelete008_aggregation() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("{\"id\":\"1\",\"body\":\"東京の観光\",\"category\":\"観光\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"Javaプログラミング\",\"category\":\"技術\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"京都の寺院\",\"category\":\"観光\"}");
			search.commit();

			// 削除前: 観光=2, 技術=1
			java.util.Map<String, Long> before = search.aggregate("category", 10);
			System.out.println("testDelete008_aggregation before: " + before);
			assertEquals(Long.valueOf(2L), before.get("観光"));
			assertEquals(Long.valueOf(1L), before.get("技術"));

			// id=1（観光）を削除
			search.delete("1");
			search.commit();

			// 削除後: 観光=1, 技術=1
			java.util.Map<String, Long> after = search.aggregate("category", 10);
			System.out.println("testDelete008_aggregation after: " + after);
			assertEquals(Long.valueOf(1L), after.get("観光"));
			assertEquals(Long.valueOf(1L), after.get("技術"));
		}
	}

	/**
	 * 永続化テスト: delete → commit → saveIndexTo → reopen 後も削除が反映されていることを確認する。
	 *
	 * <pre>
	 * add 3件 → commit → delete("2") → commit → saveIndexTo → close
	 * → reopen → count() == 2
	 * </pre>
	 */
	public void testDelete009_persistence() throws Exception {
		Path tempDir = Files.createTempDirectory("localsearch_delete_test_");
		try {
			// フェーズ1: 登録・削除・保存
			try (LocalSearch search = new LocalSearch("ja")) {
				search.add("1", "東京");
				search.add("2", "京都");
				search.add("3", "大阪");
				search.commit();

				search.delete("2");
				search.commit();

				search.saveIndexTo(tempDir);
			}

			// フェーズ2: 再オープンして削除が永続化されていることを確認
			try (LocalSearch search = new LocalSearch("ja", 0, tempDir)) {
				long count = search.count();
				System.out.println("testDelete009_persistence count after reopen: " + count);
				assertEquals(2L, count);

				// id=2 が存在しないことを確認
				SearchResult[] r2 = search.search("id", "2", 10);
				assertEquals(0, r2.length);

				// id=1, id=3 は残っていること
				SearchResult[] r1 = search.search("id", "1", 10);
				assertEquals(1, r1.length);

				SearchResult[] r3 = search.search("id", "3", 10);
				assertEquals(1, r3.length);
			}

		} finally {
			// 一時ディレクトリを削除
			try (Stream<Path> files = Files.walk(tempDir)) {
				files.sorted(Comparator.reverseOrder()).forEach(p -> {
					try {
						Files.deleteIfExists(p);
					} catch (Exception ignored) {
					}
				});
			}
		}
	}

	/**
	 * null ID: delete(null) は LocalSearchException をスローすることを確認する。
	 */
	public void testDelete010_nullId() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京");
			search.commit();

			try {
				search.delete(null);
				fail("delete(null) は LocalSearchException がスローされること");
			} catch (LocalSearchException e) {
				System.out.println("testDelete010_nullId exception: " + e.getMessage());
				assertTrue("メッセージに 'null' が含まれること", e.getMessage().contains("null"));
			}
		}
	}

}
