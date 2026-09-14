package nlp4j.lucene;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.lucene9.DateHistogramBucket;
import nlp4j.lucene9.DateHistogramInterval;
import nlp4j.lucene9.FieldTypeDef;

/**
 * DATE フィールドの統合テスト。
 *
 * <p>
 * DATE 型のルール:
 * </p>
 * <ul>
 * <li>明示 schema で DATE → DATE</li>
 * <li>{@code *_dt} suffix → DATE</li>
 * <li>フィールド名が正確に {@code "date"} かつ値が ISO 8601 → DATE</li>
 * <li>それ以外 → KEYWORD</li>
 * <li>派生フィールド（*_year_i 等）は生成しない</li>
 * </ul>
 * 
 * @since 1.7.1.0
 */
public class LocalSearchDateFieldTestCase extends TestCase {

	// -----------------------------------------------------------------------
	// 1. created_dt = ISO8601 → DATE
	// -----------------------------------------------------------------------

	public void test01_createdDt_isDate() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"created_dt\":\"2026-09-14\"}");
			search.commit();

			// DATE range query must work
			SearchResult[] results = search.search("created_dt:[2026-09-14 TO 2026-09-14]", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// 2. date = ISO8601 date-only → DATE
	// -----------------------------------------------------------------------

	public void test02_dateField_isoDateOnly_isDate() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"date\":\"2026-09-14\"}");
			search.commit();

			SearchResult[] results = search.search("date:[2026-09-14 TO 2026-09-14]", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// 3. date = ISO8601 datetime (no offset) → DATE
	// -----------------------------------------------------------------------

	public void test03_dateField_isoDatetime_isDate() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"date\":\"2026-09-14T12:34:56\"}");
			search.commit();

			SearchResult[] results = search.search("date:[2026-09-14 TO 2026-09-15]", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// 4. date = ISO8601 datetime with Z → DATE
	// -----------------------------------------------------------------------

	public void test04_dateField_isoDatetimeZ_isDate() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"date\":\"2026-09-14T12:34:56Z\"}");
			search.commit();

			SearchResult[] results = search.search("date:[2026-09-14 TO 2026-09-15]", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// 5. date = ISO8601 datetime with offset → DATE
	// -----------------------------------------------------------------------

	public void test05_dateField_isoDatetimeOffset_isDate() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"date\":\"2026-09-14T12:34:56+09:00\"}");
			search.commit();

			SearchResult[] results = search.search("date:[2026-09-14 TO 2026-09-15]", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// 6. date = "abc" → KEYWORD (not DATE)
	// -----------------------------------------------------------------------

	public void test06_dateField_nonIso_isKeyword() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"date\":\"abc\"}");
			search.commit();

			// keyword search should work
			SearchResult[] results = search.search("date:abc", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// 7. created_date = ISO8601 → KEYWORD (not DATE: suffix not _dt)
	// -----------------------------------------------------------------------

	public void test07_createdDate_isoValue_isKeyword() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"created_date\":\"2026-09-14\"}");
			search.commit();

			// keyword query works
			SearchResult[] results = search.search("created_date:2026-09-14", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// 8. 明示 date=KEYWORD → ISO8601でもKEYWORD
	// -----------------------------------------------------------------------

	public void test08_explicitKeyword_overridesDateDetection() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("date", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"date\":\"2026-09-14\"}");
			search.commit();

			// keyword query must work (field is KEYWORD, not DATE)
			SearchResult[] results = search.search("date:2026-09-14", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// 9. date DATE 確定後に不正日付 → エラー
	// -----------------------------------------------------------------------

	public void test09_dateConfirmedAsDate_thenInvalidValue_throws() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			// first doc confirms "date" as DATE
			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"date\":\"2026-09-14\"}");
			search.commit();

			// second doc: invalid date value → must throw
			try {
				search.addJson("{\"id\":\"2\",\"body\":\"world\",\"date\":\"abc\"}");
				fail("Should have thrown LocalSearchException");
			} catch (LocalSearchException e) {
				// expected
			}
		}
	}

	// -----------------------------------------------------------------------
	// 10. ISO8601 の date 配列 → multi-valued DATE エラー
	// -----------------------------------------------------------------------

	public void test10_dateArray_iso8601_throws() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			try {
				search.addJson("""
						{
						  "id": "1",
						  "body": "hello",
						  "date": ["2026-09-14", "2026-09-15"]
						}
						""");
				fail("Should have thrown LocalSearchException for multi-valued DATE");
			} catch (LocalSearchException e) {
				// expected
			}
		}
	}

	// -----------------------------------------------------------------------
	// 11. dateHistogram("date", YEAR) が正常動作
	// -----------------------------------------------------------------------

	public void test11_dateHistogram_date_year() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"a\",\"date\":\"2024-03-01\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"b\",\"date\":\"2024-07-15\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"c\",\"date\":\"2026-09-14\"}");
			search.commit();

			List<DateHistogramBucket> buckets = search.dateHistogram("date", DateHistogramInterval.YEAR);

			assertFalse("buckets must not be empty", buckets.isEmpty());

			long count2024 = buckets.stream().filter(b -> "2024".equals(b.getKeyAsString()))
					.mapToLong(DateHistogramBucket::getDocCount).sum();
			assertEquals(2L, count2024);

			long count2026 = buckets.stream().filter(b -> "2026".equals(b.getKeyAsString()))
					.mapToLong(DateHistogramBucket::getDocCount).sum();
			assertEquals(1L, count2026);
		}
	}

	// -----------------------------------------------------------------------
	// 12. dateHistogram("date", MONTH) が正常動作
	// -----------------------------------------------------------------------

	public void test12_dateHistogram_date_month() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"a\",\"date\":\"2026-09-01\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"b\",\"date\":\"2026-09-15\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"c\",\"date\":\"2026-10-01\"}");
			search.commit();

			List<DateHistogramBucket> buckets = search.dateHistogram("date", DateHistogramInterval.MONTH);
			assertFalse("buckets must not be empty", buckets.isEmpty());

			long sepCount = buckets.stream().filter(b -> b.getKeyAsString().startsWith("2026-09"))
					.mapToLong(DateHistogramBucket::getDocCount).sum();
			assertEquals(2L, sepCount);
		}
	}

	public void test12_dateHistogram_date_month_2() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"a\",\"date\":\"2026-09-01T00:00:00\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"b\",\"date\":\"2026-09-15T00:00:00\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"c\",\"date\":\"2026-10-01T00:00:00\"}");
			search.commit();

			List<DateHistogramBucket> buckets = search.dateHistogram("date", DateHistogramInterval.MONTH);
			assertFalse("buckets must not be empty", buckets.isEmpty());

			long sepCount = buckets.stream().filter(b -> b.getKeyAsString().startsWith("2026-09"))
					.mapToLong(DateHistogramBucket::getDocCount).sum();
			assertEquals(2L, sepCount);
		}
	}

	public void test12_dateHistogram_date_month_3() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"a\",\"date\":\"2026-09-01T00:00:00.123\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"b\",\"date\":\"2026-09-15T00:00:00.123\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"c\",\"date\":\"2026-10-01T00:00:00.123\"}");
			search.commit();

			List<DateHistogramBucket> buckets = search.dateHistogram("date", DateHistogramInterval.MONTH);
			assertFalse("buckets must not be empty", buckets.isEmpty());

			long sepCount = buckets.stream().filter(b -> b.getKeyAsString().startsWith("2026-09"))
					.mapToLong(DateHistogramBucket::getDocCount).sum();
			assertEquals(2L, sepCount);
		}
	}

	// -----------------------------------------------------------------------
	// 13. DATE 範囲検索が正常動作
	// -----------------------------------------------------------------------

	public void test13_dateRangeSearch() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"a\",\"event_dt\":\"2026-01-01\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"b\",\"event_dt\":\"2026-06-15\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"c\",\"event_dt\":\"2026-12-31\"}");
			search.commit();

			SearchResult[] results = search.search("event_dt:[2026-01-01 TO 2026-06-30]", 10);
			assertEquals(2, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// 14. 保存→再読み込みしても date が DATE のまま
	// -----------------------------------------------------------------------

	public void test14_saveReload_dateStaysDate() throws Exception {

		Path dir = Files.createTempDirectory("localsearch-date-test-");

		try {
			try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

				search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"date\":\"2026-09-14\"}");
				search.commit();
				search.saveIndexTo(dir);
			}

			try (LocalSearch search = LocalSearch.builder("en").loadIndexFrom(dir).build()) {

				// DATE range query must still work after reload
				SearchResult[] results = search.search("date:[2026-09-14 TO 2026-09-14]", 10);
				assertEquals(1, results.length);
			}

		} finally {
			for (File f : dir.toFile().listFiles()) {
				f.delete();
			}
			dir.toFile().delete();
		}
	}

	// -----------------------------------------------------------------------
	// 15. *_year_i 等が生成されないこと
	// -----------------------------------------------------------------------

	public void test15_noDerivedFields() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("{\"id\":\"1\",\"body\":\"hello\",\"event_dt\":\"2026-09-14\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"world\",\"date\":\"2026-09-14\"}");
			search.commit();

			List<String> fields = search.getFieldsWithValues();

			assertFalse("event_year_i must not exist", fields.contains("event_year_i"));
			assertFalse("event_month_i must not exist", fields.contains("event_month_i"));
			assertFalse("event_day_i must not exist", fields.contains("event_day_i"));
			assertFalse("event_dow_i must not exist", fields.contains("event_dow_i"));
			assertFalse("event_hour_i must not exist", fields.contains("event_hour_i"));
		}
	}

	// -----------------------------------------------------------------------
	// timeZone builder method sanity checks
	// -----------------------------------------------------------------------

	public void testBuilder_timeZone_validZoneId() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").timeZone("Asia/Tokyo").build()) {
			assertNotNull(search);
		}
	}

	public void testBuilder_timeZone_invalidZoneId() {
		try {
			LocalSearch.builder("ja").timeZone("Invalid/Zone").build();
			fail("Should throw exception for invalid zone ID");
		} catch (java.time.zone.ZoneRulesException e) {
			// expected
		}
	}

	// -----------------------------------------------------------------------
	// 16. 明示 schema date=DATE → DATE として扱われる
	// -----------------------------------------------------------------------

	public void test16_explicitDateSchema_isDate() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("date", FieldTypeDef.date().stored(true).aggregatable(true)).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "hello",
					  "date": "2026-09-14"
					}
					""");

			search.commit();

			// schema 上 DATE であること
			assertEquals(FieldTypeDef.Kind.DATE, search.getSchema().get("date").kind());

			// DATE range query が動作すること
			SearchResult[] results = search.search("date:[2026-09-14 TO 2026-09-14]", 10);

			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// 17. date = ISO8601 datetime with milliseconds → DATE
	// -----------------------------------------------------------------------

	public void test17_dateField_isoDatetimeMillis_isDate() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "hello",
					  "date": "2026-09-14T12:34:56.123"
					}
					""");

			search.commit();

			// 自動判定で DATE になっていること
			assertEquals(FieldTypeDef.Kind.DATE, search.getSchema().get("date").kind());

			SearchResult[] results = search.search("date:[2026-09-14T00:00:00 TO 2026-09-15T00:00:00]", 10);

			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// 18. 最初に非ISO値が来た場合、date は KEYWORD として固定される
	// -----------------------------------------------------------------------

	public void test18_dateField_firstNonIso_thenIso_staysKeyword() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			// 最初の値は ISO8601 ではない
			search.addJson("""
					{
					  "id": "1",
					  "body": "first",
					  "date": "unknown"
					}
					""");

			// この時点で date は KEYWORD として schema に確定
			assertEquals(FieldTypeDef.Kind.KEYWORD, search.getSchema().get("date").kind());

			// 次に ISO8601 値を登録しても、既存 schema が優先される
			search.addJson("""
					{
					  "id": "2",
					  "body": "second",
					  "date": "2026-09-14"
					}
					""");

			search.commit();

			assertEquals(FieldTypeDef.Kind.KEYWORD, search.getSchema().get("date").kind());

			// 両方 KEYWORD 値として検索できる
			SearchResult[] r1 = search.search("date:unknown", 10);
			assertEquals(1, r1.length);
			assertEquals("1", r1[0].id);

			SearchResult[] r2 = search.search("date:2026-09-14", 10);
			assertEquals(1, r2.length);
			assertEquals("2", r2[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// 19. offset 付き日時は Instant として正しく扱われる
	// -----------------------------------------------------------------------

	public void test19_dateField_offset_crossesDateBoundary() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).timeZone("Asia/Tokyo").build()) {

			/*
			 * 2026-09-14T23:30:00-05:00
			 *
			 * UTC: 2026-09-15T04:30:00Z
			 *
			 * Asia/Tokyo: 2026-09-15T13:30:00+09:00
			 */
			search.addJson("""
					{
					  "id": "1",
					  "body": "timezone test",
					  "date": "2026-09-14T23:30:00-05:00"
					}
					""");

			search.commit();

			assertEquals(FieldTypeDef.Kind.DATE, search.getSchema().get("date").kind());

			// Tokyo 時間の 9/15 の範囲には入る
			SearchResult[] sep15 = search.search("date:[2026-09-15T00:00:00+09:00 TO 2026-09-15T23:59:59+09:00]", 10);

			assertEquals(1, sep15.length);
			assertEquals("1", sep15[0].id);

			// Tokyo 時間の 9/14 の範囲には入らない
			SearchResult[] sep14 = search.search("date:[2026-09-14T00:00:00+09:00 TO 2026-09-14T23:59:59+09:00]", 10);

			assertEquals(0, sep14.length);
		}
	}

	// -----------------------------------------------------------------------
	// 20. getFieldKind() public API tests
	// -----------------------------------------------------------------------

	public void test20_getFieldKind_date() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "test",
					  "date": "2026-09-14"
					}
					""");
			search.commit();

			assertEquals(FieldTypeDef.Kind.DATE, search.getFieldKind("date"));
		}
	}

	public void test21_getFieldKind_dtSuffix() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "test",
					  "created_dt": "2026-09-14"
					}
					""");
			search.commit();

			assertEquals(FieldTypeDef.Kind.DATE, search.getFieldKind("created_dt"));
		}
	}

	public void test22_getFieldKind_nonDate() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "test",
					  "created_date": "2026-09-14"
					}
					""");
			search.commit();

			assertEquals(FieldTypeDef.Kind.KEYWORD, search.getFieldKind("created_date"));
		}
	}

	public void test23_getFieldKind_unknownField() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			assertNull(search.getFieldKind("unknown_field"));
		}
	}

	public void test24_getFieldKind_invalidArguments() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			try {
				search.getFieldKind(null);
				fail("Expected IllegalArgumentException on null fieldName");
			} catch (IllegalArgumentException e) {
				// expected
			}

			try {
				search.getFieldKind("   ");
				fail("Expected IllegalArgumentException on blank fieldName");
			} catch (IllegalArgumentException e) {
				// expected
			}
		}
	}

}
