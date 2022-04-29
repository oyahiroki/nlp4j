package example;

import java.io.IOException;

import nlp4j.webcrawler.utils.MD5Utils;

public class WikipediaDumpIndexCheckMD5Example {

	public static void main(String[] args) throws IOException {
		{
			String fileName = "/usr/local/data/wiki/jawiki-20220401-pages-articles-multistream.xml.bz2";
			String result = MD5Utils.md5(fileName);
			System.err.println(result);
		}
		{
			String fileName = "/usr/local/data/wiki/jawiki-20220401-pages-articles-multistream-index.txt.bz2";
			String result = MD5Utils.md5(fileName);
			System.err.println(result);
		}
	}

}
