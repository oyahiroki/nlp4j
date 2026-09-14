package nlp4j.lucene;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import junit.framework.TestCase;

/**
 * created on 2026-09-14
 * 
 * @since 1.7.1.0
 */
public class LocalSearchGetFieldsWithValuesTestCase extends TestCase {

	/**
	 * 基本ケース: autoAnalyze=false で最小限のフィールドのみ登録されること
	 */
	public void testGetFieldsWithValues_basic() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "Hello",
					  "category_s": "city"
					}
					""");

			search.commit();

			List<String> fields = search.getFieldsWithValues();

			assertTrue(fields.contains("id"));
			assertTrue(fields.contains("body"));
			assertTrue(fields.contains("data"));
			assertTrue(fields.contains("category_s"));

			assertFalse(fields.contains("text"));
			assertFalse(fields.contains("text_en"));
			assertFalse(fields.contains("text_ja"));

			assertFalse(fields.contains("word.noun"));
			assertFalse(fields.contains("word.verb"));
		}
	}

	/**
	 * 空インデックスでは空リストを返すこと
	 */
	public void testGetFieldsWithValues_emptyIndex() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			List<String> fields = search.getFieldsWithValues();

			assertTrue(fields.isEmpty());
		}
	}

	/**
	 * autoAnalyze=true の場合、実際に NLP enrichment された word フィールドが現れること
	 */
	public void testGetFieldsWithValues_autoAnalyze() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).build()) {

			search.add("1", "京都の寺院を訪れる");
			search.commit();

			List<String> fields = search.getFieldsWithValues();

			assertTrue(fields.contains("id"));
			assertTrue(fields.contains("text_ja"));

			// 実際に解析結果があれば表示される
			assertTrue(fields.contains("word.noun"));
		}
	}

	/**
	 * DATE フィールド（_dt suffix）が実際に登録されていること。
	 * 派生フィールド（*_year_i 等）は生成されない。
	 */
	public void testGetFieldsWithValues_dateField() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).timeZone("Asia/Tokyo").build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "イベント",
					  "event_dt": "2026-08-21"
					}
					""");

			search.commit();

			List<String> fields = search.getFieldsWithValues();

			// DATE フィールド自体は登録されている
			assertTrue(fields.contains("event_dt"));

			// 派生フィールドは生成されない
			assertFalse(fields.contains("event_year_i"));
			assertFalse(fields.contains("event_month_i"));
			assertFalse(fields.contains("event_day_i"));
			assertFalse(fields.contains("event_dow_i"));
			assertFalse(fields.contains("event_hour_i"));
		}
	}

	/**
	 * "date" フィールドに ISO8601 datetime を登録した場合も DATE として登録されること。
	 */
	public void testGetFieldsWithValues_dateFieldWithDatetime() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "イベント",
					  "date": "2026-08-21T14:30:00+09:00"
					}
					""");

			search.commit();

			List<String> fields = search.getFieldsWithValues();

			// "date" フィールドが DATE として登録されている
			assertTrue(fields.contains("date"));

			// 派生フィールドは生成されない
			assertFalse(fields.contains("date_year_i"));
			assertFalse(fields.contains("date_hour_i"));
		}
	}

	/**
	 * vector フィールドが実データ登録後のみ現れること
	 */
	public void testGetFieldsWithValues_vector() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {

			// schema には vector が存在するが、
			// まだ実データには存在しない
			assertFalse(search.getFieldsWithValues().contains("vector"));

			search.add("1", new float[] { 1.0f, 0.0f });
			search.commit();

			assertTrue(search.getFieldsWithValues().contains("vector"));
		}
	}

	/**
	 * save/reload 後も同じフィールド一覧が返ること
	 */
	public void testGetFieldsWithValues_saveReload() throws Exception {

		Path dir = Files.createTempDirectory("localsearch-test-");

		List<String> beforeFields;

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "Hello",
					  "category_s": "city"
					}
					""");

			search.commit();

			beforeFields = search.getFieldsWithValues();

			search.saveIndexTo(dir);
		}

		try (LocalSearch search = LocalSearch.builder("en").loadIndexFrom(dir).build()) {

			List<String> afterFields = search.getFieldsWithValues();

			assertEquals(beforeFields, afterFields);

		} finally {
			// cleanup
			for (File f : dir.toFile().listFiles()) {
				f.delete();
			}
			dir.toFile().delete();
		}
	}
}
