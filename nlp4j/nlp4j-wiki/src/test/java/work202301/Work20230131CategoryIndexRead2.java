package work202301;

import java.io.File;
import java.io.IOException;

import nlp4j.wiki.category.WikiCategoryIndexReader;

public class Work20230131CategoryIndexRead2 {

	public static void main(String[] args) throws IOException {

		// カテゴリ情報を読み込み、親カテゴリの情報を取得する

		File indexFile = new File("R:/jawiki-20221101-pages-articles-multistream-categories-index.txt");
		File categoryFile = new File("R:/jawiki-20221101-pages-articles-multistream-categories.txt");

		WikiCategoryIndexReader reader = new WikiCategoryIndexReader(indexFile, categoryFile);

//		System.err.println(reader.getCategory("漫画"));
		{
			String title = "貝塚";
			System.err.println(title + "->" + reader.getCategory(title).getRoot());
			System.err.println(title + "->" + reader.getCategory(title).getRootCategories());
			System.err.println(title + "->" + reader.getCategory(title).getFirstRootCategories());
		}
//		System.err.println(reader.getCategory("娯楽").getRoot());
//		System.err.println(reader.getCategory("主題別分類").getRoot());
//		System.err.println(reader.getCategory("主要カテゴリ").getRoot());
//		System.err.println(reader.getCategory("ジャンル別の書物").getRoot());

//		System.err.println(reader.getCategory("JR"));
		
	}

}
