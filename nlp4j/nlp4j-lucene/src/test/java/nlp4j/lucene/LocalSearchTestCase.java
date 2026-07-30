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
	 * addJson() で登録した追加フィールド（category）をフィールド検索できることを確認する。
	 * category=技術 を持つドキュメントのみが返ること。
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
	 * addJson() で body フィールド検索と category フィールド検索を組み合わせて動作確認する。
	 * body の全文検索と追加フィールド検索が共存できることを確認する。
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
			search.addJson("""
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
	 * addJson() で追加したドキュメントの SearchResult.data に元 JSON が入ることを確認する。
	 * add(id, body) で追加した場合は data が null であることも確認する。
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
	 * aggregateJson() で category フィールドの terms aggregation が動作することを確認する。
	 * バケット件数と key の検証を行う。
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
					{"field":"category","size":10}
					""");
			System.out.println("testAggregateJson001: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			assertEquals("category", result.get("field").asString());

			nlp4j.json.JsonNode buckets = result.get("buckets");
			assertEquals(2, buckets.size());
			// 先頭バケットは件数最多の "観光"
			assertEquals("観光", buckets.get(0).get("key").asString());
			assertEquals(3, buckets.get(0).get("count").asInt());
			assertEquals("技術", buckets.get(1).get("key").asString());
			assertEquals(2, buckets.get(1).get("count").asInt());
		}
	}

	/**
	 * aggregateJson() で全文検索クエリで絞り込んだ上での集計が動作することを確認する。
	 * query=東京 で絞り込むと category=観光, 技術 の 2 バケットがヒットすること。
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
			assertEquals("category", result.get("field").asString());

			nlp4j.json.JsonNode buckets = result.get("buckets");
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
			search.commit();

			// size=2 で上位 2 バケットのみ返す
			String json = search.aggregateJson("""
					{"field":"category","size":2}
					""");
			System.out.println("testAggregateJson003: " + json);

			nlp4j.json.JsonNode result = nlp4j.json.JsonNode.parse(json);
			nlp4j.json.JsonNode buckets = result.get("buckets");
			assertEquals(2, buckets.size());
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
			search.commit();

			SearchResult[] results = search.search(
					"Kyoto",
					10,
					java.util.Map.of("category", "company")
			);

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
			nlp4j.json.JsonNode buckets = result.get("buckets");

			// city: 2, company: 1 の 2 バケット
			assertEquals(2, buckets.size());
			assertEquals("city", buckets.get(0).get("key").asString());
			assertEquals(2, buckets.get(0).get("count").asInt());
			assertEquals("company", buckets.get(1).get("key").asString());
			assertEquals(1, buckets.get(1).get("count").asInt());
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
			SearchResult[] results = search.search(
					"Kyoto",
					10,
					java.util.Map.of("category", "company")
			);
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

			nlp4j.json.JsonNode aggResult = nlp4j.json.JsonNode.parse(agg);
			assertEquals("category", aggResult.get("field").asString());
			assertEquals(2, aggResult.get("buckets").size());
		}
	}

}
