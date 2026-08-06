package nlp4j.lucene;

import junit.framework.TestCase;

public class LocalSearchTestCase extends TestCase {

	public void testSearch001() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京都は日本の都道府県のひとつです");
			search.add("2", "京都は日本の都市です。");
			search.add("3", "京都市には任天堂の本社があります");
			search.addJson("""
					{
					"id":"4",
					"body":"京都府は広いです"
					}
					""");
			search.commit();
			SearchResult[] results = search.search("京都", 10);
			System.out.println("size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
				System.out.println("result[" + n + "].score: " + results[n].score);
			}

			assertEquals(3, results.length);

		}
// Expected output
//		size: 3
//		result[0].id: 1
//		result[0].body: 京都は日本の都市です。
//		result[0].score: 0.1805949
//		result[1].id: 3
//		result[1].body: 京都府は広いです
//		result[1].score: 0.1805949
//		result[2].id: 2
//		result[2].body: 京都市には任天堂の本社があります
//		result[2].score: 0.16212496
	}

	public void testSearch002() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京都は日本の都道府県のひとつです");
			search.add("2", "京都は日本の都市です。");
			search.add("3", "京都市には任天堂の本社があります");

			search.add("3", "京都市には任天堂の本社があります"); // duplicated!

			search.commit();
			SearchResult[] results = search.search("京都", 10);
			System.out.println("size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
				System.out.println("result[" + n + "].score: " + results[n].score);
			}

			assertEquals(2, results.length);

		}
	}

	public void testSearch003() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京都です。1");
			search.add("2", "それは京都です。2");
			search.add("3", "京都です。3");
			search.commit();
			SearchResult[] results = search.search("京都", 10);
			System.out.println("size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
				System.out.println("result[" + n + "].score: " + results[n].score);
			}

			assertEquals(2, results.length);

		}
	}

	public void testSearch100() throws Exception {

		try (LocalSearch search = new LocalSearch("ja", 2)) {
			search.add("1_East", new float[] { 1.0f, 0.0f });
			search.add("2_North", new float[] { 0.0f, 1.0f });
			search.add("3_West", new float[] { -1.0f, 0.0f });
			search.add("4_South", new float[] { -1.0f, -1.0f });
			search.commit();
			SearchResult[] results = search.search(new float[] { 0.9f, 0.1f }, 10);
			System.out.println("size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
				System.out.println("result[" + n + "].score: " + results[n].score);
				System.out.println("---");
			}

			assertEquals(4, results.length);

		}
	}

	/**
	 * add(id, vector, fields) でフィールド付きベクトル文書を登録し、
	 * search(vector, limit, filters) で単一フィールドフィルターが動作することを確認する。
	 * category=technology を持つ文書のみがヒットすること。
	 */
	public void testVectorSearchWithFilter001() throws Exception {

		try (LocalSearch search = new LocalSearch("ja", 2)) {
			search.add("1_tech_east",  new float[] { 1.0f, 0.0f },
					java.util.Map.of("category", "technology"));
			search.add("2_tech_north", new float[] { 0.0f, 1.0f },
					java.util.Map.of("category", "technology"));
			search.add("3_travel_east", new float[] { 0.9f, 0.1f },
					java.util.Map.of("category", "travel"));
			search.add("4_travel_west",  new float[] { -1.0f, 0.0f },
					java.util.Map.of("category", "travel"));
			search.commit();

			// クエリベクトル (0.9, 0.1) は 1_tech_east に最近傍
			// category=technology でフィルターすると 1_tech_east, 2_tech_north の 2 件
			SearchResult[] results = search.search(
					new float[] { 0.9f, 0.1f }, 10,
					java.util.Map.of("category", "technology"));

			System.out.println("testVectorSearchWithFilter001 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].score: " + results[n].score);
			}

			assertEquals(2, results.length);
			assertEquals("1_tech_east", results[0].id);
		}
	}

	/**
	 * 複数フィルター（category + country）でベクトル検索が絞り込まれることを確認する。
	 */
	public void testVectorSearchWithFilter002() throws Exception {

		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.add("1", new float[] { 1.0f, 0.0f },
					java.util.Map.of("category", "technology", "country", "Japan"));
			search.add("2", new float[] { 0.9f, 0.2f },
					java.util.Map.of("category", "technology", "country", "USA"));
			search.add("3", new float[] { 0.8f, 0.3f },
					java.util.Map.of("category", "travel",     "country", "Japan"));
			search.add("4", new float[] { -1.0f, 0.0f },
					java.util.Map.of("category", "technology", "country", "Japan"));
			search.commit();

			// category=technology + country=Japan → id=1, id=4 の 2 件
			SearchResult[] results = search.search(
					new float[] { 0.9f, 0.1f }, 10,
					java.util.Map.of("category", "technology", "country", "Japan"));

			System.out.println("testVectorSearchWithFilter002 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
			}

			assertEquals(2, results.length);
		}
	}

	/**
	 * フィルターに一致する文書が存在しない場合、空の結果が返ることを確認する。
	 */
	public void testVectorSearchWithFilter003() throws Exception {

		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.add("1", new float[] { 1.0f, 0.0f },
					java.util.Map.of("category", "technology"));
			search.add("2", new float[] { 0.0f, 1.0f },
					java.util.Map.of("category", "technology"));
			search.commit();

			// category=travel は存在しない → 0 件
			SearchResult[] results = search.search(
					new float[] { 0.9f, 0.1f }, 10,
					java.util.Map.of("category", "travel"));

			System.out.println("testVectorSearchWithFilter003 size: " + results.length);
			assertEquals(0, results.length);
		}
	}

	/**
	 * addJson() で登録した追加フィールド（category）をフィールド検索できることを確認する。 category=技術
	 * を持つドキュメントのみが返ること。
	 */
	public void testFieldSearch001() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"Javaプログラミング入門","category":"技術"}
					""");
			search.addJson("""
					{"id":"3","body":"京都の寺院と歴史","category":"観光"}
					""");
			search.addJson("""
					{"id":"4","body":"機械学習の基礎","category":"技術"}
					""");
			search.commit();

			// category フィールドで "技術" を検索
			SearchResult[] results = search.search("category", "技術", 10);
			System.out.println("testFieldSearch001 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
				System.out.println("result[" + n + "].score: " + results[n].score);
			}

			// id=2, id=4 の 2 件のみヒットすること
			assertEquals(2, results.length);
		}
	}

	/**
	 * 複数の追加フィールド（category, country）をそれぞれフィールド検索できることを確認する。
	 */
	public void testFieldSearch002() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京タワーの説明","category":"観光","country":"Japan"}
					""");
			search.addJson("""
					{"id":"2","body":"パリの観光地","category":"観光","country":"France"}
					""");
			search.addJson("""
					{"id":"3","body":"東京のIT企業","category":"技術","country":"Japan"}
					""");
			search.addJson("""
					{"id":"4","body":"シリコンバレーのスタートアップ","category":"技術","country":"USA"}
					""");
			search.commit();

			// country=Japan で検索 → id=1, id=3 の 2 件
			SearchResult[] byJapan = search.search("country", "Japan", 10);
			System.out.println("testFieldSearch002 country=Japan size: " + byJapan.length);
			for (int n = 0; n < byJapan.length; n++) {
				System.out.println("result[" + n + "].id: " + byJapan[n].id);
				System.out.println("result[" + n + "].body: " + byJapan[n].body);
			}
			assertEquals(2, byJapan.length);

			// category=技術 で検索 → id=3, id=4 の 2 件
			SearchResult[] byTech = search.search("category", "技術", 10);
			System.out.println("testFieldSearch002 category=技術 size: " + byTech.length);
			for (int n = 0; n < byTech.length; n++) {
				System.out.println("result[" + n + "].id: " + byTech[n].id);
				System.out.println("result[" + n + "].body: " + byTech[n].body);
			}
			assertEquals(2, byTech.length);
		}
	}

	/**
	 * フィールド検索でヒットしない値を指定した場合、空の結果が返ることを確認する。
	 */
	public void testFieldSearch003() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"Javaプログラミング","category":"技術"}
					""");
			search.commit();

			// 存在しない category 値で検索 → 0 件
			SearchResult[] results = search.search("category", "スポーツ", 10);
			System.out.println("testFieldSearch003 size: " + results.length);
			assertEquals(0, results.length);
		}
	}

	/**
	 * addJson() で body フィールド検索と category フィールド検索を組み合わせて動作確認する。 body
	 * の全文検索と追加フィールド検索が共存できることを確認する。
	 */
	public void testFieldSearch004() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"東京のIT企業","category":"技術"}
					""");
			search.addJson("""
					{"id":"3","body":"大阪の観光地","category":"観光"}
					""");
			search.commit();

			// body フィールドで "東京" を全文検索 → id=1, id=2 の 2 件
			SearchResult[] byBody = search.search("東京", 10);
			System.out.println("testFieldSearch004 body=東京 size: " + byBody.length);
			assertEquals(2, byBody.length);

			// category フィールドで "観光" を検索 → id=1, id=3 の 2 件
			SearchResult[] byCategory = search.search("category", "観光", 10);
			System.out.println("testFieldSearch004 category=観光 size: " + byCategory.length);
			assertEquals(2, byCategory.length);
		}
	}

	/**
	 * searchJson() で term クエリ（keyword フィールド完全一致）が動作することを確認する。
	 */
	public void testSearchJson001() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"Javaプログラミング入門","category":"技術"}
					""");
			search.addJson("""
					{"id":"3","body":"京都の寺院と歴史","category":"観光"}
					""");
			search.commit();

			// term クエリで category=観光 を検索 → id=1, id=3 の 2 件
			SearchResult[] results = search.searchJson("""
					{"query":{"term":{"category":"観光"}},"size":10}
					""");
			System.out.println("testSearchJson001 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
			}
			assertEquals(2, results.length);
		}
	}

	/**
	 * searchJson() で match クエリ（全文検索）が動作することを確認する。
	 */
	public void testSearchJson002() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"東京のIT企業","category":"技術"}
					""");
			search.addJson("""
					{"id":"3","body":"大阪の観光地","category":"観光"}
					""");
			search.commit();

			// match クエリで text_ja=東京 を全文検索 → id=1, id=2 の 2 件
			SearchResult[] results = search.searchJson("""
					{"query":{"match":{"text_ja":"東京"}},"size":10}
					""");
			System.out.println("testSearchJson002 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
			}
			assertEquals(2, results.length);
		}
	}

	/**
	 * searchJson() で match_all クエリが動作することを確認する。
	 */
	public void testSearchJson003() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"大阪","category":"技術"}
					""");
			search.commit();

			// match_all で全件取得 → 2 件
			SearchResult[] results = search.searchJson("""
					{"query":{"match_all":{}},"size":10}
					""");
			System.out.println("testSearchJson003 size: " + results.length);
			assertEquals(2, results.length);
		}
	}

	/**
	 * searchByQuery() で全文検索のみ（filters なし）が動作することを確認する。
	 */
	public void testSearchByQuery001() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"東京のIT企業","category":"技術"}
					""");
			search.addJson("""
					{"id":"3","body":"大阪の観光地","category":"観光"}
					""");
			search.commit();

			// query=東京 のみ → id=1, id=2 の 2 件
			SearchResult[] results = search.searchByQuery("""
					{"query":"東京","limit":10}
					""");
			System.out.println("testSearchByQuery001 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
			}
			assertEquals(2, results.length);
		}
	}

	/**
	 * searchByQuery() で全文検索 + filters（keyword 絞り込み）が動作することを確認する。
	 */
	public void testSearchByQuery002() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"東京のIT企業","category":"技術"}
					""");
			search.addJson("""
					{"id":"3","body":"大阪の観光地","category":"観光"}
					""");
			search.commit();

			// query=東京 + category=観光 → id=1 の 1 件のみ
			SearchResult[] results = search.searchByQuery("""
					{"query":"東京","limit":10,"filters":{"category":"観光"}}
					""");
			System.out.println("testSearchByQuery002 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
			}
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * searchByQuery() で query 省略（match_all）+ filters が動作することを確認する。
	 */
	public void testSearchByQuery003() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"Javaプログラミング","category":"技術"}
					""");
			search.addJson("""
					{"id":"3","body":"大阪の観光地","category":"観光"}
					""");
			search.commit();

			// query 省略 + category=観光 → id=1, id=3 の 2 件
			SearchResult[] results = search.searchByQuery("""
					{"filters":{"category":"観光"},"limit":10}
					""");
			System.out.println("testSearchByQuery003 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
			}
			assertEquals(2, results.length);
		}
	}

	/**
	 * searchByQuery() で複数 filters が動作することを確認する。
	 */
	public void testSearchByQuery004() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京タワー","category":"観光","country":"Japan"}
					""");
			search.addJson("""
					{"id":"2","body":"パリ観光","category":"観光","country":"France"}
					""");
			search.addJson("""
					{"id":"3","body":"東京のIT","category":"技術","country":"Japan"}
					""");
			search.commit();

			// category=観光 + country=Japan → id=1 の 1 件のみ
			SearchResult[] results = search.searchByQuery("""
					{"filters":{"category":"観光","country":"Japan"},"limit":10}
					""");
			System.out.println("testSearchByQuery004 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
			}
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * search(query, limit, Map) オーバーロードで全文検索＋フィールド絞り込みが動作することを確認する。
	 */
	public void testSearchWithFilters001() throws Exception {

		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city in Japan.","category":"city","country":"Japan"}
					""");
			search.addJson(
					"""
							{"id":"2","body":"Nintendo is a video game company headquartered in Kyoto.","category":"company","country":"Japan"}
							""");
			search.addJson("""
					{"id":"3","body":"Paris is the capital city of France.","category":"city","country":"France"}
					""");
			search.commit();

			// "Kyoto" + category=company → id=2 の 1 件のみ
			SearchResult[] results = search.search("Kyoto", 10, java.util.Map.of("category", "company"));
			System.out.println("testSearchWithFilters001 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
				System.out.println("result[" + n + "].data: " + results[n].data);
			}
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	/**
	 * addJson() で追加したドキュメントの SearchResult.data に元 JSON が入ることを確認する。 add(id, body)
	 * で追加した場合は data が null であることも確認する。
	 */
	public void testSearchResultData001() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "通常の追加");
			search.addJson("""
					{"id":"2","body":"JSON追加","category":"技術"}
					""");
			search.commit();

			// id=1: add() → data は null（id フィールドの term 検索で取得）
			SearchResult[] r1 = search.search("id", "1", 10);
			System.out.println("testSearchResultData001 id=1 data: " + r1[0].data);
			assertNull(r1[0].data);

			// id=2: addJson() → data に元 JSON が格納されている
			SearchResult[] r2 = search.search("category", "技術", 10);
			System.out.println("testSearchResultData001 id=2 data: " + r2[0].data);
			assertNotNull(r2[0].data);
			assertTrue(r2[0].data.contains("\"id\":\"2\""));
		}
	}

	/**
	 * aggregateJson() で category フィールドの terms aggregation が動作することを確認する。 バケット件数と key
	 * の検証を行う。
	 */
	public void testAggregateJson001() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"Javaプログラミング","category":"技術"}
					""");
			search.addJson("""
					{"id":"3","body":"京都の寺院","category":"観光"}
					""");
			search.addJson("""
					{"id":"4","body":"機械学習入門","category":"技術"}
					""");
			search.addJson("""
					{"id":"5","body":"大阪の食文化","category":"観光"}
					""");
			search.commit();

			// category の全件集計 → 観光:3, 技術:2 の 2 バケット
			String json = search.aggregateJson("""
					{"field":"category","query":null,"size":10}
					""");
			System.out.println("testAggregateJson001: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			System.out.println(result);

			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");
			assertEquals(2, buckets.size());

			{
				// 先頭バケットは件数最多の "観光"
				assertEquals("観光", buckets.get(0).get("key").asString());
				assertEquals(3, buckets.get(0).get("doc_count").asInt());
			}
			{
				assertEquals("技術", buckets.get(1).get("key").asString());
				assertEquals(2, buckets.get(1).get("doc_count").asInt());
			}

		}
	}

	/**
	 * aggregateJson() で全文検索クエリで絞り込んだ上での集計が動作することを確認する。 query=東京 で絞り込むと category=観光,
	 * 技術 の 2 バケットがヒットすること。
	 */
	public void testAggregateJson002() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.addJson("""
					{"id":"2","body":"東京のIT企業","category":"技術"}
					""");
			search.addJson("""
					{"id":"3","body":"大阪の観光地","category":"観光"}
					""");
			search.commit();

			// query=東京 で絞り込んだ上で category 集計 → 観光:1, 技術:1 の 2 バケット
			String json = search.aggregateJson("""
					{"field":"category","query":"東京","size":10}
					""");
			System.out.println("testAggregateJson002: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);

			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");
			assertEquals(2, buckets.size());
		}
	}

	/**
	 * aggregateJson() で size パラメータによるバケット数制限が動作することを確認する。
	 */
	public void testAggregateJson003() throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"doc1","category":"A"}
					""");
			search.addJson("""
					{"id":"2","body":"doc2","category":"B"}
					""");
			search.addJson("""
					{"id":"3","body":"doc3","category":"C"}
					""");
			search.addJson("""
					{"id":"4","body":"doc4","category":"D"}
					""");
			search.addJson("""
					{"id":"5","body":"doc4","category":"D"}
					""");
			search.addJson("""
					{"id":"6","body":"doc4","category":"D"}
					""");
			search.addJson("""
					{"id":"7","body":"doc4","category":"D"}
					""");
			search.commit();

			// size=2 で上位 2 バケットのみ返す
			String json = search.aggregateJson("""
					{"field":"category","size":2}
					""");
			System.out.println("testAggregateJson003: " + json);
			{
				nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
				nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");
				{
					assertEquals(2, buckets.size());

				}
				{
					assertEquals(4, buckets.get(0).get("doc_count").getAsInt());

				}
			}
		}
	}

	/**
	 * フィールド検索（en）: "Kyoto" + category=company → id=2 のみ。
	 */
	public void testFieldSearch100() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
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
					  "body": "Sony is a video game company headquartered in Tokyo.",
					  "category": "company",
					  "country": "Japan"
					}
					""");
			search.commit();

			SearchResult[] results = search.search("Kyoto", 10, java.util.Map.of("category", "company"));

			assertEquals(1, results.length);
			assertEquals("2", results[0].id);
		}
	}

	/**
	 * aggregation（en）: category の terms aggregation → city:2, company:1。
	 */
	public void testAggregation001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "Kyoto is a historic city in Japan.",
					  "category": "city"
					}
					""");
			search.addJson("""
					{
					  "id": "2",
					  "body": "Tokyo is the capital city of Japan.",
					  "category": "city"
					}
					""");
			search.addJson("""
					{
					  "id": "3",
					  "body": "Nintendo is a video game company headquartered in Kyoto.",
					  "category": "company"
					}
					""");
			search.commit();

			String json = search.aggregateJson("""
					{
					  "field": "category",
					  "size": 10
					}
					""");

			System.out.println(json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");

			// city: 2, company: 1 の 2 バケット
			assertEquals(2, buckets.size());
			assertEquals("city", buckets.get(0).get("key").asString());
			assertEquals(2, buckets.get(0).get("doc_count").asInt());
			assertEquals("company", buckets.get(1).get("key").asString());
			assertEquals(1, buckets.get(1).get("doc_count").asInt());
		}
	}

	/**
	 * 最終設計イメージの確認: search() と aggregateJson() を同一インスタンスで組み合わせて使えること。
	 */
	public void testDesignImage001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
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
			search.commit();

			// search: "Kyoto" + category=company → id=2 のみ
			SearchResult[] results = search.search("Kyoto", 10, java.util.Map.of("category", "company"));
			assertEquals(1, results.length);
			assertEquals("2", results[0].id);

			// aggregateJson: "Kyoto" で絞り込んだ上で category 集計 → city:1, company:1
			String agg = search.aggregateJson("""
					{
					  "field": "category",
					  "query": "Kyoto",
					  "size": 10
					}
					""");
			System.out.println(agg);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(agg);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");
			assertEquals("city", buckets.get(0).get("key").asString());
			assertEquals(1, buckets.get(0).get("doc_count").asInt());
		}
	}

	/**
	 * aggregateJson() で filters（keyword 絞り込み）を使った terms aggregation が動作することを確認する。
	 * query=東京 + source=news で絞り込んだ上での category 集計。
	 */
	public void testAggregateJson_WithFilters001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京のAI企業について","category":"technology","country":"Japan","source":"news"}
					""");
			search.addJson("""
					{"id":"2","body":"東京の観光スポット","category":"travel","country":"Japan","source":"blog"}
					""");
			search.addJson("""
					{"id":"3","body":"大阪のIT企業","category":"technology","country":"Japan","source":"news"}
					""");
			search.commit();

			// query=東京 + source=news → id=1 のみ → category=technology:1
			String json = search.aggregateJson("""
					{"field":"category","query":"東京","size":10,"filters":{"source":"news"}}
					""");

			System.out.println("testAggregateJson_WithFilters001: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			{
// v1				nlp4j.json.JsonNode buckets = result.get("buckets");
// v1			assertEquals(1, buckets.size());
// v1				assertEquals("technology", buckets.get(0).get("key").asString());
// v1				assertEquals(1, buckets.get(0).get("count").asInt());
			}
			{
				nlp4j.json.JsonNode aggregations = result.get("aggregations");
				assertEquals(1, aggregations.size());
				nlp4j.json.JsonNode values = aggregations.get("values");
				assertEquals(3, values.size()); //
				nlp4j.json.JsonNode buckets = values.get("buckets");
				System.out.println(buckets);
				assertEquals("technology", buckets.get(0).get("key").asString());
				assertEquals(1, buckets.get(0).get("doc_count").asInt());
			}

		}
	}

	/**
	 * aggregateJson() で filters のみ（query なし）の terms aggregation が動作することを確認する。
	 * country=Japan に絞り込んだ上での category 集計。
	 */
	public void testAggregateJson_WithFilters002() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"travel","country":"Japan"}
					""");
			search.addJson("""
					{"id":"2","body":"東京のIT企業","category":"technology","country":"Japan"}
					""");
			search.addJson("""
					{"id":"3","body":"パリの観光地","category":"travel","country":"France"}
					""");
			search.commit();

			// filters のみ（query なし）: country=Japan → id=1, id=2 を対象に category 集計
			String json = search.aggregateJson("""
					{"field":"category","size":10,"filters":{"country":"Japan"}}
					""");
			System.out.println("testAggregateJson_WithFilters002: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");

			// travel:1, technology:1 の 2 バケット（France の travel は除外）
			assertEquals(2, buckets.size());
		}
	}

	/**
	 * aggregateJson() で複数 filters の terms aggregation が動作することを確認する。
	 * 調査結果の使用例と同じパターン。
	 */
	public void testAggregateJson_WithFilters003() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京のAI企業について","category":"technology","country":"Japan","source":"news"}
					""");
			search.addJson("""
					{"id":"2","body":"東京の観光スポット","category":"travel","country":"Japan","source":"blog"}
					""");
			search.addJson("""
					{"id":"3","body":"大阪のIT企業","category":"technology","country":"Japan","source":"news"}
					""");
			search.commit();

			// query=東京 + country=Japan + source=news → id=1 のみ → category=technology:1
			String json = search.aggregateJson("""
					{"field":"category","query":"東京","size":10,"filters":{"country":"Japan","source":"news"}}
					""");
			System.out.println("testAggregateJson_WithFilters003: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");

			assertEquals(1, buckets.size());
			assertEquals("technology", buckets.get(0).get("key").asString());
			assertEquals(1, buckets.get(0).get("doc_count").asInt());
		}
	}

	/**
	 * searchResponseJson() が OpenSearch 形式のレスポンス全体（hits + aggregations）を返すことを確認する。
	 */
	public void testSearchResponseJson001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city in Japan.","category":"city","country":"Japan"}
					""");
			search.addJson(
					"""
							{"id":"2","body":"Nintendo is a video game company headquartered in Kyoto.","category":"company","country":"Japan"}
							""");
			search.addJson("""
					{"id":"3","body":"Paris is the capital city of France.","category":"city","country":"France"}
					""");
			search.commit();

			// hits + aggregations を同時に取得
			String responseJson = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {"match": {"text_en": "Kyoto"}},
					  "aggs": {
					    "values": {"terms": {"field": "category", "size": 10}}
					  }
					}
					""");
			System.out.println("testSearchResponseJson001: " + responseJson);

			nlp4j.json.JsonNode response = nlp4j.json.JsonNode.parse(responseJson);

			// hits が含まれること
			nlp4j.json.JsonNode hits = response.get("hits").get("hits");
			assertEquals(2, hits.size()); // Kyoto を含む id=1, id=2

			// aggregations が含まれること（searchJson() では失われる）
			nlp4j.json.JsonNode buckets = response.get("aggregations").get("values").get("buckets");
			assertEquals(2, buckets.size()); // city:1, company:1
		}
	}

	/**
	 * addJson() で JSON 配列フィールド（keywords）を登録し、
	 * aggregateJson() で各キーワードの doc_count が正しく集計されることを確認する。
	 *
	 * <p>期待結果:</p>
	 * <pre>
	 * keywords の集計:
	 *   "これ"   → 2件 (id001, id002)
	 *   "テスト" → 2件 (id001, id002)
	 *   "Java"   → 1件 (id003)
	 *   "だ"     → 1件 (id001)
	 *   "は"     → 1件 (id001)
	 *   "サンプル" → 1件 (id003)
	 *   "別"     → 1件 (id002)
	 * </pre>
	 */
	public void testAddJsonArray001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{
					  "id": "id001",
					  "text": "これはテキストです",
					  "keywords": ["これ", "は", "テスト", "だ"]
					}
					""");
			search.addJson("""
					{
					  "id": "id002",
					  "text": "これは別のテストです",
					  "keywords": ["これ", "テスト", "別"]
					}
					""");
			search.addJson("""
					{
					  "id": "id003",
					  "text": "Javaのサンプルです",
					  "keywords": ["Java", "サンプル"]
					}
					""");
			search.commit();

			String json = search.aggregateJson("""
					{
					  "name": "keyword_counts",
					  "field": "keywords",
					  "size": 100
					}
					""");

			System.out.println("testAddJsonArray001: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("keyword_counts").get("buckets");

			// 7種類のキーワードが集計されること
			assertEquals(7, buckets.size());

			// "これ" と "テスト" が doc_count=2 であること
			boolean foundKore = false;
			boolean foundTest = false;
			for (nlp4j.json.JsonNode bucket : buckets.asList()) {
				String key = bucket.get("key").asString();
				int count = bucket.get("doc_count").asInt();
				if ("これ".equals(key)) {
					assertEquals(2, count);
					foundKore = true;
				}
				if ("テスト".equals(key)) {
					assertEquals(2, count);
					foundTest = true;
				}
			}
			assertTrue("'これ' bucket not found", foundKore);
			assertTrue("'テスト' bucket not found", foundTest);
		}
	}

	/**
	 * addJson() で "text" フィールドが "body" の代替として動作することを確認する。
	 * 全文検索で text フィールドの内容がヒットすること。
	 */
	public void testAddJsonTextField001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{
					  "id": "1",
					  "text": "東京の観光スポット",
					  "category": "観光"
					}
					""");
			search.addJson("""
					{
					  "id": "2",
					  "body": "京都の寺院",
					  "category": "観光"
					}
					""");
			search.commit();

			// "text" で登録した id=1 も全文検索でヒットすること
			SearchResult[] results = search.search("東京", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);

			// category フィールド検索は両方ヒット
			SearchResult[] byCategory = search.search("category", "観光", 10);
			assertEquals(2, byCategory.length);
		}
	}


	// =========================================================
	// MultiValued Field - Aggregation テスト
	// =========================================================

	/**
	 * addJson() で JSON 配列フィールド（tags）を MultiValued keyword として登録し、
	 * aggregateJson() で各タグの doc_count が正しく集計されることを確認する。
	 *
	 * <pre>
	 * ドキュメント:
	 *   id=1  tags=["city","tourism","Japan"]
	 *   id=2  tags=["company","Japan"]
	 *   id=3  tags=["city","capital","Japan"]
	 *   id=4  tags=["city","tourism","France"]
	 *   id=5  tags=["company","Japan"]
	 *
	 * 期待結果（件数降順）:
	 *   Japan   = 4
	 *   city    = 3
	 *   company = 2
	 *   tourism = 2
	 *   capital = 1
	 *   France  = 1
	 * </pre>
	 */
	public void testMultiValuedAggregation001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","tags":["city","tourism","Japan"]}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is headquartered in Kyoto.","tags":["company","Japan"]}
					""");
			search.addJson("""
					{"id":"3","body":"Tokyo is the capital city of Japan.","tags":["city","capital","Japan"]}
					""");
			search.addJson("""
					{"id":"4","body":"Paris is a beautiful city in France.","tags":["city","tourism","France"]}
					""");
			search.addJson("""
					{"id":"5","body":"Sony is a Japanese company based in Tokyo.","tags":["company","Japan"]}
					""");
			search.commit();

			String json = search.aggregateJson("""
					{"name":"tags","field":"tags","size":10}
					""");
			System.out.println("testMultiValuedAggregation001: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("tags").get("buckets");

			// 6 種類のタグが集計されること
			assertEquals(6, buckets.size());

			// 先頭バケット: Japan = 4
			assertEquals("Japan", buckets.get(0).get("key").asString());
			assertEquals(4, buckets.get(0).get("doc_count").asInt());

			// 2番目: city = 3
			assertEquals("city", buckets.get(1).get("key").asString());
			assertEquals(3, buckets.get(1).get("doc_count").asInt());
		}
	}

	/**
	 * aggregateJson() で全文検索クエリで絞り込んだ上での MultiValued フィールド集計が
	 * 正しく動作することを確認する。
	 *
	 * <pre>
	 * query="Kyoto" → id=1（tags=city,tourism,Japan）, id=2（tags=company,Japan）
	 * 期待結果:
	 *   Japan   = 2
	 *   city    = 1
	 *   tourism = 1
	 *   company = 1
	 * </pre>
	 */
	public void testMultiValuedAggregation002() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","tags":["city","tourism","Japan"]}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is headquartered in Kyoto.","tags":["company","Japan"]}
					""");
			search.addJson("""
					{"id":"3","body":"Tokyo is the capital city of Japan.","tags":["city","capital","Japan"]}
					""");
			search.commit();

			String json = search.aggregateJson("""
					{"name":"tags","field":"tags","query":"Kyoto","size":10}
					""");
			System.out.println("testMultiValuedAggregation002: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("tags").get("buckets");

			// Kyoto を含む id=1, id=2 のタグ: Japan=2, city=1, tourism=1, company=1 → 4 バケット
			assertEquals(4, buckets.size());

			// Japan が最多 (2件)
			assertEquals("Japan", buckets.get(0).get("key").asString());
			assertEquals(2, buckets.get(0).get("doc_count").asInt());
		}
	}

	/**
	 * aggregateJson() で size パラメータが MultiValued フィールドの集計にも適用されることを確認する。
	 * size=3 で上位 3 バケットのみ返ること。
	 */
	public void testMultiValuedAggregation003() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","tags":["city","tourism","Japan"]}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is headquartered in Kyoto.","tags":["company","Japan"]}
					""");
			search.addJson("""
					{"id":"3","body":"Tokyo is the capital city of Japan.","tags":["city","capital","Japan"]}
					""");
			search.addJson("""
					{"id":"4","body":"Paris is a beautiful city in France.","tags":["city","tourism","France"]}
					""");
			search.addJson("""
					{"id":"5","body":"Sony is a Japanese company based in Tokyo.","tags":["company","Japan"]}
					""");
			search.commit();

			// size=3 → 上位 3 バケット（Japan=4, city=3, tourism=2 or company=2）のみ
			String json = search.aggregateJson("""
					{"name":"tags","field":"tags","size":3}
					""");
			System.out.println("testMultiValuedAggregation003: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("tags").get("buckets");

			assertEquals(3, buckets.size());
			// 先頭は Japan=4
			assertEquals("Japan", buckets.get(0).get("key").asString());
			assertEquals(4, buckets.get(0).get("doc_count").asInt());
		}
	}

	// =========================================================
	// MultiValued Field - Filter テスト
	// =========================================================

	/**
	 * addJson() で JSON 配列フィールド（tags）を登録し、
	 * search(field, value, limit) で MultiValued フィールドの単一値フィルターが
	 * 正しく動作することを確認する。
	 *
	 * <pre>
	 * tags="Japan" → id=1,2,3,5 の 4件（id=4 は France のみ）
	 * tags="city"  → id=1,3,4 の 3件
	 * </pre>
	 */
	public void testMultiValuedFilter001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","tags":["city","tourism","Japan"]}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is headquartered in Kyoto.","tags":["company","Japan"]}
					""");
			search.addJson("""
					{"id":"3","body":"Tokyo is the capital city of Japan.","tags":["city","capital","Japan"]}
					""");
			search.addJson("""
					{"id":"4","body":"Paris is a beautiful city in France.","tags":["city","tourism","France"]}
					""");
			search.addJson("""
					{"id":"5","body":"Sony is a Japanese company based in Tokyo.","tags":["company","Japan"]}
					""");
			search.commit();

			// tags="Japan" → 4件
			SearchResult[] byJapan = search.search("tags", "Japan", 10);
			System.out.println("testMultiValuedFilter001 tags=Japan size: " + byJapan.length);
			assertEquals(4, byJapan.length);

			// tags="city" → 3件
			SearchResult[] byCity = search.search("tags", "city", 10);
			System.out.println("testMultiValuedFilter001 tags=city size: " + byCity.length);
			assertEquals(3, byCity.length);

			// tags="tourism" → 2件
			SearchResult[] byTourism = search.search("tags", "tourism", 10);
			System.out.println("testMultiValuedFilter001 tags=tourism size: " + byTourism.length);
			assertEquals(2, byTourism.length);

			// tags="capital" → 1件
			SearchResult[] byCapital = search.search("tags", "capital", 10);
			System.out.println("testMultiValuedFilter001 tags=capital size: " + byCapital.length);
			assertEquals(1, byCapital.length);
			assertEquals("3", byCapital[0].id);

			// tags="sports"（存在しない値）→ 0件
			SearchResult[] byNone = search.search("tags", "sports", 10);
			assertEquals(0, byNone.length);
		}
	}

	/**
	 * search(query, limit, filters) で全文検索と MultiValued フィールドフィルターの
	 * 組み合わせが正しく動作することを確認する。
	 *
	 * <pre>
	 * "Kyoto" + tags="Japan" → id=1,2 の 2件
	 * "Japan" + tags="city" → id=3 の 1件（Japan を含む body で tags に city を持つ）
	 * "Tokyo" + tags="company" → id=5 の 1件
	 * </pre>
	 */
	public void testMultiValuedFilter002() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","tags":["city","tourism","Japan"]}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is headquartered in Kyoto.","tags":["company","Japan"]}
					""");
			search.addJson("""
					{"id":"3","body":"Tokyo is the capital city of Japan.","tags":["city","capital","Japan"]}
					""");
			search.addJson("""
					{"id":"4","body":"Paris is a beautiful city in France.","tags":["city","tourism","France"]}
					""");
			search.addJson("""
					{"id":"5","body":"Sony is a Japanese company based in Tokyo.","tags":["company","Japan"]}
					""");
			search.commit();

			// "Kyoto" + tags="Japan" → id=1, id=2 の 2件
			SearchResult[] r1 = search.search("Kyoto", 10, java.util.Map.of("tags", "Japan"));
			System.out.println("testMultiValuedFilter002 Kyoto+Japan size: " + r1.length);
			assertEquals(2, r1.length);

			// "Tokyo" + tags="company" → id=5 の 1件
			// （id=3 は Tokyo を含むが tags に company がない）
			SearchResult[] r2 = search.search("Tokyo", 10, java.util.Map.of("tags", "company"));
			System.out.println("testMultiValuedFilter002 Tokyo+company size: " + r2.length);
			assertEquals(1, r2.length);
			assertEquals("5", r2[0].id);

			// "city" + tags="France" → id=4 の 1件
			SearchResult[] r3 = search.search("city", 10, java.util.Map.of("tags", "France"));
			System.out.println("testMultiValuedFilter002 city+France size: " + r3.length);
			assertEquals(1, r3.length);
			assertEquals("4", r3[0].id);
		}
	}

	/**
	 * searchResponseJson() で bool/filter に複数 term を指定することで、
	 * MultiValued フィールドの値を AND 条件で絞り込めることを確認する。
	 *
	 * <pre>
	 * tags="Japan" AND tags="city"    → id=1,3 の 2件
	 * tags="Japan" AND tags="tourism" → id=1 の 1件（id=4 は tourism だが France）
	 * tags="Japan" AND tags="capital" → id=3 の 1件
	 * </pre>
	 */
	public void testMultiValuedFilter003() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","tags":["city","tourism","Japan"]}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is headquartered in Kyoto.","tags":["company","Japan"]}
					""");
			search.addJson("""
					{"id":"3","body":"Tokyo is the capital city of Japan.","tags":["city","capital","Japan"]}
					""");
			search.addJson("""
					{"id":"4","body":"Paris is a beautiful city in France.","tags":["city","tourism","France"]}
					""");
			search.addJson("""
					{"id":"5","body":"Sony is a Japanese company based in Tokyo.","tags":["company","Japan"]}
					""");
			search.commit();

			// tags="Japan" AND tags="city" → id=1, id=3 の 2件
			String resp1 = search.searchResponseJson(
					"{\"size\":10,\"query\":{\"bool\":{\"filter\":["
					+ "{\"term\":{\"tags\":\"Japan\"}},"
					+ "{\"term\":{\"tags\":\"city\"}}"
					+ "]}}}");
			System.out.println("testMultiValuedFilter003 Japan+city: " + resp1);
			nlp4j.json.JsonNode r1 = nlp4j.json.JsonNode.parse(resp1);
			assertEquals(2, r1.get("hits").get("hits").size());

			// tags="Japan" AND tags="tourism" → id=1 の 1件
			// （id=4 は tourism を持つが Japan を持たない）
			String resp2 = search.searchResponseJson(
					"{\"size\":10,\"query\":{\"bool\":{\"filter\":["
					+ "{\"term\":{\"tags\":\"Japan\"}},"
					+ "{\"term\":{\"tags\":\"tourism\"}}"
					+ "]}}}");
			System.out.println("testMultiValuedFilter003 Japan+tourism: " + resp2);
			nlp4j.json.JsonNode r2 = nlp4j.json.JsonNode.parse(resp2);
			assertEquals(1, r2.get("hits").get("hits").size());
			assertEquals("1", r2.get("hits").get("hits").get(0).get("_source").get("id").asString());

			// tags="Japan" AND tags="capital" → id=3 の 1件
			String resp3 = search.searchResponseJson(
					"{\"size\":10,\"query\":{\"bool\":{\"filter\":["
					+ "{\"term\":{\"tags\":\"Japan\"}},"
					+ "{\"term\":{\"tags\":\"capital\"}}"
					+ "]}}}");
			System.out.println("testMultiValuedFilter003 Japan+capital: " + resp3);
			nlp4j.json.JsonNode r3 = nlp4j.json.JsonNode.parse(resp3);
			assertEquals(1, r3.get("hits").get("hits").size());
			assertEquals("3", r3.get("hits").get("hits").get(0).get("_source").get("id").asString());
		}
	}


}
