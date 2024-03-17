# Introduction 

NLP4J for Stanford NLP

# Features

nlp4j-stanford provides

- Easy use wrapper for Stanford NLP

- Dependency extraction


## Stanford NLP

https://nlp.stanford.edu/

### Stanford Dependencies

https://nlp.stanford.edu/software/stanford-dependencies.html

```
$ java -mx4g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -port 9000 -timeout 15000
```


# Build for Release

```
mvn -DperformRelease=true clean deploy
```

