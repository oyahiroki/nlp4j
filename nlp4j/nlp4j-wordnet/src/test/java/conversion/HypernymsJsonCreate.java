package conversion;

import java.io.File;

import nlp4j.wordnetja.LexicalEntry;
import nlp4j.wordnetja.WordNetJa;

public class HypernymsJsonCreate {

	public static void main(String[] args) throws Exception {
		String fileName = "files/jpn_wn_lmf.xml.gz";
		File file = new File(fileName);

		WordNetJa db = new WordNetJa(file);

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
	}

}
