package nlp4j.trie;

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
			for (Keyword found : result.getKeywords()) {
				System.err.println(found);
				assertNotSame("AB", found.getLex());
				assertNotSame("E", found.getLex());
			}
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
			for (Keyword found : result.getKeywords()) {
				System.err.println(found);
				assertNotSame("AB", found.getLex());
				assertNotSame("E", found.getLex());
			}
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
			for (Keyword found : result.getKeywords()) {
				System.err.println(found);
			}
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
			System.err.println(result);
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
			for (Keyword found : result.getKeywords()) {
				System.err.println(found);
			}
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
			for (Keyword found : result.getKeywords()) {
				System.err.println(found);
			}
		}
	}

}
