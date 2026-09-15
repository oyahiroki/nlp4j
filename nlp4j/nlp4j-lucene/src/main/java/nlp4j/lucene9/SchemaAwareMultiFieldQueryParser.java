/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.time.ZoneId;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;

/**
 * Lucene {@link MultiFieldQueryParser} that generates type-aware queries using
 * {@link SearchSchema}.
 */
public class SchemaAwareMultiFieldQueryParser extends MultiFieldQueryParser {

	private final SearchSchema schema;
	private final ZoneId zoneId;

	public SchemaAwareMultiFieldQueryParser(String[] fields, Analyzer analyzer, SearchSchema schema) {
		this(fields, analyzer, schema, ZoneId.systemDefault());
	}

	public SchemaAwareMultiFieldQueryParser(String[] fields, Analyzer analyzer, SearchSchema schema, ZoneId zoneId) {
		super(fields, SchemaAwareQueryParser.wrapAnalyzer(analyzer, schema));
		this.schema = schema;
		this.zoneId = (zoneId != null) ? zoneId : ZoneId.systemDefault();
	}

	@Override
	protected Query getFieldQuery(String field, String queryText, boolean quoted) throws ParseException {
		if (field == null) {
			return super.getFieldQuery(null, queryText, quoted);
		}
		FieldTypeDef def = TypedFieldQueryFactory.resolveFieldType(field, schema);
		if (def.kind() == FieldTypeDef.Kind.KEYWORD) {
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

	@Override
	protected Query getRangeQuery(String field, String part1, String part2, boolean startInclusive,
			boolean endInclusive) throws ParseException {

		if (field == null || !TypedFieldQueryFactory.isNumericOrDate(field, schema)) {
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
		ParseException pe = new ParseException(message);
		pe.initCause(cause);
		return pe;
	}
}
