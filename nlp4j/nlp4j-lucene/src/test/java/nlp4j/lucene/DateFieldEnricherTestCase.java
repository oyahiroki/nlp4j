package nlp4j.lucene;

import java.time.ZoneId;

import junit.framework.TestCase;
import nlp4j.lucene9.SearchSchema;

/**
 * JUnit3 test case for {@link DateFieldEnricher}.
 */
public class DateFieldEnricherTestCase extends TestCase {

	// -----------------------------------------------------------------------
	// 基本動作: offset datetime → 派生フィールドが生成されること
	// -----------------------------------------------------------------------

	public void testEnrich_basic_utcZ() {
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("event_dt", "2026-08-19T10:30:00Z");

		DateFieldEnricher enricher = new DateFieldEnricher(null);
		enricher.enrich(record);

		assertEquals("2026", firstValue(record, "event_year_i"));
		assertEquals("8",    firstValue(record, "event_month_i"));
		assertEquals("19",   firstValue(record, "event_day_i"));
		assertEquals("10",   firstValue(record, "event_hour_i")); // UTC Z: hour=10
		// 2026-08-19 はWednesday → ISO dow=3
		assertEquals("3",    firstValue(record, "event_dow_i"));
	}

	public void testEnrich_positiveOffset_localCalendar() {
		// +09:00 のローカル時刻がそのまま派生されること（UTC に変換されないこと）
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("event_dt", "2026-08-19T20:15:30+09:00");

		DateFieldEnricher enricher = new DateFieldEnricher(null);
		enricher.enrich(record);

		assertEquals("2026", firstValue(record, "event_year_i"));
		assertEquals("8",    firstValue(record, "event_month_i"));
		assertEquals("19",   firstValue(record, "event_day_i"));
		assertEquals("20",   firstValue(record, "event_hour_i"));
		assertEquals("3",    firstValue(record, "event_dow_i")); // Wednesday
	}

	public void testEnrich_negativeOffset_localCalendar() {
		// -07:00 の場合、UTC では翌日だが、ローカルでは 8/19 のまま
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("event_dt", "2026-08-19T23:30:00-07:00");

		DateFieldEnricher enricher = new DateFieldEnricher(null);
		enricher.enrich(record);

		assertEquals("19",   firstValue(record, "event_day_i"));
		assertEquals("23",   firstValue(record, "event_hour_i"));
	}

	public void testEnrich_differentFieldName() {
		// created_dt → created_year_i 等
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("created_dt", "2024-01-01T00:00:00Z");

		DateFieldEnricher enricher = new DateFieldEnricher(null);
		enricher.enrich(record);

		assertEquals("2024", firstValue(record, "created_year_i"));
		assertEquals("1",    firstValue(record, "created_month_i"));
		assertEquals("1",    firstValue(record, "created_day_i"));
		assertEquals("0",    firstValue(record, "created_hour_i"));
		// 2024-01-01 はMonday → ISO dow=1
		assertEquals("1",    firstValue(record, "created_dow_i"));
	}

	// -----------------------------------------------------------------------
	// DATE精度: 日付のみ入力の場合 hour_i が生成されないこと
	// -----------------------------------------------------------------------

	public void testEnrich_dateOnly_noHourField() {
		// 日付のみ入力 → precision = DATE → hour_i は生成しない
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("event_dt", "2026-08-21");

		DateFieldEnricher enricher = new DateFieldEnricher(null, ZoneId.of("UTC"));
		enricher.enrich(record);

		assertEquals("2026", firstValue(record, "event_year_i"));
		assertEquals("8",    firstValue(record, "event_month_i"));
		assertEquals("21",   firstValue(record, "event_day_i"));
		// 2026-08-21 = Friday → ISO dow=5
		assertEquals("5",    firstValue(record, "event_dow_i"));

		// hour_i が生成されていないこと
		assertTrue("event_hour_i must NOT be generated for date-only input",
				record.getDataValues("event_hour_i").isEmpty());
	}

	public void testEnrich_dateOnly_withTokyo_noHourField() {
		// Asia/Tokyo で日付のみ → hour_i なし、ローカル日付が適用されること
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("event_dt", "2026-08-21");

		DateFieldEnricher enricher = new DateFieldEnricher(null, ZoneId.of("Asia/Tokyo"));
		enricher.enrich(record);

		assertEquals("2026", firstValue(record, "event_year_i"));
		assertEquals("8",    firstValue(record, "event_month_i"));
		assertEquals("21",   firstValue(record, "event_day_i"));
		assertEquals("5",    firstValue(record, "event_dow_i")); // Friday

		assertTrue("event_hour_i must NOT be generated for date-only input",
				record.getDataValues("event_hour_i").isEmpty());
	}

	// -----------------------------------------------------------------------
	// ZoneId: offsetなし local datetime → defaultZone が適用されること
	// -----------------------------------------------------------------------

	public void testEnrich_localDatetime_TokyoZone() {
		// タイムゾーンなしの datetime → Tokyo ゾーンで解釈
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("event_dt", "2026-08-21T14:30:00");

		DateFieldEnricher enricher = new DateFieldEnricher(null, ZoneId.of("Asia/Tokyo"));
		enricher.enrich(record);

		assertEquals("2026", firstValue(record, "event_year_i"));
		assertEquals("8",    firstValue(record, "event_month_i"));
		assertEquals("21",   firstValue(record, "event_day_i"));
		assertEquals("14",   firstValue(record, "event_hour_i")); // local hour
		assertEquals("5",    firstValue(record, "event_dow_i"));  // Friday
	}

	public void testEnrich_localDatetime_epochMillis_timezone() {
		// 同じ local datetime でも timezone が違えば epoch millis が異なること
		SearchRecord record1 = new SearchRecord("1", "test");
		record1.addData("event_dt", "2026-08-21T14:30:00");
		new DateFieldEnricher(null, ZoneId.of("Asia/Tokyo")).enrich(record1);

		SearchRecord record2 = new SearchRecord("2", "test");
		record2.addData("event_dt", "2026-08-21T14:30:00");
		new DateFieldEnricher(null, ZoneId.of("UTC")).enrich(record2);

		// 両者で year/month/day/hour は同じ
		assertEquals(firstValue(record1, "event_year_i"),  firstValue(record2, "event_year_i"));
		assertEquals(firstValue(record1, "event_hour_i"),  firstValue(record2, "event_hour_i"));
	}

	// -----------------------------------------------------------------------
	// dow (Day of Week) の検証
	// -----------------------------------------------------------------------

	public void testEnrich_dow_monday() {
		// 2024-01-01 = Monday
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("d_dt", "2024-01-01T12:00:00Z");

		new DateFieldEnricher(null).enrich(record);
		assertEquals("1", firstValue(record, "d_dow_i")); // ISO Mon=1
	}

	public void testEnrich_dow_sunday() {
		// 2026-08-23 = Sunday
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("d_dt", "2026-08-23T12:00:00Z");

		new DateFieldEnricher(null).enrich(record);
		assertEquals("7", firstValue(record, "d_dow_i")); // ISO Sun=7
	}

	public void testEnrich_dow_saturday() {
		// 2026-08-22 = Saturday
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("d_dt", "2026-08-22T12:00:00Z");

		new DateFieldEnricher(null).enrich(record);
		assertEquals("6", firstValue(record, "d_dow_i")); // ISO Sat=6
	}

	// -----------------------------------------------------------------------
	// _dt サフィックスを持たないフィールドは無視されること
	// -----------------------------------------------------------------------

	public void testEnrich_nonDtField_ignored() {
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("category", "technology"); // KEYWORD → 無視
		record.addData("score_i", "42");           // INTEGER → 無視

		new DateFieldEnricher(null).enrich(record);

		// 派生フィールドが生成されていないこと
		assertTrue(record.getDataValues("category_year_i").isEmpty());
		assertTrue(record.getDataValues("score_year_i").isEmpty());
	}

	// -----------------------------------------------------------------------
	// null record は例外なく無視されること
	// -----------------------------------------------------------------------

	public void testEnrich_nullRecord_noException() {
		DateFieldEnricher enricher = new DateFieldEnricher(null);
		// NullPointerException が発生しないこと
		enricher.enrich(null);
	}

	// -----------------------------------------------------------------------
	// _dt フィールドが存在しない場合は何もしないこと
	// -----------------------------------------------------------------------

	public void testEnrich_noDtField_noDerivation() {
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("name", "Alice");

		new DateFieldEnricher(null).enrich(record);

		// データキーに派生フィールドが追加されていないこと
		assertTrue(record.getDataValues("name_year_i").isEmpty());
	}

	// -----------------------------------------------------------------------
	// 元の _dt フィールドは変更されないこと
	// -----------------------------------------------------------------------

	public void testEnrich_originalFieldPreserved() {
		SearchRecord record = new SearchRecord("1", "test");
		record.addData("event_dt", "2026-08-19T20:15:30+09:00");

		new DateFieldEnricher(null).enrich(record);

		// 元の _dt フィールドはそのまま残ること
		assertEquals("2026-08-19T20:15:30+09:00", firstValue(record, "event_dt"));
	}

	// -----------------------------------------------------------------------
	// SearchSchema を使ったフィールド型解決
	// -----------------------------------------------------------------------

	public void testEnrich_withExplicitSchema() {
		SearchSchema schema = new SearchSchema();
		schema.add("publish_dt",
				nlp4j.lucene9.FieldTypeDef.date().stored(true).aggregatable(true));

		SearchRecord record = new SearchRecord("1", "test");
		record.addData("publish_dt", "2025-12-31T23:59:59+09:00");

		new DateFieldEnricher(schema).enrich(record);

		assertEquals("2025", firstValue(record, "publish_year_i"));
		assertEquals("12",   firstValue(record, "publish_month_i"));
		assertEquals("31",   firstValue(record, "publish_day_i"));
		assertEquals("23",   firstValue(record, "publish_hour_i"));
	}

	// -----------------------------------------------------------------------
	// Helper
	// -----------------------------------------------------------------------

	private static String firstValue(SearchRecord record, String fieldName) {
		java.util.List<String> values = record.getDataValues(fieldName);
		assertFalse("No values for field: " + fieldName, values.isEmpty());
		return values.get(0);
	}
}
