# NLP4J-Wiki (Wikipedia Dump Parser)

# Maven Repository

	https://mvnrepository.com/artifact/org.nlp4j/nlp4j-wiki

# Dump file Download Site

## Wikipedia

https://dumps.wikimedia.org/enwiki/

## Wiktionary

https://dumps.wikimedia.org/enwiktionary/

# Code Example

## Read Wiki Dump file with Index file

```java

// File names
String dir = "/usr/local/wiki/enwiktionary/20230101/";
String dumpIndexFileName = dir + "enwiktionary-20230101-pages-articles-multistream-index.txt.bz2";
String dumpFileName = dir + "enwiktionary-20230101-pages-articles-multistream.xml.bz2";

// THE PAGE TITLE YOU WANT
String itemString = "NLP"; 

// READ DUMP FILE
try (WikiDumpReader dumpReader = new WikiDumpReader(new File(dumpFileName), new File(dumpIndexFileName));) {
	WikiPage page = dumpReader.getItem(itemString);
	System.out.println("<text>\n" + page.getText() + "\n</text>");
	// System.out.println("<xml>\n" + page.getXml() + "\n</xml>");
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

# Read wiki dump without Index sequentially

```java

// DUMP FILE NAME
String dumpFileName = "/usr/local/wiki/enwiktionary/20230101/"
		+ "enwiktionary-20230101-pages-articles-multistream.xml.bz2";

// WIKI DUMP FILE
File dumpFile = new File(dumpFileName);
// READ WIKI DUMP
try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
	dumpReader.read(
		// YOUR WikiPageHandler HERE
		new WikiPageHandler() {
			int count = 0; // YOUR BREAK CONDITIN IF YOU NEED
				@Override
			public void read(WikiPage page) throws BreakException {
				if (page.getTitle().contains(":")) {
					return; // SKIP Template page
				}
				count++;
				if (count > 3) { // YOUR BREAK CONDITION HERE
					throw new BreakException();
				}
				System.out.println(page.getTitle());
				System.out.println(page.getText());
			}
		});
} catch (BreakException be) {
	System.err.println("OK");
}

```




# Links

Alternative parsers - MediaWiki
https://www.mediawiki.org/wiki/Alternative_parsers


Wikidata/Wikidata-Toolkit: Java library to interact with Wikibase
https://github.com/Wikidata/Wikidata-Toolkit


