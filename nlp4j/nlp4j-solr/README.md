# NLP4J Solr

NLP4J Library for Apache Solr

# Apache Solr

https://solr.apache.org/


# Install Solr container

```
docker run --name solr-sandbox -d -v "d:\solrdata:/var/solr" -p 8983:8983 solr:8.11.2 solr-precreate gettingstarted

```

## Run Solr docker

```
docker image list

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


