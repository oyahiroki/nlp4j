package hello.compress;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelloPlainTextMain {

	public static void main(String[] args) throws Exception {

		Path p = Paths.get("src/test/resources/hello.compress/hello-compress.txt");

		try (InputStream is = Files.newInputStream(p);
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr);) {
			String s;
			while ((s = br.readLine()) != null) {
				System.err.println(s);
			}
		}
	}

}
