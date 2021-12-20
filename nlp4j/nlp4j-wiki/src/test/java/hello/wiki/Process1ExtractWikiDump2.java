package hello.wiki;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import nlp4j.Keyword;

public class Process1ExtractWikiDump2 {

	public static void main(String[] args) throws Exception {

		String fileName = "/usr/local/data/wiki/" //
				+ "jawiki-20210401-pages-meta-current.xml.bz2";

		File file = new File(fileName);

		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();

			MediawikiXmlHandlerPrototype handler = new MediawikiXmlHandlerPrototype();

			saxParser.parse(input, handler);

		} catch (Exception e) {
			throw new IOException(e);
		}

	}

}
