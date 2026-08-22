/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LocalSearch に登録する文書の中間表現クラス。
 *
 * <p>
 * {@code add()} / {@code addJson()} の入口から Lucene Document 生成までの間で
 * 文書データを保持します。形態素解析結果は {@link SearchKeyword} として
 * {@code keywords} リストに蓄積されます。
 * </p>
 *
 * <pre>
 * SearchRecord record = new SearchRecord("doc1", "私は歩いて学校に行きました。");
 * // → analyze() で keywords が設定される
 * // → addRecord() で Lucene Document に変換される
 * </pre>
 */
public class SearchRecord {

	/** ドキュメントの一意識別子 */
	private final String id;

	/** 本文テキスト */
	private final String body;

	/** KNN ベクトル（null の場合はベクトルなし） */
	private float[] vector;

	/** 形態素解析結果のキーワードリスト */
	private final List<SearchKeyword> keywords = new ArrayList<>();

	/** 追加フィールド（フィールド名 → 値リスト）。複数値フィールド対応 */
	private final Map<String, List<String>> data = new LinkedHashMap<>();

	/**
	 * @param id   ドキュメントの一意識別子
	 * @param body 本文テキスト
	 */
	public SearchRecord(String id, String body) {
		this.id = id;
		this.body = body;
	}

	public float[] getVector() {
		return vector;
	}

	public void setVector(float[] vector) {
		this.vector = vector;
	}

	public boolean hasVector() {
		return vector != null;
	}

	public String getId() {
		return id;
	}

	public String getBody() {
		return body;
	}

	public List<SearchKeyword> getKeywords() {
		return Collections.unmodifiableList(keywords);
	}

	/**
	 * 形態素解析結果を追加します。
	 *
	 * @param pos   POS フィールド名（例: "word.noun"）
	 * @param lex   原形（見出し語）
	 * @param str   表層形
	 * @param begin 開始位置
	 * @param end   終了位置
	 */
	public void addKeyword(String pos, String lex, String str, int begin, int end) {
		keywords.add(new SearchKeyword(pos, lex, str, begin, end));
	}

	/**
	 * 追加フィールドに値を1件追加します。
	 *
	 * @param fieldName フィールド名
	 * @param value     値
	 */
	public void addData(String fieldName, String value) {
		data.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(value);
	}

	/**
	 * 追加フィールドのフィールド名セットを返します。
	 */
	public java.util.Set<String> dataKeys() {
		return Collections.unmodifiableSet(data.keySet());
	}

	/**
	 * 指定フィールドの値リストを返します。
	 *
	 * @param fieldName フィールド名
	 * @return 値リスト（存在しない場合は空リスト）
	 */
	public List<String> getDataValues(String fieldName) {
		List<String> values = data.get(fieldName);
		return values != null ? Collections.unmodifiableList(values) : Collections.emptyList();
	}

	@Override
	public String toString() {
		return "SearchRecord [id=" + id + ", body=" + body
				+ ", keywords=" + keywords.size() + ", data=" + data.keySet()
				+ ", hasVector=" + (vector != null) + "]";
	}
}
