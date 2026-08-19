package nlp4j.lucene9;

import java.time.Instant;
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
 * Date format requirements: ISO 8601 date-time with timezone/offset is mandatory.
 * </p>
 *
 * <pre>
 * OK: 2026-08-19T10:30:00Z
 *     2026-08-19T10:30:00.123Z
 *     2026-08-19T19:30:00+09:00
 *
 * NG: 2026/08/19
 *     2026-08-19
 *     2026-08-19 10:30:00
 * </pre>
 *
 * <p>
 * All dates are normalized to UTC instant → epoch milliseconds.
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
	 * Parses an ISO 8601 date-time string (timezone/offset required) to epoch
	 * milliseconds.
	 *
	 * @param value ISO 8601 date-time string with timezone, e.g.
	 *              {@code 2026-08-19T10:30:00Z}
	 * @return epoch milliseconds (UTC)
	 * @throws IllegalArgumentException if the value cannot be parsed
	 */
	public static long dateToEpochMillis(String value) {
		try {
			return Instant.parse(value).toEpochMilli();
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(
					"Invalid ISO 8601 date-time (timezone required): " + value, e);
		}
	}
}
