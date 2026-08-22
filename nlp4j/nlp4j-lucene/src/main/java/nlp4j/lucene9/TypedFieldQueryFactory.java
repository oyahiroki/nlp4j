/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.time.ZoneId;

import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

/**
 * Factory for creating Lucene queries according to {@link SearchSchema} field types.
 *
 * <p>
 * Field type resolution order: explicit schema &gt; field-name suffix pattern &gt; default (KEYWORD).
 * </p>
 *
 * <p>
 * Supported numeric/date mappings:
 * </p>
 * <ul>
 * <li>INTEGER → {@link IntPoint}</li>
 * <li>LONG → {@link LongPoint}</li>
 * <li>DOUBLE → {@link DoublePoint}</li>
 * <li>DATE → ISO 8601 (date / local datetime / offset datetime) → epoch millis → {@link LongPoint}</li>
 * </ul>
 *
 * <p>
 * For DATE fields, when the value has no timezone offset, the {@code zoneId} parameter
 * is used. Passing {@code null} for {@code zoneId} falls back to
 * {@link ZoneId#systemDefault()}.
 * </p>
 */
public final class TypedFieldQueryFactory {

	private static final DynamicFieldResolver DYNAMIC_FIELD_RESOLVER = new DynamicFieldResolver();

	private TypedFieldQueryFactory() {
	}

	/**
	 * Resolves the {@link FieldTypeDef} for the given field name.
	 * Explicit schema takes priority over field-name suffix patterns.
	 *
	 * @param field  the field name
	 * @param schema the SearchSchema, or {@code null}
	 * @return resolved FieldTypeDef
	 */
	public static FieldTypeDef resolveFieldType(String field, SearchSchema schema) {
		if (schema != null && schema.contains(field)) {
			return schema.get(field);
		}
		return DYNAMIC_FIELD_RESOLVER.resolve(field);
	}

	/**
	 * Returns {@code true} if the field resolves to a numeric or date type.
	 *
	 * @param field  the field name
	 * @param schema the SearchSchema, or {@code null}
	 * @return true for INTEGER, LONG, DOUBLE, DATE
	 */
	public static boolean isNumericOrDate(String field, SearchSchema schema) {
		FieldTypeDef def = resolveFieldType(field, schema);
		switch (def.kind()) {
		case INTEGER:
		case LONG:
		case DOUBLE:
		case DATE:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Creates an exact (term) query for the given field and string value.
	 * Numeric/date fields are converted to the appropriate Lucene Point query;
	 * other field types fall back to {@link TermQuery}.
	 *
	 * <p>
	 * For DATE fields without a timezone offset, {@link ZoneId#systemDefault()} is used.
	 * Prefer {@link #newExactQuery(String, String, SearchSchema, ZoneId)} for reproducible behavior.
	 * </p>
	 *
	 * @param field  the field name
	 * @param value  the string representation of the value
	 * @param schema the SearchSchema, or {@code null}
	 * @return a Lucene Query
	 */
	public static Query newExactQuery(String field, String value, SearchSchema schema) {
		return newExactQuery(field, value, schema, ZoneId.systemDefault());
	}

	/**
	 * Creates an exact (term) query for the given field and string value,
	 * using the given {@link ZoneId} for DATE fields that carry no offset.
	 *
	 * @param field   the field name
	 * @param value   the string representation of the value
	 * @param schema  the SearchSchema, or {@code null}
	 * @param zoneId  default timezone for DATE parsing when no offset is present;
	 *                falls back to {@link ZoneId#systemDefault()} when {@code null}
	 * @return a Lucene Query
	 */
	public static Query newExactQuery(String field, String value, SearchSchema schema, ZoneId zoneId) {
		ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
		FieldTypeDef def = resolveFieldType(field, schema);
		switch (def.kind()) {
		case INTEGER:
			return IntPoint.newExactQuery(field, FieldValueConverter.toInteger(value));
		case LONG:
			return LongPoint.newExactQuery(field, FieldValueConverter.toLong(value));
		case DOUBLE:
			return DoublePoint.newExactQuery(field, FieldValueConverter.toDouble(value));
		case DATE:
			return LongPoint.newExactQuery(field, FieldValueConverter.dateToEpochMillis(value, zone));
		default:
			return new TermQuery(new Term(field, value));
		}
	}

	/**
	 * Creates a range query for the given field.
	 *
	 * <p>
	 * {@code null} or {@code "*"} for lowerValue/upperValue means open-ended.
	 * </p>
	 *
	 * <p>
	 * For DATE fields without a timezone offset, {@link ZoneId#systemDefault()} is used.
	 * Prefer {@link #newRangeQuery(String, String, String, boolean, boolean, SearchSchema, ZoneId)}
	 * for reproducible behavior.
	 * </p>
	 *
	 * @param field          the field name
	 * @param lowerValue     lower bound as string, or {@code null}/{@code "*"} for open
	 * @param upperValue     upper bound as string, or {@code null}/{@code "*"} for open
	 * @param lowerInclusive whether the lower bound is inclusive
	 * @param upperInclusive whether the upper bound is inclusive
	 * @param schema         the SearchSchema, or {@code null}
	 * @return a Lucene range Query
	 * @throws IllegalArgumentException if the field is not a numeric/date type
	 */
	public static Query newRangeQuery(
			String field,
			String lowerValue,
			String upperValue,
			boolean lowerInclusive,
			boolean upperInclusive,
			SearchSchema schema) {
		return newRangeQuery(field, lowerValue, upperValue, lowerInclusive, upperInclusive,
				schema, ZoneId.systemDefault());
	}

	/**
	 * Creates a range query for the given field, using the given {@link ZoneId} for
	 * DATE fields that carry no offset.
	 *
	 * <p>
	 * {@code null} or {@code "*"} for lowerValue/upperValue means open-ended.
	 * </p>
	 *
	 * @param field          the field name
	 * @param lowerValue     lower bound as string, or {@code null}/{@code "*"} for open
	 * @param upperValue     upper bound as string, or {@code null}/{@code "*"} for open
	 * @param lowerInclusive whether the lower bound is inclusive
	 * @param upperInclusive whether the upper bound is inclusive
	 * @param schema         the SearchSchema, or {@code null}
	 * @param zoneId         default timezone for DATE parsing when no offset is present;
	 *                       falls back to {@link ZoneId#systemDefault()} when {@code null}
	 * @return a Lucene range Query
	 * @throws IllegalArgumentException if the field is not a numeric/date type
	 */
	public static Query newRangeQuery(
			String field,
			String lowerValue,
			String upperValue,
			boolean lowerInclusive,
			boolean upperInclusive,
			SearchSchema schema,
			ZoneId zoneId) {

		ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
		FieldTypeDef def = resolveFieldType(field, schema);
		switch (def.kind()) {
		case INTEGER:
			return integerRange(field, lowerValue, upperValue, lowerInclusive, upperInclusive);
		case LONG:
			return longRange(field, lowerValue, upperValue, lowerInclusive, upperInclusive);
		case DOUBLE:
			return doubleRange(field, lowerValue, upperValue, lowerInclusive, upperInclusive);
		case DATE:
			return dateRange(field, lowerValue, upperValue, lowerInclusive, upperInclusive, zone);
		default:
			throw new IllegalArgumentException(
					"Range query is only supported for numeric/date fields: " + field);
		}
	}

	// -----------------------------------------------------------------------
	// Private range helpers
	// -----------------------------------------------------------------------

	private static Query integerRange(
			String field,
			String lowerValue,
			String upperValue,
			boolean lowerInclusive,
			boolean upperInclusive) {

		int lower = Integer.MIN_VALUE;
		int upper = Integer.MAX_VALUE;

		if (!isOpen(lowerValue)) {
			lower = FieldValueConverter.toInteger(lowerValue);
			if (!lowerInclusive) {
				if (lower == Integer.MAX_VALUE) {
					return new MatchNoDocsQuery();
				}
				lower++;
			}
		}

		if (!isOpen(upperValue)) {
			upper = FieldValueConverter.toInteger(upperValue);
			if (!upperInclusive) {
				if (upper == Integer.MIN_VALUE) {
					return new MatchNoDocsQuery();
				}
				upper--;
			}
		}

		return IntPoint.newRangeQuery(field, lower, upper);
	}

	private static Query longRange(
			String field,
			String lowerValue,
			String upperValue,
			boolean lowerInclusive,
			boolean upperInclusive) {

		long lower = Long.MIN_VALUE;
		long upper = Long.MAX_VALUE;

		if (!isOpen(lowerValue)) {
			lower = FieldValueConverter.toLong(lowerValue);
			if (!lowerInclusive) {
				if (lower == Long.MAX_VALUE) {
					return new MatchNoDocsQuery();
				}
				lower++;
			}
		}

		if (!isOpen(upperValue)) {
			upper = FieldValueConverter.toLong(upperValue);
			if (!upperInclusive) {
				if (upper == Long.MIN_VALUE) {
					return new MatchNoDocsQuery();
				}
				upper--;
			}
		}

		return LongPoint.newRangeQuery(field, lower, upper);
	}

	private static Query doubleRange(
			String field,
			String lowerValue,
			String upperValue,
			boolean lowerInclusive,
			boolean upperInclusive) {

		double lower = Double.NEGATIVE_INFINITY;
		double upper = Double.POSITIVE_INFINITY;

		if (!isOpen(lowerValue)) {
			lower = FieldValueConverter.toDouble(lowerValue);
			if (!lowerInclusive) {
				lower = Math.nextUp(lower);
			}
		}

		if (!isOpen(upperValue)) {
			upper = FieldValueConverter.toDouble(upperValue);
			if (!upperInclusive) {
				upper = Math.nextDown(upper);
			}
		}

		return DoublePoint.newRangeQuery(field, lower, upper);
	}

	private static Query dateRange(
			String field,
			String lowerValue,
			String upperValue,
			boolean lowerInclusive,
			boolean upperInclusive,
			ZoneId zoneId) {

		long lower = Long.MIN_VALUE;
		long upper = Long.MAX_VALUE;

		if (!isOpen(lowerValue)) {
			lower = FieldValueConverter.dateToEpochMillis(lowerValue, zoneId);
			if (!lowerInclusive) {
				if (lower == Long.MAX_VALUE) {
					return new MatchNoDocsQuery();
				}
				lower++;
			}
		}

		if (!isOpen(upperValue)) {
			upper = FieldValueConverter.dateToEpochMillis(upperValue, zoneId);
			if (!upperInclusive) {
				if (upper == Long.MIN_VALUE) {
					return new MatchNoDocsQuery();
				}
				upper--;
			}
		}

		return LongPoint.newRangeQuery(field, lower, upper);
	}

	private static boolean isOpen(String value) {
		return value == null || "*".equals(value);
	}
}
