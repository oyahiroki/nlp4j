package example;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.mecab.MecabAnnotator;
import nlp4j.util.LoggerUtils;

public class HelloMecab2Neologd {

	public static void main(String[] args) throws Exception {

		LoggerUtils.setLoggerDebug();

		// 自然文のテキスト
		String text = "令和元年です。";
		Document doc = new DefaultDocument();
		doc.putAttribute("text", text);
		try (MecabAnnotator ann = new MecabAnnotator();) {
			{
				ann.setProperty("target", "text");
//				ann.setProperty("option", "-u " + "/usr/local/MeCab/dic/NEologd/NEologd.20200910.dic");
//				ann.setProperty("option", "-u " + "C:\\usr\\local\\MeCab\\dic\\NEologd\\NEologd.20200910.dic");
				ann.setProperty("option", "-u " + "D:\\local\\MeCab\\dic\\NEologd\\NEologd.20200910.dic");
			}
			ann.annotate(doc); // throws Exception
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println( //
						"lex=" + kwd.getLex() //
								+ ",facet=" + kwd.getFacet() //
								+ ",upos=" + kwd.getUPos() //
								+ ",reading=" + kwd.getReading() //

				);
			}

		}
	}

}
