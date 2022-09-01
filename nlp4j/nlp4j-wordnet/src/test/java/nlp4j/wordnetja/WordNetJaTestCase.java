package nlp4j.wordnetja;

import java.io.File;
import java.util.List;

import com.google.gson.JsonArray;

import junit.framework.TestCase;

public class WordNetJaTestCase extends TestCase {

	public void test000WordNetJaFile() throws Exception {

		String fileName = "files/jpn_wn_lmf.xml.gz";
		File file = new File(fileName);

		WordNetJa db = new WordNetJa(file);

		System.err.println("-----");
		String word = "お客様";

		System.err.println(db.getPos(word));
		System.err.println("-----");
		{ // 同義語
			for (LexicalEntry le : db.getLexicalEntriesInSameSynset(word)) {
				System.err.println(le.getLemmaWrittenForm());
			}
		}
		System.err.println("-----");
		{ // 上位概念
			System.err.println(db.findHypernyms(word));
		}
		System.err.println("-----");
		{ // 下位概念
			System.err.println(db.findHyponyms(word));
		}

		{

		}

		int count = 1;
		// Lexical Entries
		for (LexicalEntry e : db.getLexicalEntries()) {
			String data = count + "," + e.getId() + "," + e.getLemmaWrittenForm() + "," + e.getPos();
			System.err.println(data);
			count++;
			if (count > 100) {
				break;
			}
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

	public void test000WordNetJaInputStream() {
//		fail("Not yet implemented");
	}

	public void testFind() {
//		fail("Not yet implemented");
	}

	public void testFindHypernyms() {
//		fail("Not yet implemented");
	}

	public void testFindHyponyms() {
//		fail("Not yet implemented");
	}

	public void testFindLexicalEntry() {
//		fail("Not yet implemented");
	}

	public void testGetEntry() {
//		fail("Not yet implemented");
	}

	public void testGetLexicalEntries() {
//		fail("Not yet implemented");
	}

	public void testGetLexicalEntriesInSameSynsetLexicalEntry() {
//		fail("Not yet implemented");
	}

	public void testGetLexicalEntriesInSameSynsetAsListListString001() throws Exception {
		String fileName = "files/jpn_wn_lmf.xml.gz";
		File file = new File(fileName);

		WordNetJa db = new WordNetJa(file);

		System.err.println("-----");
		String word = "お客様";

		System.err.println(db.getPos(word));
		System.err.println("-----");
		{ // 同義語
			LexicalEntry entry = db.getEntry(word, "n");
			{
				List<Synset> synsets = db.getLinkedSynsets(entry);
				{
					JsonArray glosses = new JsonArray();
					synsets.stream() //
							.filter(o -> o.getDefinition_gloss() != null) //
							.forEach(syn -> {
								glosses.add(syn.getDefinition_gloss());
							});
//					doc.add("glosses", glosses);
					System.err.println(glosses);
				}
			}
			System.err.println("-----");
			for (List<LexicalEntry> le : db.getLexicalEntriesInSameSynsetAsListList(entry)) {
				StringBuilder sb = new StringBuilder();
				for (LexicalEntry lee : le) {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(lee.getLemmaWrittenForm());
				}
				System.err.println(sb.toString());
				System.err.println(le);
				System.err.println("---");
			}
		}
//		-----
//		[n]
//		-----
//		御客, 買い手, 御客様, 施主, 得意, 贔負, 取り引き先, お客様, お客さん, 依頼人, 顧客, お客さま, 得意先, 取引き先, お客, クライアント, 客筋, 依頼者, カスタマ, 御客さん, 買手, カスタマー, 得意客, 取引先, 客
//		[LexicalEntry [id=w180332, lemmaWrittenForm=御客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w184889, lemmaWrittenForm=買い手, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-09885145-n]], LexicalEntry [id=w179851, lemmaWrittenForm=御客様, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w220411, lemmaWrittenForm=施主, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-09608709-n]], LexicalEntry [id=w243044, lemmaWrittenForm=得意, pos=n, synset_ids=[jpn-1.1-07508486-n, jpn-1.1-09984659-n, jpn-1.1-07527656-n, jpn-1.1-07528097-n, jpn-1.1-10407726-n]], LexicalEntry [id=w161683, lemmaWrittenForm=贔負, pos=n, synset_ids=[jpn-1.1-06200617-n, jpn-1.1-09984659-n, jpn-1.1-10407726-n]], LexicalEntry [id=w176100, lemmaWrittenForm=取り引き先, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10407726-n]], LexicalEntry [id=w191561, lemmaWrittenForm=お客様, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w157241, lemmaWrittenForm=お客さん, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w169100, lemmaWrittenForm=依頼人, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-09985075-n]], LexicalEntry [id=w207436, lemmaWrittenForm=顧客, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-08401554-n, jpn-1.1-09985075-n, jpn-1.1-10407726-n]], LexicalEntry [id=w171340, lemmaWrittenForm=お客さま, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w179016, lemmaWrittenForm=得意先, pos=n, synset_ids=[jpn-1.1-09984659-n]], LexicalEntry [id=w172938, lemmaWrittenForm=取引き先, pos=n, synset_ids=[jpn-1.1-09984659-n]], LexicalEntry [id=w240096, lemmaWrittenForm=お客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w166536, lemmaWrittenForm=クライアント, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-03827107-n, jpn-1.1-09985075-n, jpn-1.1-10407726-n]], LexicalEntry [id=w216852, lemmaWrittenForm=客筋, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10407726-n]], LexicalEntry [id=w195459, lemmaWrittenForm=依頼者, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-09985075-n]], LexicalEntry [id=w208245, lemmaWrittenForm=カスタマ, pos=n, synset_ids=[jpn-1.1-09984659-n]], LexicalEntry [id=w160130, lemmaWrittenForm=御客さん, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w163137, lemmaWrittenForm=買手, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-09885145-n]], LexicalEntry [id=w206991, lemmaWrittenForm=カスタマー, pos=n, synset_ids=[jpn-1.1-09984659-n]], LexicalEntry [id=w205285, lemmaWrittenForm=得意客, pos=n, synset_ids=[jpn-1.1-09984659-n]], LexicalEntry [id=w209746, lemmaWrittenForm=取引先, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10407726-n]], LexicalEntry [id=w178610, lemmaWrittenForm=客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-10403876-n, jpn-1.1-09887850-n, jpn-1.1-10151133-n]]]
//		---
//		来遊者, 来客, 御客, 御客様, 賓, 来者, 観光客, お客様, お客さん, 訪問者, 訪問客, お客さま, お客, 客人, 来観者, 見舞客, 訪客, 客来, 御客さん, 賓客, 見舞い客, 来訪者, 客, ビジター
//		[LexicalEntry [id=w182678, lemmaWrittenForm=来遊者, pos=n, synset_ids=[jpn-1.1-10757193-n]], LexicalEntry [id=w209419, lemmaWrittenForm=来客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-10757193-n, jpn-1.1-09887850-n]], LexicalEntry [id=w180332, lemmaWrittenForm=御客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w179851, lemmaWrittenForm=御客様, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w185235, lemmaWrittenForm=賓, pos=n, synset_ids=[jpn-1.1-10757193-n]], LexicalEntry [id=w219072, lemmaWrittenForm=来者, pos=n, synset_ids=[jpn-1.1-10757193-n, jpn-1.1-15121625-n, jpn-1.1-09887850-n]], LexicalEntry [id=w240715, lemmaWrittenForm=観光客, pos=n, synset_ids=[jpn-1.1-10757193-n, jpn-1.1-10718131-n]], LexicalEntry [id=w191561, lemmaWrittenForm=お客様, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w157241, lemmaWrittenForm=お客さん, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w227015, lemmaWrittenForm=訪問者, pos=n, synset_ids=[jpn-1.1-10757193-n]], LexicalEntry [id=w183615, lemmaWrittenForm=訪問客, pos=n, synset_ids=[jpn-1.1-10757193-n]], LexicalEntry [id=w171340, lemmaWrittenForm=お客さま, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w240096, lemmaWrittenForm=お客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w222055, lemmaWrittenForm=客人, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w174205, lemmaWrittenForm=来観者, pos=n, synset_ids=[jpn-1.1-10757193-n]], LexicalEntry [id=w188538, lemmaWrittenForm=見舞客, pos=n, synset_ids=[jpn-1.1-10757193-n]], LexicalEntry [id=w170104, lemmaWrittenForm=訪客, pos=n, synset_ids=[jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w213761, lemmaWrittenForm=客来, pos=n, synset_ids=[jpn-1.1-10757193-n]], LexicalEntry [id=w160130, lemmaWrittenForm=御客さん, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w207546, lemmaWrittenForm=賓客, pos=n, synset_ids=[jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w163651, lemmaWrittenForm=見舞い客, pos=n, synset_ids=[jpn-1.1-10757193-n]], LexicalEntry [id=w213170, lemmaWrittenForm=来訪者, pos=n, synset_ids=[jpn-1.1-10757193-n]], LexicalEntry [id=w178610, lemmaWrittenForm=客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-10403876-n, jpn-1.1-09887850-n, jpn-1.1-10151133-n]], LexicalEntry [id=w179371, lemmaWrittenForm=ビジター, pos=n, synset_ids=[jpn-1.1-10757193-n]]]
//		---
//		御客, 御客様, 来賓, 過客, お客様, お客さん, 招待客, お客さま, お客, 客人, ゲスト, 訪客, 御客さん, 客分, 賓客, 客
//		[LexicalEntry [id=w180332, lemmaWrittenForm=御客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w179851, lemmaWrittenForm=御客様, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w217107, lemmaWrittenForm=来賓, pos=n, synset_ids=[jpn-1.1-10150940-n]], LexicalEntry [id=w208862, lemmaWrittenForm=過客, pos=n, synset_ids=[jpn-1.1-10150940-n, jpn-1.1-09629752-n]], LexicalEntry [id=w191561, lemmaWrittenForm=お客様, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w157241, lemmaWrittenForm=お客さん, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w230110, lemmaWrittenForm=招待客, pos=n, synset_ids=[jpn-1.1-10150940-n]], LexicalEntry [id=w171340, lemmaWrittenForm=お客さま, pos=n, synset_ids=[jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w240096, lemmaWrittenForm=お客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w222055, lemmaWrittenForm=客人, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w228821, lemmaWrittenForm=ゲスト, pos=n, synset_ids=[jpn-1.1-11018683-n, jpn-1.1-10150940-n, jpn-1.1-03827107-n]], LexicalEntry [id=w170104, lemmaWrittenForm=訪客, pos=n, synset_ids=[jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w160130, lemmaWrittenForm=御客さん, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-09887850-n]], LexicalEntry [id=w234216, lemmaWrittenForm=客分, pos=n, synset_ids=[jpn-1.1-10150940-n]], LexicalEntry [id=w207546, lemmaWrittenForm=賓客, pos=n, synset_ids=[jpn-1.1-10757193-n, jpn-1.1-10150940-n]], LexicalEntry [id=w178610, lemmaWrittenForm=客, pos=n, synset_ids=[jpn-1.1-08184861-n, jpn-1.1-09984659-n, jpn-1.1-10757193-n, jpn-1.1-10150940-n, jpn-1.1-10403876-n, jpn-1.1-09887850-n, jpn-1.1-10151133-n]]]
//		---

	}

	public void testGetLexicalEntriesInSameSynsetString001() throws Exception {
		String fileName = "files/jpn_wn_lmf.xml.gz";
		File file = new File(fileName);

		WordNetJa db = new WordNetJa(file);

		System.err.println("-----");
		String word = "お客様";

		System.err.println(db.getPos(word));
		System.err.println("-----");
		{ // 同義語
			for (LexicalEntry le : db.getLexicalEntriesInSameSynset(word)) {
				System.err.println(le.getLemmaWrittenForm());
			}
		}
	}

	public void testGetLinkedSynsets() {
//		fail("Not yet implemented");
	}

	public void testGetPos() {
//		fail("Not yet implemented");
	}

	public void testGetWrittenFormList() {
//		fail("Not yet implemented");
	}

	public void testSearchEntries() {
//		fail("Not yet implemented");
	}

	public void testSearchEntriesByRegex() {
//		fail("Not yet implemented");
	}

}
