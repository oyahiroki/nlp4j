package nlp4j.wikianalyticsdatasets.pageviews;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.util.List;

import nlp4j.counter.Count;
import nlp4j.counter.Counter;
import nlp4j.util.TextFileUtils;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

public class HelloPageViews3_Wikipedia {

	public static void main(String[] args) throws Exception {

		String url = "https://dumps.wikimedia.org/other/pageviews/2023/2023-10/pageviews-20231027-030000.gz";

		Counter<String> counter = new Counter<>();

		String wiki_dir = "/usr/local/wiki/jawiki/20230101/";

		String wiki_dumpIndexFileName = wiki_dir + "jawiki-20230101-pages-articles-multistream-index.txt.bz2";
		String wiki_dumpFileName = wiki_dir + "jawiki-20230101-pages-articles-multistream.xml.bz2";

		try (BufferedReader br = TextFileUtils.openPlainTextFileAsBufferedReader(new URL(url));

		) {

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

		try (WikiDumpReader dumpReader = new WikiDumpReader( //
				new File(wiki_dumpFileName), //
				new File(wiki_dumpIndexFileName)); //
		) {

			List<Count<String>> top = counter.getCountListSorted();
			int rank = 1;

			String view = "大学";

			for (int n = 0; n < 3000; n++) {
				String v = top.get(n).getValue();
				if (v.contains(":") || v.equals("メインページ")) {
					continue;
				}

				WikiPage page = dumpReader.getItem(v);

				if (page == null) {
					continue;
				}

				if (page.getCategoryTags() == null) {
					continue;
				}

//				if (page.getPlainText().contains(view) == false
//						&& page.getCategoryTags().toString().contains(view) == false) {
//					continue;
//				}
				if (page.getCategoryTags().toString().contains(view) == false) {
					continue;
				}

				if (v.contains(view) == false) {
					continue;
				}

//				System.out.println(v);
//
//				System.out.println("<root_node_text>");
//				System.out.println(page.getRootNodePlainText());
//				System.out.println("</root_node_text>");
				/*
				 * <root_node_text> is a Japanese multinational video game company headquartered
				 * in Kyoto, Japan. It develops video games and video game consoles ...
				 * </root_node_text>
				 * 
				 */
//				System.out.println("<text>\n" + page.getText() + "\n</text>");
				/*
				 * <text> {{Short description|Japanese video game company}} <!-- popup
				 * [[File:Nintendo.svg]] --> {{Pp-vandalism|small=yes}} {{Use dmy
				 * dates|date=October 2022}} {{Use American English|date=November 2020}}
				 * {{Infobox company | name = Nintendo Co., Ltd. | logo = Nintendo.svg |
				 * logo_alt = Logo in white on red background since 2016 | logo_caption = Logo
				 * in white on red background since 2016 | image =
				 * Nintendo_Headquarters_-_panoramio.jpg ... </text>
				 * 
				 */

//				System.out.println("<xml>\n" + page.getXml() + "\n</xml>");

//				System.out.println(page.getCategoryTags());

				System.err.println(rank + "," + v + ", " + top.get(n).getCount());

				String text = (page.getRootNodePlainText().indexOf("。") > 0)
						? page.getRootNodePlainText().substring(0, page.getRootNodePlainText().indexOf("。"))
						: page.getRootNodePlainText();

				System.err.println(" -> " + text);

				rank++;
			}

		}

	}

}
