package hello.wiki;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public class Process1ExtractWikiDump1 {

	public static void main(String[] args) throws Exception {

		String fileName = "/usr/local/data/wiki/" //
				+ "jawiki-20210401-pages-meta-current.xml.bz2";

		File file = new File(fileName);

		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(input));

		String line = br.readLine();

		System.err.println(line);

	}

}
