/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import nlp4j.lucene9.DatePrecision;
import nlp4j.lucene9.DateValue;
import nlp4j.lucene9.FieldTypeDef;
import nlp4j.lucene9.FieldValueConverter;
import nlp4j.lucene9.SearchSchema;
import nlp4j.lucene9.TypedFieldQueryFactory;

/**
 * {@link SearchRecordEnricher} that derives calendar fields from DATE fields.
 *
 * <p>
 * For each field whose name ends with {@code _dt} and whose type resolves to
 * {@link FieldTypeDef.Kind#DATE}, the following derived INTEGER fields are
 * added to the {@link SearchRecord}:
 * </p>
 *
 * <pre>
 * event_dt = 2026-08-21T14:30:00+09:00   → DatePrecision.DATE_TIME
 *   → event_year_i  = 2026
 *   → event_month_i = 8
 *   → event_day_i   = 21
 *   → event_dow_i   = 5   (ISO: Mon=1 … Sun=7)
 *   → event_hour_i  = 14
 *
 * event_dt = 2026-08-21                   → DatePrecision.DATE
 *   → event_year_i  = 2026
 *   → event_month_i = 8
 *   → event_day_i   = 21
 *   → event_dow_i   = 5
 *   (event_hour_i is NOT generated – no time information in the input)
 * </pre>
 *
 * <p>
 * When the input has no offset (date-only or local datetime), the {@code zoneId}
 * passed to the constructor is used for conversion.
 * When the input already carries an offset, that offset is respected.
 * </p>
 *
 * <p>
 * The derived {@code *_i} fields are automatically resolved as INTEGER by
 * {@link nlp4j.lucene9.DynamicFieldResolver}, so they are immediately available
 * for range queries and aggregations without any additional schema configuration.
 * </p>
 *
 * <p>
 * Note: DATE fields must be single-valued. An {@link IllegalArgumentException}
 * is thrown if a DATE field contains more than one value.
 * </p>
 */
public class DateFieldEnricher implements SearchRecordEnricher {

	private final SearchSchema schema;
	private final ZoneId zoneId;

	/**
	 * Creates a {@link DateFieldEnricher} using the system default timezone.
	 *
	 * @param schema the SearchSchema used for field type resolution; may be
	 *               {@code null}, in which case only suffix-based resolution applies
	 */
	public DateFieldEnricher(SearchSchema schema) {
		this(schema, ZoneId.systemDefault());
	}

	/**
	 * Creates a {@link DateFieldEnricher} with the given timezone.
	 *
	 * @param schema the SearchSchema used for field type resolution; may be
	 *               {@code null}, in which case only suffix-based resolution applies
	 * @param zoneId the default {@link ZoneId} to use when the input date string
	 *               carries no timezone offset; must not be {@code null}
	 */
	public DateFieldEnricher(SearchSchema schema, ZoneId zoneId) {
		if (zoneId == null) {
			throw new IllegalArgumentException("zoneId must not be null");
		}
		this.schema = schema;
		this.zoneId = zoneId;
	}

	@Override
	public void enrich(SearchRecord record) {

		if (record == null) {
			return;
		}

		// addData() によって dataKeys() が変化するのでコピーしてから走査
		List<String> fieldNames = new ArrayList<>(record.dataKeys());

		for (String fieldName : fieldNames) {

			FieldTypeDef type = TypedFieldQueryFactory.resolveFieldType(fieldName, schema);

			if (type.kind() != FieldTypeDef.Kind.DATE) {
				continue;
			}

			// DATE single-valued を検証（全DATEフィールド対象）
			List<String> values = record.getDataValues(fieldName);

			if (values.size() > 1) {
				throw new IllegalArgumentException(
						"DATE field must be single-valued: " + fieldName);
			}

			if (values.isEmpty()) {
				continue;
			}

			// 派生フィールド名を作るため _dt サフィックスを前提とする
			if (!fieldName.endsWith("_dt")) {
				continue;
			}

			String base = fieldName.substring(0, fieldName.length() - "_dt".length());

			String value = values.get(0);

			DateValue dateValue = FieldValueConverter.toDateValue(value, zoneId);

			record.addData(base + "_year_i",
					Integer.toString(dateValue.localDate().getYear()));

			record.addData(base + "_month_i",
					Integer.toString(dateValue.localDate().getMonthValue()));

			record.addData(base + "_day_i",
					Integer.toString(dateValue.localDate().getDayOfMonth()));

			record.addData(base + "_dow_i",
					Integer.toString(dateValue.localDate().getDayOfWeek().getValue()));

			// hour_i は DATE_TIME 精度のときだけ生成する
			// DATE 精度（日付のみ入力）の場合は midnight に変換しても
			// 「元データに 0時という情報が存在した」という誤った意味になるため生成しない
			if (dateValue.precision() == DatePrecision.DATE_TIME) {
				record.addData(base + "_hour_i",
						Integer.toString(dateValue.localTime().getHour()));
			}
		}
	}
}
