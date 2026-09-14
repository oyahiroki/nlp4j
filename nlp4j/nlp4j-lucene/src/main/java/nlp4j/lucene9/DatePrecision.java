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
 * Future values such as {@code YEAR} and {@code YEAR_MONTH} may be added when needed.
 * </p>
 */
public enum DatePrecision {

	/** Date only – no time information is available. */
	DATE,

	/** Date and time – time information is available. */
	DATE_TIME
}
