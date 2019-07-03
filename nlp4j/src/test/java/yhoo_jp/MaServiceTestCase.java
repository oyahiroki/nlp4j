/**
 * 
 */
package yhoo_jp;

import java.util.ArrayList;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.impl.NlpServiceResponseImpl;
import nlp4j.yhoo_jp.MaService;

/**
 * @author oyahiroki
 *
 */
public class MaServiceTestCase extends TestCase {

	/**
	 * Test method for {@link nlp4j.yhoo_jp.MaService#process()}.
	 */
	public void testProcess001() throws Exception {
		String text = "今日はいい天気です。";
		MaService service = new MaService();
		NlpServiceResponseImpl res = service.process(text);
		System.err.println(res.getOriginalResponseBody());
		System.err.println(res.getResponseCode());
		System.err.println(res.getKeywords().size());
	}

	public void testGetKeywords001() throws Exception {
		String text = "今日はいい天気です。";
		MaService service = new MaService();
		ArrayList<Keyword> kwds = service.getKeywords(text);
		for (Keyword kwd : kwds) {
			System.err.println(kwd);
		}
	}

}
