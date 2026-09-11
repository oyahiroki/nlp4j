/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

public class SearchResult {

	/**
	 * ユーザー定義の文書ID。
	 */
	public String id;

	/**
	 * 文書の論理本文。
	 *
	 * addJson() では body / text / language-specific text field
	 * （text_ja, text_en など）から解決された本文が設定される。
	 */
	public String body;

	/**
	 * Lucene の検索スコア。
	 */
	public float score;

	/**
	 * addJson() でドキュメントを追加した場合の元JSON文字列。
	 * vector フィールドは除外される。
	 * add(id, body) で追加した場合は null。
	 */
	public String data;

	@Override
	public String toString() {
		return "SearchResult [id=" + id + ", body=" + body + ", score=" + score + ", data=" + data + "]";
	}
}
