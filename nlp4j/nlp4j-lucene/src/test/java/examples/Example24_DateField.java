/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package examples;

import java.util.List;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;
import nlp4j.lucene9.DateHistogramBucket;
import nlp4j.lucene9.DateHistogramInterval;

/**
 * Example24_DateField
 *
 * Demonstrates:
 *
 * 1. Adding DATE fields with the *_dt suffix 2. Searching automatically derived
 * calendar fields 3. YEAR and MONTH date histogram aggregations 4. Filling
 * missing periods with doc_count=0 5. Applying a Lucene query before date
 * histogram aggregation
 * @since 1.7.0.0
 */
public class Example24_DateField {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja") //
				.autoAnalyze(false) //
				.timeZone("Asia/Tokyo") //
				.build()) {

			// ------------------------------------------------------------
			// Add documents with DATE fields
			// ------------------------------------------------------------

			search.addJson("""
					{
					  "id": "1",
					  "body": "ニッサン EV のイベント",
					  "created_dt": "2023-06-01T10:00:00+09:00"
					}
					""");

			search.addJson("""
					{
					  "id": "2",
					  "body": "ニッサン e-POWER のイベント",
					  "created_dt": "2026-03-15T14:00:00+09:00"
					}
					""");

			search.addJson("""
					{
					  "id": "3",
					  "body": "トヨタ ハイブリッド車のイベント",
					  "created_dt": "2020-01-01T09:00:00+09:00"
					}
					""");

			search.commit();

			// ------------------------------------------------------------
			// 1. Search automatically derived calendar fields
			//
			// created_dt automatically creates:
			//
			// created_year_i
			// created_month_i
			// created_day_i
			// created_dow_i
			// created_hour_i
			// ------------------------------------------------------------

			System.out.println("=== Search: created_year_i:2026 ===");

			SearchResult[] results = //
					search.search("created_year_i:2026", 10); //

			for (SearchResult result : results) {
				System.out.println( //
						result.id + ": " + result.body);
			}

			System.out.println();

			System.out.println("=== Search: created_month_i:6 ===");

			results = search.search("created_month_i:6", 10);

			for (SearchResult result : results) { //
				System.out.println( //
						result.id + ": " + result.body); //
			}

			// ------------------------------------------------------------
			// 2. YEAR histogram
			//
			// Missing years are automatically included with count=0.
			// ------------------------------------------------------------

			System.out.println();
			System.out.println("=== Date Histogram: YEAR ===");

			List<DateHistogramBucket> yearBuckets = //
					search.dateHistogram( //
							"created_dt", //
							DateHistogramInterval.YEAR); //

			for (DateHistogramBucket bucket : yearBuckets) {

				System.out.println( //
						bucket.getKeyAsString() //
								+ " : " //
								+ bucket.getDocCount()); //
			}

			// ------------------------------------------------------------
			// 3. MONTH histogram
			//
			// The range is automatically determined from the oldest
			// and newest DATE values in the matching documents.
			// Missing months are returned with count=0.
			// ------------------------------------------------------------

			System.out.println();
			System.out.println("=== Date Histogram: MONTH ===");

			List<DateHistogramBucket> monthBuckets = //
					search.dateHistogram( //
							"created_dt", //
							DateHistogramInterval.MONTH); //

			for (DateHistogramBucket bucket : monthBuckets) {

				System.out.println( //
						bucket.getKeyAsString() //
								+ " : " //
								+ bucket.getDocCount()); //
			}

			// ------------------------------------------------------------
			// 4. YEAR histogram after applying a Lucene query
			//
			// Only documents matching "ニッサン" are considered.
			//
			// Nissan:
			// 2023 -> 1
			// 2024 -> 0
			// 2025 -> 0
			// 2026 -> 1
			//
			// Toyota's 2020 document does not affect min/max.
			// ------------------------------------------------------------

			System.out.println();
			System.out.println( //
					"=== Date Histogram: YEAR, query=ニッサン ==="); //

			List<DateHistogramBucket> nissanBuckets = //
					search.dateHistogram( //
							"created_dt", //
							DateHistogramInterval.YEAR, //
							"ニッサン");

			for (DateHistogramBucket bucket : nissanBuckets) {

				System.out.println( //
						bucket.getKeyAsString() //
								+ " : " //
								+ bucket.getDocCount()); //
			}
		}
	}
}