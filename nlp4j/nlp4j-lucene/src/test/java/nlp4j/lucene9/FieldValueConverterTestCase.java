package nlp4j.lucene9;

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
	// dateToEpochMillis
	// -----------------------------------------------------------------------

	public void testDate_utcZ() {
		long millis = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00Z");
		assertTrue(millis > 0);
		// 2026-08-19T10:30:00Z = 1787135400000
		assertEquals(1787135400000L, millis);
	}

	public void testDate_withOffset() {
		// +09:00 → UTC -9h
		long millis1 = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00Z");
		long millis2 = FieldValueConverter.dateToEpochMillis("2026-08-19T19:30:00+09:00");
		assertEquals(millis1, millis2);
	}

	public void testDate_withMillis() {
		long millis = FieldValueConverter.dateToEpochMillis("2026-08-19T10:30:00.000Z");
		assertEquals(1787135400000L, millis);
	}

	public void testDate_noTimezone_invalid() {
		try {
			FieldValueConverter.dateToEpochMillis("2026-08-19");
			fail("Should throw IllegalArgumentException for date-only string");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	public void testDate_dateTimeNoTimezone_invalid() {
		try {
			FieldValueConverter.dateToEpochMillis("2026-08-19 10:30:00");
			fail("Should throw IllegalArgumentException for datetime without timezone");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
}
