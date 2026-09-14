/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene9;

/**
 * A single bucket in a date histogram aggregation result.
 */
public class DateHistogramBucket {

	private final long key;
	private final String keyAsString;
	private final long docCount;

	public DateHistogramBucket(long key, String keyAsString, long docCount) {
		this.key = key;
		this.keyAsString = keyAsString;
		this.docCount = docCount;
	}

	/**
	 * The bucket key as epoch milliseconds.
	 */
	public long getKey() {
		return key;
	}

	/**
	 * The bucket key as a formatted string (e.g., "2026", "2026-01", "2026-01-01T14").
	 */
	public String getKeyAsString() {
		return keyAsString;
	}

	/**
	 * The number of documents in this bucket.
	 */
	public long getDocCount() {
		return docCount;
	}

	@Override
	public String toString() {
		return "DateHistogramBucket[key=" + key
				+ ", keyAsString=" + keyAsString
				+ ", docCount=" + docCount + "]";
	}
}
