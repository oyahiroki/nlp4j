# Introduction 

NLP4J for Apache Solr

https://solr.apache.org/


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
	
	