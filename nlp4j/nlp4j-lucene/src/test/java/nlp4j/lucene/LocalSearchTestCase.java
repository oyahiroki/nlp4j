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
			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10);
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
	 * add(id, vector, fields) でフィールド付きベクトル文書を登録し、 searchVector(vector, limit,
	 * filters) で単一フィールドフィルターが動作することを確認する。 category=technology を持つ文書のみがヒットすること。
	 */
	public void testVectorSearchWithFilter001() throws Exception {

		try (LocalSearch search = new LocalSearch("ja", 2)) {
			search.add("1_tech_east", new float[] { 1.0f, 0.0f }, java.util.Map.of("category", "technology"));
			search.add("2_tech_north", new float[] { 0.0f, 1.0f }, java.util.Map.of("category", "technology"));
			search.add("3_travel_east", new float[] { 0.9f, 0.1f }, java.util.Map.of("category", "travel"));
			search.add("4_travel_west", new float[] { -1.0f, 0.0f }, java.util.Map.of("category", "travel"));
			search.commit();

			// クエリベクトル (0.9, 0.1) は 1_tech_east に最近傍
			// category=technology でフィルターすると 1_tech_east, 2_tech_north の 2 件
			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10,
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
			search.add("1", new float[] { 1.0f, 0.0f }, java.util.Map.of("category", "technology", "country", "Japan"));
			search.add("2", new float[] { 0.9f, 0.2f }, java.util.Map.of("category", "technology", "country", "USA"));
			search.add("3", new float[] { 0.8f, 0.3f }, java.util.Map.of("category", "travel", "country", "Japan"));
			search.add("4", new float[] { -1.0f, 0.0f },
					java.util.Map.of("category", "technology", "country", "Japan"));
			search.commit();

			// category=technology + country=Japan → id=1, id=4 の 2 件
			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10,
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
			search.add("1", new float[] { 1.0f, 0.0f }, java.util.Map.of("category", "technology"));
			search.add("2", new float[] { 0.0f, 1.0f }, java.util.Map.of("category", "technology"));
			search.commit();

			// category=travel は存在しない → 0 件
			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10,
					java.util.Map.of("category", "travel"));

			System.out.println("testVectorSearchWithFilter003 size: " + results.length);
			assertEquals(0, results.length);
		}
	}

	/**
	 * addJson() で登録した追加フィールド（category）をLucene Queryで検索できることを確認する。 category=技術
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

			// Lucene Query で category=技術 を検索
			SearchResult[] results = search.search("category:技術", 10);
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
	 * 複数の追加フィールド（category, country）をそれぞれLucene Queryで検索できることを確認する。
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
			SearchResult[] byJapan = search.search("country:Japan", 10);
			System.out.println("testFieldSearch002 country=Japan size: " + byJapan.length);
			for (int n = 0; n < byJapan.length; n++) {
				System.out.println("result[" + n + "].id: " + byJapan[n].id);
				System.out.println("result[" + n + "].body: " + byJapan[n].body);
			}
			assertEquals(2, byJapan.length);

			// category=技術 で検索 → id=3, id=4 の 2 件
			SearchResult[] byTech = search.search("category:技術", 10);
			System.out.println("testFieldSearch002 category=技術 size: " + byTech.length);
			for (int n = 0; n < byTech.length; n++) {
				System.out.println("result[" + n + "].id: " + byTech[n].id);
				System.out.println("result[" + n + "].body: " + byTech[n].body);
			}
			assertEquals(2, byTech.length);
		}
	}

	/**
	 * Lucene Query でヒットしない値を指定した場合、空の結果が返ることを確認する。
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
			SearchResult[] results = search.search("category:スポーツ", 10);
			System.out.println("testFieldSearch003 size: " + results.length);
			assertEquals(0, results.length);
		}
	}

	/**
	 * addJson() で body 全文検索と category Lucene Query を組み合わせて動作確認する。
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

			// category=観光 を Lucene Query で検索 → id=1, id=3 の 2 件
			SearchResult[] byCategory = search.search("category:観光", 10);
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

			// match クエリで body=東京 を全文検索 → id=1, id=2 の 2 件
			SearchResult[] results = search.searchJson("""
					{"query":{"match":{"body":"東京"}},"size":10}
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

			// id=1: add() → data は null（Lucene Query で id:1 を検索）
			SearchResult[] r1 = search.search("id:1", 10);
			System.out.println("testSearchResultData001 id=1 data: " + r1[0].data);
			assertNull(r1[0].data);

			// id=2: addJson() → data に元 JSON が格納されている
			SearchResult[] r2 = search.search("category:技術", 10);
			System.out.println("testSearchResultData001 id=2 data: " + r2[0].data);
			assertNotNull(r2[0].data);
			assertTrue(r2[0].data.contains("\"id\":\"2\""));
		}
	}

	/**
	 * addJson() で vector を含む JSON を登録した場合、 SearchResult.data（stored field）に vector
	 * が含まれないことを確認する。
	 *
	 * <p>
	 * KNN vector は Lucene 専用フィールドとして保存されるため、 data stored field
	 * への二重保存はストレージ効率上不要であり、除外される。
	 * </p>
	 */
	public void testSearchResultDataNoVector001() throws Exception {
		try (LocalSearch search = new LocalSearch("en", 2)) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","vector":[1.0,0.0],"category":"city"}
					""");
			search.commit();

			SearchResult[] results = search.search("category:city", 10);
			System.out.println("testSearchResultDataNoVector001 data: " + results[0].data);

			assertNotNull(results[0].data);
			// data に "category" は含まれること
			assertTrue(results[0].data.contains("\"category\""));
			// data に "vector" は含まれないこと
			assertFalse("data field must not contain vector", results[0].data.contains("\"vector\""));
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
					  "query": {"match": {"body": "Kyoto"}},
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
	 * addJson() で JSON 配列フィールド（keywords）を登録し、 aggregateJson() で各キーワードの doc_count
	 * が正しく集計されることを確認する。
	 *
	 * <p>
	 * 期待結果:
	 * </p>
	 * 
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
	 * addJson() で "text" フィールドを独立した全文検索フィールドとして 登録できることを確認する。 LocalSearch("ja") では
	 * JapaneseAnalyzer で解析され、 デフォルト検索対象にも含まれる。
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
			SearchResult[] byCategory = search.search("category:観光", 10);
			assertEquals(2, byCategory.length);
		}
	}

	// =========================================================
	// MultiValued Field - Aggregation テスト
	// =========================================================

	/**
	 * addJson() で JSON 配列フィールド（tags）を MultiValued keyword として登録し、 aggregateJson()
	 * で各タグの doc_count が正しく集計されることを確認する。
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
	 * aggregateJson() で全文検索クエリで絞り込んだ上での MultiValued フィールド集計が 正しく動作することを確認する。
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
	 * aggregateJson() で size パラメータが MultiValued フィールドの集計にも適用されることを確認する。 size=3 で上位
	 * 3 バケットのみ返ること。
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
	 * addJson() で JSON 配列フィールド（tags）を登録し、Lucene Query で MultiValued フィールドの単一値フィルターが
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

			// tags:Japan → 4件
			SearchResult[] byJapan = search.search("tags:Japan", 10);
			System.out.println("testMultiValuedFilter001 tags=Japan size: " + byJapan.length);
			assertEquals(4, byJapan.length);

			// tags:city → 3件
			SearchResult[] byCity = search.search("tags:city", 10);
			System.out.println("testMultiValuedFilter001 tags=city size: " + byCity.length);
			assertEquals(3, byCity.length);

			// tags:tourism → 2件
			SearchResult[] byTourism = search.search("tags:tourism", 10);
			System.out.println("testMultiValuedFilter001 tags=tourism size: " + byTourism.length);
			assertEquals(2, byTourism.length);

			// tags:capital → 1件
			SearchResult[] byCapital = search.search("tags:capital", 10);
			System.out.println("testMultiValuedFilter001 tags=capital size: " + byCapital.length);
			assertEquals(1, byCapital.length);
			assertEquals("3", byCapital[0].id);

			// tags:sports（存在しない値）→ 0件
			SearchResult[] byNone = search.search("tags:sports", 10);
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
	 * searchResponseJson() で bool/filter に複数 term を指定することで、 MultiValued フィールドの値を
	 * AND 条件で絞り込めることを確認する。
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
			String resp1 = search.searchResponseJson("{\"size\":10,\"query\":{\"bool\":{\"filter\":["
					+ "{\"term\":{\"tags\":\"Japan\"}}," + "{\"term\":{\"tags\":\"city\"}}" + "]}}}");
			System.out.println("testMultiValuedFilter003 Japan+city: " + resp1);
			nlp4j.json.JsonNode r1 = nlp4j.json.JsonNode.parse(resp1);
			assertEquals(2, r1.get("hits").get("hits").size());

			// tags="Japan" AND tags="tourism" → id=1 の 1件
			// （id=4 は tourism を持つが Japan を持たない）
			String resp2 = search.searchResponseJson("{\"size\":10,\"query\":{\"bool\":{\"filter\":["
					+ "{\"term\":{\"tags\":\"Japan\"}}," + "{\"term\":{\"tags\":\"tourism\"}}" + "]}}}");
			System.out.println("testMultiValuedFilter003 Japan+tourism: " + resp2);
			nlp4j.json.JsonNode r2 = nlp4j.json.JsonNode.parse(resp2);
			assertEquals(1, r2.get("hits").get("hits").size());
			assertEquals("1", r2.get("hits").get("hits").get(0).get("_source").get("id").asString());

			// tags="Japan" AND tags="capital" → id=3 の 1件
			String resp3 = search.searchResponseJson("{\"size\":10,\"query\":{\"bool\":{\"filter\":["
					+ "{\"term\":{\"tags\":\"Japan\"}}," + "{\"term\":{\"tags\":\"capital\"}}" + "]}}}");
			System.out.println("testMultiValuedFilter003 Japan+capital: " + resp3);
			nlp4j.json.JsonNode r3 = nlp4j.json.JsonNode.parse(resp3);
			assertEquals(1, r3.get("hits").get("hits").size());
			assertEquals("3", r3.get("hits").get("hits").get(0).get("_source").get("id").asString());
		}
	}

	// =========================================================
	// 形態素解析（Kuromoji）- word.* フィールドテスト
	// =========================================================

	/**
	 * add(id, body) で日本語テキストを登録すると、KuromojiAnnotator が自動実行され word.verb
	 * フィールドに動詞の原形が登録されることを確認する。
	 *
	 * <pre>
	* 入力: "私は歩いて学校に行きました。"
	* 期待:
	*   word.verb = ["歩く", "行く"]   （活用形 → 原形に変換済み）
	*   word.noun = ["私", "学校"]
	*   word      = ["私", "学校", "歩く", "行く"]
	 * </pre>
	 */
	public void testMorphologicalAnalysis001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "私は歩いて学校に行きました。");
			search.commit();

			// word.verb = ["歩く", "行く"] の 2 件が集計されること
			String verbJson = search.aggregateJson("""
					{"field":"word.verb","size":10}
					""");
			System.out.println("testMorphologicalAnalysis001 word.verb: " + verbJson);

			nlp4j.json.JsonNode verbResult = nlp4j.json.JsonNode.parse(verbJson);
			nlp4j.json.JsonNode verbBuckets = verbResult.get("aggregations").get("values").get("buckets");

			assertEquals(2, verbBuckets.size());

			boolean foundAruku = false;
			boolean foundIku = false;
			for (nlp4j.json.JsonNode bucket : verbBuckets.asList()) {
				String key = bucket.get("key").asString();
				if ("歩く".equals(key)) {
					foundAruku = true;
					assertEquals(1, bucket.get("doc_count").asInt());
				}
				if ("行く".equals(key)) {
					foundIku = true;
					assertEquals(1, bucket.get("doc_count").asInt());
				}
			}
			assertTrue("'歩く' not found in word.verb", foundAruku);
			assertTrue("'行く' not found in word.verb", foundIku);

			// word.noun = ["私", "学校"] の 2 件が集計されること
			String nounJson = search.aggregateJson("""
					{"field":"word.noun","size":10}
					""");
			System.out.println("testMorphologicalAnalysis001 word.noun: " + nounJson);

			nlp4j.json.JsonNode nounResult = nlp4j.json.JsonNode.parse(nounJson);
			nlp4j.json.JsonNode nounBuckets = nounResult.get("aggregations").get("values").get("buckets");

			assertEquals(2, nounBuckets.size());

			boolean foundWatashi = false;
			boolean foundGakkou = false;
			for (nlp4j.json.JsonNode bucket : nounBuckets.asList()) {
				String key = bucket.get("key").asString();
				if ("私".equals(key))
					foundWatashi = true;
				if ("学校".equals(key))
					foundGakkou = true;
			}
			assertTrue("'私' not found in word.noun", foundWatashi);
			assertTrue("'学校' not found in word.noun", foundGakkou);
		}
	}

	/**
	 * 同一文書に同じ語が複数回出た場合でも、aggregation の doc_count は文書数（1）であることを確認する。
	 *
	 * <pre>
	* 入力: "東京は東京の都市です。"  → "東京" が 2回出現
	* 期待: word.noun.東京.doc_count = 1  （文書数）
	 * </pre>
	 */
	public void testMorphologicalAnalysis002() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京は東京の都市です。");
			search.commit();

			String json = search.aggregateJson("""
					{"field":"word.noun","size":10}
					""");
			System.out.println("testMorphologicalAnalysis002 word.noun: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");

			// "東京" の doc_count は 1（文書数）であること
			boolean foundTokyo = false;
			for (nlp4j.json.JsonNode bucket : buckets.asList()) {
				if ("東京".equals(bucket.get("key").asString())) {
					assertEquals("東京 の doc_count は文書数（1）であること", 1, bucket.get("doc_count").asInt());
					foundTokyo = true;
				}
			}
			assertTrue("'東京' not found in word.noun", foundTokyo);
		}
	}

	/**
	 * add(id, body) と addJson() で同じ本文を登録したとき、 word.verb の aggregation
	 * 結果が等しくなることを確認する。
	 */
	public void testMorphologicalAnalysis003() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "私は歩いて学校に行きました。");
			search.addJson("""
					{"id":"2","body":"私は歩いて学校に行きました。"}
					""");
			search.commit();

			String json = search.aggregateJson("""
					{"field":"word.verb","size":10}
					""");
			System.out.println("testMorphologicalAnalysis003 word.verb: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");

			// add() と addJson() 合計で doc_count=2 になること
			for (nlp4j.json.JsonNode bucket : buckets.asList()) {
				String key = bucket.get("key").asString();
				if ("歩く".equals(key) || "行く".equals(key)) {
					assertEquals("add() と addJson() で " + key + " は各 2文書にヒットすること", 2, bucket.get("doc_count").asInt());
				}
			}
		}
	}

	/**
	 * word フィールド（NOUN/PROPN/VERB/ADJ のみ）に内容語が登録されることを確認する。
	 * 助詞（ADP）・助動詞（AUX）・記号（SYM）は word フィールドに含まれないこと。
	 *
	 * <pre>
	* 入力: "今日はいい天気です。"
	* Kuromoji 出力:
	*   NOUN 今日, ADP は, ADJ いい, NOUN 天気, AUX です, SYM 。
	* word フィールドに期待する値:
	*   今日（NOUN）, いい（ADJ）, 天気（NOUN）
	* word フィールドに含まれてはいけない値:
	*   は（ADP）, です（AUX）, 。（SYM）
	 * </pre>
	 */
	public void testMorphologicalAnalysis004() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "今日はいい天気です。");
			search.commit();

			String json = search.aggregateJson("""
					{"field":"word","size":20}
					""");
			System.out.println("testMorphologicalAnalysis004 word: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");

			java.util.Set<String> wordKeys = new java.util.HashSet<>();
			for (nlp4j.json.JsonNode bucket : buckets.asList()) {
				wordKeys.add(bucket.get("key").asString());
			}

			// 内容語が含まれること
			assertTrue("'今日' should be in word", wordKeys.contains("今日"));
			assertTrue("'天気' should be in word", wordKeys.contains("天気"));
			assertTrue("'いい' should be in word", wordKeys.contains("いい"));

			// 機能語が含まれないこと
			assertFalse("'は' should NOT be in word", wordKeys.contains("は"));
			assertFalse("'です' should NOT be in word", wordKeys.contains("です"));
			assertFalse("'。' should NOT be in word", wordKeys.contains("。"));
		}
	}

	/**
	 * word.verb フィールドで絞り込み検索が動作することを確認する。 "歩く" が word.verb に登録されている文書のみがヒットすること。
	 */
	public void testMorphologicalAnalysis005() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "私は歩いて学校に行きました。");
			search.add("2", "彼女は車で学校に行きました。");
			search.add("3", "今日はいい天気です。");
			search.commit();

			// word.verb="歩く" でフィルター → id=1 のみ
			SearchResult[] results = search.search("word.verb:歩く", 10);
			System.out.println("testMorphologicalAnalysis005 word.verb=歩く size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
				System.out.println("result[" + n + "].body: " + results[n].body);
			}
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * 複数文書に対する word.verb aggregation が正しく集計されることを確認する。 会話 kaiwa0808.md
	 * で示された最終的なユースケース。
	 */
	public void testMorphologicalAnalysis006() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "私は歩いて学校に行きました。");
			search.add("2", "彼女は走って学校に行きました。");
			search.commit();

			String json = search.aggregateJson("""
					{
					  "field": "word.verb",
					  "size": 10
					}
					""");
			System.out.println("testMorphologicalAnalysis006 word.verb: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");

			// "行く" は 2文書に出現 → doc_count=2
			// "歩く" は 1文書 → doc_count=1
			// "走る" は 1文書 → doc_count=1
			boolean foundIku = false;
			boolean foundAruku = false;
			boolean foundHashiru = false;
			for (nlp4j.json.JsonNode bucket : buckets.asList()) {
				String key = bucket.get("key").asString();
				int count = bucket.get("doc_count").asInt();
				System.out.println("  word.verb: " + key + " = " + count);
				if ("行く".equals(key)) {
					foundIku = true;
					assertEquals(2, count);
				}
				if ("歩く".equals(key)) {
					foundAruku = true;
					assertEquals(1, count);
				}
				if ("走る".equals(key)) {
					foundHashiru = true;
					assertEquals(1, count);
				}
			}
			assertTrue("'行く' not found", foundIku);
			assertTrue("'歩く' not found", foundAruku);
			assertTrue("'走る' not found", foundHashiru);
		}
	}

	// =========================================================
	// Builder API テスト
	// =========================================================

	/**
	 * {@code LocalSearch.builder("ja").build()} がデフォルト設定（autoAnalyze=true）で
	 * 動作することを確認する。従来の {@code new LocalSearch("ja")} と同等の動作になること。
	 */
	public void testBuilder001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").build()) {
			search.add("1", "東京の観光スポット");
			search.add("2", "京都の寺院と歴史");
			search.commit();

			// 全文検索
			SearchResult[] results = search.search("東京", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * {@code .autoAnalyze(false)} を指定すると Kuromoji 形態素解析がスキップされ、 word.verb
	 * フィールドで検索してもヒットしないことを確認する。
	 *
	 * <pre>
	 * autoAnalyze=true  → word.verb="行く" で検索するとヒットする
	 * autoAnalyze=false → word.verb="行く" で検索しても 0 件
	 * </pre>
	 */
	public void testBuilder002_autoAnalyzeFalse() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {
			search.add("1", "私は歩いて学校に行きました。");
			search.commit();

			// word.verb="行く" で検索しても 0 件（形態素解析されていないため登録なし）
			SearchResult[] results = search.search("word.verb:行く", 10);
			assertEquals("autoAnalyze=false では word.verb に値が登録されないこと", 0, results.length);
		}
	}

	/**
	 * {@code .autoAnalyze(true)} が明示指定された場合も形態素解析が正しく動作することを確認する。
	 * デフォルト値（true）と同じ挙動であること。
	 */
	public void testBuilder003_autoAnalyzeTrue() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).build()) {
			search.add("1", "私は歩いて学校に行きました。");
			search.commit();

			// word.verb に "行く" が集計されること
			String json = search.aggregateJson("""
					{"field":"word.verb","size":10}
					""");
			nlp4j.json.JsonNode buckets = nlp4j.json.JsonNode.parse(json).get("aggregations").get("values")
					.get("buckets");

			boolean foundIku = false;
			for (nlp4j.json.JsonNode bucket : buckets.asList()) {
				if ("行く".equals(bucket.get("key").asString())) {
					foundIku = true;
				}
			}
			assertTrue("autoAnalyze=true では word.verb に '行く' が登録されること", foundIku);
		}
	}

	/**
	 * {@code .vectorDimension(2)} を指定してベクトル検索が動作することを確認する。
	 */
	public void testBuilder004_vectorDimension() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			search.add("1_East", new float[] { 1.0f, 0.0f });
			search.add("2_North", new float[] { 0.0f, 1.0f });
			search.add("3_West", new float[] { -1.0f, 0.0f });
			search.commit();

			// クエリ (0.9, 0.1) に最近傍の 1_East がスコア最高
			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 3);
			assertEquals(3, results.length);
			assertEquals("1_East", results[0].id);
		}
	}

	/**
	 * {@code .autoAnalyze(false).vectorDimension(2)} の組み合わせが正しく動作することを確認する。
	 * 形態素解析なし＋ベクトル検索の両方が有効であること。
	 */
	public void testBuilder005_autoAnalyzeFalseWithVector() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).vectorDimension(2).build()) {
			search.add("1", new float[] { 1.0f, 0.0f });
			search.add("2", new float[] { 0.0f, 1.0f });
			search.commit();

			// ベクトル検索が動作すること
			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 2);
			assertEquals(2, results.length);
			assertEquals("1", results[0].id);

			// word.verb="行く" で検索しても 0 件
			// （add(id, vector) では本文がなく、かつ autoAnalyze=false のため word フィールドは空）
			SearchResult[] verbResults = search.search("word.verb:行く", 10);
			assertEquals(0, verbResults.length);
		}
	}

	/**
	 * {@code .loadIndexFrom(Path)} でディスクインデックスを使用した場合に 文書の追加・検索が正しく動作することを確認する。
	 */
	public void testBuilder006_loadIndexFrom() throws Exception {
		java.nio.file.Path tempDir = java.nio.file.Files.createTempDirectory("localsearch_test_");
		try {
			try (LocalSearch search = LocalSearch.builder("en").loadIndexFrom(tempDir).build()) {
				search.addJson("""
						{"id":"1","body":"Kyoto is a historic city.","category":"city"}
						""");
				search.addJson("""
						{"id":"2","body":"Nintendo is headquartered in Kyoto.","category":"company"}
						""");
				search.commit();

				// 全文検索
				SearchResult[] results = search.search("Kyoto", 10);
				assertEquals(2, results.length);

				// フィールド検索
				SearchResult[] byCategory = search.search("category:city", 10);
				assertEquals(1, byCategory.length);
				assertEquals("1", byCategory[0].id);
			}
		} finally {
			// 一時ディレクトリを削除
			try (java.util.stream.Stream<java.nio.file.Path> files = java.nio.file.Files.walk(tempDir)) {
				files.sorted(java.util.Comparator.reverseOrder()).forEach(p -> {
					try {
						java.nio.file.Files.deleteIfExists(p);
					} catch (Exception ignored) {
					}
				});
			}
		}
	}

	/**
	 * {@code .vectorDimension(n)} に負の値を渡すと {@link LocalSearchException}
	 * がスローされることを確認する。
	 */
	public void testBuilder007_negativeDimensionThrows() throws Exception {
		try {
			LocalSearch.builder("ja").vectorDimension(-1).build();
			fail("vectorDimension < 0 では LocalSearchException がスローされること");
		} catch (LocalSearchException e) {
			// 期待通り
			assertTrue(e.getMessage().contains("vectorDimension must be >= 0"));
		}
	}

	// =========================================================
	// count() テスト
	// =========================================================

	/**
	 * count() が全ドキュメント件数を返すことを確認する。
	 */
	public void testCount001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京都は日本の都道府県のひとつです");
			search.add("2", "京都は日本の都市です。");
			search.add("3", "京都市には任天堂の本社があります");
			search.commit();

			long count = search.count();
			System.out.println("testCount001 count: " + count);
			assertEquals(3L, count);
		}
	}

	/**
	 * count(query) が全文検索にマッチするドキュメント件数を返すことを確認する。
	 */
	public void testCount002() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京都は日本の都道府県のひとつです");
			search.add("2", "京都は日本の都市です。");
			search.add("3", "京都市には任天堂の本社があります");
			search.commit();

			long countKyoto = search.count("京都");
			System.out.println("testCount002 count(京都): " + countKyoto);
			assertEquals(2L, countKyoto);

			long countTokyo = search.count("東京");
			System.out.println("testCount002 count(東京): " + countTokyo);
			assertEquals(1L, countTokyo);
		}
	}

	/**
	 * count(query) でマッチしない場合は 0 を返すことを確認する。
	 */
	public void testCount003() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京都は日本の都道府県のひとつです");
			search.add("2", "京都は日本の都市です。");
			search.commit();

			long count = search.count("大阪");
			System.out.println("testCount003 count(大阪): " + count);
			assertEquals(0L, count);
		}
	}

	/**
	 * count(query, filters) が全文検索＋フィールド絞り込みの件数を返すことを確認する。
	 */
	public void testCount004() throws Exception {
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

			// "東京" + category=観光 → 1件
			long count = search.count("東京", java.util.Map.of("category", "観光"));
			System.out.println("testCount004 count(東京, category=観光): " + count);
			assertEquals(1L, count);
		}
	}

	/**
	 * count(query, filters) で filters のみ（query なし）の場合も正しく動作することを確認する。
	 */
	public void testCount005() throws Exception {
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

			// query=null + category=観光 → 2件
			long count = search.count(null, java.util.Map.of("category", "観光"));
			System.out.println("testCount005 count(null, category=観光): " + count);
			assertEquals(2L, count);
		}
	}

	// =========================================================
	// aggregate() Java API テスト
	// =========================================================

	/**
	 * aggregate(field, size) が全ドキュメントを対象に terms aggregation を実行し、
	 * {@code Map<String, Long>} を返すことを確認する。
	 */
	public void testAggregate001() throws Exception {
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
			search.commit();

			java.util.Map<String, Long> result = search.aggregate("category", 10);
			System.out.println("testAggregate001 aggregate(category, 10): " + result);

			// 観光:2, 技術:1 の 2 エントリー
			assertEquals(2, result.size());
			assertEquals(Long.valueOf(2L), result.get("観光"));
			assertEquals(Long.valueOf(1L), result.get("技術"));
		}
	}

	/**
	 * aggregate(field, query, size) が全文検索クエリで絞り込んだ上で集計することを確認する。
	 */
	public void testAggregate002() throws Exception {
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

			// query="東京" で絞り込み → 観光:1, 技術:1
			java.util.Map<String, Long> result = search.aggregate("category", "東京", 10);
			System.out.println("testAggregate002 aggregate(category, 東京, 10): " + result);

			assertEquals(2, result.size());
			assertEquals(Long.valueOf(1L), result.get("観光"));
			assertEquals(Long.valueOf(1L), result.get("技術"));
		}
	}

	/**
	 * aggregate(field, query, size, filters) が全文検索＋フィールド絞り込みで集計することを確認する。
	 */
	public void testAggregate003() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京のAI企業","category":"技術","source":"news"}
					""");
			search.addJson("""
					{"id":"2","body":"東京の観光スポット","category":"観光","source":"blog"}
					""");
			search.addJson("""
					{"id":"3","body":"大阪のIT企業","category":"技術","source":"news"}
					""");
			search.commit();

			// query="東京" + source=news → id=1 のみ → 技術:1
			java.util.Map<String, Long> result = search.aggregate("category", "東京", 10,
					java.util.Map.of("source", "news"));
			System.out.println("testAggregate003 aggregate(category, 東京, 10, source=news): " + result);

			assertEquals(1, result.size());
			assertEquals(Long.valueOf(1L), result.get("技術"));
		}
	}

	/**
	 * aggregate(field, size) の返却 Map の順序が件数降順であることを確認する（LinkedHashMap）。
	 */
	public void testAggregate004() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"doc1","category":"技術"}
					""");
			search.addJson("""
					{"id":"2","body":"doc2","category":"観光"}
					""");
			search.addJson("""
					{"id":"3","body":"doc3","category":"観光"}
					""");
			search.addJson("""
					{"id":"4","body":"doc4","category":"観光"}
					""");
			search.commit();

			java.util.Map<String, Long> result = search.aggregate("category", 10);
			System.out.println("testAggregate004 aggregate(category, 10): " + result);

			// 先頭は最多件数の "観光"（3件）
			String firstKey = result.keySet().iterator().next();
			assertEquals("観光", firstKey);
			assertEquals(Long.valueOf(3L), result.get("観光"));
			assertEquals(Long.valueOf(1L), result.get("技術"));
		}
	}

	/**
	 * aggregate(field, null, size) が全件集計と等価であることを確認する。
	 */
	public void testAggregate005() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"doc1","category":"A"}
					""");
			search.addJson("""
					{"id":"2","body":"doc2","category":"B"}
					""");
			search.addJson("""
					{"id":"3","body":"doc3","category":"A"}
					""");
			search.commit();

			// query=null は全件集計と同じ
			java.util.Map<String, Long> result = search.aggregate("category", null, 10);
			System.out.println("testAggregate005 aggregate(category, null, 10): " + result);

			assertEquals(2, result.size());
			assertEquals(Long.valueOf(2L), result.get("A"));
			assertEquals(Long.valueOf(1L), result.get("B"));
		}
	}

	/**
	 * count() と aggregate() を組み合わせた利用が Example10 と同等に動作することを確認する。 kaiwa0813.md
	 * のユースケース検証。
	 */
	public void testCountAndAggregate001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "ニッサン ドアミラーが破損");
			search.add("2", "ニッサン ドアミラーが動かない");
			search.add("3", "トヨタ ドアミラーが外れた");
			search.add("4", "トヨタ ブレーキの効きが悪い");
			search.add("5", "トヨタ ドアから水が入った");
			search.commit();

			// 全件数 = 5
			long countAll = search.count();
			System.out.println("testCountAndAggregate001 countAll: " + countAll);
			assertEquals(5L, countAll);

			// ニッサンに関する文書 = 2件
			long countNissan = search.count("ニッサン");
			System.out.println("testCountAndAggregate001 count(ニッサン): " + countNissan);
			assertEquals(2L, countNissan);

			// 全体の word.noun 集計
			// Kuromoji は「ドアミラー」を「ドア」「ミラー」に分割するため、それぞれで確認する
			java.util.Map<String, Long> nounAll = search.aggregate("word.noun", 1000);
			System.out.println("testCountAndAggregate001 nounAll: " + nounAll);
			assertFalse("nounAll は空でないこと", nounAll.isEmpty());
			// "ドア" または "ドアミラー" のいずれかが含まれること（Kuromoji の分割に依存）
			assertTrue("'ドア' か 'ドアミラー' が noun に含まれること", nounAll.containsKey("ドア") || nounAll.containsKey("ドアミラー"));

			// ニッサンで絞り込んだ word.noun 集計
			java.util.Map<String, Long> nounNissan = search.aggregate("word.noun", "ニッサン", 1000);
			System.out.println("testCountAndAggregate001 nounNissan: " + nounNissan);
			assertFalse("nounNissan は空でないこと", nounNissan.isEmpty());
			// ニッサン絞り込みでは「ドア」または「ドアミラー」が 2件
			String doorKey = nounNissan.containsKey("ドアミラー") ? "ドアミラー" : "ドア";
			assertTrue("ニッサン noun の " + doorKey + " は 2件であること",
					nounNissan.containsKey(doorKey) && nounNissan.get(doorKey) == 2L);
		}
	}

	// =========================================================
	// count(filterField, filterValue) テスト
	// =========================================================

	/**
	 * count(filterField, filterValue) が keyword フィールドの完全一致で ドキュメント件数を返すことを確認する。
	 * category="観光" を持つ文書 2件が返ること。
	 */
	public void testCountByField001() throws Exception {
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
			search.commit();

			long count = search.count("category:観光");
			System.out.println("testCountByField001 count(category, 観光): " + count);
			assertEquals(2L, count);
		}
	}

	/**
	 * count(filterField, filterValue) で word.noun フィールドを絞り込み条件に使えることを確認する。
	 * 形態素解析によって word.noun に登録された値で件数を取得できること。
	 */
	public void testCountByField002() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "ニッサン ドアが破損");
			search.add("2", "ニッサン ドアが動かない");
			search.add("3", "トヨタ ドアが外れた");
			search.add("4", "トヨタ ブレーキの効きが悪い");
			search.add("5", "トヨタ ドアから水が入った");
			search.commit();

			// word.noun=ニッサン が出現する文書 → 2件
			long countNissan = search.count("word.noun:ニッサン");
			System.out.println("testCountByField002 count(word.noun, ニッサン): " + countNissan);
			assertEquals(2L, countNissan);

			// word.noun=トヨタ が出現する文書 → 3件
			long countToyota = search.count("word.noun:トヨタ");
			System.out.println("testCountByField002 count(word.noun, トヨタ): " + countToyota);
			assertEquals(3L, countToyota);
		}
	}

	/**
	 * count(filterField, filterValue) でマッチしない場合は 0 を返すことを確認する。
	 */
	public void testCountByField003() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光"}
					""");
			search.commit();

			long count = search.count("category:技術");
			System.out.println("testCountByField003 count(category, 技術): " + count);
			assertEquals(0L, count);
		}
	}

	// =========================================================
	// aggregate(aggregationField, filterField, filterValue, size) テスト
	// =========================================================

	/**
	 * aggregate(aggregationField, filterField, filterValue, size) が
	 * 指定フィールドの完全一致で絞り込んだ上で集計することを確認する。
	 *
	 * <pre>
	 * category="観光" に絞り込んだ上で source を集計
	 * → id=1（source=news）, id=3（source=blog）
	 * → news:1, blog:1
	 * </pre>
	 */
	public void testAggregateByField001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光","source":"news"}
					""");
			search.addJson("""
					{"id":"2","body":"東京のIT企業","category":"技術","source":"news"}
					""");
			search.addJson("""
					{"id":"3","body":"京都の寺院","category":"観光","source":"blog"}
					""");
			search.commit();

			// category=観光 に絞り込んで source を集計 → news:1, blog:1
			java.util.Map<String, Long> result = search.aggregate("source", "category:観光", 10);
			System.out.println("testAggregateByField001 aggregate(source, category, 観光, 10): " + result);

			assertEquals(2, result.size());
			assertEquals(Long.valueOf(1L), result.get("news"));
			assertEquals(Long.valueOf(1L), result.get("blog"));
		}
	}

	/**
	 * aggregate(aggregationField, filterField, filterValue, size) で word.noun
	 * フィールドを絞り込み条件、word.verb を集計対象に使えることを確認する。
	 *
	 * <pre>
	 * word.noun=ニッサン が出現する文書の word.verb を集計
	 * </pre>
	 */
	public void testAggregateByField002() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "ニッサン ドアが破損した");
			search.add("2", "ニッサン ドアが動かない");
			search.add("3", "トヨタ ドアが外れた");
			search.add("4", "トヨタ ブレーキが効かない");
			search.commit();

			// word.noun=ニッサン に絞り込んで word.verb を集計
			java.util.Map<String, Long> result = search.aggregate("word.verb", "word.noun:ニッサン", 1000);
			System.out.println("testAggregateByField002 aggregate(word.verb, word.noun, ニッサン, 1000): " + result);

			// ニッサンが出現する文書（id=1, id=2）の verb が集計されていること
			assertFalse("結果は空でないこと", result.isEmpty());
		}
	}

	/**
	 * aggregate(aggregationField, filterField, filterValue, size) と
	 * aggregate(field, null, size, Map.of(filterField, filterValue)) が
	 * 同じ結果を返すことを確認する（API等価性）。
	 */
	public void testAggregateByField003() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"東京の観光スポット","category":"観光","country":"Japan"}
					""");
			search.addJson("""
					{"id":"2","body":"パリの観光地","category":"観光","country":"France"}
					""");
			search.addJson("""
					{"id":"3","body":"東京のIT企業","category":"技術","country":"Japan"}
					""");
			search.commit();

			// 新シグネチャ
			java.util.Map<String, Long> result1 = search.aggregate("category", "country:Japan", 10);

			// 既存シグネチャ（等価な呼び出し）
			java.util.Map<String, Long> result2 = search.aggregate("category", null, 10,
					java.util.Map.of("country", "Japan"));

			System.out.println("testAggregateByField003 result1: " + result1);
			System.out.println("testAggregateByField003 result2: " + result2);

			assertEquals(result2.size(), result1.size());
			for (java.util.Map.Entry<String, Long> entry : result2.entrySet()) {
				assertEquals("キー '" + entry.getKey() + "' の値が一致すること", entry.getValue(), result1.get(entry.getKey()));
			}
		}
	}

	/**
	 * count(filterField, filterValue) と aggregate(aggregationField, filterField,
	 * filterValue, size) を 組み合わせた kaiwa0813-2048.md のユースケースを確認する。
	 *
	 * <pre>
	 * word.noun=ニッサン が出現する文書を基準に relativeRate を計算する
	 * （全文検索ではなく分析フィールドで定義した「ニッサンが出現する文書」）
	 * </pre>
	 */
	public void testCountAndAggregateByField001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "ニッサン ドアが破損した");
			search.add("2", "ニッサン ドアが動かない");
			search.add("3", "トヨタ ドアが外れた");
			search.add("4", "トヨタ ブレーキが効かない");
			search.add("5", "トヨタ ドアから水が入った");
			search.commit();

			{ // 全件=5
				long countAll = search.count();
				System.out.println("testCountAndAggregateByField001 countAll: " + countAll);
				assertEquals(5L, countAll);

			}
			{ // ニッサン=2
				// word.noun=ニッサン が出現する文書の件数（分析フィールドによる絞り込み）
				long countNissan = search.count("word.noun:ニッサン");
				System.out.println("testCountAndAggregateByField001 count(word.noun, ニッサン): " + countNissan);
				assertEquals(2L, countNissan);
			}
			{ // word.noun=トヨタ が出現する文書の件数 = 3
				long countToyota = search.count("word.noun:トヨタ");
				System.out.println("testCountAndAggregateByField001 count(word.noun, トヨタ): " + countToyota);
				assertEquals(3L, countToyota);
			}
			{ // word.noun=ニッサン の文書で word.noun を集計（新シグネチャ）
				java.util.Map<String, Long> nounNissan = search.aggregate("word.noun", "word.noun:ニッサン", 1000);
				System.out.println("testCountAndAggregateByField001 nounNissan: " + nounNissan);
				assertFalse("nounNissan は空でないこと", nounNissan.isEmpty());
				assertEquals("ニッサンが含まれること", Long.valueOf(2L), nounNissan.get("ニッサン"));

				{ // word.noun=ニッサン の文書で word.noun を集計（既存シグネチャで等価）
					java.util.Map<String, Long> nounNissan2 = search.aggregate("word.noun", null, 1000,
							java.util.Map.of("word.noun", "ニッサン"));
					assertEquals("新旧シグネチャの結果が一致すること", nounNissan, nounNissan2);
				}
			}

		}
	}

	// =========================================================
	// addJson() vector 対応テスト
	// =========================================================

	/**
	 * ① addJson() で vector を登録し、ベクトル検索で最近傍が正しく返ることを確認する。
	 *
	 * <pre>
	 * {"id":"1","body":"East","vector":[1.0,0.0]}
	 * {"id":"2","body":"North","vector":[0.0,1.0]}
	 * クエリ (0.9, 0.1) → id=1 が先頭
	 * </pre>
	 */
	public void testAddJsonVector001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"East\",\"vector\":[1.0,0.0]}");
			search.addJson("{\"id\":\"2\",\"body\":\"North\",\"vector\":[0.0,1.0]}");
			search.commit();

			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10);
			System.out.println("testAddJsonVector001 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
			}

			assertEquals(2, results.length);
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * ② addJson() で vector + metadata を登録し、ベクトル検索＋フィールドフィルターが動作することを確認する。
	 *
	 * <pre>
	 * id=1: category=technology, country=Japan, vector=[1.0,0.0]
	 * id=2: category=technology, country=USA,   vector=[0.9,0.2]
	 * id=3: category=travel,     country=Japan, vector=[0.8,0.3]
	 * クエリ (0.9, 0.1) + category=technology + country=Japan → id=1 のみ
	 * </pre>
	 */
	public void testAddJsonVectorWithFilter001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"Kyoto technology\","
					+ "\"category\":\"technology\",\"country\":\"Japan\",\"vector\":[1.0,0.0]}");
			search.addJson("{\"id\":\"2\",\"body\":\"US technology\","
					+ "\"category\":\"technology\",\"country\":\"USA\",\"vector\":[0.9,0.2]}");
			search.addJson("{\"id\":\"3\",\"body\":\"Kyoto travel\","
					+ "\"category\":\"travel\",\"country\":\"Japan\",\"vector\":[0.8,0.3]}");
			search.commit();

			// category=technology + country=Japan → id=1 のみ
			SearchResult[] results = search.searchVector(new float[] { 0.9f, 0.1f }, 10,
					java.util.Map.of("category", "technology", "country", "Japan"));
			System.out.println("testAddJsonVectorWithFilter001 size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id);
			}

			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	/**
	 * ③ addJson() で vector なし JSON が従来どおり登録できることを確認する（後方互換）。
	 *
	 * <pre>
	 * vectorDimension(2) を指定していても vector フィールドがなければ通常ドキュメントとして登録可能。
	 * </pre>
	 */
	public void testAddJsonVectorOptional001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			// vector なし
			search.addJson("{\"id\":\"1\",\"body\":\"Kyoto\",\"category\":\"city\"}");
			// vector あり
			search.addJson("{\"id\":\"2\",\"body\":\"Tokyo\",\"category\":\"city\",\"vector\":[0.5,0.5]}");
			search.commit();

			// フィールド検索: category=city → 2件どちらも取得できること
			SearchResult[] results = search.search("category:city", 10);
			System.out.println("testAddJsonVectorOptional001 size: " + results.length);
			assertEquals(2, results.length);
		}
	}

	/**
	 * ④ vectorDimension(2) なのに vector=[1.0,0.0,0.5]（3次元）が来た場合、 LocalSearchException
	 * がスローされることを確認する。
	 */
	public void testAddJsonVectorDimensionMismatch001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			try {
				search.addJson("{\"id\":\"1\",\"body\":\"test\",\"vector\":[1.0,0.0,0.5]}");
				fail("次元数不一致では LocalSearchException がスローされること");
			} catch (LocalSearchException e) {
				System.out.println("testAddJsonVectorDimensionMismatch001 exception: " + e.getMessage());
				assertTrue("メッセージに 'dimension mismatch' が含まれること",
						e.getMessage().contains("dimension mismatch") || e.getMessage().contains("mismatch"));
			}
		}
	}

	/**
	 * ⑤ vectorDimension 未設定の LocalSearch に vector フィールドを含む JSON を addJson() すると、
	 * LocalSearchException がスローされることを確認する。
	 */
	public void testAddJsonVectorDimensionNotEnabled001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").build()) {
			try {
				search.addJson("{\"id\":\"1\",\"body\":\"test\",\"vector\":[1.0,0.0]}");
				fail("vectorDimension 未設定では LocalSearchException がスローされること");
			} catch (LocalSearchException e) {
				System.out.println("testAddJsonVectorDimensionNotEnabled001 exception: " + e.getMessage());
				assertTrue("メッセージに 'not enabled' が含まれること",
						e.getMessage().contains("not enabled") || e.getMessage().contains("enabled"));
			}
		}
	}

	// =========================================================
	// Schema 保存・再ロード テスト
	// =========================================================

	/**
	 * addJson() で動的に追加されたフィールドが saveIndexTo() / loadIndexFrom() 後に復元されることを確認する。
	 * maker(KEYWORD), year_i(INTEGER), tags(KEYWORD+multiValued) が正しく復元される。
	 */
	public void testSaveAndLoadSchema001() throws Exception {
		java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-schema-");
		try {
			// 1. インデックスを作成して保存
			try (LocalSearch search = LocalSearch.builder("en").build()) {
				search.addJson("""
						{
						  "id": "1",
						  "body": "Nissan vehicle",
						  "maker": "Nissan",
						  "year_i": 2026,
						  "tags": ["EV", "Japan"]
						}
						""");
				search.commit();
				search.saveIndexTo(dir);
			}

			// 2. 再ロードしてスキーマが復元されていることを確認
			try (LocalSearch loaded = LocalSearch.builder("en").loadIndexFrom(dir).build()) {
				nlp4j.lucene9.SearchSchema schema = loaded.getSchema();

				// maker は KEYWORD として動的登録されていること
				assertTrue("maker field should exist", schema.contains("maker"));
				assertEquals(nlp4j.lucene9.FieldTypeDef.Kind.KEYWORD, schema.get("maker").kind());

				// year_i は INTEGER（suffix ルール）として動的登録されていること
				assertTrue("year_i field should exist", schema.contains("year_i"));
				assertEquals(nlp4j.lucene9.FieldTypeDef.Kind.INTEGER, schema.get("year_i").kind());

				// tags は KEYWORD + multiValued として動的登録されていること
				assertTrue("tags field should exist", schema.contains("tags"));
				nlp4j.lucene9.FieldTypeDef tagsDef = schema.get("tags");
				assertEquals(nlp4j.lucene9.FieldTypeDef.Kind.KEYWORD, tagsDef.kind());
				assertTrue(tagsDef.is_multiValued());

				// 検索も動作すること
				SearchResult[] results = loaded.search("maker:Nissan", 10);
				assertEquals(1, results.length);
				assertEquals("1", results[0].id);
			}
		} finally {
			deleteDirectory(dir);
		}
	}

	/**
	 * saveIndexTo() / loadIndexFrom() で vectorDimension が schema から復元されることを確認する。
	 */
	public void testSaveAndLoadSchemaVectorDimension001() throws Exception {
		java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-vecschema-");
		try {
			// 1. ベクトル付きインデックスを作成して保存
			try (LocalSearch search = LocalSearch.builder("en").vectorDimension(3).build()) {
				search.add("1", new float[] { 1.0f, 0.0f, 0.0f });
				search.add("2", new float[] { 0.0f, 1.0f, 0.0f });
				search.commit();
				search.saveIndexTo(dir);
			}

			// 2. vectorDimension を指定せずに再ロード → schema から 3 が復元されること
			try (LocalSearch loaded = LocalSearch.builder("en").loadIndexFrom(dir).build()) {
				assertEquals("vectorDimension should be restored from schema", 3, loaded.vectorDimension);

				// ベクトル検索も動作すること
				SearchResult[] results = loaded.searchVector(new float[] { 1.0f, 0.0f, 0.0f }, 10);
				assertEquals(2, results.length);
				assertEquals("1", results[0].id);
			}
		} finally {
			deleteDirectory(dir);
		}
	}

	/**
	 * Builder.field() で追加したフィールドが保存 schema に存在しない場合、schema に追加されることを確認する（merge）。
	 */
	public void testSaveAndLoadSchemaBuilderFieldMerge001() throws Exception {
		java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-merge-");
		try {
			// 1. 保存
			try (LocalSearch search = LocalSearch.builder("en").build()) {
				search.addJson("{\"id\":\"1\",\"body\":\"test\",\"maker\":\"Honda\"}");
				search.commit();
				search.saveIndexTo(dir);
			}

			// 2. 再ロード時に新規フィールドを Builder.field() で追加
			try (LocalSearch loaded = LocalSearch.builder("en").loadIndexFrom(dir)
					.field("new_field", nlp4j.lucene9.FieldTypeDef.keyword().stored(true).aggregatable(true)).build()) {

				nlp4j.lucene9.SearchSchema schema = loaded.getSchema();
				assertTrue("new_field should be added via builder", schema.contains("new_field"));
				assertEquals(nlp4j.lucene9.FieldTypeDef.Kind.KEYWORD, schema.get("new_field").kind());
				assertTrue(schema.get("new_field").is_aggregatable());
			}
		} finally {
			deleteDirectory(dir);
		}
	}

	/**
	 * Builder.field() で保存済みフィールドと型が異なる定義を指定した場合、LocalSearchException が
	 * スローされることを確認する。
	 */
	public void testSaveAndLoadSchemaConflictThrows001() throws Exception {
		java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-conflict-");
		try {
			// 1. 保存（maker は KEYWORD として動的登録）
			try (LocalSearch search = LocalSearch.builder("en").build()) {
				search.addJson("{\"id\":\"1\",\"body\":\"test\",\"maker\":\"Toyota\"}");
				search.commit();
				search.saveIndexTo(dir);
			}

			// 2. 再ロード時に maker を INTEGER として指定 → 型の不一致でエラー
			try {
				LocalSearch loaded = LocalSearch.builder("en").loadIndexFrom(dir)
						.field("maker", nlp4j.lucene9.FieldTypeDef.integer().stored(true)).build();
				loaded.close();
				fail("Expected LocalSearchException for conflicting field definition");
			} catch (LocalSearchException e) {
				System.out.println("testSaveAndLoadSchemaConflictThrows001 exception: " + e.getMessage());
				assertTrue(e.getMessage().contains("conflict"));
			}
		} finally {
			deleteDirectory(dir);
		}
	}

	/**
	 * 非vector indexを保存し、reload時に .vectorDimension(3) を指定してロードした場合、 スキーマに vector
	 * が追加され、新規 vector 文書を追加およびベクトル検索できることを確認する。
	 */
	public void testSaveAndLoadSchemaAddVectorDimension001() throws Exception {
		java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-add-vector-");
		try {
			// 1. 非vector index を作成して保存
			try (LocalSearch search = LocalSearch.builder("en").build()) {
				search.add("1", "existing document");
				search.commit();
				search.saveIndexTo(dir);
			}

			// 2. reload 時に vectorDimension(3) を指定してロード
			try (LocalSearch loaded = LocalSearch.builder("en").loadIndexFrom(dir).vectorDimension(3).build()) {

				assertEquals(3, loaded.vectorDimension);
				assertTrue(loaded.getSchema().contains("vector"));
				assertEquals(nlp4j.lucene9.FieldTypeDef.Kind.KNN_VECTOR, loaded.getSchema().get("vector").kind());

				// vector を持つ文書を追加
				loaded.add("2", new float[] { 1.0f, 0.0f, 0.0f });
				loaded.commit();

				SearchResult[] results = loaded.searchVector(new float[] { 1.0f, 0.0f, 0.0f }, 10);
				assertEquals(1, results.length);
				assertEquals("2", results[0].id);
			}
		} finally {
			deleteDirectory(dir);
		}
	}

	/**
	 * vectorDimension=3 のインデックスを reload 時に .vectorDimension(2) を指定した場合、
	 * LocalSearchException がスローされることを確認する。
	 */
	public void testSaveAndLoadSchemaVectorDimensionConflictThrows001() throws Exception {
		java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-vec-conflict-");
		try {
			// 1. vectorDimension=3 で保存
			try (LocalSearch search = LocalSearch.builder("en").vectorDimension(3).build()) {
				search.add("1", new float[] { 1.0f, 0.0f, 0.0f });
				search.commit();
				search.saveIndexTo(dir);
			}

			// 2. reload 時に vectorDimension(2) を指定 → conflict
			try {
				LocalSearch loaded = LocalSearch.builder("en").loadIndexFrom(dir).vectorDimension(2).build();
				loaded.close();
				fail("Expected LocalSearchException for conflicting vector dimensions");
			} catch (LocalSearchException e) {
				assertTrue(e.getMessage().contains("conflict") || e.getMessage().contains("dimension"));
			}
		} finally {
			deleteDirectory(dir);
		}
	}

	/**
	 * vectorDimension=3 のインデックスを reload 時に .vectorDimension(3) を指定した場合、
	 * 正常にオープンできることを確認する。
	 */
	public void testSaveAndLoadSchemaVectorDimensionMatch001() throws Exception {
		java.nio.file.Path dir = java.nio.file.Files.createTempDirectory("nlp4j-test-vec-match-");
		try {
			// 1. vectorDimension=3 で保存
			try (LocalSearch search = LocalSearch.builder("en").vectorDimension(3).build()) {
				search.add("1", new float[] { 1.0f, 0.0f, 0.0f });
				search.commit();
				search.saveIndexTo(dir);
			}

			// 2. reload 時に vectorDimension(3) を指定 → 正常
			try (LocalSearch loaded = LocalSearch.builder("en").loadIndexFrom(dir).vectorDimension(3).build()) {

				assertEquals(3, loaded.vectorDimension);
				SearchResult[] results = loaded.searchVector(new float[] { 1.0f, 0.0f, 0.0f }, 10);
				assertEquals(1, results.length);
				assertEquals("1", results[0].id);
			}
		} finally {
			deleteDirectory(dir);
		}
	}

	/** Recursively deletes a temporary directory. */
	private static void deleteDirectory(java.nio.file.Path dir) throws Exception {
		if (dir == null || !java.nio.file.Files.exists(dir)) {
			return;
		}
		java.nio.file.Files.walk(dir).sorted(java.util.Comparator.reverseOrder()).map(java.nio.file.Path::toFile)
				.forEach(java.io.File::delete);
	}

	// =========================================================
	// aggregate() Lucene Query テスト
	// =========================================================

	/**
	 * aggregate() の基本ケース: Lucene Query で text_en:Kyoto を検索し、 category フィールドを集計する。
	 *
	 * <pre>
	 * id=1: body=Kyoto is a historic city in Japan.  category=city    country=Japan
	 * id=2: body=Nintendo is headquartered in Kyoto. category=company country=Japan
	 * id=3: body=Paris is the capital city of France. category=city   country=France
	 *
	 * aggregate("category", "text_en:Kyoto", 10)
	 * → city=1, company=1
	 * </pre>
	 */
	public void testAggregateQuery001() throws Exception {
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
					  "body": "Paris is the capital city of France.",
					  "category": "city",
					  "country": "France"
					}
					""");
			search.commit();

			java.util.Map<String, Long> result = search.aggregate("category", "Kyoto", 10);
			System.out.println("testAggregateQuery001 result: " + result);

			// Kyoto を含む id=1, id=2 の 2 件が対象 → city:1, company:1
			assertEquals(2, result.size());
			assertEquals(Long.valueOf(1L), result.get("city"));
			assertEquals(Long.valueOf(1L), result.get("company"));
		}
	}

	/**
	 * aggregate() の複数フィールド指定ケース: filters で country=Japan を指定して絞り込み、category を集計する。
	 *
	 * <pre>
	 * id=1: Kyoto + Japan → city
	 * id=2: Kyoto + Japan → company
	 * id=3: Paris + France → 除外（country フィルター）
	 * → city=1, company=1
	 * </pre>
	 */
	public void testAggregateQuery002() throws Exception {
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
					  "body": "Paris is the capital city of France.",
					  "category": "city",
					  "country": "France"
					}
					""");
			search.commit();

			// Lucene query で Kyoto + filters で country=Japan の組み合わせ
			java.util.Map<String, Long> result = search.aggregate("category", "Kyoto", 10,
					java.util.Map.of("country", "Japan"));
			System.out.println("testAggregateQuery002 result: " + result);

			// id=1, id=2 が対象（id=3 は country=France でフィルター除外）
			assertEquals(2, result.size());
			assertEquals(Long.valueOf(1L), result.get("city"));
			assertEquals(Long.valueOf(1L), result.get("company"));
		}
	}

	/**
	 * aggregate() の size 制限テスト: size=1 を指定すると最多 1 バケットのみ返ること。
	 */
	public void testAggregateQuerySize001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto city doc","category":"city","country":"Japan"}
					""");
			search.addJson("""
					{"id":"2","body":"Kyoto city doc2","category":"city","country":"Japan"}
					""");
			search.addJson("""
					{"id":"3","body":"Kyoto company doc","category":"company","country":"Japan"}
					""");
			search.commit();

			// size=1 → 最多バケット（city=2）のみ
			java.util.Map<String, Long> result = search.aggregate("category", "Kyoto", 1);
			System.out.println("testAggregateQuerySize001 result: " + result);

			assertEquals(1, result.size());
			assertEquals(Long.valueOf(2L), result.get("city"));
		}
	}

	/**
	 * aggregateJson() に lucene_query を指定した場合、 Lucene Query Parser
	 * で絞り込んだ集計が動作することを確認する。
	 *
	 * <pre>
	 * lucene_query="text_en:Kyoto" → city:1, company:1
	 * </pre>
	 */
	public void testAggregateJsonLuceneQuery001() throws Exception {
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
					  "body": "Paris is the capital city of France.",
					  "category": "city",
					  "country": "France"
					}
					""");
			search.commit();

			// query フィールドは Lucene Query として解釈される
			String json = search.aggregateJson("""
					{
					  "field": "category",
					  "query": "Kyoto",
					  "size": 10
					}
					""");
			System.out.println("testAggregateJsonLuceneQuery001: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("aggregations").get("values").get("buckets");

			// Kyoto を含む id=1, id=2 → city:1, company:1
			assertEquals(2, buckets.size());
			assertEquals(1L, buckets.get(0).get("doc_count").asLong(0));
		}
	}

	// =========================================================
	// count() Lucene Query テスト
	// =========================================================

	/**
	 * count() で Lucene Query にマッチする文書件数を返すことを確認する。
	 *
	 * <pre>
	 * id=1: text_en="Kyoto is a historic city."
	 * id=2: text_en="Nintendo is headquartered in Kyoto."
	 * id=3: text_en="Sony is a Japanese company based in Tokyo."
	 *
	 * count("text_en:Kyoto") → 2件
	 * </pre>
	 */
	public void testCountQuery001() throws Exception {

		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","category":"city"}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is headquartered in Kyoto.","category":"company"}
					""");
			search.addJson("""
					{"id":"3","body":"Sony is a Japanese company based in Tokyo.","category":"company"}
					""");
			search.commit();

			long count = search.count("Kyoto");

			System.out.println("testCountQuery001 count: " + count);

			assertEquals(2, count);
		}
	}

	/**
	 * count() で一致する文書がない場合に 0 を返すことを確認する。
	 */
	public void testCountQueryNoMatch001() throws Exception {

		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","category":"city"}
					""");
			search.addJson("""
					{"id":"2","body":"Tokyo is the capital of Japan.","category":"city"}
					""");
			search.commit();

			long count = search.count("Osaka");

			System.out.println("testCountQueryNoMatch001 count: " + count);

			assertEquals(0, count);
		}
	}

	/**
	 * count() で AND 条件の複合クエリが動作することを確認する。
	 *
	 * <pre>
	 * text_en:Kyoto → id=1,2 の 2件
	 * text_en:Kyoto AND category:company → id=2 の 1件
	 * </pre>
	 */
	public void testCountQueryAnd001() throws Exception {

		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","category":"city"}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is headquartered in Kyoto.","category":"company"}
					""");
			search.addJson("""
					{"id":"3","body":"Tokyo is the capital of Japan.","category":"city"}
					""");
			search.commit();

			// Kyoto のみ → 2件
			assertEquals(2, search.count("Kyoto"));

			// Kyoto AND category=company → 1件
			assertEquals(1, search.count("Kyoto AND category:company"));
		}
	}

	/**
	 * count(query, filters) のオーバーロードで フィールドフィルターが動作することを確認する。
	 *
	 * <pre>
	 * text_en:Kyoto → id=1,2 の 2件
	 * text_en:Kyoto + filters{category=company} → id=2 の 1件
	 * </pre>
	 */
	public void testCountQueryWithFilters001() throws Exception {

		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{"id":"1","body":"Kyoto is a historic city.","category":"city"}
					""");
			search.addJson("""
					{"id":"2","body":"Nintendo is headquartered in Kyoto.","category":"company"}
					""");
			search.addJson("""
					{"id":"3","body":"Tokyo is the capital of Japan.","category":"city"}
					""");
			search.commit();

			// フィルターなし → 2件
			assertEquals(2, search.count("Kyoto", null));

			// category=company フィルター → 1件
			assertEquals(1, search.count("Kyoto", java.util.Map.of("category", "company")));
		}
	}

	public void testAddTextAndVector001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {

			search.add("1", "Kyoto is a historic city.", new float[] { 1.0f, 0.0f });

			search.add("2", "Tokyo is the capital of Japan.", new float[] { 0.0f, 1.0f });

			search.commit();

			// Text search
			SearchResult[] textResults = search.search("Kyoto", 10);

			assertEquals(1, textResults.length);
			assertEquals("1", textResults[0].id);
			assertEquals("Kyoto is a historic city.", textResults[0].body);

			// Vector search
			SearchResult[] vectorResults = search.searchVector(new float[] { 0.9f, 0.1f }, 10);

			assertEquals(2, vectorResults.length);
			assertEquals("1", vectorResults[0].id);
			assertEquals("Kyoto is a historic city.", vectorResults[0].body);
		}
	}

	public void testAddTextVectorAndFields001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {

			search.add("1", "Nintendo is headquartered in Kyoto.", new float[] { 1.0f, 0.0f },
					java.util.Map.of("category", "company", "country", "Japan"));

			search.add("2", "Kyoto is a historic city.", new float[] { 0.8f, 0.2f },
					java.util.Map.of("category", "city", "country", "Japan"));

			search.commit();

			// Text + filter
			SearchResult[] textResults = search.search("Kyoto", 10, java.util.Map.of("category", "company"));

			assertEquals(1, textResults.length);
			assertEquals("1", textResults[0].id);

			// Vector + filter
			SearchResult[] vectorResults = search.searchVector(new float[] { 0.9f, 0.1f }, 10,
					java.util.Map.of("category", "company"));

			assertEquals(1, vectorResults.length);
			assertEquals("1", vectorResults[0].id);
			assertEquals("Nintendo is headquartered in Kyoto.", vectorResults[0].body);
		}
	}

	// =========================================================
	// kaiwa0911-1212 仕様検証テスト
	// =========================================================

	/**
	 * 1. body 入力: SearchResult.body が設定され、_source.body が存在し、_source.text_ja
	 * は存在しないことを確認する。
	 */
	public void testAddJsonBodyField001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "京都は日本の都市です"
					}
					""");
			search.commit();

			SearchResult[] results = search.search("京都", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
			assertEquals("京都は日本の都市です", results[0].body);

			String responseJson = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {
					    "match": {
					      "body": "京都"
					    }
					  }
					}
					""");
			nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(responseJson).get("hits").get("hits").get(0)
					.get("_source");

			assertEquals("京都は日本の都市です", source.get("body").asString());
			assertFalse(source.has("text_ja"));
		}
	}

	/**
	 * 2. text_ja 直接入力: "ja" エンジンで text_ja のみを投入した場合、検索可能で SearchResult.body /
	 * _source.text_ja に設定されることを確認する。
	 */
	public void testAddJsonTextJaDirect001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{
					  "id": "1",
					  "text_ja": "京都は日本の都市です"
					}
					""");
			search.commit();

			SearchResult[] results = search.search("京都", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
			assertEquals("京都は日本の都市です", results[0].body);

			String responseJson = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {
					    "match": {
					      "text_ja": "京都"
					    }
					  }
					}
					""");
			nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(responseJson).get("hits").get("hits").get(0)
					.get("_source");

			assertEquals("京都は日本の都市です", source.get("text_ja").asString());
			assertFalse(source.has("body"));
		}
	}

	/**
	 * 3. text_en 直接入力: "en" エンジンで text_en のみを投入した場合、検索可能で SearchResult.body /
	 * _source.text_en に設定されることを確認する。
	 */
	public void testAddJsonTextEnDirect001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{
					  "id": "1",
					  "text_en": "Kyoto is a historic city."
					}
					""");
			search.commit();

			SearchResult[] results = search.search("Kyoto", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
			assertEquals("Kyoto is a historic city.", results[0].body);

			String responseJson = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {
					    "match": {
					      "text_en": "Kyoto"
					    }
					  }
					}
					""");
			nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(responseJson).get("hits").get("hits").get(0)
					.get("_source");

			assertEquals("Kyoto is a historic city.", source.get("text_en").asString());
			assertFalse(source.has("body"));
		}
	}

	/**
	 * 4. body と text_en は独立した2フィールド
	 */
	public void testAddJsonDuplicateSameValue001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "Kyoto is a historic city.",
					  "text_en": "Kyoto is a historic city."
					}
					""");
			search.commit();

			SearchResult[] results = search.search("Kyoto", 10);
			assertEquals(1, results.length);
			assertEquals("Kyoto is a historic city.", results[0].body);

			String responseJson = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {
					    "match": {
					      "text_en": "Kyoto"
					    }
					  }
					}
					""");
			nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(responseJson).get("hits").get("hits").get(0)
					.get("_source");

			assertEquals("Kyoto is a historic city.", source.get("body").asString());
			assertEquals("Kyoto is a historic city.", source.get("text_en").asString());
		}
	}

	/**
	 * 5. body + text_en 異値: 異なる値でも両方正常に登録されることを確認する。
	 */
	public void testAddJsonMultipleTextFieldsDifferentValues001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "Kyoto is a historic city.",
					  "text_en": "Tokyo is the capital."
					}
					""");
			search.commit();

			assertEquals(1, search.search("Kyoto", 10).length);
			assertEquals(1, search.search("Tokyo", 10).length);
		}
	}

	/**
	 * 6. 別言語フィールド: "ja" エンジンに body と text_en を指定した場合、text_ja と text_en
	 * の両方で検索可能であることを確認する。
	 */
	public void testAddJsonOtherLanguageField001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "京都について",
					  "text_en": "About Kyoto"
					}
					""");
			search.commit();

			// 日本語検索でヒット
			SearchResult[] rJa = search.search("京都", 10);
			assertEquals(1, rJa.length);
			assertEquals("1", rJa[0].id);
			assertEquals("京都について", rJa[0].body);

			// 英語フィールド指定検索でもヒット
			SearchResult[] rEn = search.search("text_en:Kyoto", 10);
			assertEquals(1, rEn.length);
			assertEquals("1", rEn[0].id);
			assertEquals("京都について", rEn[0].body);
		}
	}

	/**
	 * 7. autoAnalyze(false): body は返り、全文検索可能で、word.* フィールドは生成されないことを確認する。
	 */
	public void testAutoAnalyzeFalse001() throws Exception {
		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "京都は日本の都市です"
					}
					""");
			search.commit();

			SearchResult[] results = search.search("京都", 10);
			assertEquals(1, results.length);
			assertEquals("京都は日本の都市です", results[0].body);

			String responseJson = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {
					    "match": {
					      "body": "京都"
					    }
					  }
					}
					""");
			nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(responseJson).get("hits").get("hits").get(0)
					.get("_source");

			assertEquals("京都は日本の都市です", source.get("body").asString());
			assertFalse(source.has("word"));
			assertFalse(source.has("word.noun"));
		}
	}

	/**
	 * 8. インデックス保存・再ロード後の新schema挙動確認: 保存・再オープン後も新 schema が保持され、body
	 * で検索結果が取得できることを確認する。
	 */
	public void testSaveAndReloadWithNewSchema001() throws Exception {
		java.nio.file.Path tempDir = java.nio.file.Files.createTempDirectory("localsearch_test_schema");

		try {
			try (LocalSearch search = LocalSearch.builder("en").build()) {
				search.addJson("""
						{
						  "id": "1",
						  "body": "Kyoto is a historic city.",
						  "category": "city"
						}
						""");
				search.commit();
				search.saveIndexTo(tempDir);
			}

			try (LocalSearch search = LocalSearch.builder("en").loadIndexFrom(tempDir).build()) {
				SearchResult[] results = search.search("Kyoto", 10);
				assertEquals(1, results.length);
				assertEquals("1", results[0].id);
				assertEquals("Kyoto is a historic city.", results[0].body);

				String responseJson = search.searchResponseJson("""
						{
						  "size": 10,
						  "query": {
						    "match": {
						      "body": "Kyoto"
						    }
						  }
						}
						""");
				nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(responseJson).get("hits").get("hits").get(0)
						.get("_source");

				assertEquals("Kyoto is a historic city.", source.get("body").asString());
				assertFalse(source.has("text_en"));
			}
		} finally {
			try (java.util.stream.Stream<java.nio.file.Path> files = java.nio.file.Files.walk(tempDir)) {
				files.sorted(java.util.Comparator.reverseOrder()).forEach(p -> {
					try {
						java.nio.file.Files.deleteIfExists(p);
					} catch (Exception ignored) {
					}
				});
			}
		}
	}

	/**
	 * 9. 旧schema互換テスト: 旧形式 schema (body なし, text_en stored=true) を読み込んだ場合でも、
	 * ensureCoreFields により body が追加され、toSearchResults() のフォールバックにより
	 * SearchResult.body が取得できることを確認する。
	 */
	public void testLegacySchemaCompatibility001() throws Exception {
		java.nio.file.Path tempDir = java.nio.file.Files.createTempDirectory("localsearch_test_legacy");

		try {
			// 旧 index に Document を直接書き込む (body なし, text_en stored)
			try (nlp4j.lucene9.LuceneIndex index = new nlp4j.lucene9.LuceneIndex()) {
				org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
				doc.add(new org.apache.lucene.document.StringField("id", "legacy1",
						org.apache.lucene.document.Field.Store.YES));
				doc.add(new org.apache.lucene.document.TextField("text_en", "Legacy document body",
						org.apache.lucene.document.Field.Store.YES));
				index.add(doc);
				index.commit();
				index.writeToAndClose(tempDir);
			}

			// 旧形式の schema.json を手動作成 (body なし, text_en stored=true)
			nlp4j.lucene9.SearchSchema legacySchema = new nlp4j.lucene9.SearchSchema();
			legacySchema.add("id", nlp4j.lucene9.FieldTypeDef.keyword().stored(true));
			legacySchema.add("text_en", nlp4j.lucene9.FieldTypeDef.text().stored(true));
			legacySchema.add("data", nlp4j.lucene9.FieldTypeDef.storedOnly());
			nlp4j.lucene9.SearchSchemaStore.save(tempDir, legacySchema);

			// LocalSearch でロードして検索
			try (LocalSearch search = LocalSearch.builder("en").loadIndexFrom(tempDir).build()) {
				SearchResult[] results = search.search("Legacy", 10);
				assertEquals(1, results.length);
				assertEquals("legacy1", results[0].id);
				assertEquals("Legacy document body", results[0].body);

				// 新規ドキュメントも問題なく追加可能であること (ensureCoreFields で body が追加されているため)
				search.addJson("""
						{
						  "id": "new1",
						  "body": "New document body"
						}
						""");
				search.commit();

				SearchResult[] newResults = search.search("New", 10);
				assertEquals(1, newResults.length);
				assertEquals("new1", newResults[0].id);
				assertEquals("New document body", newResults[0].body);
			}
		} finally {
			try (java.util.stream.Stream<java.nio.file.Path> files = java.nio.file.Files.walk(tempDir)) {
				files.sorted(java.util.Comparator.reverseOrder()).forEach(p -> {
					try {
						java.nio.file.Files.deleteIfExists(p);
					} catch (Exception ignored) {
					}
				});
			}
		}
	}

	public void testAddJsonTextFieldStored001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{
					  "id": "1",
					  "text": "京都は日本の都市です"
					}
					""");
			search.commit();

			SearchResult[] results = search.search("京都", 10);

			assertEquals(1, results.length);
			assertEquals("京都は日本の都市です", results[0].body);

			String json = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {"match": {"text": "京都"}}
					}
					""");

			nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(json).get("hits").get("hits").get(0).get("_source");

			assertEquals("京都は日本の都市です", source.get("text").asString());

			assertFalse(source.has("body"));
			assertFalse(source.has("text_ja"));
		}
	}

	public void testAddJsonNoTextField001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			try {
				search.addJson("""
						{
						  "id": "1",
						  "category": "city"
						}
						""");
				fail("at least one text field or vector is required");
			} catch (LocalSearchException e) {
				assertTrue(e.getMessage().contains("Required field is missing"));
			}
		}
	}

	public void testCoreTextFieldSchema001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {

			assertEquals( //
					nlp4j.lucene9.FieldTypeDef.Kind.TEXT, //
					search.getSchema().get("body").kind() //
			);

			assertEquals( //
					nlp4j.lucene9.FieldTypeDef.Kind.TEXT, //
					search.getSchema().get("text").kind() //
			);

			assertEquals( //
					nlp4j.lucene9.FieldTypeDef.Kind.TEXT, //
					search.getSchema().get("text_ja").kind() //
			);

			assertEquals( //
					nlp4j.lucene9.FieldTypeDef.Kind.TEXT, //
					search.getSchema().get("text_en").kind() //
			);

			assertTrue( //
					search.getSchema().get("body").is_stored() //
			);
			assertTrue( //
					search.getSchema().get("text").is_stored() //
			);
			assertTrue( //
					search.getSchema().get("text_ja").is_stored() //
			);
			assertTrue( //
					search.getSchema().get("text_en").is_stored() //
			);
		}
	}

	public void testAddDefaultTextJa001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "京都は日本の都市です");
			search.commit();

			SearchResult[] results = search.search("京都", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
			assertEquals("京都は日本の都市です", results[0].body);

			String json = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {"match": {"text_ja": "京都"}}
					}
					""");
			nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(json).get("hits").get("hits").get(0).get("_source");
			assertEquals("京都は日本の都市です", source.get("text_ja").asString());
			assertFalse(source.has("body"));
		}
	}

	public void testAddDefaultTextEn001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.add("1", "Kyoto is a historic city.");
			search.commit();

			SearchResult[] results = search.search("Kyoto", 10);
			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
			assertEquals("Kyoto is a historic city.", results[0].body);

			String json = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {"match": {"text_en": "Kyoto"}}
					}
					""");
			nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(json).get("hits").get("hits").get(0).get("_source");
			assertEquals("Kyoto is a historic city.", source.get("text_en").asString());
			assertFalse(source.has("body"));
		}
	}

	public void testAddJsonMultipleTextFields001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{
					  "id": "1",
					  "body": "京都について",
					  "text": "テキスト",
					  "text_ja": "日本語テキスト",
					  "text_en": "English text"
					}
					""");
			search.commit();

			String json = search.searchResponseJson("""
					{
					  "size": 10,
					  "query": {"match_all": {}}
					}
					""");
			nlp4j.json.JsonNode source = nlp4j.json.JsonNode.parse(json).get("hits").get("hits").get(0).get("_source");
			assertEquals("京都について", source.get("body").asString());
			assertEquals("テキスト", source.get("text").asString());
			assertEquals("日本語テキスト", source.get("text_ja").asString());
			assertEquals("English text", source.get("text_en").asString());
		}
	}

	public void testDefaultSearchFieldsJa001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "京都 test"); // text_ja
			search.addJson("""
					{"id":"2","text":"京都 test"}
					""");
			search.addJson("""
					{"id":"3","body":"京都 test"}
					""");
			search.commit();

			SearchResult[] results = search.search("京都", 10);
			assertEquals(3, results.length);
		}
	}

	public void testDefaultSearchFieldsEn001() throws Exception {
		try (LocalSearch search = new LocalSearch("en")) {
			search.add("1", "Kyoto test"); // text_en
			search.addJson("""
					{"id":"2","text":"Kyoto test"}
					""");
			search.addJson("""
					{"id":"3","body":"Kyoto test"}
					""");
			search.commit();

			SearchResult[] results = search.search("Kyoto", 10);
			assertEquals(3, results.length);
		}
	}

	public void testExplicitTextFieldSearch001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.addJson("""
					{"id":"1","body":"Kyoto body"}
					""");
			search.addJson("""
					{"id":"2","text":"Kyoto text"}
					""");
			search.addJson("""
					{"id":"3","text_en":"Kyoto english"}
					""");
			search.commit();

			assertEquals(1, search.search("body:Kyoto", 10).length);
			assertEquals("1", search.search("body:Kyoto", 10)[0].id);

			assertEquals(1, search.search("text:Kyoto", 10).length);
			assertEquals("2", search.search("text:Kyoto", 10)[0].id);

			assertEquals(1, search.search("text_en:Kyoto", 10).length);
			assertEquals("3", search.search("text_en:Kyoto", 10)[0].id);
		}
	}

	public void testDefaultSearchFieldsJaExcludeTextEn001() throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {

			search.addJson("""
					{"id":"1","text_en":"Kyoto only"}
					""");

			search.commit();

			// ja のデフォルト検索対象は text_ja, text, body
			assertEquals(0, search.search("Kyoto", 10).length);

			// 明示すれば検索できる
			SearchResult[] results = search.search("text_en:Kyoto", 10);

			assertEquals(1, results.length);
			assertEquals("1", results[0].id);
		}
	}

	// =========================================================
	// MultiValued Field - 1要素配列のバグ回帰テスト
	// =========================================================

	/**
	 * addJson() で最初のドキュメントに1要素の JSON 配列を含む場合でも、 multiValued=true
	 * として登録され、続く複数要素配列でも例外が発生しないことを確認する。
	 *
	 * <p>
	 * Wikipedia 漫画 JSONL のような「最初の文書は1カテゴリ、次の文書は複数カテゴリ」 というパターンで起きたバグの回帰テスト。
	 * </p>
	 */
	public void testMultiValuedSingleElementArray001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {

			// 最初の文書では配列だが要素数は1
			search.addJson("""
					{
					  "id":"222",
					  "text_ja":"日本の漫画家では、日本における漫画家について解説する。",
					  "category_s":["日本の漫画家"]
					}
					""");

			// この時点ですでに multiValued=true であること
			assertTrue(search.getSchema().contains("category_s"));
			assertTrue(search.getSchema().get("category_s").is_multiValued());

			// 次の文書では2要素
			search.addJson("""
					{
					  "id":"224",
					  "text_ja":"日本の漫画作品一覧。",
					  "category_s":["漫画作品一覧","日本の漫画"]
					}
					""");

			// さらに多数要素
			search.addJson("""
					{
					  "id":"225",
					  "text_ja":"うる星やつらは、高橋留美子による日本の漫画。",
					  "category_s":[
					    "うる星やつら",
					    "高橋留美子の漫画作品",
					    "1978年の漫画",
					    "SF漫画作品",
					    "恋愛漫画"
					  ]
					}
					""");

			search.commit();

			assertEquals(3L, search.count());

			assertEquals(1L, search.count("category_s:恋愛漫画"));

			assertEquals(1L, search.count("category_s:日本の漫画"));

			// aggregation まで確認: JSON array → multiValued schema → index → DocValues
			// aggregation
			java.util.Map<String, Long> categories = search.aggregate("category_s", 100);

			assertEquals(Long.valueOf(1L), categories.get("日本の漫画家"));

			assertEquals(Long.valueOf(1L), categories.get("日本の漫画"));

			assertEquals(Long.valueOf(1L), categories.get("恋愛漫画"));
		}
	}

	/**
	 * addJson() で最初に scalar 値として登録されたフィールドを、 後から JSON 配列として渡すと LocalSearchException
	 * が発生することを確認する。
	 *
	 * <p>
	 * scalar → array のスキーマ変更は Lucene の DocValues 型変更を引き起こすため、 明示的なエラーにすることが安全。
	 * </p>
	 */
	public void testMultiValuedScalarThenArrayThrows001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("""
					{
					  "id":"1",
					  "body":"doc1",
					  "tags":"Japan"
					}
					""");

			try {
				search.addJson("""
						{
						  "id":"2",
						  "body":"doc2",
						  "tags":["Japan","city"]
						}
						""");

				fail("Expected LocalSearchException");

			} catch (LocalSearchException e) {
				assertTrue(e.getMessage().contains("single-valued") || e.getMessage().contains("multiple values"));
			}
		}
	}

	public void testAddJsonAutoAnalyzeTrue001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).build()) {

			search.addJson("""
					{
					  "id":"222",
					  "text_ja":"日本の漫画家では、日本における漫画家について解説する。",
					  "category_s":["日本の漫画家"]
					}
					""");

			search.commit();

			assertEquals(1L, search.count());

			// 通常全文検索
			assertEquals(1, search.search("漫画家", 10).length);

			// autoAnalyze により word.* が生成されていること
			assertTrue(search.aggregate("word.noun", 100).size() > 0);
		}
	}

	public void testAddJsonAutoAnalyzeTrue001b() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).build()) {

			search.addJson("""
					{
					  "id":"222",
					  "text_ja":"日本の漫画家では、日本における漫画家について解説する。",
					  "category_s":["日本の漫画家"]
					}
					""");
			search.addJson("""
					{
					  "id":"223",
					  "text_ja":"日本の画家では、日本における画家について解説する。",
					  "category_s":["日本の画家"]
					}
					""");

			search.commit();

			assertEquals(2L, search.count());

			// 通常全文検索
			assertEquals(1, search.search("漫画家", 10).length);

			// autoAnalyze により word.* が生成されていること
			assertTrue(search.aggregate("word.noun", 100).size() > 0);
		}
	}

	public void testAddJsonAutoAnalyzeConj001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(true).build()) {

			search.addJson(
					"""
							{
							  "id":"580",
							  "text_ja":"『秘密戦隊ゴレンジャー』は、1975年4月5日から1977年3月26日まで、NET系列で毎週土曜19時30分から20時に全84話が放送された、NET・東映制作の特撮テレビドラマ特撮全史134}}、および作中に登場するヒーローチームの名称。",
							  "category_s":["秘密戦隊ゴレンジャー"]
							}
							""");

			search.commit();

			assertEquals(1L, search.count());

			assertTrue(search.getSchema().contains("word.conj"));

			assertTrue(search.aggregate("word.conj", 100).size() > 0);
		}
	}

	// =========================================================
	// KEYWORD field with colon and spaces in quoted Lucene Query
	// =========================================================

	/**
	 * _s サフィックスフィールドで「コロン＋スペース」を含む KEYWORD 値を 引用符付き Lucene Query で検索できることを確認する。
	 *
	 * <p>
	 * component_s:"POWER TRAIN:AUTOMATIC TRANSMISSION" という Lucene Query が正しく
	 * TermQuery に変換され、count / search / aggregate の 3 API で 一貫した結果が得られること。
	 * </p>
	 *
	 * <pre>
	 * Documents:
	 *   id=1 model_s=SENTRA  component_s="POWER TRAIN:AUTOMATIC TRANSMISSION"
	 *   id=2 model_s=SENTRA  component_s="POWER TRAIN:AUTOMATIC TRANSMISSION"
	 *   id=3 model_s=SENTRA  component_s="WHEELS"
	 *   id=4 model_s=ALTIMA  component_s="POWER TRAIN:AUTOMATIC TRANSMISSION"
	 *
	 * count("model_s:SENTRA") = 3
	 * count("component_s:\"POWER TRAIN:AUTOMATIC TRANSMISSION\"") = 3
	 * count("model_s:SENTRA AND component_s:\"POWER TRAIN:AUTOMATIC TRANSMISSION\"") = 2
	 * search(同上) = 2件
	 * aggregate("component_s", 同上, 100) → POWER TRAIN:AUTOMATIC TRANSMISSION → 2
	 * </pre>
	 */
	public void testKeywordQuotedValueWithColon001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("""
					{"id":"1","body":"doc1","model_s":"SENTRA",
					 "component_s":"POWER TRAIN:AUTOMATIC TRANSMISSION"}
					""");

			search.addJson("""
					{"id":"2","body":"doc2","model_s":"SENTRA",
					 "component_s":"POWER TRAIN:AUTOMATIC TRANSMISSION"}
					""");

			search.addJson("""
					{"id":"3","body":"doc3","model_s":"SENTRA",
					 "component_s":"WHEELS"}
					""");

			search.addJson("""
					{"id":"4","body":"doc4","model_s":"ALTIMA",
					 "component_s":"POWER TRAIN:AUTOMATIC TRANSMISSION"}
					""");

			search.commit();

			// model_s:SENTRA → 3件
			assertEquals(3L, search.count("model_s:SENTRA"));

			// component_s:"POWER TRAIN:AUTOMATIC TRANSMISSION" → 3件
			assertEquals(3L, search.count("component_s:" + "\"POWER TRAIN:AUTOMATIC TRANSMISSION\""));

			String query = "model_s:SENTRA AND " + "component_s:" + "\"POWER TRAIN:AUTOMATIC TRANSMISSION\"";

			// count → 2件
			assertEquals(2L, search.count(query));

			// search → 2件
			assertEquals(2, search.search(query, 10).length);

			// aggregate → POWER TRAIN:AUTOMATIC TRANSMISSION → 2
			java.util.Map<String, Long> result = search.aggregate("component_s", query, 100);

			assertEquals(Long.valueOf(2L), result.get("POWER TRAIN:AUTOMATIC TRANSMISSION"));
		}
	}

	public void testValidateKeywordQuotedValueWithColon001() throws Exception {

		try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {

			search.addJson("""
					{"id":"1",
					 "model_s":"SENTRA",
					 "text_en":"this is test",
					 "component_s":"POWER TRAIN:AUTOMATIC TRANSMISSION"}
					""");
			search.commit();

			String query = "model_s:SENTRA AND " + "component_s:" + "\"POWER TRAIN:AUTOMATIC TRANSMISSION\"";

			LuceneQueryValidationResult result = search.validateQuery(query);

			assertTrue(result.getMessage(), result.isValid());
		}
	}

}
