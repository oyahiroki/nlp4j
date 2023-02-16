package nlp4j.wiki.converter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;
import nlp4j.wiki.category.WikiCategoryIndexReader;

public class MediaWikiCsvConverter_V2 {

	public static void main(String[] args) throws Exception {

		// カテゴリの親情報も出力する

		if (args == null || args.length != 6) {
			System.err.println("Usage:");
			System.err.println("args[0]: Input file path of Wikipedia/Wiktionary dump file in bz2 format");
			System.err.println("args[1]: Category Index File");
			System.err.println("args[2]: Category file");
			System.err.println("args[3]: Output file path of CSV");
			System.err.println("args[4]: Max count of process");
			System.err.println("args[5]: Category filter: set \"null\" when not needed");
			return;
		}

		String inputFileName = args[0];
		File categoryIndexFile = new File(args[1]);
		File categoryFile = new File(args[2]);
		String outFileName = args[3];
		// 最大件数
		String maxParam = args[4];
		// カテゴリを指定する
		String categoryFilter = args[5].equals("null") ? null : args[5];

		// Category
		WikiCategoryIndexReader wikiCategoryIndexReader = new WikiCategoryIndexReader(categoryIndexFile, categoryFile);

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
			throw new IOException("Output file already exists: " + outFile.getAbsolutePath());
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
		header.add("categories_all");
		header.add("categories_firstall");
		header.add("templates");
		header.add("text_length");
		header.add("text_abstract");
		header.add("text_all");

//	final	Integer max = 100;

		try ( //
				WikiDumpReader dumpReader = new WikiDumpReader(dumpFile);
				CSVPrinter printer = new CSVPrinter( //
						out, //
						CSVFormat.RFC4180.builder().setHeader(header.toArray(new String[0])).build() //
				); //
		) {
			dumpReader.read(new WikiPageHandler() {
				int count = 0;

				@Override
				public void read(WikiPage page) throws BreakException {

					if (page.getTitle().contains(":")) {
						return; // skip
					}

					count++;
					if (count % 1000 == 0) {
						System.err.println(count);
					}

					if (max != null && count > max) {
						throw new BreakException();
					}

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
					String categories2all = "";
					{ // 全カテゴリ情報
						if (page.getCategoryTags() != null) {
							Set<String> rootCategoriesAll = new LinkedHashSet<>();
							for (String pageCategoryTag : page.getCategoryTags()) {
//								System.err.println(title + " -> Category: " + pageCategoryTag);
								if (wikiCategoryIndexReader.getCategory(pageCategoryTag) != null
										&& wikiCategoryIndexReader.getCategory(pageCategoryTag)
												.getRootCategoriesAsList() != null) {
									List<String> pageRootCategories = wikiCategoryIndexReader
											.getCategory(pageCategoryTag).getRootCategoriesAsList();
									for (String t : pageRootCategories) {
										if (rootCategoriesAll.contains(t) == false) {
											rootCategoriesAll.add(t);
										}
									}
								}
							}

							if (categoryFilter != null && rootCategoriesAll.contains(categoryFilter) == false) {
								return; // skip
							}

							categories2all = String.join(",", new ArrayList<String>(rootCategoriesAll));
//							System.err.println(categories2all);
						}

					}

					String categories3firstall = "";
					{ // 第一カテゴリをたどったもの
						if (page.getCategoryTags() != null) {
							Set<String> rootCategoriesAll = new LinkedHashSet<>();
							for (String pageCategoryTag : page.getCategoryTags()) {
								if (wikiCategoryIndexReader.getCategory(pageCategoryTag) != null
										&& wikiCategoryIndexReader.getCategory(pageCategoryTag)
												.getFirstRootCategoriesAsList() != null) {
									List<String> pageRootCategories = wikiCategoryIndexReader
											.getCategory(pageCategoryTag).getFirstRootCategoriesAsList();
									for (String t : pageRootCategories) {
										if (rootCategoriesAll.contains(t) == false) {
											rootCategoriesAll.add(t);
										}
									}
								}
							}
							categories3firstall = String.join(",", new ArrayList<String>(rootCategoriesAll));
//							System.err.println(categories3firstall);
						}

					}
					String templates = page.getTemplateTags() != null //
							? String.join(",", page.getTemplateTags()) //
							: "";
					String text_abstract = page.getRootNodePlainText();
					String text_length = "" + text_abstract.length();
					String text_all = page.getPlainText();
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
								categories2all, //
								categories3firstall, //
								templates, //
								text_length, //
								text_abstract, //
								text_all //
						);
					} catch (IOException e) {
						System.err.println("error on: " + count);
						e.printStackTrace();
						throw new BreakException();
					}

				}
			});
		} catch (BreakException be) {
			System.err.println("OK");
		}

	}

}
