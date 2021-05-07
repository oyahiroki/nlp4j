package nlp4j.cabocha;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;
import nlp4j.pattern.UserPatternAnnotator;
import nlp4j.test.NLP4JTestCase;

@SuppressWarnings("javadoc")
public class CabochaUserPatternAnnotatorTestCase extends NLP4JTestCase {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@SuppressWarnings("javadoc")
	public CabochaUserPatternAnnotatorTestCase() {
		target = UserPatternAnnotator.class;
	}

	@SuppressWarnings("javadoc")
	public void test001() throws Exception {

		super.description = "Cabocha NLP + Pattern Matcher 001";

		Document doc = new DefaultDocument();
		String texts = "私は大学に歩いて行きました。車が道路を走る。風が強く吹く。";
		doc.putAttribute("text", texts);

		CabochaAnnotator annCabocha = new CabochaAnnotator();
		{
			annCabocha.setProperty("tempDir", "R:/");
			annCabocha.setProperty("encoding", "MS932");
			annCabocha.setProperty("target", "text");
			annCabocha.annotate(doc);
		}

		UserPatternAnnotator ann = new UserPatternAnnotator();
		{
			ann.setProperty("file", "src/test/resources/nlp4j.cabocha/pattern-sample-ja-004.xml");
			ann.annotate(doc);
		}

		// 抽出されたキーワードの取り出し
		for (Keyword kwd : doc.getKeywords("pattern")) {
			System.err.println("lex=" + kwd.getLex() + ",facet=" + kwd.getFacet() + ",begin=" + kwd.getBegin() + ",end="
					+ kwd.getEnd());
		}

	}

	/**
	 * Performance TEST
	 * 
	 * @throws Exception
	 */
	public void test003() throws Exception {

		NLP4JTestCase.setLevelInfo();

		CabochaAnnotator ann = new CabochaAnnotator();
		ann.setProperty("tempDir", "R:/");
		ann.setProperty("encoding", "MS932");
		ann.setProperty("target", "text");

		int LOOP = 100;

		long time1 = System.currentTimeMillis();
		for (int n = 0; n < LOOP; n++) {
			Document doc = new DefaultDocument();
			String texts = "私は学校に歩いて行きました。車が道路を走る。";
			doc.putAttribute("text", texts);

			ann.annotate(doc);

		}
		long time2 = System.currentTimeMillis();

		double time = ((double) (time2 - time1)) / (double) LOOP;

		logger.info("process time per a document in ms: " + time);

		NLP4JTestCase.setLevelDebug();
	}

}
