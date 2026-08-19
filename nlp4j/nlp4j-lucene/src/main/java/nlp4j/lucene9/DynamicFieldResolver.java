/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

/**
 * Resolves a {@link FieldTypeDef} from a field name using suffix patterns.
 *
 * <p>
 * Resolution order inside {@code LocalSearch}:
 * </p>
 * <ol>
 * <li>Explicit schema (SearchSchema.contains)</li>
 * <li>Suffix pattern (this class)</li>
 * <li>Default: KEYWORD</li>
 * </ol>
 *
 * <p>
 * Suffix rules:
 * </p>
 * <ul>
 * <li>{@code *_i}  → INTEGER, stored, aggregatable</li>
 * <li>{@code *_l}  → LONG, stored, aggregatable</li>
 * <li>{@code *_d}  → DOUBLE, stored, aggregatable</li>
 * <li>{@code *_dt} → DATE, stored, aggregatable</li>
 * <li>(other)      → KEYWORD, stored, aggregatable</li>
 * </ul>
 */
public class DynamicFieldResolver {

	public FieldTypeDef resolve(String fieldName) {

		if (fieldName.endsWith("_dt")) {
			return FieldTypeDef.date().stored(true).aggregatable(true);
		}

		if (fieldName.endsWith("_i")) {
			return FieldTypeDef.integer().stored(true).aggregatable(true);
		}

		if (fieldName.endsWith("_l")) {
			return FieldTypeDef.longNumber().stored(true).aggregatable(true);
		}

		if (fieldName.endsWith("_d")) {
			return FieldTypeDef.doubleNumber().stored(true).aggregatable(true);
		}

		return FieldTypeDef.keyword().stored(true).aggregatable(true);
	}
}
