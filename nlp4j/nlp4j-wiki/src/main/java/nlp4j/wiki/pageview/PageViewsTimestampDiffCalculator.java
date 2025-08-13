package nlp4j.wiki.pageview;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Stream;

import nlp4j.http.FileDownloader;
import nlp4j.util.UTCUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wikipedia の PageView について、何分後に出力されているのかを確認するプログラム
 */
public class PageViewsTimestampDiffCalculator {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// https://dumps.wikimedia.org/other/pageviews/2025/2025-08/

		String yyyy_utc = UTCUtils.getUTC("yyyy");
		String MM_utd = UTCUtils.getUTC("MM");

		// https://dumps.wikimedia.org/other/pageviews/2025/2025-08/
		String url_dump_wiki_pageviews_of_this_month = //
				"https://dumps.wikimedia.org/other/pageviews/" //
						+ yyyy_utc // 2025
						+ "/" //
						+ yyyy_utc // 2025
						+ "-" //
						+ MM_utd // 08
						+ "/";

		File tempFile = File.createTempFile("nlp4j-", ".txt");
		FileDownloader.download(url_dump_wiki_pageviews_of_this_month, tempFile, true);

		System.err.println(tempFile.getAbsolutePath());

		/*
		 * 
		 * <html> <head><title>Index of /other/pageviews/2025/2025-08/</title></head>
		 * <body> <h1>Index of /other/pageviews/2025/2025-08/</h1><hr><pre><a
		 * href="../">../</a> <a
		 * href="pageviews-20250801-000000.gz">pageviews-20250801-000000.gz</a>
		 * 01-Aug-2025 01:37 48430776 <a
		 * href="pageviews-20250801-010000.gz">pageviews-20250801-010000.gz</a>
		 * 01-Aug-2025 02:23 47924918 <a
		 * href="pageviews-20250801-020000.gz">pageviews-20250801-020000.gz</a>
		 * 01-Aug-2025 03:22 48105907 <a
		 * href="pageviews-20250801-030000.gz">pageviews-20250801-030000.gz</a>
		 * 01-Aug-2025 04:07 47552554 <a
		 * href="pageviews-20250801-040000.gz">pageviews-20250801-040000.gz</a>
		 * 01-Aug-2025 05:06 48693157 ... </pre><hr></body> </html>
		 * 
		 */

		try (Stream<String> lines = Files.lines(tempFile.toPath())) {

			long totalDiffMinutes = 0;
			int count = 0;

			for (String line : (Iterable<String>) lines::iterator) {

				if (line.startsWith("<a href=\"pageview") == false) {
					continue;
				}

				// ファイル名部分のタイムスタンプを抽出 (yyyyMMdd-HHmmss)
				Pattern filePattern = Pattern.compile("pageviews-(\\d{8})-(\\d{6})\\.gz");

				// 実際のタイムスタンプ部分を抽出 (dd-MMM-yyyy HH:mm)
				Pattern actualPattern = Pattern.compile("(\\d{2}-[A-Za-z]{3}-\\d{4} \\d{2}:\\d{2})");

				Matcher fileMatcher = filePattern.matcher(line);
				LocalDateTime fileTimestamp = null;

				if (fileMatcher.find()) {
					String datePart = fileMatcher.group(1); // 20250801
					String timePart = fileMatcher.group(2); // 000000
					DateTimeFormatter fileFmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
					fileTimestamp = LocalDateTime.parse(datePart + timePart, fileFmt);
				}

				Matcher actualMatcher = actualPattern.matcher(line);
				LocalDateTime actualTimestamp = null;

				if (actualMatcher.find()) {
					String actualStr = actualMatcher.group(1);
					DateTimeFormatter actualFmt = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm", Locale.ENGLISH);
					actualTimestamp = LocalDateTime.parse(actualStr, actualFmt);
				}

				if (fileTimestamp != null && actualTimestamp != null) {
					long diffMinutes = Duration.between(fileTimestamp, actualTimestamp).toMinutes();
					totalDiffMinutes += diffMinutes;
					count++;
//					System.out.println("File timestamp   : " + fileTimestamp);
//					System.out.println("Actual timestamp : " + actualTimestamp);
//					System.out.println("Difference (min) : " + diffMinutes);
				} else {
//					System.out.println("Could not parse timestamps.");
				}
			} // END_OF_FOR

			if (count > 0) {
				double averageDiff = (double) totalDiffMinutes / count;
				System.out.printf("Average difference (min): %.2f%n", averageDiff);
			} else {
				System.out.println("No valid lines to calculate average.");
			}

		}

	}

}
