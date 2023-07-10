package nlp4j.util;

import java.io.File;

import junit.framework.TestCase;

public class ClassUtilsTestCase extends TestCase {

	public void testGetLocation001() {
		File file = ClassUtils.getLocation(this.getClass());
		System.err.println(file.getAbsolutePath());
	}

}
