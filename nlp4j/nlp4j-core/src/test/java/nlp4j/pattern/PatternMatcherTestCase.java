package nlp4j.pattern;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;
import nlp4j.impl.DefaultKeywordWithDependency;
import nlp4j.test.NLP4JTestCase;
import nlp4j.util.KeywordUtil;

public class PatternMatcherTestCase extends NLP4JTestCase {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public PatternMatcherTestCase() {
		target = PatternMatcher.class;
	}

	public void testMatch001() {
		description = "";

		KeywordUtil.Builder<DefaultKeywordWithDependency> builder = new KeywordUtil.Builder<>(
				DefaultKeywordWithDependency.class);

		DefaultKeywordWithDependency kwd = (DefaultKeywordWithDependency) builder.newKwd().facet("記号").lex("。").str("。")
				.upos("SYM").begin(9).end(10).build();
		{
			DefaultKeywordWithDependency kwd2 = (DefaultKeywordWithDependency) builder.newKwd() //
					.facet("助動詞").lex("です").str("です").upos("AUX").begin(7).end(9).build();
			kwd.addChild(kwd2);
			{
				DefaultKeywordWithDependency kwd3 = (DefaultKeywordWithDependency) builder.newKwd() //
						.facet("名詞").lex("天気").str("天気").upos("NOUN").begin(5).end(7).build();
				kwd2.addChild(kwd3);
				{
					DefaultKeywordWithDependency kwd4 = (DefaultKeywordWithDependency) builder.newKwd() //
							.facet("助詞").lex("は").str("は").upos("ADP").begin(2).end(3).build();
					kwd3.addChild(kwd4);
					{
						DefaultKeywordWithDependency kwd5 = (DefaultKeywordWithDependency) builder.newKwd() //
								.facet("名詞").lex("今日").str("今日").upos("NOUN").begin(0).end(2).build();
						kwd4.addChild(kwd5);
					}
				}
				{
					DefaultKeywordWithDependency kwd6 = (DefaultKeywordWithDependency) builder.newKwd() //
							.facet("形容詞").lex("いい").str("いい").upos("ADJ").begin(3).end(5).build();
					kwd3.addChild(kwd6);
				}
			}
		}

//		System.err.println(kwd.toStringAsXml());

//		<?xml version="1.0" encoding="UTF-8"?>
//		<w depth="0" facet="記号" id="5" lex="。" sequence="5" str="。" upos="SYM">
//		    <w depth="1" facet="助動詞" id="4" lex="です" sequence="4" str="です" upos="AUX">
//		        <w depth="2" facet="名詞" id="3" lex="天気" sequence="3" str="天気" upos="NOUN">
//		            <w depth="3" facet="助詞" id="1" lex="は" sequence="1" str="は" upos="ADP">
//		                <w depth="4" facet="名詞" id="0" lex="今日" sequence="0" str="今日" upos="NOUN"/>
//		            </w>
//		            <w depth="3" facet="形容詞" id="2" lex="いい" sequence="2" str="いい" upos="ADJ"/>
//		        </w>
//		    </w>
//		</w>

		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-001.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);

		List<Keyword> kwds = new ArrayList<Keyword>();

		for (Pattern pattern : patterns) {
			List<Keyword> kwds0 = PatternMatcher.match(kwd, pattern);
			if (kwds0 != null) {
				kwds.addAll(kwds0);
			}
		}

//		<?xml version="1.0" encoding="UTF-8"?>
//		<patterns lang="ja">
//			<!-- example: いい ... 天気 -->
//			<pattern facet="pattern.sample" value="{0.lex} ... {1.lex}">
//				<w id="1" upos="NOUN" lex="天気">
//					<w id="0" upos="ADJ" />
//				</w>
//			</pattern>
//		</patterns>

		logger.info("--- extracted Keywords");
		for (Keyword kw : kwds) {
			System.err.println("facet=" + kw.getFacet() + ",lex=" + kw.getLex() + ",begin=" + kw.getBegin() + ",end="
					+ kw.getEnd());
		}

	}

	public void testMatch002() {
		description = "";

		KeywordUtil.Builder<DefaultKeywordWithDependency> builder = new KeywordUtil.Builder<>(
				DefaultKeywordWithDependency.class);

		DefaultKeywordWithDependency kwd = (DefaultKeywordWithDependency) builder.newKwd().facet("記号").lex("。").str("。")
				.upos("SYM").begin(9).end(10).build();
		{
			DefaultKeywordWithDependency kwd2 = (DefaultKeywordWithDependency) builder.newKwd() //
					.facet("助動詞").lex("です").str("です").upos("AUX").begin(7).end(9).build();
			kwd.addChild(kwd2);
			{
				DefaultKeywordWithDependency kwd3 = (DefaultKeywordWithDependency) builder.newKwd() //
						.facet("名詞").lex("天気").str("天気").upos("NOUN").begin(5).end(7).build();
				kwd2.addChild(kwd3);
				{
					DefaultKeywordWithDependency kwd4 = (DefaultKeywordWithDependency) builder.newKwd() //
							.facet("助詞").lex("は").str("は").upos("ADP").begin(2).end(3).build();
					kwd3.addChild(kwd4);
					{
						DefaultKeywordWithDependency kwd5 = (DefaultKeywordWithDependency) builder.newKwd() //
								.facet("名詞").lex("今日").str("今日").upos("NOUN").begin(0).end(2).build();
						kwd4.addChild(kwd5);
					}
				}
				{
					DefaultKeywordWithDependency kwd6 = (DefaultKeywordWithDependency) builder.newKwd() //
							.facet("形容詞").lex("いい").str("いい").upos("ADJ").begin(3).end(5).build();
					kwd3.addChild(kwd6);
				}
			}
		}

		// System.err.println(kwd.toStringAsXml());

		// <?xml version="1.0" encoding="UTF-8"?>
		// <w depth="0" facet="記号" id="5" lex="。" sequence="5" str="。" upos="SYM">
		// <w depth="1" facet="助動詞" id="4" lex="です" sequence="4" str="です" upos="AUX">
		// <w depth="2" facet="名詞" id="3" lex="天気" sequence="3" str="天気" upos="NOUN">
		// <w depth="3" facet="助詞" id="1" lex="は" sequence="1" str="は" upos="ADP">
		// <w depth="4" facet="名詞" id="0" lex="今日" sequence="0" str="今日" upos="NOUN"/>
		// </w>
		// <w depth="3" facet="形容詞" id="2" lex="いい" sequence="2" str="いい" upos="ADJ"/>
		// </w>
		// </w>
		// </w>

		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-002.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);

		List<Keyword> kwds = new ArrayList<Keyword>();

		for (Pattern pattern : patterns) {
			List<Keyword> kwds0 = PatternMatcher.match(kwd, pattern);
			if (kwds0 != null) {
				kwds.addAll(kwds0);
			}
		}

		// <?xml version="1.0" encoding="UTF-8"?>
		// <patterns lang="ja">
		// <!-- example: いい ... 天気 -->
		// <pattern facet="pattern.sample" value="{0.lex} ... {1.lex}">
		// <w id="1" upos="NOUN" lex="天気">
		// <w id="0" upos="ADJ" />
		// </w>
		// </pattern>
		// </patterns>

		logger.info("--- extracted Keywords");
		for (Keyword kw : kwds) {
			System.err.println("facet=" + kw.getFacet() + ",lex=" + kw.getLex() + ",begin=" + kw.getBegin() + ",end="
					+ kw.getEnd());
		}

	}

	public void testMatch003() {
		description = "";
	
		KeywordUtil.Builder<DefaultKeywordWithDependency> builder = new KeywordUtil.Builder<>(
				DefaultKeywordWithDependency.class);
	
		DefaultKeywordWithDependency kwd = (DefaultKeywordWithDependency) builder.newKwd().facet("記号").lex("。").str("。")
				.upos("SYM").begin(9).end(10).build();
		{
			DefaultKeywordWithDependency kwd2 = (DefaultKeywordWithDependency) builder.newKwd() //
					.facet("助動詞").lex("です").str("です").upos("AUX").begin(7).end(9).build();
			kwd.addChild(kwd2);
			{
				DefaultKeywordWithDependency kwd3 = (DefaultKeywordWithDependency) builder.newKwd() //
						.facet("名詞").lex("天気").str("天気").upos("NOUN").begin(5).end(7).build();
				kwd2.addChild(kwd3);
				{
					DefaultKeywordWithDependency kwd4 = (DefaultKeywordWithDependency) builder.newKwd() //
							.facet("助詞").lex("は").str("は").upos("ADP").begin(2).end(3).build();
					kwd3.addChild(kwd4);
					{
						DefaultKeywordWithDependency kwd5 = (DefaultKeywordWithDependency) builder.newKwd() //
								.facet("名詞").lex("今日").str("今日").upos("NOUN").begin(0).end(2).build();
						kwd4.addChild(kwd5);
					}
				}
				{
					DefaultKeywordWithDependency kwd6 = (DefaultKeywordWithDependency) builder.newKwd() //
							.facet("形容詞").lex("いい").str("いい").upos("ADJ").begin(3).end(5).build();
					kwd3.addChild(kwd6);
				}
			}
		}
	
		// System.err.println(kwd.toStringAsXml());
	
		// <?xml version="1.0" encoding="UTF-8"?>
		// <w depth="0" facet="記号" id="5" lex="。" sequence="5" str="。" upos="SYM">
		// <w depth="1" facet="助動詞" id="4" lex="です" sequence="4" str="です" upos="AUX">
		// <w depth="2" facet="名詞" id="3" lex="天気" sequence="3" str="天気" upos="NOUN">
		// <w depth="3" facet="助詞" id="1" lex="は" sequence="1" str="は" upos="ADP">
		// <w depth="4" facet="名詞" id="0" lex="今日" sequence="0" str="今日" upos="NOUN"/>
		// </w>
		// <w depth="3" facet="形容詞" id="2" lex="いい" sequence="2" str="いい" upos="ADJ"/>
		// </w>
		// </w>
		// </w>
	
		File xml = new File("src/test/resources/nlp4j.pattern/pattern-sample-ja-003.xml");
		List<Pattern> patterns = PatternReader.readFromXml(xml);
	
		List<Keyword> kwds = new ArrayList<Keyword>();
	
		for (Pattern pattern : patterns) {
			List<Keyword> kwds0 = PatternMatcher.match(kwd, pattern);
			if (kwds0 != null) {
				kwds.addAll(kwds0);
			}
		}
	
		// <?xml version="1.0" encoding="UTF-8"?>
		// <patterns lang="ja">
		// <!-- example: いい ... 天気 -->
		// <pattern facet="pattern.sample" value="{0.lex} ... {1.lex}">
		// <w id="1" upos="NOUN" lex="天気">
		// <w id="0" upos="ADJ" />
		// </w>
		// </pattern>
		// </patterns>
	
		logger.info("--- extracted Keywords");
		for (Keyword kw : kwds) {
			System.err.println("facet=" + kw.getFacet() + ",lex=" + kw.getLex() + ",begin=" + kw.getBegin() + ",end="
					+ kw.getEnd());
		}
	
	}

}
