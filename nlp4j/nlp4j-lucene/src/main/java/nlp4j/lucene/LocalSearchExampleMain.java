package nlp4j.lucene;

public class LocalSearchExampleMain {

	public static void main(String[] args) {

		try (LocalSearch search = new LocalSearch("en")) {
			search.add("1", "(1) Developers are searching documents with a local search engine.");
			search.add("2", "(2) A developer searched many documents yesterday.");
			search.add("3", "(3) This tool searches local JSON documents.");
			search.add("4", "(4) Lucene's EnglishAnalyzer is useful for English full-text search.");
			search.add("5", "(5) The quick brown fox jumps over the lazy dog.");
			search.commit();
			SearchResult[] result = search.search("search AND developer", 10);
			System.out.println("size: " + result.length);
			for (int n = 0; n < result.length; n++) {
				System.out.println("result[" + n + "].id: " + result[n].id);
				System.out.println("result[" + n + "].body: " + result[n].body);
				System.out.println("result[" + n + "].score: " + result[n].score);
			}
		}
	}
}
// Expected Results:
//size: 2
//result[0].id: 0
//result[0].body: Developers are searching documents with a local search engine.
//result[0].score: 0.5777416
//result[1].id: 1
//result[1].body: A developer searched many documents yesterday.
//result[1].score: 0.5673906
