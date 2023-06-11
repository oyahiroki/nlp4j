package nlp4j.yhoo_jp;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.NlpServiceResponse;

public class YJpMaServiceV2TestCase extends TestCase {

	public void testProcess001() throws Exception {
		String text = "今日はいい天気です。";
		YJpMaServiceV2 yj = new YJpMaServiceV2();
		NlpServiceResponse res = yj.process(text);
		for (Keyword kwd : res.getKeywords()) {
			System.err.println( //
					"begin=" + kwd.getBegin() //
							+ ", end=" + kwd.getEnd() //
							+ ", facet=" + kwd.getFacet() //
							+ ", lex=" + kwd.getLex() //
							+ ", str=" + kwd.getStr() //
							+ ",upos=" + kwd.getUPos());
		}
	}

	public void testProcess002() throws Exception {
		String text = "私は100円を拾いました。";
		YJpMaServiceV2 yj = new YJpMaServiceV2();
		NlpServiceResponse res = yj.process(text);
		for (Keyword kwd : res.getKeywords()) {
			System.err.println(kwd + ",upos=" + kwd.getUPos());
		}
	}

}
