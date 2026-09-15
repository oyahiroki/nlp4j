# ベクトル検索実装計画

## 概要

[`LuceneIndex_HelloMain3.java`](src/test/java/nlp4j/lucene9/LuceneIndex_HelloMain3.java)のサンプルコードでベクトル検索を実現するための実装計画です。

## 現状の問題点

### 1. スキーマとクエリの不一致
- スキーマ: `vector`フィールド（2次元）を定義
- クエリ: `embedding`フィールドを参照
- **修正必要**: フィールド名を統一する

### 2. knnクエリ未対応
- [`SearchRequestParser`](src/main/java/nlp4j/lucene9/SearchRequestParser.java)が`knn`パラメータを処理していない
- 現在は`query`パラメータのみをパース

### 3. ベクトル検索クエリ未実装
- [`LuceneQueryBuilder`](src/main/java/nlp4j/lucene9/LuceneQueryBuilder.java)が以下のクエリタイプのみサポート:
  - `match_all`
  - `term`
  - `match`
  - `query_string`
- `knn`クエリタイプが未実装

### 4. フィルタ付きベクトル検索未対応
- Luceneの`KnnFloatVectorQuery`はフィルタをサポート
- しかし現在の実装では活用されていない

## 実装方針: アプローチB（シンプルな実装）

`knn`を`query`の一種として扱い、既存のクエリビルダーに統合します。

### メリット
- 既存のアーキテクチャを大きく変更しない
- コード変更が最小限
- 理解しやすいシンプルな実装

### デメリット
- OpenSearch完全互換ではない（`knn`と`query`の同時指定が不可）
- 将来的に複雑なクエリ組み合わせが必要になった場合、リファクタリングが必要

## 実装手順

### ステップ1: LuceneQueryBuilderの拡張

**ファイル**: [`src/main/java/nlp4j/lucene9/LuceneQueryBuilder.java`](src/main/java/nlp4j/lucene9/LuceneQueryBuilder.java)

**変更内容**:
```java
// build()メソッドに追加
if (queryJson.has("knn")) {
    return buildKnnQuery(queryJson.get("knn"));
}

// 新規メソッド追加
private static Query buildKnnQuery(JsonNode knnJson) {
    String field = knnJson.get("field").asString();
    JsonNode vectorNode = knnJson.get("query_vector");
    int k = knnJson.get("k").asInt();
    
    // query_vectorをfloat[]に変換
    float[] queryVector = parseFloatArray(vectorNode);
    
    // フィルタがある場合
    if (knnJson.has("filter")) {
        Query filterQuery = build(knnJson.get("filter"), analyzer);
        return new KnnFloatVectorQuery(field, queryVector, k, filterQuery);
    }
    
    // フィルタなし
    return KnnFloatVectorField.newVectorQuery(field, queryVector, k);
}

private static float[] parseFloatArray(JsonNode arrayNode) {
    // JsonNodeの配列をfloat[]に変換
}
```

**必要なimport**:
```java
import org.apache.lucene.document.KnnFloatVectorField;
import org.apache.lucene.search.KnnFloatVectorQuery;
```

### ステップ2: SearchRequestParserの拡張

**ファイル**: [`src/main/java/nlp4j/lucene9/SearchRequestParser.java`](src/main/java/nlp4j/lucene9/SearchRequestParser.java)

**変更内容**:
```java
public static SearchRequest parse(String path, JsonNode body) {
    String indexName = parseIndexName(path);
    int from = body.has("from") ? body.get("from").asInt(0) : 0;
    int size = body.has("size") ? body.get("size").asInt(10) : 10;

    JsonNode query;
    
    // knnパラメータがある場合、それをqueryとして扱う
    if (body.has("knn")) {
        query = JsonNode.object();
        query.put("knn", body.get("knn"));
    } else if (body.has("query")) {
        query = body.get("query");
    } else {
        query = defaultMatchAllQuery();
    }

    JsonNode aggs = null;
    if (body.has("aggs")) {
        aggs = body.get("aggs");
    } else if (body.has("aggregations")) {
        aggs = body.get("aggregations");
    }

    return new SearchRequest(indexName, from, size, query, aggs);
}
```

### ステップ3: サンプルコードの修正

**ファイル**: [`src/test/java/nlp4j/lucene9/LuceneIndex_HelloMain3.java`](src/test/java/nlp4j/lucene9/LuceneIndex_HelloMain3.java)

**修正箇所**:

1. **フィールド名の統一** (行71):
   ```java
   // 修正前: "field": "embedding"
   // 修正後: "field": "vector"
   "field": "vector",
   ```

2. **クエリベクトルの次元を2次元に修正** (行72-75):
   ```java
   // 修正前: [0.12, -0.03]
   // 修正後: [1.0, 0.0] または [0.0, 1.0]
   "query_vector": [1.0, 0.0],
   ```

3. **フィルタクエリのフィールド名を実際のフィールドに修正** (行83, 90):
   ```java
   // 修正前: "text": "東京 観光", "lang": "ja"
   // 修正後: "text_ja": "東京", "category": "greeting"
   ```

### ステップ4: 動作確認

1. サンプルコードを実行
2. ベクトル検索が正常に動作することを確認
3. フィルタ付きベクトル検索が正常に動作することを確認

## 実装後のクエリ例

### 基本的なベクトル検索
```json
{
  "size": 10,
  "knn": {
    "field": "vector",
    "query_vector": [1.0, 0.0],
    "k": 10
  }
}
```

### フィルタ付きベクトル検索
```json
{
  "size": 10,
  "knn": {
    "field": "vector",
    "query_vector": [1.0, 0.0],
    "k": 10,
    "filter": {
      "bool": {
        "must": [
          {
            "match": {
              "text_ja": "東京"
            }
          }
        ],
        "filter": [
          {
            "term": {
              "category": "greeting"
            }
          }
        ]
      }
    }
  }
}
```

## 参考実装

[`hello/lucene/LuceneVectorSearchExample.java`](src/test/java/hello/lucene/LuceneVectorSearchExample.java)に、Luceneの低レベルAPIを使用したベクトル検索の実装例があります。

## 注意事項

1. **ベクトルの次元**: スキーマで定義した次元数と一致する必要があります
2. **類似度関数**: デフォルトは`COSINE`ですが、[`FieldTypeDef.similarity()`](src/main/java/nlp4j/lucene9/FieldTypeDef.java:90)で変更可能
3. **kパラメータ**: 返却する最大件数を指定します
4. **num_candidates**: 現在の実装では未サポート（将来的な拡張ポイント）

## 将来の拡張ポイント

1. **OpenSearch完全互換**: `knn`と`query`の同時指定をサポート
2. **num_candidatesサポート**: より高度なベクトル検索の最適化
3. **複数ベクトルフィールド**: 異なるベクトルフィールドでの検索
4. **ハイブリッド検索**: キーワード検索とベクトル検索のスコア統合