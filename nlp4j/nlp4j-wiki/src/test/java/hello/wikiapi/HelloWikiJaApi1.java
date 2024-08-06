package hello.wikiapi;

import java.io.IOException;
import java.net.URLEncoder;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;

public class HelloWikiJaApi1 {

	public static void main(String[] args) throws IOException {

		// http だと 301が返る

		String url = "https://ja.wiktionary.org/w/api.php" //
				+ "?"//
				+ "format=xml"//
				+ "&action=query"//
				+ "&list=search"//
				+ "&srwhat=text"//
				+ "&srsearch=" + URLEncoder.encode("日本", "UTF-8") //
				+ "&srlimit=500"// 1-500 how many total pages to return
				+ "&srnamespace=0" + "&sroffset=0" + "";//

		// action: query: query Fetch data from and about MediaWiki.
		// format: One of the following values: json, jsonfm, none, php, phpfm, rawfm,
		// xml, xmlfm

		try (HttpClient client = (new HttpClientBuilder()).build();) {
			System.err.println(url);
			NlpServiceResponse res = client.get(url);

			System.err.println(res.getResponseCode());

			System.err.println(res.getHeaders());

			System.err.println(res.getOriginalResponseBody());
		}

	}

}
