/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.analytics;

/**
 * LocalAnalytics における分析条件を表すクラス。
 *
 * <p>
 * フィールド値条件（FIELD_VALUE）と Lucene Query 条件（LUCENE）の 2種類をサポートします。
 * </p>
 *
 * <pre>
 * // フィールド値条件
 * AnalyticsQuery q1 = AnalyticsQuery.fieldValue("maker", "Nissan");
 *
 * // Lucene Query 条件
 * AnalyticsQuery q2 = AnalyticsQuery.lucene("maker:Nissan AND year_i:[2020 TO 2026]");
 * </pre>
 */
public final class AnalyticsQuery {

	/**
	 * 分析条件の種別。
	 */
	public enum Kind {
		/** フィールド値による完全一致条件。 */
		FIELD_VALUE,
		/** Lucene Query Parser syntax による条件。 */
		LUCENE
	}

	private final Kind kind;

	/** FIELD_VALUE 時のフィールド名。 */
	private final String field;

	/** FIELD_VALUE 時の値。 */
	private final String value;

	/** LUCENE 時のクエリ文字列。 */
	private final String luceneQuery;

	// -----------------------------------------------------------------------
	// Factory
	// -----------------------------------------------------------------------

	/**
	 * フィールド値条件の AnalyticsQuery を生成します。
	 *
	 * @param field フィールド名
	 * @param value 値
	 * @return AnalyticsQuery
	 */
	public static AnalyticsQuery fieldValue(String field, String value) {
		if (field == null || field.isBlank()) {
			throw new IllegalArgumentException("field must not be empty");
		}
		if (value == null) {
			throw new IllegalArgumentException("value must not be null");
		}
		return new AnalyticsQuery(Kind.FIELD_VALUE, field, value, null);
	}

	/**
	 * Lucene Query 条件の AnalyticsQuery を生成します。
	 *
	 * @param luceneQuery Lucene Query Parser syntax のクエリ文字列
	 * @return AnalyticsQuery
	 */
	public static AnalyticsQuery lucene(String luceneQuery) {
		if (luceneQuery == null || luceneQuery.isBlank()) {
			throw new IllegalArgumentException("luceneQuery must not be empty");
		}
		return new AnalyticsQuery(Kind.LUCENE, null, null, luceneQuery);
	}

	// -----------------------------------------------------------------------
	// Constructor
	// -----------------------------------------------------------------------

	private AnalyticsQuery(Kind kind, String field, String value, String luceneQuery) {
		this.kind = kind;
		this.field = field;
		this.value = value;
		this.luceneQuery = luceneQuery;
	}

	// -----------------------------------------------------------------------
	// Getters
	// -----------------------------------------------------------------------

	/**
	 * 条件の種別を返します。
	 *
	 * @return Kind
	 */
	public Kind getKind() {
		return kind;
	}

	/**
	 * FIELD_VALUE 条件のフィールド名を返します。
	 *
	 * <p>
	 * 種別が FIELD_VALUE 以外の場合は null を返します。
	 * </p>
	 *
	 * @return フィールド名（FIELD_VALUE の場合）または null
	 */
	public String getField() {
		return field;
	}

	/**
	 * FIELD_VALUE 条件の値を返します。
	 *
	 * <p>
	 * 種別が FIELD_VALUE 以外の場合は null を返します。
	 * </p>
	 *
	 * @return 値（FIELD_VALUE の場合）または null
	 */
	public String getValue() {
		return value;
	}

	/**
	 * LUCENE 条件のクエリ文字列を返します。
	 *
	 * <p>
	 * 種別が LUCENE 以外の場合は null を返します。
	 * </p>
	 *
	 * @return Lucene クエリ文字列（LUCENE の場合）または null
	 */
	public String getLuceneQuery() {
		return luceneQuery;
	}

	@Override
	public String toString() {
		if (kind == Kind.FIELD_VALUE) {
			return "AnalyticsQuery[FIELD_VALUE, " + field + "=" + value + "]";
		} else {
			return "AnalyticsQuery[LUCENE, " + luceneQuery + "]";
		}
	}
}
