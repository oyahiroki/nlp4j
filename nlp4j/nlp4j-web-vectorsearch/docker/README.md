https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-web-vectorsearch

https://hub.docker.com/r/oyahiroki/nlp4j-web-vectorsearch

# Dockerfile for nlp4j-web-vectorsearch

## Docker build for daily use

```
docker build -t img_vectorsearch_yyyyMMdd .
```

> Start a build
> 
> Usage:  docker buildx build \[OPTIONS] PATH | URL | -
> 
> -t, --tag stringArray    Name and optionally a tag (format:"name:tag")

```
docker run -d --name my_solr_tomcat_container -p 8983:8983 -p 8080:8080 img_vectorsearch_yyyyMMdd
```


