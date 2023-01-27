package nlp4j.wiki.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import nlp4j.wiki.entity.WikiEntity;

public class MediaWikiTemplateUtils {

	static public String toText(WikiEntity e) {

		String t = e.getText();
		String templateBody = toTemplateBody(t);

		// 生物学名 (scientific name) 用テンプレート。任意の学名をラテン語で表示します。
		// https://ja.wikipedia.org/wiki/Template:Sname
		// https://ja.wikipedia.org/wiki/Template:Snamei
		if (isTemplateOf(t, "sname")) {
			String[] tt = templateBody.split("\\|");
			if (tt.length == 3) {
				return tt[2];
			} //
			else if (tt.length == 2) {
				return tt[1];
			} //
			else {
				return "";
			}
		}
		// 国際動物命名規約準拠での学名の命名者表記を作成します
		// https://ja.wikipedia.org/wiki/Template:AUY
		if (isTemplateOf(t, "au")) {
			return valueOfFirst(t);
		}
		// YListを{{Cite web}}で引用するとき、入力を簡易化するテンプレートです。
		// https://ja.wikipedia.org/wiki/Template:YList
		if (isTemplateOf(t, "ylist")) {
			String[] tt = templateBody.split("\\|");
			if (tt.length >= 2) {
				String t2 = tt[1];
				if (t2.startsWith("taxcon=")) {
					return t2.substring(7);
				} else {
					return t2;
				}
			} //
			else {
				return "";
			}
		}
		// 荒らし行為が原因で保護されているページに対して使用するテンプレートです
		// https://ja.wikipedia.org/wiki/Template:Pp-vandalism
		if (isTemplateOf(t, "pp")) {
			return "";
		}
		// 曖昧さ回避を行うもので、その項目名にその他の用法や類似のページがある場合に使用します
		// https://ja.wikipedia.org/wiki/Template:Otheruses
		// このテンプレートは47,000以上のページで使われています。
		if (isTemplateOf(t, "otheruses")) {
			return "";
		}
		// Sfn（shortened footnote
		// template）は、{{Cite}}テンプレートで記述された参考文献に対し、本文中で脚注リンクを生成するテンプレートです。
		// https://ja.wikipedia.org/wiki/Template:Sfn
		// sfn|貝津好孝|1995|p=78
		if (isTemplateOf(t, "sfn")) {
			String[] tt = templateBody.split("\\|");
			if (tt.length >= 2) {
				StringBuilder sb = new StringBuilder();
				for (int idx = 1; idx < tt.length; idx++) {
					if (sb.length() > 0) {
						sb.append(" ");
					}
					sb.append(tt[idx]);
				}
				return sb.toString();
			} //
			else {
				return "";
			}
		}
		// 日本語以外の表記を行う際に、言語タグを明示的に指定して表示させるためのテンプレートです
		// https://ja.wikipedia.org/wiki/Template:Lang
		// このテンプレートは300,000以上のページで使われています。
		if (isTemplateOf(t, "lang")) {
			return "";
		}

		// ハンドルシステム識別子 (HDLs)のリンクをするためのテンプレート
		// https://ja.wikipedia.org/wiki/Template:Hdl
		if (isTemplateOf(t, "hdl")) {
			return "";
		}

		// 「出典」など、記事の参考としたウェブサイトを明記するために使用するテンプレート
		// https://ja.wikipedia.org/wiki/Template:Cite_web
		// このテンプレートは400,000以上のページで使われています。
		if (isTemplateOf(t, "cite web")) {
			String[] tt = templateBody.split("\\|");
			String url = null;
			String title = null;
			String accessdate = null;
			for (String ttt : tt) {
				if (url == null && ttt.startsWith("url=")) {
					url = ttt.substring(4).trim();
				} //
				else if (title == null && ttt.startsWith("title=")) {
					title = ttt.substring(6).trim();
				} //
				else if (accessdate == null && ttt.startsWith("accessdate=")) {
					accessdate = ttt.substring(11).trim();
				}
			}
			return url + " " + title + " " + accessdate;
		}

		// リンク先がPDF文書であることを示すのに使います
		// https://ja.wikipedia.org/wiki/Template:PDFlink
		// このテンプレートは62,000以上のページで使われています。
		if (isTemplateOf(t, "pdflink")) {
//			String[] tt = templateBody.split("\\|");
//			if (tt.length >= 2) {
//				StringBuilder sb = new StringBuilder();
//				for (int idx = 1; idx < tt.length; idx++) {
//					if (sb.length() > 0) {
//						sb.append(" ");
//					}
//					sb.append(tt[idx]);
//				}
//				return sb.toString();
//			} //
//			else {
//				return "";
//			}

			String v = valueOfConcatted(t);
			return v;
		}

		// Template:国立国会図書館デジタルコレクション
		// https://ja.wikipedia.org/wiki/Template:NDLDC
		// https://ja.wikipedia.org/wiki/Template:%E5%9B%BD%E7%AB%8B%E5%9B%BD%E4%BC%9A%E5%9B%B3%E6%9B%B8%E9%A4%A8%E3%83%87%E3%82%B8%E3%82%BF%E3%83%AB%E3%82%B3%E3%83%AC%E3%82%AF%E3%82%B7%E3%83%A7%E3%83%B3
		// このテンプレートは10,000以上のページで使われています。
		// NDLDC
		if (isTemplateOf(t, "ndldc") || isTemplateOf(t, "国立国会図書館デジタルコレクション")) {
			return "";
		}

		// 2枚から5枚までの画像をボックスで並べるテンプレート
		// multiple image
		// https://ja.wikipedia.org/wiki/Template:Multiple_image
		// このテンプレートは2,400以上のページで使われています。
		if (isTemplateOf(t, "multiple image")) {
			return "";
		}

		// NAID（NII論文ID）を指定して、CiNiiへのリンクを生成するテンプレート。
		// https://ja.wikipedia.org/wiki/Template:NAID
		// このテンプレートは10,000以上のページで使われています。
		// naid|40003137141
		if (isTemplateOf(t, "naid")) {
			return "";
		}

		// https://ja.wikipedia.org/wiki/Template:%E3%81%84%E3%81%A4
		if (isTemplateOf(t, "いつ")) {
			return "";
		}
		// https://ja.wikipedia.org/wiki/Template:%E8%AA%B0
		if (isTemplateOf(t, "誰")) {
			return "";
		}
		// https://ja.wikipedia.org/wiki/Template:Cite_journal
		// Cite journal
		// Cite journal は、出典を明記するのに使います。学術雑誌の記事用です。
		// Cite book
		if (isTemplateOf(t, "cite ")) {
			String v = valueOf(t, "title");
			return v;
		}

		// ISSN(International Standard Serial Number)のリンクを生成するテンプレートです
		// https://ja.wikipedia.org/wiki/Template:ISSN
		if (isTemplateOf(t, "issn")) {
			return "";
		}

		// リンク先の記事が存在しない（いわゆる「赤リンク」の）時、他言語版（→Wikipedia:ウィキペディアの一覧）へのリンクを表示します。
		// https://ja.wikipedia.org/wiki/Template:%E4%BB%AE%E3%83%AA%E3%83%B3%E3%82%AF
		if (isTemplateOf(t, "仮リンク")) {
			return valueOfFirst(t);
		}

		// 先行するコンテンツが終了する行まで、次のコンテンツを待たせる指定
		// https://ja.wikipedia.org/wiki/Template:Clear
		if (isTemplateOf(t, "clear")) {
			return "";
		}

		// もし記事の文中や出典として示された注釈でハイパーリンクの切れた（リンク切れ、dead
		// link（デッドリンク））外部リンクを見つけたら、{{リンク切れ}}のテンプレートを使用してください
		// https://ja.wikipedia.org/wiki/Template:%E3%83%AA%E3%83%B3%E3%82%AF%E5%88%87%E3%82%8C
		// このテンプレートは47,000以上のページで使われています。
		if (isTemplateOf(t, "リンク切れ")) {
			return "";
		}

		// 要出典
		// このテンプレートは記事の内容について出典を求めるとき、記事中のどの部分について出典を求めているのかを特定するために用います
		// このテンプレートは36,000以上のページで使われています。
		if (isTemplateOf(t, "要出典")) {
			return "";
		}

		// このテンプレートはデジタルオブジェクト識別子 (DOI) を用いた外部リンクを作成するためのもの
		// https://ja.wikipedia.org/wiki/Template:Doi
		if (isTemplateOf(t, "doi")) {
			return "";
		}

		// 雑多な内容の箇条書き
		// Wikipedia:雑多な内容を箇条書きした節を避けるに関連したテンプレートです
		// このテンプレートは2,000以上のページで使われています。
		if (isTemplateOf(t, "雑多な内容の箇条書き")) {
			return "";
		}

		// 脚注ヘルプ
		// これは新しいビジュアルエディターにより使用されるテンプレートのためのTemplateData文書です
		// https://ja.wikipedia.org/wiki/Template:%E8%84%9A%E6%B3%A8%E3%83%98%E3%83%AB%E3%83%97
		if (isTemplateOf(t, "脚注ヘルプ")) {
			return "";
		}

		// 注釈
		// Notelist
		// https://ja.wikipedia.org/wiki/Template:Notelist
		// このテンプレートは21,000以上のページで使われています。
		if (isTemplateOf(t, "notelist")) {
			return "";
		}

		// Notelist2
		// 文章に解説と出典を並べたい
		// https://ja.wikipedia.org/wiki/Template:Notelist2
		if (isTemplateOf(t, "notelist2")) {
			return "";
		}

		// Reflist
		// 脚注に参照文献リストを載せるためのテンプレートです。
		// https://ja.wikipedia.org/wiki/Template:Reflist
		if (isTemplateOf(t, "reflist")) {
			return "";
		}

		// Commons&cat
		// コモンズのギャラリー (gallery) とカテゴリ (category) の両方に一度でリンクするためのテンプレートです
		// https://ja.wikipedia.org/wiki/Template:Commons&cat
		if (isTemplateOf(t, "commons&cat")) {
			return "";
		}
		// wikiquote
		// ウィキクォート用テンプレート
		// https://ja.wikipedia.org/wiki/Template:Wikiquote
		// ウィキクォートは、フリーでオープンコンテントな引用集を共同制作しオンラインで提供する多言語プロジェクトである
		// https://ja.wikipedia.org/wiki/%E3%82%A6%E3%82%A3%E3%82%AD%E3%82%AF%E3%82%A9%E3%83%BC%E3%83%88
		if (isTemplateOf(t, "wikiquote")) {
			return "";
		}
		// Kotobank
		// https://ja.wikipedia.org/wiki/Template:Kotobank
		// インターネット百科事典サイト「コトバンク」の項目にリンクするための外部リンク専用テンプレート
		if (isTemplateOf(t, "kotobank")) {
			return "";
		}
		// Normdaten
		// https://ja.wikipedia.org/wiki/Template:Normdaten
		// 図書館などで整備・管理されている典拠レコードへの外部リンクを示し、図書目録へのアクセスを簡便にするためのテンプレート
		if (isTemplateOf(t, "normdaten")) {
			return "";
		}

		// Wiktionary|りんご|リンゴ|林檎|apple|äpple
		if (isTemplateOf(t, "wiktionary")) {
			return "";
		}

		// DEFAULTSORT:りんこ
		if (isTemplateOf(t, "defaultsort")) {
			return "";
		}

		// 栄養価
		// https://ja.wikipedia.org/wiki/Template:%E6%A0%84%E9%A4%8A%E4%BE%A1
		// TODO
		if (isTemplateOf(t, "栄養価")) {
			return "";
		}
		// 生物分類表
		// TODO
		if (isTemplateOf(t, "生物分類表")) {
			return "";
		}

		// 大学
		if (isTemplateOf(t, "大学") || isTemplateOf(t, "日本の大学")) {
			Map<String, String> map = map(t);
			return "" + map.get("大学名") + "は" + map.get("国") + "の" + map.get("学校種別") + "大学";
		}
		//
		{
			// 混同
			if (isTemplateOf(t, "混同")) {
				return "";
			}
			// Redirect
			// 転送元
			if (isTemplateOf(t, "redirect")) {
				return valueOfFirst(t);
			}
			if (isTemplateOf(t, "see also")) {
				return valueOfFirst(t);
			}
			// 記事の内容を補足する記事へのリンクを生成するテンプレート
			// https://ja.wikipedia.org/wiki/Template:Main
			if (isTemplateOf(t, "main")) {
				return valueOfFirst(t);
			}
			// Commonscat
			// https://ja.wikipedia.org/wiki/Template:Commonscat
			// 画像などメディアを収集しているウィキメディア・コモンズのカテゴリへとリンクを作成します
			// このテンプレートは190,000以上のページで使われています。
			if (isTemplateOf(t, "commonscat")) {
				return valueOfFirst(t);
			}

		}

		return "";
	}

	private static Map<String, String> map(String t) {
		Map<String, String> mapKeyValue = new LinkedHashMap<String, String>();
		for (String s : t.split("\n\\|")) {
//			System.err.println(s);
			int idx = s.indexOf('=');
			if (idx != -1) {
				String key = s.substring(0, idx).trim();
				String value = s.substring(idx + 1).trim();
				mapKeyValue.put(key, value);
			} else {

			}
		}
		return mapKeyValue;

	}

	public static String valueOfConcatted(String t) {
		String[] tt = toTemplateBody(t).split("\\|");
		if (tt.length >= 2) {
			StringBuilder sb = new StringBuilder();
			for (int idx = 1; idx < tt.length; idx++) {
				if (sb.length() > 0) {
					sb.append(" ");
				}
				sb.append(tt[idx]);
			}
			return sb.toString();
		} //
		else {
			return "";
		}
	}

	public static String valueOf(String t, String key) {
		String[] tt = toTemplateBody(t).split("\\|");
		String v = null;
		for (String ttt : tt) {
			if (v == null && ttt.startsWith(key)) {
				int idx = ttt.indexOf("=");
				v = ttt.substring(idx + 1).trim();
			}
		}
		if (v != null) {
			return v;
		} else {
			return "";
		}

	}

	static public String valueOfFirst(String t) {
		String[] tt = toTemplateBody(t).split("\\|");
		if (tt.length >= 2) {
			// TODO リンクの処理
			return tt[1];
		} //
		else {
			return "";
		}
	}

	static public String toTemplateBody(String t) {
		if (t == null) {
			return null;
		} //
		else if (t.startsWith("{{") == false) {
			return t;
		} //
		else if (t.length() < 4) {
			return t;
		} //
		else {
			return t.substring(2, t.length() - 2);
		}
	}

	static public boolean lowerStartsWith(String s, String prefix) {
		Objects.nonNull(prefix);
		if (s == null) {
			return false;
		} else {
			if (s.length() < prefix.length()) {
				return false;
			} else {
				String s2 = s.substring(0, prefix.length()).toLowerCase();
				return s2.startsWith(prefix);
			}
		}

	}

	public static boolean isTemplateOf(String s, String prefix) {
		if (s == null) {
			return false;
		} else {
			if (s.length() < 3) {
				return false;
			} else {
				return lowerStartsWith(s.substring(2), prefix);
			}
		}
	}

}
