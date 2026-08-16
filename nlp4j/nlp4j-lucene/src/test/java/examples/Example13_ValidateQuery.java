package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.LuceneQueryValidationResult;

public class Example13_ValidateQuery {

	public static void main(String[] args) {
		try (LocalSearch search = LocalSearch.builder("ja").build()) {

			{
				String q = "京都 AND (寺院 OR 神社)";
				LuceneQueryValidationResult result = search.validateLuceneQuery(q);
				if (result.isValid()) {
					System.out.println("Valid query");
				} else {
					System.out.println("Invalid query: " + result.getMessage());
				}
			}
			{
				String q = "京都 AND (寺院 OR 神社";
				LuceneQueryValidationResult result = search.validateLuceneQuery(q);
				if (result.isValid()) {
					System.out.println("Valid query");
				} else {
					System.out.println("Invalid query: " + result.getMessage());
				}
			}
		}

	}

}
