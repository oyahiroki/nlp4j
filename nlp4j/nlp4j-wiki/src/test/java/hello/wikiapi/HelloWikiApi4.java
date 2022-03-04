package hello.wikiapi;

import java.io.IOException;

import com.google.gson.JsonObject;

import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.HttpClient;
import nlp4j.util.JsonUtils;
import nlp4j.util.XmlUtils;

public class HelloWikiApi4 {

	public static void main(String[] args) throws IOException {

		// ページをリストする

		// see
		// https://www.mediawiki.org/wiki/API:Categorymembers

		// http だと 301が返る

		String from = "414e415048594c4143544f47454e0a414e415048594c4143544f47454e";

		from = "4241434b5052494d494e470a4241434b5052494d494e47";

		String url = "https://en.wiktionary.org/w/api.php" //
				+ "?"//
				+ "format=json"//
				+ "&action=query"//
				+ "&list=categorymembers"//
				+ "&cmtitle=Category:en:Medicine"//
				+ "&cmlimit=500"// 1-500
//				+ "&cmsort=timestamp"//
				+ "&cmsort=sortkey"//
				+ "&cmprop=ids|title|sortkey|sortkeyprefix|type|timestamp" //
				+ "&cmstarthexsortkey=" + from //
//				+ "&cmstart=2014-01-13T06:20:07Z"
//				+ "&prop="
//				+ "categories"//
//				+ "linkshere"//
//				+ "pageterms"//
//				+ "info"//
//				+ "iwlinks"//
//				+ "&titles=Category:en:Medicine" //
		;//

		// action: query: query Fetch data from and about MediaWiki.
		// format: One of the following values: json, jsonfm, none, php, phpfm, rawfm,
		// xml, xmlfm

		HttpClient client = new HttpClient();
		System.err.println(url);
		DefaultNlpServiceResponse res = client.get(url);

		System.err.println(res.getResponseCode());

		System.err.println(res.getHeaders());

		// content-type

		JsonObject jo = res.getAsJsonObject();
		System.err.println(JsonUtils.prettyPrint(jo));

//		System.err.println(JsonUtils.prettyPrint(res.getOriginalResponseBody()));
//		System.err.println(XmlUtils.prettyFormatXml(res.getOriginalResponseBody()));

	}

}
