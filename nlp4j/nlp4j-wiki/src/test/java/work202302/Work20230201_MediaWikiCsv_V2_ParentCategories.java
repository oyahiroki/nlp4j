package work202302;

import nlp4j.wiki.converter.MediaWikiCsvConverter_V2;

public class Work20230201_MediaWikiCsv_V2_ParentCategories {

	public static void main(String[] args) throws Exception {
		// Wikipedia のページ情報を親カテゴリと出力する

		// Abstract 文に加えて、第一見出しを加える？（記事によっては見出しが一つしかない）

		String param0 = "D:/usr/local/wiki/jawiki/20221101/" //
				+ "jawiki-20221101-pages-articles-multistream.xml.bz2";
		// CategoryIndex
		String param1 = "R:/" + "jawiki-20221101-pages-articles-multistream-categories-index.txt";
		// Categories
		String param2 = "R:/" + "jawiki-20221101-pages-articles-multistream-categories.txt";
		// Output
		String param3 = "D:/" + "jawiki-20221101-pages-articles-multistream-abstract_textall_categories_機械工学v2.csv";
		// Count
//		String param4 = "10";
//		String param4 = "1000";
		String param4 = "-1";

		String[] params = { param0, param1, param2, param3, param4 };

		MediaWikiCsvConverter_V2.main(params);

	}

}
