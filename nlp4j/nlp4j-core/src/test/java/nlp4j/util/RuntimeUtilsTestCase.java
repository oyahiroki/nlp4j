package nlp4j.util;

import junit.framework.TestCase;

public class RuntimeUtilsTestCase extends TestCase {

	public void testGetMemoryInfo() {
		System.err.println(RuntimeUtils.getMemoryInfo());
	}

}
