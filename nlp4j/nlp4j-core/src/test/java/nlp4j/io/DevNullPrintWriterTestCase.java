package nlp4j.io;

import java.io.File;

import junit.framework.TestCase;

public class DevNullPrintWriterTestCase extends TestCase {

	public void testDevNullPrintWriterWriter001() throws Exception {
		DevNullPrintWriter pw = new DevNullPrintWriter(new File(""));
		pw.println("test");
		pw.flush();
		pw.close();
	}

	public void testDevNullPrintWriterOutputStream001() throws Exception {
		DevNullPrintWriter pw = new DevNullPrintWriter(new DevNullOutputStream());
		pw.println("test");
		pw.flush();
		pw.close();
	}

}
