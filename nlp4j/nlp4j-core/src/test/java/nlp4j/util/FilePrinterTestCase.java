package nlp4j.util;

import junit.framework.TestCase;

public class FilePrinterTestCase extends TestCase {

	public void testPrintln001() throws Exception {
		try (FilePrinter printer = new FilePrinter()) {
			printer.println("Hello");
		}
	}

	public void testPrintln002() throws Exception {
		try (FilePrinter printer = new FilePrinter()) {
			for (int n = 0; n < 1000; n++) {
				printer.println("Hello " + n);
			}
		}
	}

	public void testPrintln003() throws Exception {
		try (FilePrinter printer = new FilePrinter(10)) {
			for (int n = 0; n < 1000; n++) {
				printer.println("Hello " + n);
			}
		}
	}

}
