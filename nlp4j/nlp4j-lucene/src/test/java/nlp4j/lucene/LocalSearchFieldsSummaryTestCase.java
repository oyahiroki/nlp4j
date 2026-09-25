package nlp4j.lucene;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.lucene9.FieldTypeDef;

/**
 * Tests for {@link LocalSearch#getFieldsSummary()}.
 *
 * <p>
 * JUnit 3 test case covering all scenarios specified in kaiwa0916-1752.md.
 * </p>
 *
 * @since 1.7.2.0
 */
public class LocalSearchFieldsSummaryTestCase extends TestCase {

	// -------------------------------------------------------------------------
	// ① 基本情報
	// -------------------------------------------------------------------------

	/**
	 * ① Field / Type / Aggregatable が正しく返ること
	 */
	public void testBasicInfo() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("category_s", FieldTypeDef.keyword().stored(true).aggregatable(true))
				.field("year_i", FieldTypeDef.integer().aggregatable(true))
				.field("created_dt", FieldTypeDef.date().aggregatable(true)).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "text": "hello",
					  "category_s": "city",
					  "year_i": "2024",
					  "created_dt": "2024-01-15"
					}
					""");
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();
			List<FieldSummary> fields = summary.getFields();

			FieldSummary cat = findField(fields, "category_s");
			assertNotNull(cat);
			assertEquals(FieldTypeDef.Kind.KEYWORD, cat.getKind());
			assertTrue(cat.isAggregatable());

			FieldSummary year = findField(fields, "year_i");
			assertNotNull(year);
			assertEquals(FieldTypeDef.Kind.INTEGER, year.getKind());
			assertTrue(year.isAggregatable());

			FieldSummary created = findField(fields, "created_dt");
			assertNotNull(created);
			assertEquals(FieldTypeDef.Kind.DATE, created.getKind());
			assertTrue(created.isAggregatable());

			FieldSummary text = findField(fields, "text");
			assertNotNull(text);
			assertEquals(FieldTypeDef.Kind.TEXT, text.getKind());
			assertFalse(text.isAggregatable());
		}
	}

	// -------------------------------------------------------------------------
	// ② schema 順
	// -------------------------------------------------------------------------

	/**
	 * ② 結果が schema 登録順になっていること
	 */
	public void testSchemaOrder() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("z_field_s", FieldTypeDef.keyword().stored(true).aggregatable(true))
				.field("a_field_s", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "text": "hello",
					  "z_field_s": "Z",
					  "a_field_s": "A"
					}
					""");
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();
			List<FieldSummary> fields = summary.getFields();

			// z_field_s が a_field_s より先に登録されているので前に来るはず
			int zIdx = indexOfField(fields, "z_field_s");
			int aIdx = indexOfField(fields, "a_field_s");
			assertTrue("z_field_s should come before a_field_s", zIdx < aIdx);
		}
	}

	// -------------------------------------------------------------------------
	// ③ 実データのないフィールドを除外
	// -------------------------------------------------------------------------

	/**
	 * ③ schema に定義されていても値が一度も設定されていないフィールドは結果に含まれないこと
	 */
	public void testExcludeFieldsWithoutData() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("present_s", FieldTypeDef.keyword().stored(true).aggregatable(true))
				.field("absent_s", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {

			search.addJson("""
					{
					  "id": "1",
					  "text": "hello",
					  "present_s": "value"
					}
					""");
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();
			List<FieldSummary> fields = summary.getFields();

			assertNotNull(findField(fields, "present_s"));
			assertNull(findField(fields, "absent_s"));
		}
	}

	// -------------------------------------------------------------------------
	// ④ KEYWORD + aggregatable
	// -------------------------------------------------------------------------

	/**
	 * ④ KEYWORD + aggregatable=true で Coverage / Unique / Diversity が正しく計算されること
	 *
	 * <pre>
	 * doc1 maker=NISSAN
	 * doc2 maker=NISSAN
	 * doc3 maker=TOYOTA
	 * doc4 maker=null
	 *
	 * DocumentCount=4, DocumentsWithValue=3, Coverage=3/4, Unique=2, Diversity=2/3
	 * </pre>
	 */
	public void testKeywordAggregatable() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("maker_s", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {

			search.addJson("{\"id\":\"1\",\"text\":\"t\",\"maker_s\":\"NISSAN\"}");
			search.addJson("{\"id\":\"2\",\"text\":\"t\",\"maker_s\":\"NISSAN\"}");
			search.addJson("{\"id\":\"3\",\"text\":\"t\",\"maker_s\":\"TOYOTA\"}");
			search.addJson("{\"id\":\"4\",\"text\":\"t\"}"); // maker_s=null
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();
			assertEquals(4L, summary.getDocumentCount());

			FieldSummary maker = findField(summary.getFields(), "maker_s");
			assertNotNull(maker);

			assertEquals(4L, maker.getDocumentCount());
			assertEquals(3L, maker.getDocumentsWithValue());
			assertTrue(maker.hasCoverage());
			assertEquals(3.0 / 4.0, maker.getCoverage(), 1e-9);

			assertTrue(maker.hasUniqueValueCount());
			assertEquals(2L, maker.getUniqueValueCount());

			assertTrue(maker.hasDiversity());
			assertEquals(2.0 / 3.0, maker.getDiversity(), 1e-9);
		}
	}

	// -------------------------------------------------------------------------
	// ⑤ multi-valued KEYWORD
	// -------------------------------------------------------------------------

	/**
	 * ⑤ multi-valued KEYWORD で DocumentsWithValue が全値数でなくドキュメント数であること
	 *
	 * <pre>
	 * doc1 tags=[EV,SUV]
	 * doc2 tags=[EV]
	 * doc3 tags=[HYBRID,SUV]
	 * doc4 tags=null
	 *
	 * DocumentsWithValue=3, Unique=3(EV,SUV,HYBRID), Coverage=3/4, Diversity=3/3
	 * </pre>
	 */
	public void testMultiValuedKeyword() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("tags_s", FieldTypeDef.keyword().stored(true).aggregatable(true).multiValued(true)).build()) {

			search.addJson("{\"id\":\"1\",\"text\":\"t\",\"tags_s\":[\"EV\",\"SUV\"]}");
			search.addJson("{\"id\":\"2\",\"text\":\"t\",\"tags_s\":[\"EV\"]}");
			search.addJson("{\"id\":\"3\",\"text\":\"t\",\"tags_s\":[\"HYBRID\",\"SUV\"]}");
			search.addJson("{\"id\":\"4\",\"text\":\"t\"}"); // null
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();
			FieldSummary tags = findField(summary.getFields(), "tags_s");
			assertNotNull(tags);

			assertEquals(3L, tags.getDocumentsWithValue());
			assertEquals(3L, tags.getUniqueValueCount()); // EV, SUV, HYBRID
			assertEquals(3.0 / 4.0, tags.getCoverage(), 1e-9);
			assertEquals(3.0 / 3.0, tags.getDiversity(), 1e-9);
		}
	}

	// -------------------------------------------------------------------------
	// ⑥ KEYWORD + aggregatable=false
	// -------------------------------------------------------------------------

	/**
	 * ⑥ KEYWORD + aggregatable=false では Coverage / Unique / Diversity が unavailable
	 * で、 Example のみ取得できること
	 */
	public void testKeywordNotAggregatable() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			// id フィールドは KEYWORD + aggregatable=false (stored=true)
			search.addJson("{\"id\":\"doc-001\",\"text\":\"hello\"}");
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();
			FieldSummary idField = findField(summary.getFields(), "id");
			assertNotNull(idField);

			assertEquals(FieldTypeDef.Kind.KEYWORD, idField.getKind());
			assertFalse(idField.isAggregatable());
			assertFalse(idField.hasCoverage());
			assertFalse(idField.hasUniqueValueCount());
			assertFalse(idField.hasDiversity());
			// Example is available (id is stored)
			assertNotNull(idField.getExample());
		}
	}

	// -------------------------------------------------------------------------
	// ⑦ INTEGER
	// -------------------------------------------------------------------------

	/**
	 * ⑦ INTEGER では Coverage が取得でき Unique は unavailable であること
	 *
	 * <pre>
	 * 10, 10, 20, null → DocumentsWithValue=3, Coverage=3/4, Unique=unavailable
	 * </pre>
	 */
	public void testInteger() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("score_i", FieldTypeDef.integer().aggregatable(true)).build()) {

			search.addJson("{\"id\":\"1\",\"text\":\"t\",\"score_i\":\"10\"}");
			search.addJson("{\"id\":\"2\",\"text\":\"t\",\"score_i\":\"10\"}");
			search.addJson("{\"id\":\"3\",\"text\":\"t\",\"score_i\":\"20\"}");
			search.addJson("{\"id\":\"4\",\"text\":\"t\"}"); // null
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();
			FieldSummary score = findField(summary.getFields(), "score_i");
			assertNotNull(score);

			assertTrue(score.isAggregatable());
			assertEquals(3L, score.getDocumentsWithValue());
			assertTrue(score.hasCoverage());
			assertEquals(3.0 / 4.0, score.getCoverage(), 1e-9);

			// Unique は INTEGER では計算しない（仕様）
			assertFalse(score.hasUniqueValueCount());
			assertFalse(score.hasDiversity());
		}
	}

	// -------------------------------------------------------------------------
	// ⑧ LONG / DOUBLE / DATE
	// -------------------------------------------------------------------------

	/**
	 * ⑧ LONG / DOUBLE / DATE でも INTEGER と同様に Coverage のみ取得されること
	 */
	public void testLongDoubleDate() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("amount_l", FieldTypeDef.longNumber().aggregatable(true))
				.field("price_d", FieldTypeDef.doubleNumber().aggregatable(true))
				.field("event_dt", FieldTypeDef.date().aggregatable(true)).build()) {

			search.addJson(
					"{\"id\":\"1\",\"text\":\"t\",\"amount_l\":\"1000\",\"price_d\":\"9.99\",\"event_dt\":\"2024-01-01\"}");
			search.addJson("{\"id\":\"2\",\"text\":\"t\"}"); // all null
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();

			FieldSummary amount = findField(summary.getFields(), "amount_l");
			assertNotNull(amount);
			assertTrue(amount.hasCoverage());
			assertEquals(1.0 / 2.0, amount.getCoverage(), 1e-9);
			assertFalse(amount.hasUniqueValueCount());
			assertFalse(amount.hasDiversity());

			FieldSummary price = findField(summary.getFields(), "price_d");
			assertNotNull(price);
			assertTrue(price.hasCoverage());
			assertEquals(0.5, price.getCoverage(), 1e-9);
			assertFalse(price.hasUniqueValueCount());
			assertFalse(price.hasDiversity());

			FieldSummary event = findField(summary.getFields(), "event_dt");
			assertNotNull(event);
			assertTrue(event.hasCoverage());
			assertEquals(0.5, event.getCoverage(), 1e-9);
			assertFalse(event.hasUniqueValueCount());
			assertFalse(event.hasDiversity());
		}
	}

	// -------------------------------------------------------------------------
	// ⑨ TEXT / STORED_ONLY
	// -------------------------------------------------------------------------

	/**
	 * ⑨ TEXT / STORED_ONLY では Coverage / Unique / Diversity が unavailable で stored
	 * フィールドは Example が取得できること
	 */
	public void testTextAndStoredOnly() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			// text は TEXT, data は STORED_ONLY (addJson により自動登録)
			search.addJson("{\"id\":\"1\",\"text\":\"Hello world\",\"body\":\"some body text\"}");
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();

			FieldSummary text = findField(summary.getFields(), "text");
			assertNotNull(text);
			assertEquals(FieldTypeDef.Kind.TEXT, text.getKind());
			assertFalse(text.isAggregatable());
			assertFalse(text.hasCoverage());
			assertFalse(text.hasUniqueValueCount());
			assertFalse(text.hasDiversity());
			// text is stored
			assertNotNull(text.getExample());

			// STORED_ONLY: data フィールドも明示的に確認
			FieldSummary data = findField(summary.getFields(), "data");
			assertNotNull(data);
			assertEquals(FieldTypeDef.Kind.STORED_ONLY, data.getKind());
			assertFalse(data.isAggregatable());
			assertFalse(data.hasCoverage());
			assertFalse(data.hasUniqueValueCount());
			assertFalse(data.hasDiversity());
			assertNotNull(data.getExample());
		}
	}

	// -------------------------------------------------------------------------
	// ⑩ Example
	// -------------------------------------------------------------------------

	/**
	 * ⑩ KEYWORD / INTEGER / DATE など各型で Example が正しく取得されること。 DATE は ISO8601
	 * に変換されること。
	 */
	public void testExample() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("maker_s", FieldTypeDef.keyword().stored(true).aggregatable(true))
				.field("year_i", FieldTypeDef.integer().stored(true).aggregatable(true))
				.field("event_dt", FieldTypeDef.date().stored(true).aggregatable(true)).build()) {

			search.addJson(
					"{\"id\":\"1\",\"text\":\"t\",\"maker_s\":\"NISSAN\",\"year_i\":\"2024\",\"event_dt\":\"2024-06-01T00:00:00Z\"}");
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();

			FieldSummary maker = findField(summary.getFields(), "maker_s");
			assertEquals("NISSAN", maker.getExample());

			FieldSummary year = findField(summary.getFields(), "year_i");
			assertNotNull(year.getExample());
			assertEquals("2024", year.getExample());

			FieldSummary event = findField(summary.getFields(), "event_dt");
			assertNotNull(event.getExample());
			// Must be ISO 8601 format (contains 'T' and offset)
			String exampleDate = event.getExample();
			assertTrue("DATE example should be ISO8601: " + exampleDate, exampleDate.contains("T"));
		}
	}

	// -------------------------------------------------------------------------
	// ⑪ Update
	// -------------------------------------------------------------------------

	/**
	 * ⑪ Update 後に古い document が Unique に含まれないこと
	 *
	 * <pre>
	 * 初期: id=1 maker=HONDA, id=2 maker=TOYOTA → Unique=2
	 * 更新: id=1 maker=TOYOTA → Unique=1
	 * </pre>
	 */
	public void testUpdate() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("maker_s", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {

			// 初期状態
			search.addJson("{\"id\":\"1\",\"text\":\"t\",\"maker_s\":\"HONDA\"}");
			search.addJson("{\"id\":\"2\",\"text\":\"t\",\"maker_s\":\"TOYOTA\"}");
			search.commit();

			FieldSummary before = findField(search.getFieldsSummary().getFields(), "maker_s");
			assertEquals(2L, before.getUniqueValueCount());

			// id=1 を TOYOTA に更新
			search.addJson("{\"id\":\"1\",\"text\":\"t\",\"maker_s\":\"TOYOTA\"}");
			search.commit();

			FieldSummary after = findField(search.getFieldsSummary().getFields(), "maker_s");
			assertEquals(2L, search.getFieldsSummary().getDocumentCount());
			assertEquals(1L, after.getUniqueValueCount()); // HONDA は除外される
		}
	}

	// -------------------------------------------------------------------------
	// ⑫ Delete
	// -------------------------------------------------------------------------

	/**
	 * ⑫ Delete 後に DocumentCount / DocumentsWithValue / Unique が正しく更新されること
	 *
	 * <pre>
	 * id=1 maker=HONDA, id=2 maker=TOYOTA → id=1 削除
	 * DocumentCount=1, DocumentsWithValue=1, Unique=1
	 * </pre>
	 */
	public void testDelete() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("maker_s", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {

			search.addJson("{\"id\":\"1\",\"text\":\"t\",\"maker_s\":\"HONDA\"}");
			search.addJson("{\"id\":\"2\",\"text\":\"t\",\"maker_s\":\"TOYOTA\"}");
			search.commit();

			search.delete("1");
			search.commit();

			FieldsSummary summary = search.getFieldsSummary();
			assertEquals(1L, summary.getDocumentCount());

			FieldSummary maker = findField(summary.getFields(), "maker_s");
			assertNotNull(maker);
			assertEquals(1L, maker.getDocumentsWithValue());
			assertEquals(1L, maker.getUniqueValueCount());
		}
	}

	// -------------------------------------------------------------------------
	// ⑬ Empty index
	// -------------------------------------------------------------------------

	/**
	 * ⑬ 空の index でも例外にならず DocumentCount=0 / Fields=empty になること
	 */
	public void testEmptyIndex() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			FieldsSummary summary = search.getFieldsSummary();
			assertEquals(0L, summary.getDocumentCount());
			assertTrue(summary.getFields().isEmpty());
		}
	}

	// -------------------------------------------------------------------------
	// DATE timezone
	// -------------------------------------------------------------------------

	/**
	 * DATE Example が LocalSearch の zoneId で表示されること（UTC 固定でないこと）。
	 *
	 * <pre>
	 * timeZone = Asia/Tokyo
	 * input    = 2026-08-21
	 * expected = 2026-08-21T00:00:00+09:00
	 * </pre>
	 */
	public void testDateExampleTimeZone() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).timeZone("Asia/Tokyo")
				.field("event_dt", FieldTypeDef.date().stored(true).aggregatable(true)).build()) {

			search.addJson("""
					{
					  "id":"1",
					  "text":"t",
					  "event_dt":"2026-08-21"
					}
					""");
			search.commit();

			FieldSummary event = findField(search.getFieldsSummary().getFields(), "event_dt");

			assertNotNull(event);
			assertTrue("Expected +09:00 offset, got: " + event.getExample(),
					event.getExample().startsWith("2026-08-21T00:00:00+09:00"));
		}
	}

	// -------------------------------------------------------------------------
	// フィールド完全消失
	// -------------------------------------------------------------------------

	/**
	 * 全 live documents を削除するとそのフィールドが一覧から消えること。
	 *
	 * <pre>
	 * doc1 maker_s=HONDA, doc2 text only
	 * → doc1 削除後 maker_s は全件0件 → 一覧から消える
	 * </pre>
	 */
	public void testFieldDisappearsAfterDelete() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false)
				.field("maker_s", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {

			search.addJson("{\"id\":\"1\",\"text\":\"t\",\"maker_s\":\"HONDA\"}");
			search.addJson("{\"id\":\"2\",\"text\":\"t\"}");
			search.commit();

			assertNotNull(findField(search.getFieldsSummary().getFields(), "maker_s"));

			search.delete("1");
			search.commit();

			assertNull(findField(search.getFieldsSummary().getFields(), "maker_s"));
		}
	}

	// -------------------------------------------------------------------------
	// Helpers
	// -------------------------------------------------------------------------

	private static FieldSummary findField(List<FieldSummary> fields, String name) {
		for (FieldSummary f : fields) {
			if (name.equals(f.getField())) {
				return f;
			}
		}
		return null;
	}

	private static int indexOfField(List<FieldSummary> fields, String name) {
		for (int i = 0; i < fields.size(); i++) {
			if (name.equals(fields.get(i).getField())) {
				return i;
			}
		}
		return -1;
	}

	public void testKnnVectorField() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).vectorDimension(3).build()) {

			search.add("1", new float[] { 0.1f, 0.2f, 0.3f });

			search.commit();

			FieldSummary vector = findField(search.getFieldsSummary().getFields(), "vector");

			assertNotNull(vector);

			assertEquals(FieldTypeDef.Kind.KNN_VECTOR, vector.getKind());

			assertFalse(vector.isAggregatable());
			assertFalse(vector.hasCoverage());
			assertFalse(vector.hasUniqueValueCount());
			assertFalse(vector.hasDiversity());

			assertNull(vector.getExample());
		}
	}
}
