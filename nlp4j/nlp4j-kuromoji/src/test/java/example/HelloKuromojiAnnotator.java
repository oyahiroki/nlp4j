package example;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.util.DocumentUtil;

public class HelloKuromojiAnnotator {

	public static void main(String[] args) throws Exception {
		Document doc = new DefaultDocument("犬が急いで走っている。");

		KuromojiAnnotator ann = new KuromojiAnnotator();
		{
			ann.setProperty("target", "text");
		}

		System.out.println(DocumentUtil.toJsonPrettyString(doc));

		System.out.println("---");

		ann.annotate(doc);

		System.out.println(DocumentUtil.toJsonPrettyString(doc));
	}

}

// # Expected Output

//{
//	  "text": "犬が急いで走っている。",
//	  "keywords": []
//	}
//	---
//	{
//	  "text": "犬が急いで走っている。",
//	  "keywords": [
//	    {
//	      "facet": "名詞",
//	      "upos": "NOUN",
//	      "lex": "犬",
//	      "str": "犬",
//	      "begin": 0,
//	      "end": 1,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "助詞",
//	      "upos": "ADP",
//	      "lex": "が",
//	      "str": "が",
//	      "begin": 1,
//	      "end": 2,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "動詞",
//	      "upos": "VERB",
//	      "lex": "急ぐ",
//	      "str": "急い",
//	      "begin": 2,
//	      "end": 4,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "助詞",
//	      "upos": "ADP",
//	      "lex": "で",
//	      "str": "で",
//	      "begin": 4,
//	      "end": 5,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "動詞",
//	      "upos": "VERB",
//	      "lex": "走る",
//	      "str": "走っ",
//	      "begin": 5,
//	      "end": 7,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "助詞",
//	      "upos": "ADP",
//	      "lex": "て",
//	      "str": "て",
//	      "begin": 7,
//	      "end": 8,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "動詞",
//	      "upos": "VERB",
//	      "lex": "いる",
//	      "str": "いる",
//	      "begin": 8,
//	      "end": 10,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    },
//	    {
//	      "facet": "記号",
//	      "upos": "SYM",
//	      "lex": "。",
//	      "str": "。",
//	      "begin": 10,
//	      "end": 11,
//	      "@classname": "nlp4j.impl.DefaultKeyword"
//	    }
//	  ]
//	}
