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
public class WordNetJaExample2 {

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
