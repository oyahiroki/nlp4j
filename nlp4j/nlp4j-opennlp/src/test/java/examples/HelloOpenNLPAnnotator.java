package examples;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.opennlp.OpenNLPAnnotator;
import nlp4j.util.DocumentUtil;

public class HelloOpenNLPAnnotator {

	public static void main(String[] args) throws Exception {
		Document doc = new DefaultDocument("Dogs are running quickly.");

		OpenNLPAnnotator ann = new OpenNLPAnnotator();

		System.out.println(DocumentUtil.toJsonPrettyString(doc));

		System.out.println("---");

		ann.annotate(doc);

		System.out.println(DocumentUtil.toJsonPrettyString(doc));
	}

}

// # Expected Output

//{
//	  "text": "Dogs are running quickly.",
//	  "keywords": []
//	}
//	---
//	{
//	  "text": "Dogs are running quickly.",
//	  "keywords": [
//	    {
//	      "facet": "NOUN",
//	      "upos": "NOUN",
//	      "lex": "dog",
//	      "str": "Dogs",
//	      "begin": 0,
//	      "end": 4,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "AUX",
//	      "upos": "AUX",
//	      "lex": "be",
//	      "str": "are",
//	      "begin": 5,
//	      "end": 8,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "VERB",
//	      "upos": "VERB",
//	      "lex": "run",
//	      "str": "running",
//	      "begin": 9,
//	      "end": 16,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "ADV",
//	      "upos": "ADV",
//	      "lex": "quickly",
//	      "str": "quickly",
//	      "begin": 17,
//	      "end": 24,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "PUNCT",
//	      "upos": "PUNCT",
//	      "lex": ".",
//	      "str": ".",
//	      "begin": 24,
//	      "end": 25,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    }
//	  ]
//	}

