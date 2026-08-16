package nlp4j.lucene;

import java.util.Set;

public class JapaneseLanguageSupport implements LocalSearchLanguageSupport {
	

	@Override
	public String getLanguage() {
		return "ja";
	}

	@Override
	public String getTextFieldName() {
		return "text_ja";
	}

	@Override
	public SearchRecordEnricher getEnricher() {
		return new JapaneseSearchRecordEnricher();
	}

}
