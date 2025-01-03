https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-web-vectorsearch

https://hub.docker.com/r/oyahiroki/nlp4j-web-vectorsearch

# Dockerfile for nlp4j-web-vectorsearch

## Docker build for daily use

```
docker build -t nlp4j-web-vectorsearch .
```

> Start a build
> 
> Usage:  docker buildx build \[OPTIONS] PATH | URL | -
> 
> -t, --tag stringArray    Name and optionally a tag (format:"name:tag")

```
docker run -d --name nlp4j-web-vectorsearch -p 8983:8983 -p 8080:8080 nlp4j-web-vectorsearch
```

8393: for Solr Admin Console

8080: for Web Application

## Docker build for publish

```
docker build -t oyahiroki/nlp4j-web-vectorsearch:1.0.0.0 -t oyahiroki/nlp4j-web-vectorsearch:latest .
```

