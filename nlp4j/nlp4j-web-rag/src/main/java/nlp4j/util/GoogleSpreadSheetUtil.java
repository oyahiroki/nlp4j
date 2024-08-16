package nlp4j.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nlp4j.NlpServiceResponse;
import nlp4j.http.HttpClient;
import nlp4j.http.HttpClientBuilder;

public class GoogleSpreadSheetUtil {

	static public List<String> loadAsList(String url) throws IOException {
		HttpClient client = (new HttpClientBuilder()).build();

		NlpServiceResponse res = client.get(url);
		String responseBody = res.getOriginalResponseBody();
		String data = new String(responseBody.getBytes("ISO-8859-1"));
//		System.out.println(data);

		List<String> ss = new ArrayList<>();
		for (String s : data.split("\n")) {
			ss.add(s.trim());
		}

		return ss;
	}

	public static void main(String[] args) throws Exception {

		// 公開しているドキュメントのURL
		String url = "https://docs.google.com/spreadsheets/d/1PeAS8MUsi70c7gFb6Py75L19flMcw26bdfPgGrOH9wA/export?format=csv";

		List<String> ss = GoogleSpreadSheetUtil.loadAsList(url);

		for (String s : ss) {
			System.err.println(s);
		}

	}

}
