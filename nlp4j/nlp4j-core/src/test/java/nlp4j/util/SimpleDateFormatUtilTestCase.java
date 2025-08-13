package nlp4j.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

public class SimpleDateFormatUtilTestCase extends TestCase {

	public void testGetUTCFormatter() {
		String fmt = "yyyyMMdd-HHmmss";
		SimpleDateFormat f = SimpleDateFormatUtil.getUTCFormatter(fmt);
		Date date = new Date();
		System.err.println("UTC: " + f.format(date));
		System.err.println("Local: " + (new SimpleDateFormat(fmt)).format(date));
	}

}
