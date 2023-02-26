package nlp4j.wiki.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import nlp4j.util.DateUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;
import nlp4j.wiki.util.WikiUtils;

public class MediaWikiHtmlConverter {

	public void convert(File dumpFile) throws IOException {
		File outFile = new File(dumpFile.getAbsolutePath() + ".html.txt");
		convert(dumpFile, outFile);
	}

	public void convert(File dumpFile, File outFile) throws IOException {

		if (dumpFile.exists() == false) {
			throw new FileNotFoundException("NOT_FOUND: " + dumpFile.getAbsolutePath());
		}

		if (outFile.exists() == true) {
			throw new IOException("FILE_ALREADY_EXISTS: " + outFile.getAbsolutePath());
		}

		System.err.println("CONVERTING_FROM: " + dumpFile.getAbsolutePath());
		System.err.println("CONVERTING_TO: " + outFile.getAbsolutePath());

		long time1 = System.currentTimeMillis();
		String time_s1 = DateUtils.toISO8601(new Date());

		// 自作のHandlerを指定する DEFINE Wiki Dump Handler
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			int count = 0;

			StringBuilder sb = new StringBuilder();

			@Override
			public void read(WikiPage page) throws BreakException {
				String pageId = page.getId();
				String pageTitle = page.getTitle();

				// テンプレートなどはスキップ
				if (page != null && pageTitle.contains(":") == true) {
					return;
				} //
				else {
					// ページ全文
					String wikiTextAll = page.getText();

					{ // wikilink_all
						try {
							String html_all = WikiUtils.toHtml(wikiTextAll);
							sb.append(pageId + "," + pageTitle + "," + html_all.replace("\n", "") + "\n");
						} catch (Exception e) {
							e.printStackTrace();
							throw new BreakException();
						}
					}

					{ // OUTPUT
						// ALL
						try {
							FileUtils.write(outFile, sb.toString(), "UTF-8", true);
							sb = new StringBuilder();
						} catch (IOException e) {
							e.printStackTrace();
							throw new BreakException();
						}
					}

					count++;

					if (count % 1000 == 0) {
						System.err.println(count);
					}
				}
			}

		}; // END OF WikiPageHandler

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
			}
		}

		long time2 = System.currentTimeMillis();
		String time_s2 = DateUtils.toISO8601(new Date());

		System.err.println("FINISHED");
		System.err.println("time: " + (time2 - time1));
		System.err.println("input: " + dumpFile.getAbsolutePath());
		System.err.println("output: " + outFile.getAbsolutePath());
		System.err.println("start: " + time_s1);
		System.err.println("end: " + time_s2);

	}

}
