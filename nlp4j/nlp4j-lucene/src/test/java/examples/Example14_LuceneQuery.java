package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

public class Example14_LuceneQuery {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "任天堂は京都にある会社です。");
			search.add("2", "ソニーは東京都にある会社です。");
			search.add("3", "マイクロソフトは米国にある会社です。");
			search.commit();
			{
				String q = "会社 OR 京都";
				SearchResult[] results = search.searchLucene(q, 10);
				printResults("Results", results);
//				Results size: 3
//				result[0].id: 1
//				result[0].body: 任天堂は京都にある会社です。
//				result[0].score: 0.528133
//				result[1].id: 3
//				result[1].body: マイクロソフトは米国にある会社です。
//				result[1].score: 0.063285016
//				result[2].id: 2
//				result[2].body: ソニーは東京都にある会社です。
//				result[2].score: 0.05610562
			}
			System.out.println("---");
			{
				String q = "会社 AND 京都";
				SearchResult[] results = search.searchLucene(q, 10);
				printResults("Results", results);
//				Results size: 1
//				result[0].id: 1
//				result[0].body: 任天堂は京都にある会社です。
//				result[0].score: 0.528133
			}
			System.out.println("---");
		}
	}

	/**
	 * テスト結果をコンソールへ出力する。
	 */
	static private void printResults(String testName, SearchResult[] results) {

		System.out.println(testName + " size: " + results.length);

		for (int i = 0; i < results.length; i++) {

			System.out.println("result[" + i + "].id: " + results[i].id);

			System.out.println("result[" + i + "].body: " + results[i].body);

			System.out.println("result[" + i + "].score: " + results[i].score);
		}
	}
}
