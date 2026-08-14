package examples;

import nlp4j.lucene.LocalSearch;

public class Example12_Count {

	public static void main(String[] args) throws Exception {
		try (LocalSearch search = new LocalSearch("ja")) {
			search.add("1", "東京都の学校");
			search.add("2", "京都の学校");
			search.add("3", "京都市の学校");
			search.commit();

			long countKyoto = search.count("京都");
			System.out.println("testCount002 count(京都): " + countKyoto);
			assertEquals(2L, countKyoto);

			long countTokyo = search.count("東京");
			System.out.println("testCount002 count(東京): " + countTokyo);
			assertEquals(1L, countTokyo);
		}
	}

	private static void assertEquals(long n1, long n2) {
		if (n1 != n2) {
			throw new RuntimeException("not equal");
		}

	}

}
