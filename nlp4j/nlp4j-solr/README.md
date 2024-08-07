# NLP4J Solr

NLP4J Library for Apache Solr

# ベクトル検索を行う

# 0. 前提

Docker を利用する


# 1. Solr の起動と設定

## Docker で Solr ("my_solr") を起動する

```
docker run -d -p 8983:8983 --name my_solr solr
```

```
docker run: 新しい Docker コンテナを起動するためのコマンドです。
-d: コンテナをデタッチドモード（バックグラウンド）で実行するオプションです。これにより、コンテナがバックグラウンドで実行され、コマンドプロンプトが直ちに戻ります。
-p 8983:8983: ホストマシンのポート（左側）をコンテナのポート（右側）にマッピングします。この場合、ホストのポート 8983 をコンテナ内のポート 8983 にマッピングして、ホストから Solr のウェブインターフェースにアクセスできるようにします。
--name my_solr: コンテナに my_solr という名前を付けます。これにより、後でコンテナを参照するときに、この名前を使って操作できます。
solr: 実行するイメージの名前です。この場合、Docker Hub にある solr イメージが使用されます。
```

## Solr Admin Console

[http://localhost:8983/solr/](http://localhost:8983/solr/)


## Docker で core ("sandbox") を作成する

```
docker exec -it my_solr bin/solr create -c sandbox
```

```
docker exec: Docker コンテナ内でコマンドを実行するためのコマンドです。
-it: -i はインタラクティブモードを有効にし、-t は擬似ターミナルを割り当てるためのオプションです。これにより、コマンド実行後にインタラクティブに操作できるようになります。
my_solr: コマンドを実行する対象の Docker コンテナの名前または ID です。この場合、my_solr という名前のコンテナが対象です。
bin/solr: コンテナ内で実行するコマンドです。ここでは、Solr のコマンドラインツール solr を実行しています。
create: Solr のコマンドで、新しいコレクションを作成するためのサブコマンドです。
-c sandbox: -c オプションは作成するコレクションの名前を指定します。この場合、sandbox という名前のコレクションが作成されます。
```


## フィールドタイプ ("vector1024",solr.DenseVectorField) の定義 

```
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field-type":{"name":"vector1024","class":"solr.DenseVectorField","vectorDimension":1024,"similarityFunction":"cosine"}}' http://localhost:8983/solr/sandbox/schema
```

## フィールド ("vector") の定義 

```
curl -X POST -H 'Content-type:application/json' --data-binary '{ "add-field":{ "name":"vector", "type":"vector1024", "indexed":true, "stored":true } }' http://localhost:8983/solr/sandbox/schema
```

# 2. Embedding Server の起動

セットアップは以下を参照

[https://github.com/oyahiroki/nlp4j-llm-embeddings-e5](https://github.com/oyahiroki/nlp4j-llm-embeddings-e5)


```
$ python3 nlp4j-embedding-server-e5.py
```

# 3. ドキュメントの追加

```
// Hello002_ImportDocumentWithVector2.java

String endPoint = "http://localhost:8983/solr/";
String collection = "sandbox";

List<Document> docs = new ArrayList<>();
{
	docs.add((new DocumentBuilder()).id("doc001").text("今日はいい天気です") //
			.put("field1_s", "AAA").put("field2_i", 100).build());
	docs.add((new DocumentBuilder()).id("doc002").text("明日はいい天気かしら。天気予報を見てみよう。") //
			.put("field1_s", "BBB").put("field2_i", 101).build());
}
{ // Kuromoji
	KuromojiAnnotator ann = new KuromojiAnnotator();
	ann.setProperty("target", "text");
	ann.annotate(docs);
}
{ // Keyword Facet Mapping for Solr
	KeywordFacetMappingAnnotator ann = new KeywordFacetMappingAnnotator();
	ann.setProperty("mapping", KeywordFacetMappingAnnotator.DEFAULT_MAPPING);
	ann.annotate(docs);
}
{ // Vector
	DocumentAnnotator ann = new EmbeddingAnnotator();
	ann.setProperty("target", "text");
	ann.annotate(docs); // vector size is 1024
}

try (SolrDocumentImporter importer = new SolrDocumentImporter()) {
	importer.setProperty("endPoint", endPoint);
	importer.setProperty("collection", collection);
	importer.setProperty("keyword_facet_field_mapping", //
			"word->word_ss" //
	);
	importer.setProperty("attribute_field_mapping", //
			"text->text_txt_ja" //
	);
	importer.importDocuments(docs);
	importer.commit();
}
```

# 4. 検索

```
String endPoint = "http://localhost:8983/";
String indexName = "sandbox";

Document doc = new DefaultDocument();
{
	doc.putAttribute("id", "doc001");
	doc.putAttribute("text", "今日はいい天気かもしれない。");
}

{ // Vector
	DocumentAnnotator ann = new EmbeddingAnnotator();
	ann.setProperty("target", "text");
	ann.annotate(doc);
	System.err.println("size_of_vector: " + doc.getAttributeAsListNumbers("vector").size());
}
{
	SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
	try {
		JsonObject responseObject = client.searchByVector(indexName, doc);
		System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));
	} catch (IOException e) {
		System.err.println("SKIP THIS TEST: " + e.getMessage());
	} catch (RemoteSolrException e) {
		System.err.println("SKIP THIS TEST: " + e.getMessage());
	}
}

```

# 4. 検索（ベクトル検索を使わない）

```
String endPoint = "http://localhost:8983/solr/";
String collection = "sandbox";

try (Http2SolrClient solrClient = new Http2SolrClient.Builder(endPoint) //
		.withConnectionTimeout(10 * 1000, TimeUnit.MILLISECONDS)//
		.build();) {
	final Map<String, String[]> requestParamsSolr = new HashMap<>();
	requestParamsSolr.put("q", new String[] { "text_txt_ja:今日" });
	requestParamsSolr.put("fl", new String[] { "text_txt_ja" });
	MultiMapSolrParams solrQueryParams = new MultiMapSolrParams(requestParamsSolr);
	QueryResponse solrResponse = solrClient //
			.query(collection, solrQueryParams, METHOD.POST); // throws RemoteSolrException
	System.err.println(solrResponse.jsonStr());
}

```


---

---

---


# Apache Solr

https://solr.apache.org/


# Install Solr container

```
docker run --name solr-sandbox -d -v "d:\solrdata:/var/solr" -p 8983:8983 solr:8.11.2 solr-precreate gettingstarted

```

## Run Solr docker


```
>docker image list
REPOSITORY   TAG       IMAGE ID       CREATED         SIZE
solr         latest    08811aaa7dbb   6 weeks ago     581MB

>
```


```

docker container list

docker exec -it solr-sandbox /bin/bash
```


http://localhost:8983/solr/#/

# Create Solr collection コレクションの作成

```
$ bin/solr create_core -c sandbox

$ bin/solr create_core -c unittest
```











# Features

nlp4j-solr provides
- Data Indexer for Solr
- Search Client for Solr

# How to create test environment

	> mkdir C:\usr\solrdata_nlp4j-solr-unittest
	
	> docker run --name nlp4j-solr-unittest -d -v "C:\usr\solr_nlp4j_unittest:/var/solr" -p 8983:8983 solr:8.10.0 solr-precreate gettingstarted
	
	http://localhost:8983/solr/#/
	
	> docker start nlp4j-solr-unittest
	
	> docker exec -it nlp4j-solr-unittest /bin/bash
	
	$ bin/solr create_core -c unittest

# Solr

https://solr.apache.org/guide/8_1/common-query-parameters.html

# Azure

Azure Query:

https://docs.microsoft.com/en-us/rest/api/searchservice/search-documents

https://docs.microsoft.com/ja-jp/rest/api/searchservice/search-documents


# Docker で使う (2024-06-26)

[https://solr.apache.org/guide/solr/latest/deployment-guide/solr-in-docker.html](https://solr.apache.org/guide/solr/latest/deployment-guide/solr-in-docker.html)
に書いてある

```
>docker run -d -p 8983:8983 --name my_solr solr
```

```
>docker exec -it my_solr solr create_core -c gettingstarted
WARNING: Using _default configset with data driven schema functionality. NOT RECOMMENDED for production use.
         To turn off: bin/solr config -c gettingstarted -p 8983 -action set-user-property -property update.autoCreateFields -value false

Created new core 'gettingstarted'
```

# Docker で使う (2024-07-14)

## コンテナの作成と起動

```
>docker run -d -p 8983:8983 --name my_solr solr
```

docker run : Dockerコンテナを作成し、実行するための基本コマンド

-d : デタッチモードでコンテナを実行する。コンテナがバックグランドで実行される。コンテナが起動している間、ターミナルがブロックされない

-p 8983:8983 : ポートマッピングを指定する。ホストのポート 8983 をコンテナのポート 8983 にマッピングする

--name my_solr : 起動するコンテナに名前を付ける。名前を付けることで「docker stop my_solr」などのコマンドでコンテナを特定できる


## コレクションの作成

```
>docker exec -it my_solr bin/solr create -c sandbox
WARNING: Using _default configset with data driven schema functionality. NOT RECOMMENDED for production use.
         To turn off: bin/solr config -c sandbox -p 8983 -action set-user-property -property update.autoCreateFields -value false
```





