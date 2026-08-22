/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Immutable result of a date string parse operation.
 *
 * <p>
 * Produced by {@link FieldValueConverter#toDateValue(String, ZoneId)}.
 * </p>
 *
 * <ul>
 * <li>{@link #instant()} – the UTC instant, always present.</li>
 * <li>{@link #localDate()} – the local date in the resolved timezone, always present.</li>
 * <li>{@link #localTime()} – the local time in the resolved timezone; {@code null} when
 *     {@link #precision()} is {@link DatePrecision#DATE}.</li>
 * <li>{@link #zoneId()} – the resolved {@link ZoneId}.</li>
 * <li>{@link #precision()} – whether this value includes a time component.</li>
 * </ul>
 *
 * @param instant   UTC instant (never {@code null})
 * @param localDate local date in the resolved timezone (never {@code null})
 * @param localTime local time in the resolved timezone; {@code null} for date-only input
 * @param zoneId    resolved timezone (never {@code null})
 * @param precision input precision level (never {@code null})
 */
public record DateValue(
		Instant instant,
		LocalDate localDate,
		LocalTime localTime,
		ZoneId zoneId,
		DatePrecision precision) {
}
