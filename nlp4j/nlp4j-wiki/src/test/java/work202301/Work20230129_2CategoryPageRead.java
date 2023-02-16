package work202301;

import nlp4j.wiki.category.WikiCategoryIndexCreator;

public class Work20230129_2CategoryPageRead {

	public static void main(String[] args) throws Exception {

		// カテゴリ情報の出力

		// Index File
//		String indexFileName = "/usr/local/wiki/jawiki/20221101/jawiki-20221101-pages-articles-multistream-index.txt.bz2";
//		String indexFileName = "R:/jawiki-20221101-pages-articles-multistream-index.txt.bz2";
		String indexFileName = "R:/jawiki-20230101-pages-articles-multistream-index.txt.bz2";
		// Dump File
//		String dumpFileName = "/usr/local/wiki/jawiki/20221101/jawiki-20221101-pages-articles-multistream.xml.bz2";
//		String dumpFileName = "R:/jawiki-20221101-pages-articles-multistream.xml.bz2";
		String dumpFileName = "R:/jawiki-20230101-pages-articles-multistream.xml.bz2";

//		String outCategoryFileName = "R:/jawiki-20221101-pages-articles-multistream-categories.txt";
//		String outCategoryFileName = "R:/jawiki-20230101-pages-articles-multistream-categories.txt";
		String outCategoryFileName = "R:/jawiki-20230101-pages-articles-multistream-categories_v2.txt";

//		String outCategoryIndexFileName = "R:/jawiki-20221101-pages-articles-multistream-categories-index.txt";
//		String outCategoryIndexFileName = "R:/jawiki-20230101-pages-articles-multistream-categories-index.txt";
		String outCategoryIndexFileName = "R:/jawiki-20230101-pages-articles-multistream-categories-index_v2.txt";

		WikiCategoryIndexCreator wikiCategoryIndexCreator = new WikiCategoryIndexCreator();

//		wikiCategoryIndexCreator.processCategoryFiles(indexFileName, dumpFileName, outCategoryFileName,
//				outCategoryIndexFileName);
		wikiCategoryIndexCreator.processCategoryFiles(indexFileName, dumpFileName, outCategoryFileName,
				outCategoryIndexFileName);

	}

}
