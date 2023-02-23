# NLP4J-Wiki (Wikipedia Dump Parser)

# MAVEN Repository

	https://mvnrepository.com/artifact/org.nlp4j/nlp4j-wiki

# Dump Download

## Wikipedia English version dump Download

https://dumps.wikimedia.org/enwiki/20230101/

## Wiktionary English version dump Download

https://dumps.wikimedia.org/enwiktionary/20230101/

## Wikipedia Japanese version dump Download 

日本語版ウィキペディア ダンプファイル ダウンロード

https://dumps.wikimedia.org/jawiki/20230101/

## Wiktionary Japanese version dump Download 

日本語版ウィクショナリー ダンプファイル ダウンロード

https://dumps.wikimedia.org/jawiktionary/20230101/

# Code Example

## Read Dump and Index file

```java

String dumpIndexFileName = "/usr/local/wiki/enwiktionary/20230101/"
		+ "enwiktionary-20230101-pages-articles-multistream-index.txt.bz2";
String dumpFileName = "/usr/local/wiki/enwiktionary/20230101/"
		+ "enwiktionary-20230101-pages-articles-multistream.xml.bz2";

String itemString = "NLP";

// Index File
File indexFile = new File(dumpIndexFileName);
// Dump File
File dumpFile = new File(dumpFileName);

try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);) {
	{
		WikiPage page = dumpReader.getItem(itemString);
		System.out.println("<text>\n" + page.getText() + "\n</text>");
		System.out.println("<xml>\n" + page.getXml() + "\n</xml>");
	}
}

```

### Result

Almost same data as https://en.wiktionary.org/w/index.php?title=NLP&action=edit

```
<text>
==English==

===Noun===
{{en-noun|-}}

# {{lb|en|communication}} {{initialism of|en|neuro-linguistic programming}}
# {{lb|en|computing}} {{initialism of|en|natural language processing}}
# {{lb|en|mathematics}} {{initialism of|en|nonlinear programming}}
# {{lb|en|psychology}} {{initialism of|en|neuro-linguistic psychotherapy}}

===Proper noun===
{{en-proper noun}}

# {{lb|en|politics}} {{initialism of|en|{{w|National Labour Party}}}}

===Anagrams===
* {{anagrams|en|a=lnp|L/NP|LNP|LPN|NPL|PNL}}

----

==Slovene==

===Pronunciation===
* {{sl-IPA|nələpə̏}}

===Noun===
{{sl-noun|-|m-in}}

# {{abbreviation of|sl|neznani leteči predmet||[[unidentified flying object]], [[UFO]]}}

====Inflection====
{{sl-decl-noun-m|NLP|NLP-j|noacc=1}}
{{rfinfl|sl|noun}}<!-- also indeclinable -->

====Synonyms====
* {{l|sl|ÚFO}}
</text>

```





# Links

Alternative parsers - MediaWiki
https://www.mediawiki.org/wiki/Alternative_parsers


Wikidata/Wikidata-Toolkit: Java library to interact with Wikibase
https://github.com/Wikidata/Wikidata-Toolkit



# For Build Admin

## Build for Release

	mvn -DperformRelease=true clean deploy

	mvn -e -X -DperformRelease=true clean deploy
	
## Maven Commands

	mvn test
	mvn javadoc:javadoc

