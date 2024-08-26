package nlp4j.wiki.pageview;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.counter.Counter;
import nlp4j.util.TextFileUtils;

public class PageViewCounter {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private Counter<String> counter = new Counter<>();

	public Counter<String> get(String url, String domain) throws IOException {

		boolean writeCache = false;

		logger.info("URL: " + url);

		try (BufferedReader br = TextFileUtils.openPlainTextFileAsBufferedReader(new URL(url), writeCache);) {

			// domain_code page_title count_views total_response_size
//			br.readLine();

			String s;
			int countLine = 0;

			try {
				while (( //
				s = br.readLine() // may throw java.io.EOFException: Unexpected end of ZLIB input stream
				) != null) {

					countLine++;

					String[] ss = s.split(" ");
					if (ss.length < 3) {
//					System.err.println("s: " + s);
						logger.error("Invalid format, line=" + countLine + ",data=" + s);
						continue;
					}
					String domain_code = ss[0];
					String page_title = ss[1];
					int count_views = Integer.parseInt(ss[2]);
//				int total_response_size = Integer.parseInt(ss[3]);

//				if (domain_code.equals("ja") && page_title.equals("任天堂".replace(" ", "_"))) {
//					System.err.println(countLine + ": " + s + ", count_views:" + count_views);
//					break;
//				}

					if (domain_code.equals(domain)) {
						counter.add(page_title, count_views);
					}

				}

			}
			// File Error
			catch (EOFException e) {
				e.printStackTrace();
				logger.error(e);
				return this.counter;
			} //
				// 404 Not Found
			catch (java.io.FileNotFoundException e) {
				e.printStackTrace();
				logger.error(e);
				throw e;
			}
			// Other error
			catch (IOException e) {
				throw e;
			}

			logger.info("total lines: " + countLine);

		}

//		List<Count<String>> top = counter.getCountListSorted();

//		return top;

		return this.counter;
	}

}
