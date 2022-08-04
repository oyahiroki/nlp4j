package hello.compress;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public class HelloGZipMain2 {

	public static void main(String[] args) throws Exception {
		Path p = Paths.get("src/test/resources/hello.compress/hello-compress.txt.gz");
		try (InputStream is = Files.newInputStream(p);
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
