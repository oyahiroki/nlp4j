# nlp4j-opennlp

English text annotation for NLP4J using Apache OpenNLP.

`nlp4j-opennlp` provides an NLP4J `DocumentAnnotator` implementation based on Apache OpenNLP.  
It performs tokenization, part-of-speech (POS) tagging, and lemmatization on English text and adds the analysis results to an NLP4J `Document` as keywords.

## Features

- English text tokenization
- Part-of-speech tagging
- Lemmatization
- Universal POS (`upos`)
- Character offsets (`begin`, `end`)
- Integration with the NLP4J `Document` / `Keyword` framework
- Simple API: pass a document to `OpenNLPAnnotator`

## Example

```java
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.opennlp.OpenNLPAnnotator;
import nlp4j.util.DocumentUtil;

public class HelloOpenNLPAnnotator {

    public static void main(String[] args) throws Exception {

        Document doc =
                new DefaultDocument("Dogs are running quickly.");

        OpenNLPAnnotator annotator =
                new OpenNLPAnnotator();

        annotator.annotate(doc);

        System.out.println(
                DocumentUtil.toJsonPrettyString(doc));
    }
}
```

## Output

The example above produces keyword information similar to the following:

```json
{
  "text": "Dogs are running quickly.",
  "keywords": [
    {
      "facet": "NOUN",
      "upos": "NOUN",
      "lex": "dog",
      "str": "Dogs",
      "begin": 0,
      "end": 4
    },
    {
      "facet": "AUX",
      "upos": "AUX",
      "lex": "be",
      "str": "are",
      "begin": 5,
      "end": 8
    },
    {
      "facet": "VERB",
      "upos": "VERB",
      "lex": "run",
      "str": "running",
      "begin": 9,
      "end": 16
    },
    {
      "facet": "ADV",
      "upos": "ADV",
      "lex": "quickly",
      "str": "quickly",
      "begin": 17,
      "end": 24
    },
    {
      "facet": "PUNCT",
      "upos": "PUNCT",
      "lex": ".",
      "str": ".",
      "begin": 24,
      "end": 25
    }
  ]
}
```

## Keyword Properties

Each token is represented as an NLP4J keyword.

| Property | Description | Example |
|---|---|---|
| `str` | Surface form in the original text | `running` |
| `lex` | Lemma / canonical form | `run` |
| `upos` | Universal POS tag | `VERB` |
| `facet` | POS value produced by the annotator | `VERB` |
| `begin` | Start character offset | `9` |
| `end` | End character offset (exclusive) | `16` |

For example:

```json
{
  "facet": "VERB",
  "upos": "VERB",
  "lex": "run",
  "str": "running",
  "begin": 9,
  "end": 16
}
```

corresponds to:

```text
Dogs are running quickly.
         ^^^^^^^
```

## Processing Flow

`OpenNLPAnnotator` performs the following processing steps:

```text
English text
    |
    v
Tokenization
    |
    v
POS tagging
    |
    v
Lemmatization
    |
    v
NLP4J Keyword objects
```

For example:

```text
Dogs are running quickly.
```

is analyzed approximately as:

```text
Dogs      -> dog       / NOUN
are       -> be        / AUX
running   -> run       / VERB
quickly   -> quickly   / ADV
.         -> .         / PUNCT
```

## OpenNLP Models

The annotator uses separate Apache OpenNLP models for:

- tokenization
- POS tagging
- lemmatization

The current implementation loads the following models from the classpath:

```text
opennlp-en-ud-ewt-tokens-1.3-2.5.4.bin
opennlp-en-ud-ewt-pos-1.3-2.5.4.bin
opennlp-en-ud-ewt-lemmas-1.3-2.5.4.bin
```

The models are loaded when an `OpenNLPAnnotator` instance is created.

## Default Input Field

By default, `OpenNLPAnnotator` analyzes the `text` attribute of an NLP4J `Document`.

```java
Document doc =
        new DefaultDocument("Dogs are running quickly.");

OpenNLPAnnotator annotator =
        new OpenNLPAnnotator();

annotator.annotate(doc);
```

The OpenNLP-specific API is hidden inside the annotator, so applications can work with the standard NLP4J `Document` and `Keyword` interfaces.

## Why nlp4j-opennlp?

Using Apache OpenNLP directly typically requires applications to handle several processing components:

```java
TokenizerME
POSTaggerME
LemmatizerME
```

`nlp4j-opennlp` wraps these components behind the NLP4J annotator interface.

Instead of managing each OpenNLP component directly, an application can simply write:

```java
OpenNLPAnnotator annotator = new OpenNLPAnnotator();
annotator.annotate(doc);
```

The resulting linguistic information is stored in the common NLP4J keyword representation.

This makes it easier to use OpenNLP together with other NLP4J components without exposing OpenNLP-specific data structures to application code.

## Project

This project is part of the NLP4J ecosystem.

- NLP4J: Natural Language Processing for Java
- nlp4j-opennlp: Apache OpenNLP integration for NLP4J

## Related Technologies

- [Apache OpenNLP](https://opennlp.apache.org/)
- [NLP4J](https://github.com/oyahiroki/nlp4j)

