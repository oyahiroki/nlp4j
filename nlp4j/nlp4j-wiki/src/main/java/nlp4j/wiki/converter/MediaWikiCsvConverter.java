package nlp4j.wiki.converter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class MediaWikiCsvConverter {

	public static void main(String[] args) throws Exception {

		if (args == null || args.length != 3) {
			System.err.println("Usage:");
			System.err.println("args[0]: Input file path of Wikipedia/Wiktionary dump file in bz2 format");
			System.err.println("args[1]: Output file path of CSV");
			System.err.println("args[2]: Max count of process");
			return;
		}

		String inputFileName = args[0];
		String outFileName = args[1];
		String maxParam = args[2];

		int n = -1;
		try {
			n = Integer.parseInt(maxParam);
		} catch (NumberFormatException e) {

		}

		final Integer max = (n > 0) ? n : null;

//		max = null;

		// WIKI DUMP FILE
//		File dumpFile = new File("/usr/local/wiki/jawiki/20221101/" //
//				+ "jawiki-20221101-pages-articles-multistream.xml.bz2");
		File dumpFile = new File(inputFileName);
		{
			if (dumpFile.exists() == false) {
				System.err.println("Not Found: " + dumpFile.getAbsolutePath());
				return;
			}
			if (dumpFile.getName().endsWith(".bz2") == false) {
				System.err.println("Not .bz2: " + dumpFile.getAbsolutePath());
				return;
			}
		}

		Appendable out = System.out;
//		File outFile = new File("R:/" + "jawiki-20221101-pages-articles-multistream.csv");
		File outFile = new File(outFileName);
		if (outFile.exists()) {
			System.err.println("Output file already exists: " + outFile.getAbsolutePath());
			return;
		}

		out = new OutputStreamWriter(new FileOutputStream(outFile, false), "UTF-8");

		List<String> header = new ArrayList<String>();
		header.add("title");
		header.add("namespace");
		header.add("id");

		header.add("revision_id");
		header.add("revision_parentid");
		header.add("revision_timestamp");
		header.add("is_redirect");
		header.add("redirect_title");
		header.add("categories");
		header.add("templates");
		header.add("text_length");
		header.add("text");

//	final	Integer max = 100;

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile);

				CSVPrinter printer = new CSVPrinter( //
						out, //
						CSVFormat.RFC4180.builder().setHeader(header.toArray(new String[0])).build() //
				);

		) {
			dumpReader.read(new WikiPageHandler() {
				int count = 0;

				@Override
				public void read(WikiPage page) throws BreakException {

					if (page.getTitle().contains(":")) {
						return; // skip
					}

					count++;

					String namespace = page.getNamespace();
					String id = page.getId();
					String revision_id = page.getRevisionId();
					String revision_parentid = page.getParentId();
					String revision_timestamp = page.getTimestamp();
					String title = (page.getTitle());
					String is_redirect = "" + page.isRediect();
					String redirect_title = page.isRediect() ? page.getRediect_title() : "";
					String categories = page.getCategoryTags() != null //
							? String.join(",", page.getCategoryTags()) //
							: "";
					String templates = page.getTemplateTags() != null //
							? String.join(",", page.getTemplateTags()) //
							: "";
					String text = page.getRootNodePlainText();
					String text_length = "" + text.length();
					try {
						printer.printRecord( //
								title, //
								namespace, //
								id, //
								revision_id, //
								revision_parentid, //
								revision_timestamp, //
								is_redirect, //
								redirect_title, //
								categories, //
								templates, //
								text_length, //
								text //
						);
					} catch (IOException e) {
						System.err.println("error on: " + count);
						e.printStackTrace();
						throw new BreakException();
					}

					if (count % 1000 == 0) {
						System.err.println(count);
					}

					if (max != null && count > max) {
						throw new BreakException();
					}

				}
			});
		} catch (BreakException be) {
			System.err.println("OK");
		}

	}

}
