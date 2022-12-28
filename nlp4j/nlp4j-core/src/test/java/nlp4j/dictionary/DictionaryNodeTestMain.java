package nlp4j.dictionary;

public class DictionaryNodeTestMain {

	public static void main(String[] args) {

		String[] ee = { "日本IBM", "日本MS", "日本HP", "テスト" };

		DictionaryNode root = new DictionaryNode();

		for (String s : ee) {
			System.err.println(s);
			root.put(s);
		}

	}

}
