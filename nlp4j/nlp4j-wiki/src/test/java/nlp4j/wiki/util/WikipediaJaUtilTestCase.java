package nlp4j.wiki.util;

import junit.framework.TestCase;

public class WikipediaJaUtilTestCase extends TestCase {

	public void testGetUrlByPageId() {
//		fail("Not yet implemented");
	}

	public void testExtractDefaultSort() {
//		fail("Not yet implemented");
	}

	public void testGetRedirectPageTitle() {
		{
			String wikiText = "";
			String title = WikipediaJaUtil.getRedirectPageTitle(wikiText);
			System.err.println(title);
			assertNull(title);
		}
		{
			String wikiText = "#REDIRECT [[サンドボックス]]\r\n" + "";
			String title = WikipediaJaUtil.getRedirectPageTitle(wikiText);
			System.err.println(title);
			assertEquals("サンドボックス", title);
		}

	}

	public void testIsRedirectPage() {
//		fail("Not yet implemented");
	}

}
