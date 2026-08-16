package nlp4j.lucene;

public interface LocalSearchLanguageSupport {
	String getLanguage();
	String getTextFieldName();
	SearchRecordEnricher getEnricher();
}
