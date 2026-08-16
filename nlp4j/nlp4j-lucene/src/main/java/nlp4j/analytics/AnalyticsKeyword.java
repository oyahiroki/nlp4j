package nlp4j.analytics;

import java.util.Objects;

/**
 * Analytics で使用するキーワードを表すクラス。
 *
 * <p>
 * フィールド名とキーワードの正規形（lex）を保持します。
 * </p>
 *
 * <p>
 * 例:
 * </p>
 *
 * <pre>
 * AnalyticsKeyword keyword =
 * 		new AnalyticsKeyword("word.noun", "ドア");
 *
 * keyword.getField(); // "word.noun"
 * keyword.getLex();   // "ドア"
 * </pre>
 *
 * <p>
 * {@code nlp4j.lucene.SearchKeyword} が文書中の形態素解析結果
 * （表層形・位置情報など）を表すのに対して、このクラスは
 * Analytics の集計・分析で使用する軽量なキーワード表現です。
 * </p>
 */
public class AnalyticsKeyword {

	/**
	 * キーワードが属するフィールド。
	 *
	 * 例:
	 * word.noun
	 * word.verb
	 * maker
	 */
	private final String field;

	/**
	 * キーワードの正規形。
	 *
	 * 例:
	 * ドア
	 * 行く
	 * ニッサン
	 */
	private final String lex;

	/**
	 * @param field フィールド名
	 * @param lex   キーワードの正規形
	 */
	public AnalyticsKeyword(String field, String lex) {

		if (field == null || field.isBlank()) {
			throw new IllegalArgumentException(
					"field must not be empty");
		}

		if (lex == null) {
			throw new IllegalArgumentException(
					"lex must not be null");
		}

		this.field = field;
		this.lex = lex;
	}

	public String getField() {
		return field;
	}

	public String getLex() {
		return lex;
	}

	@Override
	public int hashCode() {
		return Objects.hash(field, lex);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AnalyticsKeyword)) {
			return false;
		}

		AnalyticsKeyword other =
				(AnalyticsKeyword) obj;

		return Objects.equals(field, other.field)
				&& Objects.equals(lex, other.lex);
	}

	@Override
	public String toString() {
		return "AnalyticsKeyword [field=" + field
				+ ", lex=" + lex + "]";
	}
}