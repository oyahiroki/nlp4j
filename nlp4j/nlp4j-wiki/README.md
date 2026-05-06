# NLP4J-Wiki

[![Maven Central](https://img.shields.io/maven-central/v/org.nlp4j/nlp4j-wiki.svg)](https://mvnrepository.com/artifact/org.nlp4j/nlp4j-wiki)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

A high-performance Java library for parsing and processing Wikipedia and Wiktionary dump files. NLP4J-Wiki provides efficient tools to extract, parse, and analyze content from MediaWiki XML dumps with support for both indexed and sequential reading.

## 🎯 Key Features

- **Fast Random Access**: Read specific Wikipedia/Wiktionary pages instantly using index files
- **Sequential Processing**: Stream through entire dump files with custom handlers
- **Memory Efficient**: Process multi-gigabyte dump files without loading everything into memory
- **Multi-language Support**: Works with any Wikipedia/Wiktionary language edition (English, Japanese, etc.)
- **WikiText Parsing**: Extract plain text, infoboxes, templates, and structured content
- **Compressed File Support**: Direct reading from `.bz2` compressed dump files
- **MediaWiki API Client**: Fetch live data from Wikipedia/Wiktionary APIs
- **Command-Line Tool**: Ready-to-use JAR for sampling Wikipedia dumps without coding

## 📦 Installation

### Maven

```xml
<dependency>
    <groupId>org.nlp4j</groupId>
    <artifactId>nlp4j-wiki</artifactId>
    <version>1.2.3.0</version>
</dependency>
```

### Requirements

- Java 17 or higher
- Maven 3.x (for building from source)

## 🚀 Quick Start

### 1. Download Wikipedia/Wiktionary Dump Files

Download the required dump files from Wikimedia:

**Wikipedia Dumps:**
- English: [https://dumps.wikimedia.org/enwiki/](https://dumps.wikimedia.org/enwiki/)
- Japanese: [https://dumps.wikimedia.org/jawiki/](https://dumps.wikimedia.org/jawiki/)

**Wiktionary Dumps:**
- English: [https://dumps.wikimedia.org/enwiktionary/](https://dumps.wikimedia.org/enwiktionary/)
- Japanese: [https://dumps.wikimedia.org/jawiktionary/](https://dumps.wikimedia.org/jawiktionary/)

**Required Files:**
- Index file: `{lang}{wiki|wiktionary}-{YYYYMMDD}-pages-articles-multistream-index.txt.bz2`
- Dump file: `{lang}{wiki|wiktionary}-{YYYYMMDD}-pages-articles-multistream.xml.bz2`

### 2. Read a Specific Page (Fast Random Access)

```java
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import java.io.File;

public class Example {
    public static void main(String[] args) throws Exception {
        // File paths
        String dir = "/usr/local/wiki/enwiktionary/20230101/";
        String indexFile = dir + "enwiktionary-20230101-pages-articles-multistream-index.txt.bz2";
        String dumpFile = dir + "enwiktionary-20230101-pages-articles-multistream.xml.bz2";
        
        // Read a specific page by title
        try (WikiDumpReader reader = new WikiDumpReader(
                new File(dumpFile), 
                new File(indexFile))) {
            
            WikiPage page = reader.getItem("NLP");
            
            // Get WikiText content
            System.out.println(page.getText());
            
            // Get plain text (without wiki markup)
            System.out.println(page.getRootNodePlainText());
        }
    }
}
```

**Output:**
```
==English==

===Noun===
{{en-noun|-}}

# {{lb|en|communication}} {{initialism of|en|neuro-linguistic programming}}
# {{lb|en|computing}} {{initialism of|en|natural language processing}}
# {{lb|en|mathematics}} {{initialism of|en|nonlinear programming}}
...
```

### 3. Process All Pages Sequentially

```java
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;
import nlp4j.wiki.BreakException;

public class SequentialExample {
    public static void main(String[] args) throws Exception {
        String dumpFile = "/usr/local/wiki/enwiktionary/20230101/"
                + "enwiktionary-20230101-pages-articles-multistream.xml.bz2";
        
        try (WikiDumpReader reader = new WikiDumpReader(dumpFile)) {
            reader.read(new WikiPageHandler() {
                int count = 0;
                
                @Override
                public void read(WikiPage page) throws BreakException {
                    // Skip template/meta pages if needed
                    if (page.getTitle().contains(":")) {
                        return;
                    }
                    
                    count++;
                    System.out.println("Title: " + page.getTitle());
                    System.out.println("Text: " + page.getText());
                    
                    // Break after processing 100 pages
                    if (count >= 100) {
                        throw new BreakException();
                    }
                }
            });
        } catch (BreakException e) {
            System.out.println("Processing completed");
        }
    }
}
```

## 📚 Advanced Usage

### Extract Structured Content

```java
WikiPage page = reader.getItem("Tokyo");

// Get page metadata
System.out.println("Title: " + page.getTitle());
System.out.println("Namespace: " + page.getNamespace());
System.out.println("Page ID: " + page.getId());

// Get content in different formats
String wikiText = page.getText();           // Raw WikiText
String plainText = page.getRootNodePlainText(); // Plain text
String xml = page.getXml();                 // Full XML
```

### Filter by Namespace

```java
reader.read(new WikiPageHandler() {
    @Override
    public void read(WikiPage page) throws BreakException {
        // Process only main namespace articles (namespace = 0)
        if (page.getNamespace().equals("0")) {
            // Process article
            System.out.println(page.getTitle());
        }
    }
});
```

### Build a Dictionary from Wikipedia Titles

```java
import nlp4j.wiki.WikiIndex;
import nlp4j.wiki.WikiIndexReader;

File indexFile = new File("/usr/local/wiki/jawiki/20230101/"
        + "jawiki-20230101-pages-articles-multistream-index.txt.bz2");

// Read all page titles from index
WikiIndex wikiIndex = WikiIndexReader.readIndexFile(indexFile);

System.out.println("Total titles: " + wikiIndex.getWikiItemTitles().size());

// Iterate through all titles
for (String title : wikiIndex.getWikiItemTitles()) {
    System.out.println(title);
}
```

## 🔧 Use Cases

- **Natural Language Processing**: Extract training data for NLP models
- **Knowledge Base Construction**: Build structured knowledge graphs from Wikipedia
- **Dictionary Creation**: Extract definitions and linguistic data from Wiktionary
- **Text Mining**: Analyze large-scale encyclopedia content
- **Search Engine Development**: Index Wikipedia content for custom search applications
- **Machine Learning**: Generate datasets for various ML tasks
- **Linguistic Research**: Study language patterns and usage across Wikipedia

## 📖 API Documentation

### Core Classes

- **`WikiDumpReader`**: Main class for reading dump files
  - `getItem(String title)`: Get a specific page by title (requires index file)
  - `read(WikiPageHandler handler)`: Process all pages sequentially

- **`WikiPage`**: Represents a Wikipedia/Wiktionary page
  - `getTitle()`: Get page title
  - `getText()`: Get WikiText content
  - `getRootNodePlainText()`: Get plain text without markup
  - `getNamespace()`: Get page namespace
  - `getId()`: Get page ID

- **`WikiPageHandler`**: Interface for sequential processing
  - `read(WikiPage page)`: Called for each page

- **`WikiIndex`**: Index of all page titles
  - `getWikiItemTitles()`: Get list of all titles

## 🌐 MediaWiki API

For accessing live Wikipedia/Wiktionary data:

- [MediaWiki API Tutorial (English)](https://www.mediawiki.org/wiki/API:Tutorial/en)
- [MediaWiki API Tutorial (Japanese)](https://www.mediawiki.org/wiki/API:Tutorial/ja)

## 🛠️ Command-Line Tool: WikiDump Sampler

NLP4J-Wiki includes a powerful command-line tool for extracting sample data from Wikipedia dumps without writing any code. This is perfect for quickly exploring dump files, creating test datasets, or extracting specific articles.

### Features

- **Extract N articles** from any Wikipedia/Wiktionary dump file
- **Filter by content** using regular expressions
- **JSONL output** with structured data (ID, title, text, categories, timestamp)
- **Automatic filtering** of meta pages and templates
- **Progress tracking** with real-time counters

### Usage

The tool is available as a standalone JAR file at `dist/wikidump-sampler.jar`.

#### Basic Usage

Extract 100 articles from a Wikipedia dump:

```bash
java -jar dist/wikidump-sampler.jar \
  --input /path/to/jawiki-20230101-pages-articles-multistream.xml.bz2 \
  --count 100
```

#### Save to File

```bash
java -jar dist/wikidump-sampler.jar \
  --input /path/to/enwiki-20230101-pages-articles-multistream.xml.bz2 \
  --count 1000 \
  --output sample_1000.jsonl
```

#### Filter Articles

Extract only articles containing specific keywords (e.g., articles about science):

```bash
java -jar dist/wikidump-sampler.jar \
  --input /path/to/enwiki-20230101-pages-articles-multistream.xml.bz2 \
  --count 500 \
  --filter ".*science.*" \
  --output science_articles.jsonl
```

#### Command-Line Options

```
Usage:
  java -jar wikidump-sampler.jar --input <file> --count <number> [options]

Required:
  --input <file>         Path to Wikipedia dump file (.xml.bz2)
  --count <number>       Number of articles to extract

Optional:
  --output <file>        Output file (default: stdout)
  --filter <regex>       Filter articles by content using regex
```

### Output Format

The tool outputs JSONL (JSON Lines) format, with one article per line:

```json
{"id":"12345","timestamp":"2023-01-15T10:30:00Z","title":"Natural Language Processing","text":"Natural language processing (NLP) is a subfield of linguistics, computer science...","categories":["Computational linguistics","Natural language processing","Artificial intelligence"]}
{"id":"67890","timestamp":"2023-01-20T14:22:00Z","title":"Machine Learning","text":"Machine learning (ML) is a field of inquiry devoted to understanding...","categories":["Machine learning","Artificial intelligence"]}
```

### Use Cases

- **Quick exploration** of Wikipedia dump contents
- **Creating test datasets** for NLP experiments
- **Extracting domain-specific articles** using filters
- **Building training data** for machine learning models
- **Sampling large dumps** for analysis without processing the entire file

### Performance Notes

- Processing speed: ~1,000-5,000 articles/second (depending on hardware)
- Memory usage: Low (streaming processing)
- The tool automatically skips meta pages (Wikipedia:, Template:, etc.)
- Progress is printed every 100 articles

## 🔗 Related Projects

- [Alternative MediaWiki Parsers](https://www.mediawiki.org/wiki/Alternative_parsers)
- [Wikidata Toolkit](https://github.com/Wikidata/Wikidata-Toolkit) - Java library for Wikibase interaction

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0.txt) for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

## 📞 Support

- Website: [https://nlp4j.org/](https://nlp4j.org/)
- Maven Repository: [https://mvnrepository.com/artifact/org.nlp4j/nlp4j-wiki](https://mvnrepository.com/artifact/org.nlp4j/nlp4j-wiki)

## 🏷️ Version History

- **1.2.3.0** (2026-05-06): Latest release
- **1.2.2.0** (2026-05-06): Bug fixes and improvements
- **1.2.1.0** (2026-01-02): Performance enhancements
- **1.2.0.0** (2025-03-28): Major update
- **1.1.0.0** (2023-07-22): Feature additions
- **0.3.1.0** (2022-09-04): Initial release

---

**Note**: Processing large Wikipedia dumps may take significant time and memory. For production use, consider using the index file for random access or implementing appropriate filtering in your `WikiPageHandler`.
