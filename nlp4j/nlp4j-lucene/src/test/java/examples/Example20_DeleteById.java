package examples;

import nlp4j.lucene.LocalSearch;

public class Example20_DeleteById {

	public static void main(String[] args) throws Exception {

		try (LocalSearch search = new LocalSearch("ja")) {

			search.add("1", "東京");
			search.add("2", "京都");
			search.add("3", "大阪");
			search.commit();

			System.out.println(3L == search.count()); // true

			search.delete("2");
			search.commit();

			long count = search.count();
			System.out.println("testDelete001_basic count: " + count);
			System.out.println(2L == count); // true
		}

	}

}
