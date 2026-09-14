# nlp4j-kuromoji

Kuromoji integration for [NLP4J](https://nlp4j.org/) — Japanese morphological analysis using the [Apache Lucene Kuromoji](https://lucene.apache.org/) tokenizer (IPAdic).

## Maven Dependency

```xml
<dependency>
    <groupId>org.nlp4j</groupId>
    <artifactId>nlp4j-kuromoji</artifactId>
    <version>1.3.7.21</version>
</dependency>
```

Available on Maven Central: https://repo1.maven.org/maven2/org/nlp4j/nlp4j-kuromoji/

## Overview

`KuromojiAnnotator` is a `DocumentAnnotator` that tokenizes a text attribute of an NLP4J `Document` using Kuromoji (IPAdic) and adds the resulting morphemes as `Keyword` objects to the document.

Each extracted `Keyword` contains:

| Field     | Description                                     |
|-----------|-------------------------------------------------|
| `lex`     | Base form (dictionary form) of the token        |
| `str`     | Surface form (as it appears in the original text) |
| `reading` | Reading (yomi) of the token                     |
| `facet`   | Part of speech (e.g. 名詞, 動詞, 助詞, 記号)     |
| `begin`   | Start character offset in the source text       |
| `end`     | End character offset in the source text         |

> **Note:** ASCII alphabetic tokens (e.g. `EV`) that Kuromoji cannot resolve to a base form are returned as-is in `lex`.

## Usage

```java
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.krmj.annotator.KuromojiAnnotator;

Document doc = new DefaultDocument();
doc.putAttribute("text", "犬が急いで走っている。");

KuromojiAnnotator annotator = new KuromojiAnnotator();
annotator.setProperty("target", "text");  // attribute name to tokenize
annotator.annotate(doc);

doc.getKeywords().forEach(System.out::println);
```

### Annotator Properties

| Property | Description                                                  | Example  |
|----------|--------------------------------------------------------------|----------|
| `target` | Name of the document attribute containing the text to parse | `"text"` |

## Example Output

Input: `"犬が急いで走っている。"`

```json
{
  "text": "犬が急いで走っている。",
  "keywords": [
    { "facet": "名詞", "lex": "犬",  "str": "犬",  "begin": 0,  "end": 1  },
    { "facet": "助詞", "lex": "が",  "str": "が",  "begin": 1,  "end": 2  },
    { "facet": "動詞", "lex": "急ぐ", "str": "急い", "begin": 2,  "end": 4  },
    { "facet": "助詞", "lex": "で",  "str": "で",  "begin": 4,  "end": 5  },
    { "facet": "動詞", "lex": "走る", "str": "走っ", "begin": 5,  "end": 7  },
    { "facet": "助詞", "lex": "て",  "str": "て",  "begin": 7,  "end": 8  },
    { "facet": "動詞", "lex": "いる", "str": "いる", "begin": 8,  "end": 10 },
    { "facet": "記号", "lex": "。",  "str": "。",  "begin": 10, "end": 11 }
  ]
}
```

## Test Cases

### `testAnnotateDocument001` — Japanese sentence

Input: `"私は学校に行きました。"`

| Index | `lex`  | `str`    |
|-------|--------|----------|
| 0     | 私     | 私       |
| 1     | は     | は       |
| 2     | 学校   | 学校     |
| 3     | に     | に       |
| 4     | 行く   | 行き     |
| 5     | ます   | ます     |
| 6     | た     | た       |
| 7     | 。     | 。       |

### `testAnnotateDocument002` — Mixed Japanese and ASCII

Input: `"私はEVを買いました。"`

| Index | `lex`  | `str`    |
|-------|--------|----------|
| 0     | 私     | 私       |
| 1     | は     | は       |
| 2     | EV     | EV       |
| 3     | を     | を       |
| 4     | 買う   | 買い     |
| 5     | ます   | ます     |
| 6     | た     | た       |
| 7     | 。     | 。       |

## Performance

### Migration from Atilika Kuromoji to Apache Lucene Kuromoji

Since version 1.3.7.21, `KuromojiAnnotator` uses **Apache Lucene's `JapaneseTokenizer`** instead of the Atilika Kuromoji library.

The key difference is dictionary loading strategy:

- **Atilika Kuromoji** loads the IPAdic dictionary from disk on every `new Tokenizer()` call — there is no caching.
- **Lucene Kuromoji** uses a `SingletonHolder` pattern to load the dictionary once per JVM and reuse it across all instances.

In practice, the dominant cost with Atilika was not the Viterbi algorithm itself but the dictionary load (~137 ms per call). With Lucene, that cost disappears after the first call.

### Benchmark Results

Measured with 10 sample sentences × 100 iterations (1,000 total tokenizations), JVM warm-up applied:

| Scenario | Atilika Kuromoji | Lucene Kuromoji | Improvement |
|---|---|---|---|
| New instance every call | ~139,000 ms | ~86 ms | **~1,600×** |
| Reuse single instance | ~137,000 ms | ~23 ms | **~6,000×** |

> Benchmark environment: Windows 10, JDK 11, single thread.
> See [`KuromojiAnnotatorPerformanceTestCase`](src/test/java/nlp4j/krmj/annotator/KuromojiAnnotatorPerformanceTestCase.java) for the full benchmark code.

### Additional Optimizations Applied

| Optimization | Detail |
|---|---|
| `discardPunctuation=false` | Punctuation tokens (e.g. `。`) are preserved, matching previous behavior |
| `static final Pattern` | The ASCII detection regex is pre-compiled once instead of on every token |
| Log level `INFO` → `DEBUG` | Per-document log output no longer runs at INFO level in production |

## Dependencies

| Artifact                                          | Version  |
|---------------------------------------------------|----------|
| `org.nlp4j:nlp4j-core`                            | 1.3.7.21 |
| `org.apache.lucene:lucene-core`                   | 9.12.2   |
| `org.apache.lucene:lucene-analysis-kuromoji`      | 9.12.2   |
| `com.atilika.kuromoji:kuromoji-ipadic` (retained) | 0.9.0    |

## License

Apache License, Version 2.0 — http://www.apache.org/licenses/LICENSE-2.0.txt
