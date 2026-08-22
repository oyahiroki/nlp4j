/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

/**
 * Represents the precision level of a parsed date value.
 *
 * <ul>
 * <li>{@link #DATE} – date only (e.g. {@code 2026-08-21}). No time component.</li>
 * <li>{@link #DATE_TIME} – date and time (e.g. {@code 2026-08-21T14:30:00+09:00}).</li>
 * </ul>
 *
 * <p>
 * This distinction is used by {@link DateFieldEnricher} to decide whether
 * {@code *_hour_i} should be generated: it is only generated for
 * {@link #DATE_TIME}, not for {@link #DATE}.
 * </p>
 *
 * <p>
 * Future values such as {@code YEAR} and {@code YEAR_MONTH} may be added when needed.
 * </p>
 */
public enum DatePrecision {

	/**
	 * Date only – no time information is available.
	 * Calendar analysis fields {@code *_year_i}, {@code *_month_i}, {@code *_day_i},
	 * and {@code *_dow_i} are generated; {@code *_hour_i} is <em>not</em> generated.
	 */
	DATE,

	/**
	 * Date and time – time information is available.
	 * All calendar analysis fields including {@code *_hour_i} are generated.
	 */
	DATE_TIME
}
