package work202302;

import nlp4j.wiki.converter.MediaWikiCsvConverter_V2;

public class Work20230201_MediaWikiCsv_V2_ParentCategories {

	public static void main(String[] args) throws Exception {
		// Wikipedia のページ情報を親カテゴリと出力する

		// Abstract 文に加えて、第一見出しを加える？（記事によっては見出しが一つしかない）

		// 機械工学
		String category = "自動車";

		String param0 = "/usr/local/wiki/jawiki/20230101/" //
				+ "jawiki-20230101-pages-articles-multistream.xml.bz2";
		// CategoryIndex
		String param1 = "R:/" + "jawiki-20230101-pages-articles-multistream-categories-index.txt";
		// Categories
		String param2 = "R:/" + "jawiki-20230101-pages-articles-multistream-categories.txt";
		// Output
		String param3 = "R:/" + "jawiki-20230101-pages-articles-multistream-abstract_textall_categories_" //
				+ category //
				+ ".csv";
		// Count
//		String param4 = "10";
//		String param4 = "1000";
		String param4 = "-1";
		String param5 = category;

		String[] params = { param0, param1, param2, param3, param4, param5 };

		MediaWikiCsvConverter_V2.main(params);

	}

}
