package hello.wikiapi;

import java.io.IOException;

import nlp4j.http.HttpClient5;
import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.XmlUtils;

public class HelloWikiApi3 {

	public static void main(String[] args) throws IOException {

		// http だと 301が返る

		String url = "https://en.wiktionary.org/w/api.php" //
				+ "?"//
				+ "format=xml"//
				+ "&action=query"//
				+ "&prop="
//				+ "categories"//
//				+ "linkshere"//
//				+ "pageterms"//
//				+ "info"//
				+ "iwlinks"//
				+ "&titles=Category:en:Medicine";//

		// action: query: query Fetch data from and about MediaWiki.
		// format: One of the following values: json, jsonfm, none, php, phpfm, rawfm,
		// xml, xmlfm

		try (HttpClient5 client = new HttpClient5();) {
			System.err.println(url);
			DefaultNlpServiceResponse res = client.get(url);

			System.err.println(res.getResponseCode());

			System.err.println(res.getHeaders());

			System.err.println(XmlUtils.prettyFormatXml(res.getOriginalResponseBody()));
		}

	}

}
