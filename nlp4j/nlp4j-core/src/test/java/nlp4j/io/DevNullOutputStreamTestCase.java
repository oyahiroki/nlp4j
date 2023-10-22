package nlp4j.io;

import junit.framework.TestCase;

public class DevNullOutputStreamTestCase extends TestCase {

	public void testWriteInt001() throws Exception {
	}

	public void testWriteByteArray001() throws Exception {
	}

	public void testWriteByteArrayIntInt001() throws Exception {
	}

	public void testDevNullOutputStream001() throws Exception {
		DevNullOutputStream os = new DevNullOutputStream();
		os.write(0);
		os.flush();
		os.close();
	}

}
