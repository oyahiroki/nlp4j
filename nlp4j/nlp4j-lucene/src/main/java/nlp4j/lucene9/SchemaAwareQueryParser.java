/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.time.ZoneId;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.DelegatingAnalyzerWrapper;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

/**
 * Lucene {@link QueryParser} that generates type-aware queries using
 * {@link SearchSchema}.
 *
 * <p>
 * When a field is resolved as INTEGER, LONG, DOUBLE, or DATE, this parser
 * delegates to {@link TypedFieldQueryFactory} instead of the standard
 * text-based Lucene query. This allows Lucene Query Parser syntax such as:
 * </p>
 *
 * <pre>
 * year_i:2025             → IntPoint.newExactQuery
 * year_i:[2025 TO 2026]   → IntPoint.newRangeQuery
 * price_d:[100 TO 200]    → DoublePoint.newRangeQuery
 * created_dt:[2026-08-01T00:00:00Z TO 2026-09-01T00:00:00Z]
 *                         → LongPoint.newRangeQuery (epoch millis)
 * </pre>
 */
public class SchemaAwareQueryParser extends QueryParser {

	private final SearchSchema schema;
	private final ZoneId zoneId;

	/**
	 * Constructs a new SchemaAwareQueryParser using {@link ZoneId#systemDefault()}.
	 *
	 * @param defaultField the default field for query parsing
	 * @param analyzer     the analyzer for text fields
	 * @param schema       the SearchSchema for field type resolution
	 */
	public SchemaAwareQueryParser(String defaultField, Analyzer analyzer, SearchSchema schema) {
		this(defaultField, analyzer, schema, ZoneId.systemDefault());
	}

	/**
	 * Constructs a new SchemaAwareQueryParser with an explicit timezone.
	 *
	 * @param defaultField the default field for query parsing
	 * @param analyzer     the analyzer for text fields
	 * @param schema       the SearchSchema for field type resolution
	 * @param zoneId       timezone used for DATE fields without an offset
	 */
	public SchemaAwareQueryParser(String defaultField, Analyzer analyzer, SearchSchema schema, ZoneId zoneId) {
		super(defaultField, wrapAnalyzer(analyzer, schema));
		this.schema = schema;
		this.zoneId = (zoneId != null) ? zoneId : ZoneId.systemDefault();
	}

	/**
	 * Wraps the given analyzer in a {@link DelegatingAnalyzerWrapper} that routes
	 * KEYWORD fields through {@link KeywordAnalyzer} (no tokenization) so that
	 * quoted phrases like {@code component_s:"POWER TRAIN:AUTOMATIC TRANSMISSION"}
	 * are treated as a single term rather than a PhraseQuery.
	 *
	 * <p>
	 * Dynamic KEYWORD fields (e.g. {@code _s} suffix) are handled via the
	 * {@link DynamicKeywordAnalyzerWrapper} inner class which inspects each field
	 * name at query-parse time.
	 * </p>
	 */
	static Analyzer wrapAnalyzer(Analyzer base, SearchSchema schema) {
		return new DynamicKeywordAnalyzerWrapper(base, schema);
	}

	/**
	 * An {@link Analyzer} that delegates to {@link KeywordAnalyzer} for KEYWORD
	 * fields and to the base analyzer for all other fields.
	 */
	static final class DynamicKeywordAnalyzerWrapper extends DelegatingAnalyzerWrapper {

		private static final KeywordAnalyzer KEYWORD_ANALYZER = new KeywordAnalyzer();
		private final Analyzer base;
		private final SearchSchema schema;

		DynamicKeywordAnalyzerWrapper(Analyzer base, SearchSchema schema) {
			super(Analyzer.PER_FIELD_REUSE_STRATEGY);
			this.base = base;
			this.schema = schema;
		}

		@Override
		protected Analyzer getWrappedAnalyzer(String fieldName) {
			FieldTypeDef def = TypedFieldQueryFactory.resolveFieldType(fieldName, schema);
			if (def.kind() == FieldTypeDef.Kind.KEYWORD) {
				return KEYWORD_ANALYZER;
			}
			return base;
		}
	}

	/**
	 * Overrides field query generation to use Point queries for numeric/date
	 * fields, and TermQuery (Analyzer bypass) for keyword fields.
	 */
	@Override
	protected Query getFieldQuery(String field, String queryText, boolean quoted) throws ParseException {
		FieldTypeDef def = TypedFieldQueryFactory.resolveFieldType(field, schema);
		if (def.kind() == FieldTypeDef.Kind.KEYWORD) {
			// keyword フィールドは Analyzer を bypass して TermQuery で完全一致
			try {
				return TypedFieldQueryFactory.newExactQuery(field, queryText, schema, zoneId);
			} catch (RuntimeException e) {
				throw parseException("Invalid value for field [" + field + "]: " + queryText, e);
			}
		}
		if (TypedFieldQueryFactory.isNumericOrDate(field, schema)) {
			try {
				return TypedFieldQueryFactory.newExactQuery(field, queryText, schema, zoneId);
			} catch (RuntimeException e) {
				throw parseException("Invalid value for field [" + field + "]: " + queryText, e);
			}
		}
		return super.getFieldQuery(field, queryText, quoted);
	}

	/**
	 * Overrides range query generation to use Point range queries for numeric/date
	 * fields.
	 */
	@Override
	protected Query getRangeQuery(String field, String part1, String part2, boolean startInclusive,
			boolean endInclusive) throws ParseException {

		if (!TypedFieldQueryFactory.isNumericOrDate(field, schema)) {
			return super.getRangeQuery(field, part1, part2, startInclusive, endInclusive);
		}
		try {
			return TypedFieldQueryFactory.newRangeQuery(field, part1, part2, startInclusive, endInclusive, schema,
					zoneId);
		} catch (RuntimeException e) {
			throw parseException("Invalid range for field [" + field + "]", e);
		}
	}

	private ParseException parseException(String message, Throwable cause) {
		ParseException e = new ParseException(message);
		e.initCause(cause);
		return e;
	}
}
