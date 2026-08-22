/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

/**
 * Converts String values to Java/Lucene-compatible types.
 *
 * <p>
 * This class is the single conversion point for all Numeric and Date values
 * used during indexing, term queries, and range queries.
 * </p>
 *
 * <p>
 * Accepted date formats (ISO 8601 subset):
 * </p>
 *
 * <pre>
 * OK: 2026-08-21                    (date only → LocalSearch timezone)
 *     2026-08-21T14:30:00           (local datetime → LocalSearch timezone)
 *     2026-08-21T14:30:00.123       (local datetime with millis → LocalSearch timezone)
 *     2026-08-21T14:30:00+09:00     (offset datetime → offset respected)
 *     2026-08-21T05:30:00Z          (UTC → offset respected)
 *
 * NG: 2026/08/21                    (non-ISO format)
 *     08/21/2026                    (ambiguous)
 * </pre>
 *
 * <p>
 * Resolution rule:
 * </p>
 * <pre>
 * offset present  → use the given offset
 * offset absent   → use the ZoneId passed as defaultZone
 * </pre>
 *
 * <p>
 * All dates are normalized to UTC instant → epoch milliseconds for Lucene storage.
 * </p>
 *
 * <p>
 * The central API is {@link #toDateValue(String, ZoneId)}, which returns a
 * {@link DateValue} containing both the UTC instant and local calendar fields.
 * </p>
 */
public final class FieldValueConverter {

	private FieldValueConverter() {
	}

	/**
	 * Parses a String as int.
	 *
	 * @param value the string value
	 * @return parsed int
	 * @throws NumberFormatException if the value is not a valid integer
	 */
	public static int toInteger(String value) {
		return Integer.parseInt(value);
	}

	/**
	 * Parses a String as long.
	 *
	 * @param value the string value
	 * @return parsed long
	 * @throws NumberFormatException if the value is not a valid long
	 */
	public static long toLong(String value) {
		return Long.parseLong(value);
	}

	/**
	 * Parses a String as double.
	 *
	 * @param value the string value
	 * @return parsed double
	 * @throws NumberFormatException if the value is not a valid double
	 */
	public static double toDouble(String value) {
		return Double.parseDouble(value);
	}

	/**
	 * Parses a date/datetime string and returns a {@link DateValue}.
	 *
	 * <p>
	 * Parsing order:
	 * </p>
	 * <ol>
	 * <li>Try {@link OffsetDateTime#parse(CharSequence)} – offset/zone present → use as-is.</li>
	 * <li>Try {@link LocalDateTime#parse(CharSequence)} – no offset → apply {@code defaultZone}.</li>
	 * <li>Try {@link LocalDate#parse(CharSequence)} – date only → apply {@code defaultZone},
	 *     precision becomes {@link DatePrecision#DATE}.</li>
	 * </ol>
	 *
	 * @param value       the date/datetime string
	 * @param defaultZone the {@link ZoneId} to use when no offset is present in the input;
	 *                    must not be {@code null}
	 * @return a {@link DateValue} containing the instant and local calendar fields
	 * @throws IllegalArgumentException if the value cannot be parsed as any supported format
	 */
	public static DateValue toDateValue(String value, ZoneId defaultZone) {

		if (defaultZone == null) {
			throw new IllegalArgumentException("defaultZone must not be null");
		}

		// 1. Try OffsetDateTime (includes Z and +HH:MM offsets)
		try {
			OffsetDateTime odt = OffsetDateTime.parse(value);
			ZoneId zone = odt.getOffset();
			LocalDate ld = odt.toLocalDate();
			LocalTime lt = odt.toLocalTime();
			Instant instant = odt.toInstant();
			return new DateValue(instant, ld, lt, zone, DatePrecision.DATE_TIME);
		} catch (DateTimeParseException ignored) {
			// fall through
		}

		// 2. Try LocalDateTime (no offset → apply defaultZone)
		try {
			LocalDateTime ldt = LocalDateTime.parse(value);
			Instant instant = ldt.atZone(defaultZone).toInstant();
			LocalDate ld = ldt.toLocalDate();
			LocalTime lt = ldt.toLocalTime();
			return new DateValue(instant, ld, lt, defaultZone, DatePrecision.DATE_TIME);
		} catch (DateTimeParseException ignored) {
			// fall through
		}

		// 3. Try LocalDate (date only → apply defaultZone, precision = DATE)
		try {
			LocalDate ld = LocalDate.parse(value);
			// interpret as midnight in defaultZone
			Instant instant = ld.atStartOfDay(defaultZone).toInstant();
			return new DateValue(instant, ld, null, defaultZone, DatePrecision.DATE);
		} catch (DateTimeParseException ignored) {
			// fall through
		}

		throw new IllegalArgumentException(
				"Unsupported date format (ISO 8601 date or datetime expected): " + value);
	}

	/**
	 * Parses a date/datetime string to epoch milliseconds using the given default timezone.
	 *
	 * @param value       the date/datetime string
	 * @param defaultZone the {@link ZoneId} to use when no offset is present in the input
	 * @return epoch milliseconds (UTC)
	 * @throws IllegalArgumentException if the value cannot be parsed
	 */
	public static long dateToEpochMillis(String value, ZoneId defaultZone) {
		return toDateValue(value, defaultZone).instant().toEpochMilli();
	}

	/**
	 * Parses an ISO 8601 date-time string (timezone/offset required) to epoch
	 * milliseconds.
	 *
	 * <p>
	 * This overload requires an explicit offset or 'Z' suffix. It uses
	 * {@link ZoneId#systemDefault()} as fallback for bare local datetimes, but
	 * callers that need reproducible behavior across environments should prefer
	 * {@link #dateToEpochMillis(String, ZoneId)}.
	 * </p>
	 *
	 * @param value ISO 8601 date/datetime string
	 * @return epoch milliseconds (UTC)
	 * @throws IllegalArgumentException if the value cannot be parsed
	 * @deprecated Prefer {@link #dateToEpochMillis(String, ZoneId)} to ensure
	 *             consistent timezone handling.
	 */
	@Deprecated
	public static long dateToEpochMillis(String value) {
		return dateToEpochMillis(value, ZoneId.systemDefault());
	}

	/**
	 * Parses an ISO 8601 date-time string (timezone/offset required) to
	 * {@link OffsetDateTime}.
	 *
	 * <p>
	 * The returned value retains the original timezone offset, so calendar fields
	 * such as year/month/day/hour reflect the time in the specified zone, not UTC.
	 * </p>
	 *
	 * @param value ISO 8601 date-time string with timezone/offset, e.g.
	 *              {@code 2026-08-19T19:30:00+09:00}
	 * @return parsed {@link OffsetDateTime}
	 * @throws IllegalArgumentException if the value cannot be parsed
	 * @deprecated Prefer {@link #toDateValue(String, ZoneId)} which also handles
	 *             date-only and local datetime inputs.
	 */
	@Deprecated
	public static OffsetDateTime toOffsetDateTime(String value) {
		try {
			return OffsetDateTime.parse(value);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(
					"Invalid ISO 8601 date-time (timezone required): " + value, e);
		}
	}
}
