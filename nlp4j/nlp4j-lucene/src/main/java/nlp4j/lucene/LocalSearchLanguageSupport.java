/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

public interface LocalSearchLanguageSupport {
	String getLanguage();
	String getTextFieldName();
	SearchRecordEnricher getEnricher();
}
