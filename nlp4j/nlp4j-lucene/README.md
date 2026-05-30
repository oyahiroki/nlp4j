# nlp4j-lucene

Apache Lucene を使用したローカル検索ライブラリ

## 概要

nlp4j-lucene は、Apache Lucene 9.12.2 をベースにした、シンプルで使いやすいローカル検索ライブラリです。OpenSearch/Elasticsearch 互換の JSON API を提供し、日本語と英語のテキスト検索をサポートします。

## 主な機能

- **シンプルなAPI**: 簡単に使える高レベルAPIを提供
- **多言語対応**: 日本語（Kuromoji）、英語（EnglishAnalyzer）をサポート
- **OpenSearch互換**: OpenSearch/Elasticsearch スタイルの JSON クエリをサポート
- **インメモリ検索**: 高速なインメモリインデックス
- **集約機能**: Terms Aggregation などの集約クエリをサポート
- **柔軟なスキーマ**: カスタマイズ可能なフィールド定義

## 必要要件

- Java 17 以上
- Apache Lucene 9.12.2
- nlp4j-core 1.3.7.21

## インストール

Maven を使用する場合、`pom.xml` に以下の依存関係を追加してください：

```xml
<dependency>
    <groupId>org.nlp4j</groupId>
    <artifactId>nlp4j-lucene</artifactId>
    <version>1.0.0.0</version>
</dependency>
```

## 使い方

### 基本的な使い方（日本語検索）

```java
import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

public class Example {
    public static void main(String[] args) {
        // 日本語検索インスタンスを作成
        try (LocalSearch search = new LocalSearch("ja")) {
            // ドキュメントを追加
            search.add("1", "東京都は日本の都道府県のひとつです");
            search.add("2", "京都は日本の都市です。");
            search.add("3", "京都市には任天堂の本社があります");
            
            // JSONフォーマットでも追加可能
            search.addJson("""
                {
                    "id":"4",
                    "body":"京都府は広いです"
                }
                """);
            
            // インデックスをコミット
            search.commit();
            
            // 検索を実行
            SearchResult[] results = search.search("京都", 10);
            
            // 結果を表示
            System.out.println("検索結果数: " + results.length);
            for (int i = 0; i < results.length; i++) {
                System.out.println("ID: " + results[i].id);
                System.out.println("本文: " + results[i].body);
                System.out.println("スコア: " + results[i].score);
            }
        }
    }
}
```

### 高度な使い方（OpenSearch互換API）

```java
import nlp4j.lucene9.LuceneIndex;
import nlp4j.lucene9.LuceneLocalSearchApi;
import nlp4j.json.JsonNode;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class AdvancedExample {
    public static void main(String[] args) throws Exception {
        // インデックスを作成
        try (LuceneIndex index = new LuceneIndex()) {
            // ドキュメントを追加
            Document doc = new Document();
            doc.add(new StringField("id", "1", Field.Store.YES));
            doc.add(new TextField("content", "Hello Lucene", Field.Store.YES));
            index.add(doc);
            
            // 検索APIを作成
            LuceneLocalSearchApi api = new LuceneLocalSearchApi(index);
            
            // Match All クエリ
            JsonNode matchAllRequest = JsonNode.object()
                .put("query", JsonNode.object()
                    .put("match_all", JsonNode.object()))
                .put("size", 10);
            
            JsonNode result = api.search("myindex/_search", matchAllRequest);
            System.out.println(result.toJson());
            
            // Match クエリ
            JsonNode matchRequest = JsonNode.object()
                .put("query", JsonNode.object()
                    .put("match", JsonNode.object()
                        .put("content", "Lucene")))
                .put("size", 10);
            
            result = api.search("myindex/_search", matchRequest);
            System.out.println(result.toJson());
            
            // Terms Aggregation
            JsonNode aggRequest = JsonNode.object()
                .put("query", JsonNode.object()
                    .put("match_all", JsonNode.object()))
                .put("size", 0)
                .put("aggs", JsonNode.object()
                    .put("categories", JsonNode.object()
                        .put("terms", JsonNode.object()
                            .put("field", "category")
                            .put("size", 10))));
            
            result = api.search("myindex/_search", aggRequest);
            System.out.println(result.toJson());
        }
    }
}
```

## サポートされているクエリタイプ

- **match_all**: すべてのドキュメントを取得
- **term**: 完全一致検索
- **match**: テキスト検索（アナライザーを使用）
- **query_string**: 複雑なクエリ構文をサポート

## サポートされている集約

- **terms**: フィールドの値でグループ化して集計

## プロジェクト構成

```
nlp4j-lucene/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── nlp4j/
│   │           ├── json/          # JSON処理ユーティリティ
│   │           ├── lucene/        # 高レベルAPI
│   │           │   ├── LocalSearch.java
│   │           │   ├── SearchResult.java
│   │           │   └── LocalSearchException.java
│   │           └── lucene9/       # 低レベルLucene API
│   │               ├── LuceneIndex.java
│   │               ├── LuceneLocalSearchApi.java
│   │               ├── SearchSchema.java
│   │               ├── FieldTypeDef.java
│   │               └── ...
│   └── test/
│       └── java/
│           └── nlp4j/
│               └── lucene/
│                   └── LocalSearchTestCase.java
└── pom.xml
```

## 主要クラス

### LocalSearch
シンプルなローカル検索APIを提供する高レベルクラス。言語を指定して簡単に検索機能を実装できます。

### LuceneIndex
Luceneインデックスの管理を行う低レベルクラス。ドキュメントの追加、検索セッションの管理を担当します。

### LuceneLocalSearchApi
OpenSearch互換のJSON APIを提供するクラス。JSONリクエストを受け取り、JSON レスポンスを返します。

### SearchSchema
ドキュメントのスキーマ定義を管理するクラス。フィールド名とフィールドタイプの対応を定義します。

## ライセンス

Apache License, Version 2.0

## リンク

- プロジェクトURL: https://nlp4j.org/
- Apache Lucene: https://lucene.apache.org/

## 開発者向け情報

### ビルド方法

```bash
mvn clean install
```

### テスト実行

```bash
mvn test
```

## サンプルコード

プロジェクトには以下のサンプルコードが含まれています：

- `LocalSearchExampleMain_Ja.java`: 日本語検索の基本的な使い方
- `HelloNlp4jLuceneMain.java`: OpenSearch互換APIの使い方
- `HelloNlp4jLuceneMain2.java`: より高度な使用例

## 貢献

バグ報告や機能リクエストは、プロジェクトのIssueトラッカーまでお願いします。

## バージョン情報

- nlp4j-lucene: 1.0.0.0
- Apache Lucene: 9.12.2
- nlp4j-core: 1.3.7.21
- Java: 17+