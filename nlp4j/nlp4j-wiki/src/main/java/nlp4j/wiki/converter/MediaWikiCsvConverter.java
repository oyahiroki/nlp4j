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

		// WIKI DUMP FILE
		File dumpFile = new File("/usr/local/wiki/jawiki/20221101/" //
				+ "jawiki-20221101-pages-articles-multistream.xml.bz2");

		Appendable out = System.out;

		File outFile = new File("R:/" + "jawiki-20221101-pages-articles-multistream.csv");

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
		header.add("text_length");
		header.add("text");

//	final	Integer max = 100;

		final Integer max;

//		max = null;
		max = 1000;

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
					String categories = page.getCategoryTags() != null ? page.getCategoryTags().toString() //
							.replace("[", "")//
							.replace("]", "")//
							.replace(", ", ",")//
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
