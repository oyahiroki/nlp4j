# Components

Core Data, Utilities, CSV/Json/Plaintext parser, etc. : [core](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-core)   
English language NLP: [stanford](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-stanford)  
Japanese language NLP: [kuromoji](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-kuromoji), [cabocha](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-cabocha), [mecab](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-mecab), [yahoojp](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-yahoojp)   
Wikipedia dump file parser, mediawiki api client: [wiki](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-wiki)  
Data crawling: [twitter](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-twitter4j), [webcrawler](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-webcrawler)  
Document search: [apache solr](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-solr), [azure](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-azure)  

# nlp4j
Natural Language Processing Library for Java

Following is a simple example for using Stanford NLP via NLP4J

## Maven

```xml
<!-- https://mvnrepository.com/artifact/org.nlp4j/nlp4j-stanford -->
<dependency>
    <groupId>org.nlp4j</groupId>
    <artifactId>nlp4j-core</artifactId>
    <version>[1.3.1.0,)</version>
</dependency>
<dependency>
    <groupId>org.nlp4j</groupId>
    <artifactId>nlp4j-stanford</artifactId>
    <version>[1.3.0.0,)</version>
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

## Code

```java
import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.stanford.StanfordPosAnnotator;
public class StanfordPosAnnotatorExample0 {
    public static void main(String[] args) throws Exception {
        Document doc = new DefaultDocument();
        {
            doc.putAttribute("text", "I eat sushi with chopsticks.");
        }
        StanfordPosAnnotator ann = new StanfordPosAnnotator();
        {
            ann.setProperty("target", "text");
        }
        ann.annotate(doc); // do annotation
        for (Keyword kwd : doc.getKeywords()) {
            System.err.println(kwd);
        }
    }
}
```

## Output

```
I [facet=word.PRP, str=I]
eat [facet=word.VBP, str=eat]
sushi [facet=word.NN, str=sushi]
with [facet=word.IN, str=with]
chopstick [facet=word.NNS, str=chopsticks]
. [facet=word.., str=.]
```

