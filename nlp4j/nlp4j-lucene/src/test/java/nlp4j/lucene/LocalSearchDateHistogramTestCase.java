package nlp4j.lucene;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import nlp4j.lucene9.DateHistogramBucket;
import nlp4j.lucene9.DateHistogramInterval;
import nlp4j.lucene9.FieldTypeDef;

/**
 * DATE histogram aggregation tests for LocalSearch.
 * Covers the test cases
 * specified in kaiwa0914-1726.md §23.
 */
public class LocalSearchDateHistogramTestCase extends TestCase {

	// -----------------------------------------------------------------------
	// Helper: build a LocalSearch pre-loaded with car data
	// -----------------------------------------------------------------------
	private LocalSearch buildCarSearch() throws Exception {
		LocalSearch search = LocalSearch.builder("en").build();
		// Nissan: 2023, 2026
		search.addJson("{\"id\":\"1\",\"body\":\"Nissan\",\"created_dt\":\"2023-06-01\"}");
		search.addJson("{\"id\":\"2\",\"body\":\"Nissan\",\"created_dt\":\"2026-03-15\"}");
		// Toyota: 2020
		search.addJson("{\"id\":\"3\",\"body\":\"Toyota\",\"created_dt\":\"2020-01-01\"}");
		search.commit();
		return search;
	}

	// -----------------------------------------------------------------------
	// 1. YEAR 基本集計
	// -----------------------------------------------------------------------
	public void testDateHistogram_Year_Basic() throws Exception {
		try (LocalSearch search = buildCarSearch()) {
			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.YEAR);
			assertFalse("buckets must not be empty", buckets.isEmpty());
			// Should contain 2020, 2023, 2024, 2025, 2026
			assertEquals(7, buckets.size()); // 2020..2026
			assertEquals("2020", buckets.get(0).getKeyAsString());
			assertEquals(1L, buckets.get(0).getDocCount());
		}
	}

	// -----------------------------------------------------------------------
	// 2. YEAR 途中 0件
	// -----------------------------------------------------------------------
	public void testDateHistogram_Year_ZeroGap() throws Exception {
		try (LocalSearch search = buildCarSearch()) {
			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.YEAR);
			buckets.forEach(b -> {
				System.err.println(b.toString());
			});
			// 2021, 2022 are zero-count gaps
			DateHistogramBucket b2021 = buckets.stream().filter(b -> "2021".equals(b.getKeyAsString())).findFirst()
					.orElse(null);
			assertNotNull("2021 bucket must exist", b2021);
			assertEquals(0L, b2021.getDocCount());
		}
	}

	// -----------------------------------------------------------------------
	// 3. MONTH 途中 0件
	// -----------------------------------------------------------------------
	public void testDateHistogram_Month_ZeroGap() throws Exception {
		try (LocalSearch search = buildCarSearch()) {
			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.MONTH);
			buckets.forEach(b -> {
				System.err.println(b.toString());
			});
			assertTrue("month buckets must not be empty", buckets.size() > 0);
			// Between 2020-01 and 2023-06 there should be many zero-count buckets
			long zeroCount = buckets.stream().filter(b -> b.getDocCount() == 0).count();
			assertTrue("There must be zero-count month buckets", zeroCount > 0);
		}
	}

	// -----------------------------------------------------------------------
	// 4. HOUR 途中 0件
	// -----------------------------------------------------------------------
	public void testDateHistogram_Hour_ZeroGap() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"A\",\"created_dt\":\"2026-01-01T08:00:00\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"B\",\"created_dt\":\"2026-01-01T10:00:00\"}");
			search.commit();
			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.HOUR);
			buckets.forEach(b -> {
				System.err.println(b.toString());
			});
			// Expect 3 buckets: 08, 09, 10
			assertEquals(3, buckets.size());
			assertEquals("2026-01-01T08", buckets.get(0).getKeyAsString());
			assertEquals(1L, buckets.get(0).getDocCount());
			assertEquals("2026-01-01T09", buckets.get(1).getKeyAsString());
			assertEquals(0L, buckets.get(1).getDocCount());
			assertEquals("2026-01-01T10", buckets.get(2).getKeyAsString());
			assertEquals(1L, buckets.get(2).getDocCount());
		}
	}

	// -----------------------------------------------------------------------
	// 5. query 適用後 min/max
	// -----------------------------------------------------------------------
	public void testDateHistogram_QueryApplied_MinMax() throws Exception {
		try (LocalSearch search = buildCarSearch()) {
			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.YEAR,
					"Nissan");
			buckets.forEach(b -> {
				System.err.println(b.toString());
			});
			// Nissan: 2023, 2026 → buckets from 2023 to 2026 (no 2020)
			assertFalse("buckets must not be empty", buckets.isEmpty());
			assertEquals("2023", buckets.get(0).getKeyAsString());
			assertEquals("2026", buckets.get(buckets.size() - 1).getKeyAsString());
			// 2020 must NOT appear
			boolean has2020 = buckets.stream().anyMatch(b -> "2020".equals(b.getKeyAsString()));
			assertFalse("2020 must not appear when query=Nissan", has2020);
		}
	}

	// -----------------------------------------------------------------------
	// 6. filters 適用後 min/max
	// -----------------------------------------------------------------------
	public void testDateHistogram_FilterApplied_MinMax() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.field("brand", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"car\",\"brand\":\"Nissan\",\"created_dt\":\"2023-05-01\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"car\",\"brand\":\"Nissan\",\"created_dt\":\"2026-07-01\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"car\",\"brand\":\"Toyota\",\"created_dt\":\"2020-01-01\"}");
			search.commit();

			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.YEAR, null,
					Map.of("brand", "Nissan"));
			buckets.forEach(b -> {
				System.err.println(b.toString());
			});
			assertEquals("2023", buckets.get(0).getKeyAsString());
			assertEquals("2026", buckets.get(buckets.size() - 1).getKeyAsString());
			boolean has2020 = buckets.stream().anyMatch(b -> "2020".equals(b.getKeyAsString()));
			assertFalse("2020 must not appear when filter=Nissan", has2020);
		}
	}

	// -----------------------------------------------------------------------
	// 7. query + filters
	// -----------------------------------------------------------------------
	public void testDateHistogram_QueryAndFilter() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.field("brand", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"sport car\",\"brand\":\"Nissan\",\"created_dt\":\"2023-01-01\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"sport car\",\"brand\":\"Toyota\",\"created_dt\":\"2024-01-01\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"truck\",\"brand\":\"Nissan\",\"created_dt\":\"2025-01-01\"}");
			search.commit();

			// query=sport + filter=Nissan → only doc1 (2023)
			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.YEAR, "sport",
					Map.of("brand", "Nissan"));
			assertEquals(1, buckets.size());
			assertEquals("2023", buckets.get(0).getKeyAsString());
			assertEquals(1L, buckets.get(0).getDocCount());
		}
	}

	// -----------------------------------------------------------------------
	// 8. 対象文書 0件 → buckets 空
	// -----------------------------------------------------------------------
	public void testDateHistogram_NoMatch_EmptyBuckets() throws Exception {
		try (LocalSearch search = buildCarSearch()) {
			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.YEAR,
					"NonExistingBrand");
			assertTrue("buckets must be empty when no docs match", buckets.isEmpty());
		}
	}

	// -----------------------------------------------------------------------
	// 9. DATE 値を持たない文書は無視
	// -----------------------------------------------------------------------
	public void testDateHistogram_DocWithoutDateIgnored() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"has date\",\"created_dt\":\"2026-01-01\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"no date\"}");
			search.commit();

			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.YEAR);
			assertEquals(1, buckets.size());
			assertEquals("2026", buckets.get(0).getKeyAsString());
			assertEquals(1L, buckets.get(0).getDocCount());
		}
	}

	// -----------------------------------------------------------------------
	// 10. DATE 以外のフィールドに date_histogram → エラー
	// -----------------------------------------------------------------------
	public void testDateHistogram_NonDateField_Error() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.field("category", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"test\",\"category\":\"A\"}");
			search.commit();

			try {
				search.dateHistogram("category", DateHistogramInterval.YEAR);
				fail("Expected LocalSearchException for non-DATE field");
			} catch (LocalSearchException e) {
				assertTrue("Error must mention date_histogram",
						e.getMessage().contains("date_histogram") || e.getMessage().contains("DATE"));
			}
		}
	}

	// -----------------------------------------------------------------------
	// 11. _dt JSON array → エラー
	// -----------------------------------------------------------------------
	public void testAddJson_DateArray_Error() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			try {
				search.addJson("{\"id\":\"1\",\"body\":\"test\",\"created_dt\":[\"2026-01-01\",\"2026-02-01\"]}");
				fail("Expected LocalSearchException for DATE array");
			} catch (LocalSearchException e) {
				assertTrue("Error must mention single-valued", e.getMessage().toLowerCase().contains("single-valued")
						|| e.getMessage().toLowerCase().contains("single"));
			}
		}
	}

	// -----------------------------------------------------------------------
	// 12. SearchRecord DATE 複数値 → エラー
	// -----------------------------------------------------------------------
	public void testSearchRecord_DateMultiValue_Error() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			try {
				SearchRecord record = new SearchRecord("1", "test");
				record.addData("created_dt", "2026-01-01");
				record.addData("created_dt", "2026-02-01"); // second value
				search.add(record);
				search.commit();
				fail("Expected exception for DATE multi-value");
			} catch (LocalSearchException | IllegalArgumentException e) {
				assertTrue("Error must mention single-valued", e.getMessage().toLowerCase().contains("single-valued")
						|| e.getMessage().toLowerCase().contains("single"));
			}
		}
	}

	// -----------------------------------------------------------------------
	// 13. SearchDocumentBuilder DATE 複数 put → エラー
	// -----------------------------------------------------------------------
	public void testSearchDocumentBuilder_DateDoublePut_Error() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			// trigger schema registration for created_dt
			search.addJson("{\"id\":\"setup\",\"body\":\"setup\",\"created_dt\":\"2026-01-01\"}");
			search.commit();

			nlp4j.lucene9.SearchSchema schema = search.getSchema();
			try {
				schema.document(java.time.ZoneId.systemDefault()).put("id", "1").put("created_dt", "2026-01-01")
						.put("created_dt", "2026-02-01") // duplicate
						.build();
				fail("Expected IllegalArgumentException for duplicate single-valued DATE");
			} catch (IllegalArgumentException e) {
				assertTrue("Error must mention single-valued", e.getMessage().toLowerCase().contains("single-valued")
						|| e.getMessage().toLowerCase().contains("single"));
			}
		}
	}

	// -----------------------------------------------------------------------
	// 14. Asia/Tokyo timezone
	// -----------------------------------------------------------------------
	public void testDateHistogram_AsiaTokyo_Timezone() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").timeZone("Asia/Tokyo").build()) {
			// Midnight UTC = previous day in Tokyo
			search.addJson("{\"id\":\"1\",\"body\":\"event\",\"created_dt\":\"2026-01-01T00:00:00+09:00\"}");
			search.commit();

			List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.YEAR);
			assertEquals(1, buckets.size());
			assertEquals("2026", buckets.get(0).getKeyAsString());
		}
	}

	// -----------------------------------------------------------------------
	// 15. time_zone JSON override
	// -----------------------------------------------------------------------
	public void testDateHistogram_TimeZoneJsonOverride() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"event\",\"created_dt\":\"2026-01-01T00:00:00Z\"}");
			search.commit();

			String response = search.searchResponseJson("""
					{
					  "size": 0,
					  "aggs": {
					    "values": {
					      "date_histogram": {
					        "field": "created_dt",
					        "calendar_interval": "year",
					        "time_zone": "Asia/Tokyo"
					      }
					    }
					  }
					}
					""");
			assertTrue("Response must contain aggregations", response.contains("aggregations"));
			assertTrue("Response must contain 2026", response.contains("2026"));
		}
	}

	// -----------------------------------------------------------------------
	// 16. searchResponseJson() date_histogram
	// -----------------------------------------------------------------------
	public void testSearchResponseJson_DateHistogram() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"Nissan\",\"created_dt\":\"2023-01-01\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"Toyota\",\"created_dt\":\"2026-01-01\"}");
			search.commit();

			String response = search.searchResponseJson("""
					{
					  "size": 0,
					  "aggs": {
					    "dates": {
					      "date_histogram": {
					        "field": "created_dt",
					        "calendar_interval": "year"
					      }
					    }
					  }
					}
					""");

			assertTrue("Response must contain aggregations", response.contains("aggregations"));
			assertTrue("Response must contain doc_count", response.contains("doc_count"));
			assertTrue("Response must contain 2023", response.contains("2023"));
			assertTrue("Response must contain 2026", response.contains("2026"));
		}
	}

	// -----------------------------------------------------------------------
	// 17. save/reload 後 date histogram
	// -----------------------------------------------------------------------
	public void testDateHistogram_SaveAndReload() throws Exception {
		java.nio.file.Path indexDir = java.nio.file.Files.createTempDirectory("lsdt_test");
		try {
			// Save
			try (LocalSearch search = LocalSearch.builder("en").build()) {
				search.addJson("{\"id\":\"1\",\"body\":\"Nissan\",\"created_dt\":\"2023-06-01\"}");
				search.addJson("{\"id\":\"2\",\"body\":\"Toyota\",\"created_dt\":\"2026-03-15\"}");
				search.commit();
				search.saveIndexTo(indexDir);
			}

			// Reload
			try (LocalSearch search = LocalSearch.builder("en").loadIndexFrom(indexDir).build()) {
				List<DateHistogramBucket> buckets = search.dateHistogram("created_dt", DateHistogramInterval.YEAR);
				assertFalse("Reloaded buckets must not be empty", buckets.isEmpty());
				assertEquals("2023", buckets.get(0).getKeyAsString());
				assertEquals("2026", buckets.get(buckets.size() - 1).getKeyAsString());
			}
		} finally {
			// Cleanup
			try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.walk(indexDir)) {
				stream.sorted(java.util.Comparator.reverseOrder()).map(java.nio.file.Path::toFile)
						.forEach(java.io.File::delete);
			}
		}
	}

	// -----------------------------------------------------------------------
	// 18. 既存 terms aggregation の回帰テスト
	// -----------------------------------------------------------------------
	public void testTermsAggregation_RegressionAfterDateHistogram() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en")
				.field("category", FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"item\",\"category\":\"A\"}");
			search.addJson("{\"id\":\"2\",\"body\":\"item\",\"category\":\"A\"}");
			search.addJson("{\"id\":\"3\",\"body\":\"item\",\"category\":\"B\"}");
			search.commit();

			Map<String, Long> agg = search.aggregate("category", 10);
			assertEquals(2L, (long) agg.get("A"));
			assertEquals(1L, (long) agg.get("B"));
		}
	}
}
