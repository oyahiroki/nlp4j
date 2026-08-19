/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

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
