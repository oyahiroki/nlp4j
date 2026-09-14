/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

/**
 * Calendar interval for date histogram aggregation.
 */
public enum DateHistogramInterval {

	YEAR("year"),
	MONTH("month"),
	HOUR("hour");

	private final String value;

	DateHistogramInterval(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static DateHistogramInterval of(String value) {
		for (DateHistogramInterval interval : values()) {
			if (interval.value.equalsIgnoreCase(value)) {
				return interval;
			}
		}
		throw new IllegalArgumentException(
				"Unsupported calendar_interval: " + value);
	}
}
