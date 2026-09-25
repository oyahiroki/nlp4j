package nlp4j.trie;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;

public class TrieTestCase extends TestCase {

	public void testContains() throws Exception {

		Trie trie = new Trie();

		trie.insert("テスト", false, "noun");
		trie.insert("テストケース", false, "noun");
		trie.insert("株式会社");
		trie.insert("株式");
		trie.insert("会社");
		trie.insert("自動車");
		trie.insert("自動車会社");
		trie.insert("自動運転");

		{
			boolean b1 = trie.contains("テスト");
			System.err.println(b1);
			assertEquals(true, b1);
		}
		{
			boolean b1 = trie.contains("てすと");
			System.err.println(b1);
			assertEquals(false, b1);
		}
		{
			boolean b1 = trie.contains("テストケース");
			System.err.println(b1);
			assertEquals(true, b1);
		}
		{
			boolean b1 = trie.contains("株式");
			System.err.println(b1);
			assertEquals(true, b1);
		}
		{
			boolean b1 = trie.contains("株式x");
			System.err.println(b1);
			assertEquals(false, b1);
		}

	}

	public void testPrint001() throws Exception {
		Trie trie = new Trie();
		{
			trie.insert("テスト");
			trie.insert("テストケース");
			trie.insert("株式会社");
			trie.insert("株式");
			trie.insert("会社");
			trie.insert("自動");
			trie.insert("自動車");
			trie.insert("自動車会");
			trie.insert("自動車会社");
			trie.insert("自動車会社社員");
			trie.insert("自動運転");
		}
		trie.print();
	}

	public void testPrint002() throws Exception {
		Trie trie = new Trie();
		{
			trie.insert("テ", true, "test");
			trie.insert("テスト", true, "test");
			trie.insert("テストケース", true, "test");
		}
		trie.print();
	}

	public void testSearch000() throws Exception {

		Trie trie = new Trie();
		{ // 辞書の追加
			trie.insert("A", true, "test"); // overwrite(長いもの勝ち)の設定は上書きされる
			trie.insert("AB", true, "test"); // overwrite(長いもの勝ち)の設定は上書きされる
			trie.insert("ABC", true, "test"); // overwrite(長いもの勝ち)の設定は上書きされる
		}
		trie.print();

		{
			TrieSearchResult result = trie.search("ABCDE");
			for (Keyword found : result.getKeywords()) {
				System.err.println(found);
			}
			// overwritten=true: 同じ開始位置では最長マッチのみが残る
			assertEquals(1, result.getKeywords().size());
			assertEquals("ABC", result.getKeywords().get(0).getLex());
		}
	}

	public void testSearch001() throws Exception {

		Trie trie = new Trie();
		{ // 辞書の追加
			trie.insert("A", false, "facet1"); // size=1
			trie.insert("AB", true, "facet2"); // size=2(3) overwrite(長いもの勝ち)の設定は上書きされる
			trie.insert("ABC", false, "facet2"); // size=3(6)
			trie.insert("BCD", false, "facet2"); // size=5(11)
			trie.insert("CD", false, "facet2"); // size=2(13)
			trie.insert("CDE", false, "facet3"); // size=3(16)
			trie.insert("EFG", false, "facet3"); // size=3(19)
			trie.insert("E", true, "facet3"); // size=1(20) overwrite(長いもの勝ち)の設定は上書きされる
			trie.insert("XX", false, "facetx"); // size=2(22)
		}

		System.err.println("Size Original: " + trie.getSizeOriginal());
		System.err.println("Size: " + trie.getSize());

		trie.print();

		{
			TrieSearchResult result = trie.search("ABCDE");
			List<Keyword> keywords = result.getKeywords();
			for (Keyword found : keywords) {
				System.err.println(found);
			}
			// begin=0: "A"(false)→kk, "AB"(true)→clear+kk, "ABC"(false)→kk → AB, ABC
			// begin=1: "BCD"(false)→kk → BCD
			// begin=2: "CD"(false)→kk, "CDE"(false)→kk → CD, CDE
			// begin=4: "E"(true)→clear+kk → E
			// 合計6件: AB(facet2), ABC(facet2), BCD(facet2), CD(facet2), CDE(facet3), E(facet3)
			assertEquals(6, keywords.size());

			// "A"(facet1) は overwritten=true の "AB" によって除去されるため含まれない
			boolean hasA_facet1 = keywords.stream()
					.anyMatch(k -> "A".equals(k.getLex()) && "facet1".equals(k.getFacet()));
			assertFalse("A(facet1) should not be in results", hasA_facet1);

			// "AB"(facet2) は overwritten=true なので短マッチとして残る（より長い "ABC" は overwritten=false）
			boolean hasAB = keywords.stream().anyMatch(k -> "AB".equals(k.getLex()));
			assertTrue("AB should be in results", hasAB);

			// "ABC"(facet2) が含まれること
			boolean hasABC = keywords.stream().anyMatch(k -> "ABC".equals(k.getLex()));
			assertTrue("ABC should be in results", hasABC);

			// "E"(facet3) は overwritten=true だが同開始位置で他にマッチがないため残る
			boolean hasE = keywords.stream().anyMatch(k -> "E".equals(k.getLex()));
			assertTrue("E should be in results", hasE);
		}
	}

	public void testSearch001b() throws Exception {

		Trie trie = new Trie();
		{ // 辞書の追加
			trie.insert("ABC", false, "facet1"); // size=1
			trie.insert("ABD", true, "facet1"); // size=2(3) overwrite(長いもの勝ち)の設定は上書きされる
			trie.insert("AE", false, "facet1"); // size=3(6)
		}

		System.err.println("Size Original: " + trie.getSizeOriginal());
		System.err.println("Size: " + trie.getSize());

		trie.print();

		{
			TrieSearchResult result = trie.search("ABCDE");
			List<Keyword> keywords = result.getKeywords();
			for (Keyword found : keywords) {
				System.err.println(found);
			}
			// begin=0: "ABC"(false) のみマッチ → ABC
			// begin=1: no match
			// begin=2: no match
			// begin=3: no match
			// begin=4: no match
			// "ABD" は "ABCDE" に存在しない。"AE" も存在しない。
			assertEquals(1, keywords.size());
			assertEquals("ABC", keywords.get(0).getLex());
		}
	}

	public void testSearch002() throws Exception {

		Trie trie = new Trie();
		{ // 辞書の追加
			trie.insert("AB", true, "facet1");
			trie.insert("AB", true, "facet2");
			trie.insert("ABC", false, "facet2");
			trie.insert("BC", true, "facet1");
			trie.insert("BC", false, "facet2");
			trie.insert("BCD", false, "facet2");
			trie.insert("CDE", false, "facet3");
			trie.insert("XX");
		}

		{
			TrieSearchResult result = trie.search("ABCDE");
			List<Keyword> keywords = result.getKeywords();
			for (Keyword found : keywords) {
				System.err.println(found);
			}
			// begin=0: "AB"(true,facet1+facet2)→clear+kk, "ABC"(false,facet2)→kk → AB(f1),AB(f2),ABC(f2)
			// begin=1: "BC"(true,facet1+facet2)→clear+kk, "BCD"(false,facet2)→kk → BC(f1),BC(f2),BCD(f2)
			// begin=2: "CDE"(false,facet3) → CDE(f3)
			// "XX" は入力に存在しない
			assertEquals(7, keywords.size());
		}
	}

	public void testSearch003() throws Exception {

		Trie trie = new Trie();
		{ // 辞書の追加
			trie.insert("テスト");
			trie.insert("テストケース");
			trie.insert("株式会社");
			trie.insert("株式");
			trie.insert("会社");
			trie.insert("自動");
			trie.insert("自動車");
			trie.insert("自動車会");
			trie.insert("自動車会社");
			trie.insert("自動車会社社員");
			trie.insert("自動運転");
		}
		{
			TrieSearchResult result = trie.search("自動車会社x");
			List<Keyword> keywords = result.getKeywords();
			for (Keyword found : keywords) {
				System.err.println(found);
			}
			// overwritten=false(デフォルト): 全マッチが返る
			// 自動(0-2), 自動車(0-3), 自動車会(0-4), 自動車会社(0-5), 会社(3-5) の5件
			assertEquals(5, keywords.size());
		}
	}

	public void testSearch004() throws Exception {
		Trie trie = new Trie();
		{ // 辞書の追加
			trie.insert("新大阪", true, "word");
			trie.insert("新", true, "word");
			trie.insert("新大", true, "word");
			trie.insert("大", true, "word");
			trie.insert("大阪", true, "word");
			trie.insert("阪", true, "word");
		}
		trie.print();
		{
			TrieSearchResult result = trie.search("新大阪");
			List<Keyword> keywords = result.getKeywords();
			for (Keyword found : keywords) {
				System.err.println(found);
			}
			// overwritten=true: 各開始位置で最長マッチのみが残る
			// begin=0: 新(true)→新大(true)→新大阪(true) → 新大阪
			// begin=1: 大(true)→大阪(true) → 大阪
			// begin=2: 阪(true) → 阪
			assertEquals(3, keywords.size());
			assertEquals("新大阪", keywords.get(0).getLex());
			assertEquals("大阪",   keywords.get(1).getLex());
			assertEquals("阪",     keywords.get(2).getLex());
		}
	}

	public void testSearch005() throws Exception {
		Trie trie = new Trie();
		{ // 辞書の追加
			trie.insert("新大阪", false, "word");
			trie.insert("新", false, "word");
			trie.insert("新大", false, "word");
			trie.insert("大", false, "word");
			trie.insert("大阪", false, "word");
			trie.insert("阪", false, "word");
		}
		trie.print();
		{
			TrieSearchResult result = trie.search("新大阪");
			List<Keyword> keywords = result.getKeywords();
			for (Keyword found : keywords) {
				System.err.println(found);
			}
			// overwritten=false: 全マッチが返る (6件)
			assertEquals(6, keywords.size());
		}
	}

}
