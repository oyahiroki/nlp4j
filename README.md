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
	<version>4.4.0</version>
	<scope>provided</scope>
</dependency>
<!-- https://mvnrepository.com/artifact/edu.stanford.nlp/stanford-corenlp -->
<dependency>
	<groupId>edu.stanford.nlp</groupId>
	<artifactId>stanford-corenlp</artifactId>
	<version>4.4.0</version>
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

## Code for simple English Syntax analysis

```java
StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
ann.setProperty("target", "text");

Document doc = new DefaultDocument();
doc.putAttribute("text", "I eat sushi with chopsticks.");

ann.annotate(doc);

doc.getKeywords().forEach(kwd -> {
	if (kwd instanceof KeywordWithDependency) {
		KeywordWithDependency kd = (KeywordWithDependency) kwd;
		System.out.println(kd.toStringAsXml()); // print as xml
		System.out.println("I: " + kwd.getLex());
		kd.getChildren().forEach(child -> {
			System.out.println("children: " + child.getLex());
		});
	}
});

// Expected output
// <?xml version="1.0" encoding="UTF-8"?>
// <w begin="2" depth="0" end="5" facet="VBP" id="0" lex="eat" relation="root" sequence="0" str="eat">
//     <w begin="0" depth="1" end="1" facet="PRP" id="1" lex="I" relation="nsubj" sequence="1" str="I"/>
//     <w begin="6" depth="1" end="11" facet="NN" id="2" lex="sushi" relation="obj" sequence="2" str="sushi"/>
//     <w begin="17" depth="1" end="27" facet="NNS" id="3" lex="chopstick" relation="obl" sequence="3" str="chopsticks">
//         <w begin="12" depth="2" end="16" facet="IN" id="4" lex="with" relation="case" sequence="4" str="with"/>
//     </w>
//     <w begin="27" depth="1" end="28" facet="." id="5" lex="." relation="punct" sequence="5" str="."/>
// </w>
// I: eat
// children: I
// children: sushi
// children: chopstick
// children: .

```

## Code for simple English Syntax analysis (2)

```java
public static void main(String[] args) throws Exception {
	Document doc = new DefaultDocument();
	doc.putAttribute("text", "I eat sushi with chopsticks.");
	StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
	ann.setProperty("target", "text");
	ann.annotate(doc);
	for (Keyword kwd : doc.getKeywords()) {
		if (kwd instanceof KeywordWithDependency) {
			KeywordWithDependency kd = (KeywordWithDependency) kwd;
			// Print dependency as a XML
			System.out.println(kd.toStringAsXml());
			print(kd);
		}
	}
}

private static void print(KeywordWithDependency kd) {
	kd.getChildren().forEach(kwd -> {
		System.out.println(kd.getLex() + " -> (" + kwd.getRelation() + ") " + kwd.getLex());
		print(kwd);
	});
}
}

// Expected output:
// <?xml version="1.0" encoding="UTF-8"?>
// <w begin="2" depth="0" end="5" facet="VBP" id="0" lex="eat" relation="root" sequence="0" str="eat">
//     <w begin="0" depth="1" end="1" facet="PRP" id="1" lex="I" relation="nsubj" sequence="1" str="I"/>
//     <w begin="6" depth="1" end="11" facet="NN" id="2" lex="sushi" relation="obj" sequence="2" str="sushi"/>
//     <w begin="17" depth="1" end="27" facet="NNS" id="3" lex="chopstick" relation="obl" sequence="3" str="chopsticks">
//         <w begin="12" depth="2" end="16" facet="IN" id="4" lex="with" relation="case" sequence="4" str="with"/>
//     </w>
//     <w begin="27" depth="1" end="28" facet="." id="5" lex="." relation="punct" sequence="5" str="."/>
// </w>
//
// eat -> (nsubj) I
// eat -> (obj) sushi
// eat -> (obl) chopstick
// chopstick -> (case) with
// eat -> (punct) .

```

# See also

Natural Language Processing with Groovy, OpenNLP, CoreNLP, Nlp4j, Datumbox, Smile, Spark NLP, DJL and TensorFlow


https://groovy.apache.org/blog/natural-language-processing-with-groovy


# Author

Hiroki Oya [twitter](https://twitter.com/oyahiroki) [linkedin](https://www.linkedin.com/in/oyahiroki/)

