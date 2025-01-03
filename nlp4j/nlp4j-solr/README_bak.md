---

Old document

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





