package nlp4j.mecab;

import java.io.IOException;

import junit.framework.TestCase;
import nlp4j.Keyword;

public class MecabUtilsTestCase extends TestCase {

	public void testGetKeywords001() throws IOException {
		{
			String text = "今日はいい天気です";
			long time1 = System.currentTimeMillis();
			for (Keyword kwd : MecabUtils.getKeywords(text)) {
				System.err.println(kwd.getLex());
			}
			long time2 = System.currentTimeMillis();
			System.err.println(time2 - time1);
		}
		{
			String text = "明日もいい天気です";
			long time1 = System.currentTimeMillis();
			for (Keyword kwd : MecabUtils.getKeywords(text)) {
				System.err.println(kwd.getLex());
			}
			long time2 = System.currentTimeMillis();
			System.err.println(time2 - time1);
		}
	}

	public void testGetKeywords002() throws IOException {
		String text = "これは[[ABC]]です";
		long time1 = System.currentTimeMillis();
		for (Keyword kwd : MecabUtils.getKeywords(text)) {
			System.err.println(kwd.getLex());
		}
		long time2 = System.currentTimeMillis();
		System.err.println(time2 - time1);
	}

}
