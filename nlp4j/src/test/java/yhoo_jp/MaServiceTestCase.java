/**
 * 
 */
package yhoo_jp;

import junit.framework.TestCase;
import nlp4j.yhoo_jp.MaService;

/**
 * @author oyahiroki
 *
 */
public class MaServiceTestCase extends TestCase {

	/**
	 * Test method for {@link nlp4j.yhoo_jp.MaService#process()}.
	 */
	public void testProcess() throws Exception {
		MaService service = new MaService();
		service.process();
	}

}
