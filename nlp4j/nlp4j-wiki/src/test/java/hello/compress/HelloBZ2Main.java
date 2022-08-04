package hello.compress;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public class HelloBZ2Main {

	public static void main(String[] args) throws Exception {

		File bz2File = new File("src/test/resources/hello.compress/hello-compress.txt.bz2");

		// try-with-resources
		try (FileInputStream fis = new FileInputStream(bz2File);
				BufferedInputStream bis = new BufferedInputStream(fis);
				// see https://mvnrepository.com/artifact/org.apache.commons/commons-compress
				// org.apache.commons.compress.compressors.CompressorStreamFactory
				CompressorInputStream cis = new CompressorStreamFactory() //
						.createCompressorInputStream(bis);
				BufferedReader br = new BufferedReader(new InputStreamReader(cis, "UTF-8"));) {
			String line;
			while ((line = br.readLine()) != null) {
				System.err.println(line);
			}
		} catch (CompressorException e) {
			throw new IOException(e);
		}
	}

}
