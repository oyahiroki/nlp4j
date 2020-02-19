package nlp4j.cotoha;

import junit.framework.TestCase;

public class ConfigTestCase extends TestCase {

	static {
		String s1 = Config.COTOHA_CLIENT_ID;
		String s2 = Config.COTOHA_CLIENT_SECRET;

		if (s1 == null) {
			fail("COTOHA_CLIENT_ID is null");
		}
		if (s2 == null) {
			fail("COTOHA_CLIENT_SECRET is null");
		}

	}

	public void test001() throws Exception {

		String s1 = Config.COTOHA_CLIENT_ID;
		String s2 = Config.COTOHA_CLIENT_SECRET;

		System.err.println(s1);
		System.err.println(s2);
	}

}
