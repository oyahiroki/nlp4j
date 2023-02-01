package work202301;

import java.io.File;
import java.io.IOException;

import nlp4j.wiki.category.WikiCategoryIndexReader;

public class Work20230131CategoryIndexRead {

	public static void main(String[] args) throws IOException {

		File indexFile = new File("R:/jawiki-20221101-pages-articles-multistream-categories-index.txt");
		File categoryFile = new File("R:/jawiki-20221101-pages-articles-multistream-categories.txt");

		WikiCategoryIndexReader reader = new WikiCategoryIndexReader(indexFile, categoryFile);

//		System.err.println(reader.getCategory("漫画"));
		{
			String title = "漫画";
			System.err.println(title + "->" + reader.getCategory(title).getRoot());
			System.err.println(title + "->" + reader.getCategory(title).getRootCategories());
			System.err.println(title + "->" + reader.getCategory(title).getFirstRootCategories());
		}
//		System.err.println(reader.getCategory("娯楽").getRoot());
//		System.err.println(reader.getCategory("主題別分類").getRoot());
//		System.err.println(reader.getCategory("主要カテゴリ").getRoot());
//		System.err.println(reader.getCategory("ジャンル別の書物").getRoot());

//		System.err.println(reader.getCategory("JR"));
		{
			String title = "JR";
			System.err.println(title + "->" + reader.getCategory(title).getRoot());
			System.err.println(title + "->" + reader.getCategory(title).getRootCategories());
			System.err.println(title + "->" + reader.getCategory(title).getFirstRootCategories());
		}
		{
			String title = "日本の漫画家";
			System.err.println(title + "->" + reader.getCategory(title).getRoot());
			System.err.println(title + "->" + reader.getCategory(title).getRootCategories());
			System.err.println(title + "->" + reader.getCategory(title).getFirstRootCategories());
		}
		{
			String title = "高橋留美子"; // 実在の人物名：有名人はカテゴリ化されている？
			System.err.println(title + "->" + reader.getCategory(title).getRoot());
			System.err.println(title + "->" + reader.getCategory(title).getRootCategories());
			System.err.println(title + "->" + reader.getCategory(title).getFirstRootCategories());
		}
		{
			String title = "こちら葛飾区亀有公園前派出所の登場人物"; // 架空の人物名
			System.err.println(title + "->" + reader.getCategory(title).getRoot());
			System.err.println(title + "->" + reader.getCategory(title).getRootCategories());
			System.err.println(title + "->" + reader.getCategory(title).getFirstRootCategories());
		}
		{
			String title = "架空の警察官"; // 架空の人物名
			System.err.println(title + "->" + reader.getCategory(title).getRoot());
			System.err.println(title + "->" + reader.getCategory(title).getRootCategories());
			System.err.println(title + "->" + reader.getCategory(title).getFirstRootCategories());
		}

	}

}
