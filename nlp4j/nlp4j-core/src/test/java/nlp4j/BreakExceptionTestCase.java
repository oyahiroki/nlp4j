package nlp4j;

import junit.framework.TestCase;

public class BreakExceptionTestCase extends TestCase {

	public void test001() throws Exception {

		try {
			for (int n = 0; n < 100; n++) {
				process(n);
			}

		} catch (BreakException e) {
			e.printStackTrace();
			System.err.println("OK");
			return;
		}
		fail();

	}

	private void process(int n) throws BreakException {
		if (n > 10) {
			throw new BreakException("n>10;n=" + n);
		}
	}

}
