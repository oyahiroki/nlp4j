/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import java.util.Locale;

public final class SearchRecordEnrichers {

	private SearchRecordEnrichers() {
	}

	public static SearchRecordEnricher forLanguage(String language) {

		if (language == null) {
			return new NoOpSearchRecordEnricher();
		}

		String lang = language.toLowerCase(Locale.ROOT);

		switch (lang) {
		case "ja":
			return new JapaneseSearchRecordEnricher();

		case "en":
			return new EnglishSearchRecordEnricher();

//		case "ko":
//			return new KoreanSearchRecordEnricher();
//
//		case "zh":
//			return new ChineseSearchRecordEnricher();

		default:
			return new NoOpSearchRecordEnricher();
		}
	}
}