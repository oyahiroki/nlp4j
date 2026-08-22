package nlp4j.lucene;

import junit.framework.TestCase;

/**
 * LocalSearch を使った日付フィールド（DateFieldEnricher）の統合テスト。
 *
 * <p>
 * addJson() で _dt フィールドを含むドキュメントを登録すると、 DateFieldEnricher によって派生 INTEGER
 * フィールドが自動生成され、 Lucene クエリで絞り込みできることを確認する。
 * </p>
 */
public class LocalSearchDateFieldTestCase extends TestCase {

	// -----------------------------------------------------------------------
	// addJson: 派生フィールドで検索できること（offset datetime）
	// -----------------------------------------------------------------------

	public void testAddJson_dateField_derivedFieldsSearchable() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "イベントA",
					  "event_dt": "2026-08-19T20:00:00+09:00"
					}
					""");
			search.addJson("""
					{
					  "id": "2",
					  "body": "イベントB",
					  "event_dt": "2026-09-01T10:00:00+09:00"
					}
					""");
			search.commit();

			// event_year_i:2026 で2件ヒット
			SearchResult[] results = search.searchLucene("event_year_i:2026", 10);
			assertEquals(2, results.length);

			// event_month_i:8 で1件（8月）
			results = search.searchLucene("event_month_i:8", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);

			// event_month_i:9 で1件（9月）
			results = search.searchLucene("event_month_i:9", 10);
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	public void testAddJson_dateField_dayAndHour() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "朝のイベント",
					  "event_dt": "2026-08-19T08:00:00+09:00"
					}
					""");
			search.addJson("""
					{
					  "id": "2",
					  "body": "夜のイベント",
					  "event_dt": "2026-08-19T20:00:00+09:00"
					}
					""");
			search.commit();

			// event_day_i:19 で2件
			SearchResult[] results = search.searchLucene("event_day_i:19", 10);
			assertEquals(2, results.length);

			// event_hour_i:8 で1件（朝）
			results = search.searchLucene("event_hour_i:8", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);

			// event_hour_i:20 で1件（夜）
			results = search.searchLucene("event_hour_i:20", 10);
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	public void testAddJson_dateField_dowSearch() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			// 2026-08-19 = Wednesday (dow=3)
			search.addJson("""
					{
					  "id": "1",
					  "body": "水曜日のイベント",
					  "event_dt": "2026-08-19T10:00:00Z"
					}
					""");
			// 2026-08-22 = Saturday (dow=6)
			search.addJson("""
					{
					  "id": "2",
					  "body": "土曜日のイベント",
					  "event_dt": "2026-08-22T10:00:00Z"
					}
					""");
			search.commit();

			// 平日（月〜金）のイベント: dow [1 TO 5]
			SearchResult[] results = search.searchLucene("event_dow_i:[1 TO 5]", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);

			// 土曜日のイベント: dow=6
			results = search.searchLucene("event_dow_i:6", 10);
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// 日付のみ入力: 派生フィールドが生成され hour_i は生成されないこと
	// -----------------------------------------------------------------------

	public void testAddJson_dateOnly_noHourField() throws Exception {

		// 日付のみ (2026-08-21 = Friday) を UTC で解釈
		try (LocalSearch search = LocalSearch.builder("ja")
				.autoAnalyze(false)
				.timeZone("UTC")
				.build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "日付のみイベント",
					  "event_dt": "2026-08-21"
					}
					""");
			search.commit();

			// year/month/day/dow はヒットすること
			SearchResult[] results = search.searchLucene("event_year_i:2026", 10);
			assertEquals(1, results.length);

			results = search.searchLucene("event_month_i:8", 10);
			assertEquals(1, results.length);

			results = search.searchLucene("event_day_i:21", 10);
			assertEquals(1, results.length);

			// 2026-08-21 = Friday → dow=5
			results = search.searchLucene("event_dow_i:5", 10);
			assertEquals(1, results.length);

			// hour_i が生成されないため event_hour_i:0 はヒットしないこと
			results = search.searchLucene("event_hour_i:0", 10);
			assertEquals(0, results.length);
		}
	}

	public void testAddJson_dateOnly_Tokyo_derivedFields() throws Exception {

		// 日付のみ (2026-08-21) を Asia/Tokyo で解釈
		try (LocalSearch search = LocalSearch.builder("ja")
				.autoAnalyze(false)
				.timeZone("Asia/Tokyo")
				.build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "東京日付イベント",
					  "event_dt": "2026-08-21"
					}
					""");
			search.commit();

			// year/day/dow はヒット
			SearchResult[] results = search.searchLucene("event_year_i:2026", 10);
			assertEquals(1, results.length);

			results = search.searchLucene("event_day_i:21", 10);
			assertEquals(1, results.length);

			// hour_i が生成されないこと（0時 midnight として誤解されないよう）
			results = search.searchLucene("event_hour_i:0", 10);
			assertEquals(0, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// timeZone 設定: offset なし local datetime にタイムゾーンが適用されること
	// -----------------------------------------------------------------------

	public void testAddJson_localDatetime_TokyoZone() throws Exception {

		// タイムゾーンなし datetime (2026-08-21T14:30:00) を Asia/Tokyo で解釈
		try (LocalSearch search = LocalSearch.builder("ja")
				.autoAnalyze(false)
				.timeZone("Asia/Tokyo")
				.build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "東京のイベント",
					  "event_dt": "2026-08-21T14:30:00"
					}
					""");
			search.commit();

			// hour=14 (ローカル時刻) でヒット
			SearchResult[] results = search.searchLucene("event_hour_i:14", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);

			// year/month/day も正しいこと
			results = search.searchLucene("event_day_i:21", 10);
			assertEquals(1, results.length);
		}
	}

	public void testAddJson_localDatetime_UTCZone() throws Exception {

		// タイムゾーンなし datetime を UTC で解釈（同じ値でも Tokyo と hour が一致）
		try (LocalSearch search = LocalSearch.builder("ja")
				.autoAnalyze(false)
				.timeZone("UTC")
				.build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "UTCイベント",
					  "event_dt": "2026-08-21T09:00:00"
					}
					""");
			search.commit();

			// hour=9 (UTC ローカル) でヒット
			SearchResult[] results = search.searchLucene("event_hour_i:9", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// timeZone 設定: LocalSearch.builder でタイムゾーンが正しく設定できること
	// -----------------------------------------------------------------------

	public void testBuilder_timeZone_validZoneId() throws Exception {
		// 例外が発生しないこと
		try (LocalSearch search = LocalSearch.builder("ja")
				.timeZone("Asia/Tokyo")
				.build()) {
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
	// addJson: 不正なフォーマット（非ISO）は LocalSearchException になること
	// -----------------------------------------------------------------------

	public void testAddJson_dateField_501_InvalidFormat() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			// 2026/08/19 = non-ISO → will throw LocalSearchException
			search.addJson("""
					{
					  "id": "1",
					  "body": "水曜日のイベント",
					  "event_dt": "2026/08/19"
					}
					""");

			fail();

			search.commit();

		} catch (LocalSearchException e) {
			// expected
		}
	}

	// -----------------------------------------------------------------------
	// add(SearchRecord): 派生フィールドで検索できること
	// -----------------------------------------------------------------------

	public void testAddRecord_dateField_derivedFieldsSearchable() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			SearchRecord record = new SearchRecord("1", "テスト文書");
			record.addData("created_dt", "2025-03-15T09:30:00+09:00");

			search.add(record);
			search.commit();

			// created_year_i:2025
			SearchResult[] results = search.searchLucene("created_year_i:2025", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);

			// created_month_i:3
			results = search.searchLucene("created_month_i:3", 10);
			assertEquals(1, results.length);

			// created_day_i:15
			results = search.searchLucene("created_day_i:15", 10);
			assertEquals(1, results.length);

			// created_hour_i:9
			results = search.searchLucene("created_hour_i:9", 10);
			assertEquals(1, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// autoAnalyze=false でも Date 派生フィールドが生成されること
	// -----------------------------------------------------------------------

	public void testAutoAnalyzeFalse_dateEnrichmentStillWorks() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "body": "テスト",
					  "event_dt": "2026-08-19T20:00:00+09:00"
					}
					""");
			search.commit();

			// autoAnalyze=false でも event_year_i でヒットすること
			SearchResult[] results = search.searchLucene("event_year_i:2026", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// -----------------------------------------------------------------------
	// タイムゾーンオフセットのローカル時刻保持の確認
	// -----------------------------------------------------------------------

	public void testTimezoneOffset_localCalendarPreserved() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			// UTC では 2026-08-20T06:30:00Z となるが、ローカル (JST) では 8/19 T23:30
			search.addJson("""
					{
					  "id": "1",
					  "body": "深夜のイベント",
					  "event_dt": "2026-08-19T23:30:00+09:00"
					}
					""");
			search.commit();

			// ローカル時刻ベースの派生フィールド: day=19 でヒット
			SearchResult[] results = search.searchLucene("event_day_i:19", 10);
			assertEquals(1, results.length);

			// UTC ベースの day=20 ではヒットしないこと
			results = search.searchLucene("event_day_i:20", 10);
			assertEquals(0, results.length);
		}
	}

	// -----------------------------------------------------------------------
	// count: 派生フィールドで件数確認できること
	// -----------------------------------------------------------------------

	public void testCount_derivedMonthField() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			search.addJson("""
					{"id":"1","body":"A","event_dt":"2026-08-01T10:00:00Z"}
					""");
			search.addJson("""
					{"id":"2","body":"B","event_dt":"2026-08-15T10:00:00Z"}
					""");
			search.addJson("""
					{"id":"3","body":"C","event_dt":"2026-09-01T10:00:00Z"}
					""");
			search.commit();

			// event_month_i:8 で2件、event_month_i:9 で1件
			SearchResult[] aug = search.searchLucene("event_month_i:8", 10);
			assertEquals(2, aug.length);

			SearchResult[] sep = search.searchLucene("event_month_i:9", 10);
			assertEquals(1, sep.length);
		}
	}
}
