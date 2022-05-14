package nlp4j.pattern;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class HelloResource {

	public static void main(String[] args) throws Exception {

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream is = classloader.getResourceAsStream("test.txt");

		List<String> lines = IOUtils.readLines(is, "UTF-8");

		for (String s : lines) {
			System.err.println(s);
		}

	}

}
