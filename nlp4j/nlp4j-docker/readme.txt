
# Build

	docker build -t oyahiroki/hello-tomcat:20211016 .

# Run

	docker container run -d -p 80:8080 --rm oyahiroki/hello-tomcat:20211016

# Access

	http://localhost/nlp4j-web-api-1.0.0.0/index.jsp


	-d バックグラウンドで動作する。detached



# Docker で EOF を入力したい

	→ Ctrl+D




# Solr
>docker run -d -v "c:\usr\solrdata:/var/solr" -p 8983:8983 solr:8.10.0 solr-precreate gettingstarted

