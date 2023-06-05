package examples;

import java.io.File;

import nlp4j.wordnetja.LexicalEntry;
import nlp4j.wordnetja.WordNetJa;

/**
 * <pre>
 * created_at 2021-12-10
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class WordNetJaExample1x {

	public static void main(String[] args) throws Exception {

		String fileName = "files/jpn_wn_lmf.xml.gz";
		File file = new File(fileName);

		WordNetJa dbWordnet = new WordNetJa(file);

		System.err.println("-----");
		String word = "学校x";

		System.err.println(dbWordnet.getPos(word));
		System.err.println("-----");
		{ // 同義語
			for (LexicalEntry le : dbWordnet.getLexicalEntriesInSameSynset(word)) {
				System.err.println(le.getLemmaWrittenForm());
			}
		}
		System.err.println("-----");
		{ // 上位概念
			System.err.println(dbWordnet.findHypernyms(word));
		}
		System.err.println("-----");
		{ // 下位概念
			System.err.println(dbWordnet.findHyponyms(word));
		}

//		-----
//		[n]
//		-----
//		学校
//		学院
//		スクール
//		学園
//		授業
//		学校
//		授業時間
//		-----
//		[教育機関, 時間, ピリオド, スパン, 年代, ピリオッド, 会期, 期間, 時代, 期, 時期]
//		-----
//		[翰林院, 学院, アカデミー, 実業学校, 専修学校, 専門ガッコ, 工業専門学校, 専門ガッコウ, 専門学校, 獣医大学, 看護専門学校, 花嫁学校, 一部, 外国語学校, 私立学校, 私学, 私学校, 自動車学校, 自動車教習所, 夜学校, ダンススクール, 小学, 小学校, 初等学校, 大学院, 大学院大学, 中等学校, パブリックスクール, 日曜学校, 母校, 職業学校]

	}

}
