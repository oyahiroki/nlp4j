package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

public class Example05_VectorAndFieldSearch_2 {

	public static void main(String[] args) throws Exception {
		try (LocalSearch search = LocalSearch.builder("en").vectorDimension(2).build()) {
			search.addJson("{\"id\":\"1\",\"body\":\"East\",\"vector\":[1.0,0.0]}");
			search.addJson("{\"id\":\"2\",\"body\":\"North\",\"vector\":[0.0,1.0]}");
			search.addJson("{\"id\":\"3\",\"body\":\"West\",\"vector\":[-1.0,0.0]}");
			search.addJson("{\"id\":\"4\",\"body\":\"South\",\"vector\":[0.0,-1.0]}");
			search.commit();

			SearchResult[] results = search.search(new float[] { 0.9f, 0.1f }, 10);
			System.out.println("size: " + results.length);
			for (int n = 0; n < results.length; n++) {
				System.out.println("result[" + n + "].id: " + results[n].id + results[n].body);
			}

		}

	}

}
