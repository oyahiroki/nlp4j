package nlp4j.lucene;

/**
 * 形態素解析結果を保持する軽量 DTO クラス。
 *
 * <p>
 * {@code nlp4j.Keyword} は NLP 処理用モデルであるのに対し、
 * このクラスは検索・集計用のモデルとして責務を分離しています。
 * </p>
 *
 * <p>使用例:</p>
 * <pre>
 * SearchKeyword kw = new SearchKeyword("word.verb", "行く", "行き", 8, 10);
 * kw.getPos();   // "word.verb"
 * kw.getLex();   // "行く"  （原形）
 * kw.getStr();   // "行き"  （表層形）
 * kw.getBegin(); // 8
 * kw.getEnd();   // 10
 * </pre>
 */
public class SearchKeyword {

	/** POS（品詞）フィールド名。例: "word.noun", "word.verb" */
	private final String pos;

	/** 原形（見出し語）。例: "行く" */
	private final String lex;

	/** 表層形（テキスト上の文字列）。例: "行き" */
	private final String str;

	/** 開始位置（文字インデックス、inclusive） */
	private final int begin;

	/** 終了位置（文字インデックス、exclusive） */
	private final int end;

	/**
	 * @param pos   POS フィールド名（例: "word.noun"）
	 * @param lex   原形（見出し語）
	 * @param str   表層形
	 * @param begin 開始位置
	 * @param end   終了位置
	 */
	public SearchKeyword(String pos, String lex, String str, int begin, int end) {
		this.pos = pos;
		this.lex = lex;
		this.str = str;
		this.begin = begin;
		this.end = end;
	}

	public String getPos() {
		return pos;
	}

	public String getLex() {
		return lex;
	}

	public String getStr() {
		return str;
	}

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}

	@Override
	public String toString() {
		return "SearchKeyword [pos=" + pos + ", lex=" + lex + ", str=" + str
				+ ", begin=" + begin + ", end=" + end + "]";
	}
}
