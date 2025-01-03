https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-solr

# nlp4j-solr

NLP4J Library for Apache Solr

- Search Client (Keyword search, Vector search)
- Document Importer
- Admin Client
- Utilities

# Usecase: Vector Search

# How to setup Solr Server

# 0. Prerequisites (Docker | Docker Desktop | Rancher Desktop)

Before starting, ensure that Docker is installed and properly configured on your system. For installation instructions, visit the [Docker documentation](https://docs.docker.com/get-docker/).



# 1. RUN and Config Solr

## 1-1. docker run solr

```
docker run -d -p 8983:8983 --name my_solr solr
```

Port 8983 is for Solr Admin (Web Console)


> docker run \[OPTIONS] IMAGE \[COMMAND] \[ARG...]
> 
> Create and run a new container from an image
> 
> -d, --detach         Run container in background and print container ID
> 
> -p, --publish list   Publish a container's port(s) to the host
> 
>  --name string       Assign a name to the container


> docker run \[OPTIONS] IMAGE \[COMMAND] \[ARG...]
> 
> 新しい Docker コンテナを起動するためのコマンドです。
> 
> -d: コンテナをデタッチドモード（バックグラウンド）で実行するオプションです。これにより、コンテナがバックグラウンドで実行され、コマンドプロンプトが直ちに戻ります。
> 
> -p 8983:8983: ホストマシンのポート（左側）をコンテナのポート（右側）にマッピングします。この場合、ホストのポート 8983 をコンテナ内のポート 8983 にマッピングして、ホストから Solr のウェブインターフェースにアクセスできるようにします。
> 
> --name my_solr: コンテナに my_solr という名前を付けます。これにより、後でコンテナを参照するときに、この名前を使って操作できます。
> 
> solr: 実行するイメージの名前です。この場合、Docker Hub にある solr イメージが使用されます。


## Open Solr Admin Console (Optional)

```
http://localhost:8983/solr/
```

## 1-2. exec "bin/solr create -c sandbox" for creating Solr core [sandbox] .

```
docker exec -it my_solr bin/solr create -c sandbox
```


> Usage:  docker exec [OPTIONS] CONTAINER COMMAND [ARG...]
> 
> Execute a command in a running container
> 
>  -i, --interactive          Keep STDIN open even if not attached
>  -t, --tty                  Allocate a pseudo-TTY 



> Usage:  docker exec [OPTIONS] CONTAINER COMMAND [ARG...]
> 
> コンテナ内でコマンドを実行するためのコマンドです。
> 
> -it: -i はインタラクティブモードを有効にし、-t は擬似ターミナルを割り当てるためのオプションです。これにより、コマンド実行後にインタラクティブに操作できるようになります。
> 
> my_solr: コマンドを実行する対象の Docker コンテナの名前または ID です。この場合、my_solr という名前のコンテナが対象です。
> 
> bin/solr: コンテナ内で実行するコマンドです。ここでは、Solr のコマンドラインツール solr を実行しています。
> 
> create: Solr のコマンドで、新しいコレクションを作成するためのサブコマンドです。
> 
> -c sandbox: -c オプションは作成するコレクションの名前を指定します。この場合、sandbox という名前のコレクションが作成されます。



## 1-3. Create field type via curl command

field type: "name":"vector1024","class":"solr.DenseVectorField","vectorDimension":1024

```
curl -X POST -H 'Content-type:application/json' --data-binary '{"add-field-type":{"name":"vector1024","class":"solr.DenseVectorField","vectorDimension":1024,"similarityFunction":"cosine"}}' http://localhost:8983/solr/sandbox/schema
```

## 1-4. Add field for vector search

field: "name":"vector", "type":"vector1024"

```
curl -X POST -H 'Content-type:application/json' --data-binary '{ "add-field":{ "name":"vector", "type":"vector1024", "indexed":true, "stored":true } }' http://localhost:8983/solr/sandbox/schema
```

# Other useful command

## Delete all document from collection

```
curl 'http://localhost:8983/solr/sandbox/update?commit=true' -d '<delete><query>*:*</query></delete>'
```

```
$ curl 'http://localhost:8983/solr/sandbox/update?commit=true' -d '<delete><query>*:*</query></delete>'
<?xml version="1.0" encoding="UTF-8"?>
<response>

<lst name="responseHeader">
  <int name="status">0</int>
  <int name="QTime">96</int>
</lst>
</response>
```

# 2. Run Multilingual-E5-Embedding Server (nlp4j-llm-embeddings-e5)

See following page

```
https://github.com/oyahiroki/nlp4j-llm-embeddings-e5
```

```
$ python3 nlp4j-embedding-server-e5.py
```

# IF(you want to run server only) EXIT


# 3. Add document from Java

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

# 4-1. Vector Search Document from Java

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

# 4-2. Keyword Search Document from Java

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

# Reference

## Apache Solr

```
https://solr.apache.org/
```

## Command Examples (Docker)

```
docker image list
```

```
docker container list
```

```
docker exec -it solr-sandbox /bin/bash
```

```
docker run --name solr-sandbox -d -v "d:\solrdata:/var/solr" -p 8983:8983 solr:8.11.2 solr-precreate gettingstarted
```

# Command Examples (Solr) 

```
$ bin/solr create_core -c sandbox
```

```
$ bin/solr create_core -c unittest
```


EOF
