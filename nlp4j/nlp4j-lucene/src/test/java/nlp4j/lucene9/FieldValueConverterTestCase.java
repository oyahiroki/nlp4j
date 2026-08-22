package nlp4j.lucene9;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import junit.framework.TestCase;

/**
 * JUnit3 test case for {@link FieldValueConverter}.
 */
public class FieldValueConverterTestCase extends TestCase {

	// -----------------------------------------------------------------------
	// toInteger
	// -----------------------------------------------------------------------

	public void testToInteger_valid() {
		assertEquals(2026, FieldValueConverter.toInteger("2026"));
	}

	public void testToInteger_negative() {
		assertEquals(-1, FieldValueConverter.toInteger("-1"));
	}

	public void testToInteger_invalid() {
		try {
			FieldValueConverter.toInteger("abc");
			fail("Should throw NumberFormatException");
		} catch (NumberFormatException e) {
			// expected
		}
	}

	// -----------------------------------------------------------------------
	// toLong
	// -----------------------------------------------------------------------

	public void testToLong_valid() {
		assertEquals(9999999999L, FieldValueConverter.toLong("9999999999"));
	}

	public void testToLong_invalid() {
		try {
			FieldValueConverter.toLong("not-a-number");
			fail("Should throw NumberFormatException");
		} catch (NumberFormatException e) {
			// expected
		}
	}

	// -----------------------------------------------------------------------
	// toDouble
	// -----------------------------------------------------------------------

	public void testToDouble_valid() {
		assertEquals(123.45, FieldValueConverter.toDouble("123.45"), 1e-9);
	}

	public void testToDouble_integer_string() {
		assertEquals(100.0, FieldValueConverter.toDouble("100"), 1e-9);
	}

	// -----------------------------------------------------------------------
	// toDateValue: offset datetime (backward compat)
	// -----------------------------------------------------------------------

	public void testToDateValue_utcZ_precision() {
		ZoneId utc = ZoneId.of("UTC");
		DateValue dv = FieldValueConverter.toDateValue("2026-08-19T10:30:00Z", utc);
		assertEquals(DatePrecision.DATE_TIME, dv.precision());
		assertEquals(2026, dv.localDate().getYear());
		assertEquals(8, dv.localDate().getMonthValue());
		assertEquals(19, dv.localDate().getDayOfMonth());
		assertNotNull(dv.localTime());
		assertEquals(10, dv.localTime().getHour());
	}

	public void testToDateValue_positiveOffset_localCalendar() {
		ZoneId utc = ZoneId.of("UTC");
		// +09:00: local time=20:15, UTC=11:15
		DateValue dv = FieldValueConverter.toDateValue("2026-08-19T20:15:30+09:00", utc);
		assertEquals(DatePrecision.DATE_TIME, dv.precision());
		assertEquals(2026, dv.localDate().getYear());
		assertEquals(8, dv.localDate().getMonthValue());
		assertEquals(19, dv.localDate().getDayOfMonth());
		assertEquals(20, dv.localTime().getHour());
		assertEquals(15, dv.localTime().getMinute());
		assertEquals(30, dv.localTime().getSecond());
	}

	public void testToDateValue_positiveOffset_epochMillisConsistency() {
		ZoneId utc = ZoneId.of("UTC");
		long ms1 = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00Z", utc);
		long ms2 = FieldValueConverter.dateToEpochMillis("2026-08-19T19:30:00+09:00", utc);
		assertEquals(ms1, ms2);
	}

	// -----------------------------------------------------------------------
	// toDateValue: local datetime (no offset → defaultZone applied)
	// -----------------------------------------------------------------------

	public void testToDateValue_localDatetime_Tokyo() {
		ZoneId tokyo = ZoneId.of("Asia/Tokyo");
		DateValue dv = FieldValueConverter.toDateValue("2026-08-21T14:30:00", tokyo);
		assertEquals(DatePrecision.DATE_TIME, dv.precision());
		assertEquals(2026, dv.localDate().getYear());
		assertEquals(8, dv.localDate().getMonthValue());
		assertEquals(21, dv.localDate().getDayOfMonth());
		assertEquals(14, dv.localTime().getHour());
		assertEquals(30, dv.localTime().getMinute());
		// epoch millis should equal the Tokyo-offset equivalent
		long expected = FieldValueConverter.dateToEpochMillis("2026-08-21T14:30:00+09:00", tokyo);
		assertEquals(expected, dv.instant().toEpochMilli());
	}

	public void testToDateValue_localDatetime_withMillis() {
		ZoneId utc = ZoneId.of("UTC");
		DateValue dv = FieldValueConverter.toDateValue("2026-08-21T14:30:00.123", utc);
		assertEquals(DatePrecision.DATE_TIME, dv.precision());
		assertEquals(14, dv.localTime().getHour());
		assertEquals(123_000_000, dv.localTime().getNano());
	}

	// -----------------------------------------------------------------------
	// toDateValue: date only (no time → precision = DATE, localTime = null)
	// -----------------------------------------------------------------------

	public void testToDateValue_dateOnly_precision() {
		ZoneId tokyo = ZoneId.of("Asia/Tokyo");
		DateValue dv = FieldValueConverter.toDateValue("2026-08-21", tokyo);
		assertEquals(DatePrecision.DATE, dv.precision());
		assertEquals(2026, dv.localDate().getYear());
		assertEquals(8, dv.localDate().getMonthValue());
		assertEquals(21, dv.localDate().getDayOfMonth());
		assertNull("localTime must be null for date-only input", dv.localTime());
	}

	public void testToDateValue_dateOnly_epochMillisIsMidnight() {
		ZoneId utc = ZoneId.of("UTC");
		DateValue dv = FieldValueConverter.toDateValue("2026-08-21", utc);
		// UTC midnight of 2026-08-21
		long expected = LocalDate.of(2026, 8, 21)
				.atStartOfDay(utc).toInstant().toEpochMilli();
		assertEquals(expected, dv.instant().toEpochMilli());
	}

	public void testToDateValue_dateOnly_timezoneApplied() {
		ZoneId tokyo = ZoneId.of("Asia/Tokyo");
		ZoneId utc = ZoneId.of("UTC");
		// midnight Tokyo = UTC-9h
		long tokyoMillis = FieldValueConverter.dateToEpochMillis("2026-08-21", tokyo);
		long utcMillis = FieldValueConverter.dateToEpochMillis("2026-08-21", utc);
		// Tokyo midnight is 9 hours earlier (smaller epoch) than UTC midnight
		assertEquals(utcMillis - 9 * 3600 * 1000L, tokyoMillis);
	}

	// -----------------------------------------------------------------------
	// toDateValue: invalid formats
	// -----------------------------------------------------------------------

	public void testToDateValue_nonIso_invalid() {
		try {
			FieldValueConverter.toDateValue("2026/08/21", ZoneId.of("UTC"));
			fail("Should throw IllegalArgumentException for non-ISO format");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	public void testToDateValue_ambiguousUs_invalid() {
		try {
			FieldValueConverter.toDateValue("08/21/2026", ZoneId.of("UTC"));
			fail("Should throw IllegalArgumentException for ambiguous US format");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	public void testToDateValue_nullZone_throws() {
		try {
			FieldValueConverter.toDateValue("2026-08-21", null);
			fail("Should throw IllegalArgumentException for null zone");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	// -----------------------------------------------------------------------
	// dateToEpochMillis(String, ZoneId)
	// -----------------------------------------------------------------------

	public void testDateToEpochMillis_withZone_utcZ() {
		ZoneId utc = ZoneId.of("UTC");
		long millis = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00Z", utc);
		assertEquals(1787135400000L, millis);
	}

	public void testDateToEpochMillis_withZone_offset() {
		ZoneId utc = ZoneId.of("UTC");
		long ms1 = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00Z", utc);
		long ms2 = FieldValueConverter.dateToEpochMillis("2026-08-19T19:30:00+09:00", utc);
		assertEquals(ms1, ms2);
	}

	public void testDateToEpochMillis_withZone_dateOnly() {
		ZoneId utc = ZoneId.of("UTC");
		long millis = FieldValueConverter.dateToEpochMillis("2026-08-21", utc);
		assertTrue(millis > 0);
		// 2026-08-21T00:00:00Z
		long expected = LocalDate.of(2026, 8, 21)
				.atStartOfDay(utc).toInstant().toEpochMilli();
		assertEquals(expected, millis);
	}

	// -----------------------------------------------------------------------
	// dateToEpochMillis(String) — deprecated; backward compat
	// -----------------------------------------------------------------------

	@SuppressWarnings("deprecation")
	public void testDate_utcZ() {
		long millis = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00Z");
		assertTrue(millis > 0);
		// 2026-08-19T10:30:00Z = 1787135400000
		assertEquals(1787135400000L, millis);
	}

	@SuppressWarnings("deprecation")
	public void testDate_withOffset() {
		// +09:00 → UTC -9h
		long millis1 = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00Z");
		long millis2 = FieldValueConverter.dateToEpochMillis("2026-08-19T19:30:00+09:00");
		assertEquals(millis1, millis2);
	}

	@SuppressWarnings("deprecation")
	public void testDate_withMillis() {
		long millis = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00.000Z");
		assertEquals(1787135400000L, millis);
	}

	// -----------------------------------------------------------------------
	// toOffsetDateTime — deprecated; backward compat
	// -----------------------------------------------------------------------

	@SuppressWarnings("deprecation")
	public void testToOffsetDateTime_utcZ() {
		OffsetDateTime odt = FieldValueConverter.toOffsetDateTime("2026-08-19T10:30:00Z");
		assertEquals(2026, odt.getYear());
		assertEquals(8, odt.getMonthValue());
		assertEquals(19, odt.getDayOfMonth());
		assertEquals(10, odt.getHour());
		assertEquals(ZoneOffset.UTC, odt.getOffset());
	}

	@SuppressWarnings("deprecation")
	public void testToOffsetDateTime_positiveOffset() {
		OffsetDateTime odt = FieldValueConverter.toOffsetDateTime("2026-08-19T20:15:30+09:00");
		assertEquals(2026, odt.getYear());
		assertEquals(8, odt.getMonthValue());
		assertEquals(19, odt.getDayOfMonth());
		assertEquals(20, odt.getHour());
		assertEquals(15, odt.getMinute());
		assertEquals(30, odt.getSecond());
		assertEquals(ZoneOffset.of("+09:00"), odt.getOffset());
	}

	@SuppressWarnings("deprecation")
	public void testToOffsetDateTime_negativeOffset() {
		OffsetDateTime odt = FieldValueConverter.toOffsetDateTime("2026-08-19T23:30:00-07:00");
		assertEquals(2026, odt.getYear());
		assertEquals(8, odt.getMonthValue());
		assertEquals(19, odt.getDayOfMonth());
		assertEquals(23, odt.getHour());
	}

	@SuppressWarnings("deprecation")
	public void testToOffsetDateTime_epochMillisConsistency() {
		long ms1 = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00Z");
		long ms2 = FieldValueConverter.dateToEpochMillis("2026-08-19T19:30:00+09:00");
		assertEquals(ms1, ms2);
	}

	@SuppressWarnings("deprecation")
	public void testToOffsetDateTime_withMillis() {
		OffsetDateTime odt = FieldValueConverter.toOffsetDateTime("2026-08-19T10:30:00.123Z");
		assertEquals(123_000_000, odt.getNano());
	}

	@SuppressWarnings("deprecation")
	public void testToOffsetDateTime_invalid() {
		try {
			FieldValueConverter.toOffsetDateTime("2026-08-19");
			fail("Should throw IllegalArgumentException for date-only string");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@SuppressWarnings("deprecation")
	public void testToOffsetDateTime_noTimezone_invalid() {
		try {
			FieldValueConverter.toOffsetDateTime("2026-08-19T10:30:00");
			fail("Should throw IllegalArgumentException for datetime without timezone");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
}
