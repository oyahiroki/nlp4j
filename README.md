# NLP4J

Natural Language Processing Library for Java

![png](https://raw.githubusercontent.com/oyahiroki/nlp4j/master/nlp4j/files/icon/NLP4J_N_128.png)

# NLP4J Components

Core Data, Utilities, CSV/Json/Plaintext parser, etc. : [nlp4j-core](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-core)   
English language NLP: [nlp4j-stanford](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-stanford)  
Japanese language NLP: [nlp4j-kuromoji](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-kuromoji), [nlp4j-cabocha](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-cabocha), [nlp4j-mecab](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-mecab), [nlp4j-yahoojp](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-yahoojp) , [nlp4j-sudachi](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-sudachi)  
Wikipedia dump file parser, mediawiki api client: [wiki](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-wiki)  
Data crawling: [twitter](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-twitter4j), [webcrawler](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-webcrawler), [wikipedia dump](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-wiki)  
Document search: [apache solr](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-solr), [azure](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-azure)  

## Maven

```xml
<!-- for English NLP -->
<!-- https://mvnrepository.com/artifact/org.nlp4j/nlp4j-stanford -->
<dependency>
    <groupId>org.nlp4j</groupId>
    <artifactId>nlp4j-stanford</artifactId>
    <version>1.3.5.0</version>
</dependency>
<!-- https://mvnrepository.com/artifact/edu.stanford.nlp/stanford-corenlp -->
<dependency>
    <groupId>edu.stanford.nlp</groupId>
    <artifactId>stanford-corenlp</artifactId>
    <version>4.0.0</version>
    <scope>provided</scope>
</dependency>
<!-- https://mvnrepository.com/artifact/edu.stanford.nlp/stanford-corenlp -->
<dependency>
    <groupId>edu.stanford.nlp</groupId>
    <artifactId>stanford-corenlp</artifactId>
    <version>4.0.0</version>
    <classifier>models</classifier>
    <scope>provided</scope>
</dependency>
```

## Code for simple English Morphological analysis

```java
String text = "I eat sushi with chopsticks.";
DocumentAnnotator ann = (new DocumentAnnotatorBuilder<>(StanfordPosAnnotator.class)).set("target", "text")
		.build();
Document doc = (new DocumentBuilder()).text(text).build();
ann.annotate(doc);
doc.getKeywords().forEach(kwd -> {
	System.out.println(kwd.getBegin() + "," + kwd.getEnd() + "," + kwd.getFacet() + "," + kwd.getLex());
});

// Expected output:
// 0,1,word.PRP,I
// 2,5,word.VBP,eat
// 6,11,word.NN,sushi
// 12,16,word.IN,with
// 17,27,word.NNS,chopstick
// 27,28,word..,.
```

# See also

Natural Language Processing with Groovy, OpenNLP, CoreNLP, Nlp4j, Datumbox, Smile, Spark NLP, DJL and TensorFlow


https://groovy.apache.org/blog/natural-language-processing-with-groovy


# Author

Hiroki Oya [twitter](https://twitter.com/oyahiroki) [linkedin](https://www.linkedin.com/in/oyahiroki/)

