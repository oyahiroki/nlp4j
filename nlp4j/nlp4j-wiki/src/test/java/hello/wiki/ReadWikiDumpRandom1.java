package hello.wiki;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

public class ReadWikiDumpRandom1 {

	public static void main(String[] args) throws Exception {

		String fileName = "/usr/local/data/wiki/" //
				+ "jawiki-20210401-pages-articles-multistream1.xml-p1p114794.bz2";

		File file = new File(fileName);

		long p1 = 691L;
		long p2 = 863362L;
		int size = (int) (p2 - p1);

		byte[] b = new byte[size];

		RandomAccessFile randomfile1 = new RandomAccessFile(file, "r");

		randomfile1.seek(p1);

		int r = randomfile1.read(b);
		System.err.println(r);

		System.err.println("length: " + b.length);
		System.err.println("begin: " + Integer.toHexString(b[0]));
		System.err.println("char: " + (char) b[0]);
		System.err.println("char: " + (char) b[1]);
		System.err.println("char: " + (char) b[2]);
		System.err.println("char: " + (char) b[3]);
		System.err.println("char: " + (char) b[4]);
		System.err.println("char: " + (char) b[5]);
		System.err.println("char: " + (char) b[6]);
		System.err.println("end: " + Integer.toHexString(b[b.length - 1]));

		System.err.println("OK");

		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		BZip2CompressorInputStream s = new BZip2CompressorInputStream(bis);

		String xml = "<mediawiki>" //
				+ org.apache.commons.io.IOUtils.toString(s, "UTF-8") //
				+ "</mediawiki>" //
		;

//		System.err.println(xml);

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();

			MediawikiXmlHandlerPrototype handler = new MediawikiXmlHandlerPrototype();

			saxParser.parse(new ByteArrayInputStream(xml.getBytes("utf-8")), handler);

		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			randomfile1.close();
		}

	}

}
