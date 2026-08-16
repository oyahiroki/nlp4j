package nlp4j.lucene;

public class NoOpSearchRecordEnricher implements SearchRecordEnricher {

	@Override
	public void enrich(SearchRecord record) {
		// nothing
	}
}