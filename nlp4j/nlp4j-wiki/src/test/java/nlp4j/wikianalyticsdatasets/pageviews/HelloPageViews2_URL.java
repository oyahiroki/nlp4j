package nlp4j.wikianalyticsdatasets.pageviews;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.util.List;

import nlp4j.counter.Count;
import nlp4j.counter.Counter;
import nlp4j.util.TextFileUtils;

public class HelloPageViews2_URL {

	public static void main(String[] args) throws Exception {

		String url = "https://dumps.wikimedia.org/other/pageviews/2023/2023-10/pageviews-20231027-030000.gz";

		Counter<String> counter = new Counter<>();

		try (BufferedReader br = TextFileUtils.openPlainTextFileAsBufferedReader(new URL(url));) {

			// domain_code page_title count_views total_response_size
//			br.readLine();

			String s;
			int countLine = 0;
			while ((s = br.readLine()) != null) {

				countLine++;

				String[] ss = s.split(" ");
				String domain_code = ss[0];
				String page_title = ss[1];
				int count_views = Integer.parseInt(ss[2]);
				int total_response_size = Integer.parseInt(ss[3]);

//				if (domain_code.equals("ja") && page_title.equals("任天堂".replace(" ", "_"))) {
//					System.err.println(countLine + ": " + s + ", count_views:" + count_views);
//					break;
//				}

				if (domain_code.equals("ja")) {
					counter.add(page_title, count_views);
				}

			}
		}

		List<Count<String>> top = counter.getCountListSorted();
		int rank = 1;
		for (int n = 0; n < 100; n++) {
			String v = top.get(n).getValue();
			if (v.contains(":") || v.equals("メインページ")) {
				continue;
			}
			System.err.println(rank + "," + v + ", " + top.get(n).getCount());
			rank++;
		}

	}

}
