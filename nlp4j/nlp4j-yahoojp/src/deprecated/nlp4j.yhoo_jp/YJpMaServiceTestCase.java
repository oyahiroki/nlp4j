package nlp4j.yhoo_jp;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.NlpServiceResponse;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class YJpMaServiceTestCase extends TestCase {
	/**
	 * Test method for {@link nlp4j.yhoo_jp.YJpMaService#process()}.
	 */
	public void testProcess001() throws Exception {
		String text = "今日はいい天気です。今日はいい天気です。";
		YJpMaService service = new YJpMaService();
		NlpServiceResponse res = service.process(text);
		System.err.println(res.getOriginalResponseBody());
		System.err.println(res.getResponseCode());
		System.err.println(res.getKeywords().size());
	}

	/**
	 * Test method for {@link nlp4j.yhoo_jp.YJpMaService#process()}.
	 */
	public void testProcess002() throws Exception {
		String text = "今日はいい天気です。２０１９年４月１日";
		YJpMaService service = new YJpMaService();
		NlpServiceResponse res = service.process(text);
		System.err.println(res.getOriginalResponseBody());
		System.err.println(res.getResponseCode());
		System.err.println(res.getKeywords().size());
	}

	public void testGetKeywords001() throws Exception {
		String text = "今日はいい天気です。２０１９年４月１日";
		YJpMaService service = new YJpMaService();
		List<Keyword> kwds = service.getKeywords(text);
		for (Keyword kwd : kwds) {
			System.err.println(kwd);
		}
	}

}
