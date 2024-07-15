# NLP4J Solr

NLP4J Library for Apache Solr

# ベクトル検索を行う

# 1.Solr の起動と設定

## Docker で Solr ("my_solr") を起動する

```
docker run -d -p 8983:8983 --name my_solr solr
```

## Docker で core ("sandbox") を作成する

```
docker exec -it my_solr bin/solr create -c sandbox
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

```
$ python3 nlp4j-embedding-server-e5.py
```

# 3. ドキュメントの追加

```
String endPoint = "http://localhost:8983/solr/";
String collection = "sandbox";

List<Document> docs = new ArrayList<>();
{
	Document doc = new DefaultDocument();
	{
		doc.putAttribute("id", "doc001");
		// dynamic field
		// *_txt_ja
		doc.putAttribute("text", "今日はいい天気です。");
		doc.putAttribute("field1_s", "AAA");
		doc.putAttribute("field2_i", 100);
		doc.addKeyword(new DefaultKeyword(0, 2, "word.nn", "今日", "今日"));
		doc.addKeyword(new DefaultKeyword(3, 5, "word.adj", "いい", "いい"));
		doc.addKeyword(new DefaultKeyword(4, 6, "word.nn", "天気", "天気"));
	}
	docs.add(doc);
}
{ // Vector
	DocumentAnnotator ann = new EmbeddingAnnotator();
	ann.setProperty("target", "text");
	ann.annotate(docs);
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





