package nlp4j.trie;

import junit.framework.TestCase;

public class TrieTestCase extends TestCase {

	public void testContains() throws Exception {

		Trie trie = new Trie();

		trie.insert("テスト");
		trie.insert("テストケース");
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

	public void testSearch001() throws Exception {

		Trie trie = new Trie();

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

		{
			TrieSearchResult result = trie.search("自動車会社");
			System.err.println(result);
		}
		System.err.println("---");
		{
			TrieSearchResult result = trie.search("自動車会社x");
			System.err.println(result);
		}
	}

	public void testPrint001() throws Exception {

		Trie trie = new Trie();

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

		trie.print();
	}

}
