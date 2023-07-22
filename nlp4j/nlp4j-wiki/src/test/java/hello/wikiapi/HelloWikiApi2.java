package hello.wikiapi;

import java.io.IOException;

import nlp4j.http.HttpClient5;
import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.XmlUtils;

public class HelloWikiApi2 {

	public static void main(String[] args) throws IOException {

		// http だと 301が返る

		String url = "https://ja.wikipedia.org/w/api.php" //
				+ "?"//
				+ "format=xml"//
				+ "&action=query"//
				+ "&prop=info"//
				+ "&prop=categories"//
				+ "&titles=%E3%82%A8%E3%83%9E%E3%83%BB%E3%83%AF%E3%83%88%E3%82%BD%E3%83%B3";//

		// action: query: query Fetch data from and about MediaWiki.
		// format: One of the following values: json, jsonfm, none, php, phpfm, rawfm,
		// xml, xmlfm

		try (HttpClient5 client = new HttpClient5();) {
			System.err.println(url);
			DefaultNlpServiceResponse res = client.get(url);

			System.err.println(res.getResponseCode());

			System.err.println(res.getHeaders());

//		System.err.println(res.getOriginalResponseBody());

			System.err.println(XmlUtils.prettyFormatXml(res.getOriginalResponseBody()));
		}

	}

}
