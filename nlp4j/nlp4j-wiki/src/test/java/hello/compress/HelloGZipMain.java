package hello.compress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class HelloGZipMain {

	public static void main(String[] args) throws Exception {
		File gzFile = new File("src/test/resources/hello.compress/hello-compress.txt.gz");
		try (InputStream is = new FileInputStream(gzFile);
				// java.util.zip.GZIPInputStream
				GZIPInputStream gzis = new GZIPInputStream(is);
				InputStreamReader isr = new InputStreamReader(gzis, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr);) {
			String s;
			while ((s = br.readLine()) != null) {
				System.err.println(s);
			}
		}
	}

}
