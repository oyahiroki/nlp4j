/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.time.ZoneId;

/**
 * Resolves a {@link FieldTypeDef} from a field name using suffix patterns.
 *
 * <p>
 * Resolution order inside {@code LocalSearch}:
 * </p>
 * <ol>
 * <li>Explicit schema (SearchSchema.contains)</li>
 * <li>Suffix pattern (this class)</li>
 * <li>Value-based rule: field name {@code "date"} with ISO 8601 value → DATE</li>
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
 * <li>exact name {@code "date"} and value is ISO 8601 → DATE, stored, aggregatable</li>
 * <li>(other)      → KEYWORD, stored, aggregatable</li>
 * </ul>
 */
public class DynamicFieldResolver {

	/**
	 * Resolves a {@link FieldTypeDef} from the field name alone (suffix-based).
	 *
	 * @param fieldName the field name
	 * @return the resolved {@link FieldTypeDef}
	 */
	public FieldTypeDef resolve(String fieldName) {
		return resolveByName(fieldName);
	}

	/**
	 * Resolves a {@link FieldTypeDef} from the field name and the first value.
	 *
	 * <p>
	 * In addition to suffix-based rules, a field named exactly {@code "date"}
	 * is resolved as DATE when its value is a valid ISO 8601 date or datetime.
	 * If the value is not a valid date, it falls back to KEYWORD.
	 * </p>
	 *
	 * @param fieldName   the field name
	 * @param value       the first value of the field (used for {@code "date"} detection)
	 * @param zoneId      the default timezone for ISO 8601 parsing
	 * @return the resolved {@link FieldTypeDef}
	 */
	public FieldTypeDef resolve(String fieldName, String value, ZoneId zoneId) {

		// suffix-based rules take priority over value-based "date" detection
		FieldTypeDef byName = resolveByName(fieldName);
		if (byName.kind() != FieldTypeDef.Kind.KEYWORD) {
			return byName;
		}

		// special rule: exact field name "date" + ISO 8601 value → DATE
		if ("date".equals(fieldName)
				&& FieldValueConverter.isDateValue(value, zoneId)) {
			return FieldTypeDef.date().stored(true).aggregatable(true);
		}

		return byName;
	}

	private FieldTypeDef resolveByName(String fieldName) {

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
