package nlp4j.wiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;

/**
 * Wikipedia multistream dump command line tool.
 *
 * <pre>
 * java -jar nlp4j-wiki.jar \
 *   --dump jawiki-20260801-pages-articles-multistream.xml.bz2 \
 *   --index jawiki-20260801-pages-articles-multistream-index.txt.bz2
 * </pre>
 *
 * Output:
 *
 * <pre>
 * java -jar nlp4j-wiki.jar \
 *   --dump jawiki-20260801-pages-articles-multistream.xml.bz2 \
 *   --index jawiki-20260801-pages-articles-multistream-index.txt.bz2 \
 *   > test.txt
 * </pre>
 *
 * Single page:
 *
 * <pre>
 * java -jar nlp4j-wiki.jar \
 *   --dump jawiki-20260801-pages-articles-multistream.xml.bz2 \
 *   --index jawiki-20260801-pages-articles-multistream-index.txt.bz2 \
 *   --title 日本
 * </pre>
 */
public class App {

	public static void main(String[] args) throws Exception {

		File dumpFile = null;
		File indexFile = null;
		String title = null;

		// ------------------------------
		// Parse command line arguments
		// ------------------------------

		for (int i = 0; i < args.length; i++) {

			switch (args[i]) {

			case "--dump":
				if (++i >= args.length) {
					usageAndExit("--dump requires a file.");
				}
				dumpFile = new File(args[i]);
				break;

			case "--index":
				if (++i >= args.length) {
					usageAndExit("--index requires a file.");
				}
				indexFile = new File(args[i]);
				break;

			case "--title":
				if (++i >= args.length) {
					usageAndExit("--title requires a page title.");
				}
				title = args[i];
				break;

			case "--help":
			case "-h":
				usage();
				return;

			default:
				usageAndExit("Unknown option: " + args[i]);
			}
		}

		// ------------------------------
		// Validate arguments
		// ------------------------------

		if (dumpFile == null) {
			usageAndExit("--dump is required.");
		}

		if (indexFile == null) {
			usageAndExit("--index is required.");
		}

		if (!dumpFile.isFile()) {
			usageAndExit(
					"Dump file not found: "
							+ dumpFile.getAbsolutePath());
		}

		if (!indexFile.isFile()) {
			usageAndExit(
					"Index file not found: "
							+ indexFile.getAbsolutePath());
		}

		/*
		 * --dump:
		 *   jawiki-YYYYMMDD-pages-articles-multistream.xml.bz2
		 */
		if (!looksLikeDump(dumpFile)) {
			usageAndExit(
					"Invalid --dump file: "
							+ dumpFile.getName()
							+ System.lineSeparator()
							+ "--dump must specify "
							+ "pages-articles-multistream.xml.bz2");
		}

		/*
		 * --index:
		 *   jawiki-YYYYMMDD-pages-articles-multistream-index.txt.bz2
		 */
		if (!looksLikeIndex(indexFile)) {
			usageAndExit(
					"Invalid --index file: "
							+ indexFile.getName()
							+ System.lineSeparator()
							+ "--index must specify "
							+ "pages-articles-multistream-index.txt.bz2");
		}

		// ------------------------------
		// Execute
		// ------------------------------

		if (title != null) {
			dumpOne(dumpFile, indexFile, title);
		}
		else {
			dumpAll(dumpFile, indexFile);
		}
	}

	/**
	 * Output one Wikipedia page.
	 */
	private static void dumpOne(
			File dumpFile,
			File indexFile,
			String title) throws Exception {

		try (WikiDumpReader dumpReader =
				new WikiDumpReader(dumpFile, indexFile)) {

			WikiPage page = dumpReader.getItem(title);

			if (page == null) {
				System.err.println(
						"Not Found: " + title);
				return;
			}

			printPage(page, System.out);
		}
	}

	/**
	 * Output all Wikipedia pages listed in the multistream index.
	 *
	 * Index format:
	 *
	 * <pre>
	 * offset:pageId:title
	 * </pre>
	 *
	 * Example:
	 *
	 * <pre>
	 * 4422909868:4821051:日本
	 * </pre>
	 */
	private static void dumpAll(
			File dumpFile,
			File indexFile) throws Exception {

		try (
				WikiDumpReader dumpReader =
						new WikiDumpReader(
								dumpFile,
								indexFile);

				FileInputStream fis =
						new FileInputStream(indexFile);

				BZip2CompressorInputStream bz =
						new BZip2CompressorInputStream(fis);

				BufferedReader br =
						new BufferedReader(
								new InputStreamReader(
										bz,
										StandardCharsets.UTF_8))) {

			String line;

			long count = 0;

			while ((line = br.readLine()) != null) {

				/*
				 * Wikipedia multistream index:
				 *
				 * offset:pageId:title
				 *
				 * ':' may appear in a Wikipedia title,
				 * so split into at most 3 parts.
				 */
				String[] values =
						line.split(":", 3);

				if (values.length != 3) {

					System.err.println(
							"Invalid index line: "
									+ line);

					continue;
				}

				String title = values[2];

				WikiPage page =
						dumpReader.getItem(title);

				if (page == null) {

					System.err.println(
							"Not Found: "
									+ title);

					continue;
				}

				printPage(page, System.out);

				count++;

				/*
				 * Progress messages go to stderr
				 * so stdout can be redirected.
				 */
				if (count % 10000 == 0) {

					System.err.printf(
							"Processed: %,d pages%n",
							count);
				}
			}

			System.err.printf(
					"Done: %,d pages%n",
					count);
		}
	}

	/**
	 * Output WikiPage content to stdout.
	 */
	private static void printPage(
			WikiPage page,
			PrintStream out) {

		out.println(page.getText());
	}

	private static boolean looksLikeDump(
			File file) {

		String name =
				file.getName().toLowerCase();

		return name.endsWith(
				"-pages-articles-multistream.xml.bz2");
	}

	private static boolean looksLikeIndex(
			File file) {

		String name =
				file.getName().toLowerCase();

		return name.endsWith(
				"-pages-articles-multistream-index.txt.bz2");
	}

	private static void usageAndExit(
			String message) {

		System.err.println(
				"ERROR: " + message);

		System.err.println();

		usage();

		System.exit(1);
	}

	private static void usage() {

		System.err.println("""
				Usage:

				  java -jar nlp4j-wiki.jar \\
				    --dump <pages-articles-multistream.xml.bz2> \\
				    --index <pages-articles-multistream-index.txt.bz2>

				Single page:

				  java -jar nlp4j-wiki.jar \\
				    --dump <pages-articles-multistream.xml.bz2> \\
				    --index <pages-articles-multistream-index.txt.bz2> \\
				    --title <Wikipedia title>
				""");
	}

}
