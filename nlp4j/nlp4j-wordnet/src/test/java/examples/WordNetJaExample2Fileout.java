package examples;

import java.io.File;

import org.apache.commons.io.FileUtils;

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
public class WordNetJaExample2Fileout {

	public static void main(String[] args) throws Exception {

		String fileName = "files/jpn_wn_lmf.xml.gz";
		File file = new File(fileName);

		WordNetJa db = new WordNetJa(file);

		File outFile = new File("files/files_outLexicalEntries.txt");

		for (LexicalEntry e : db.getLexicalEntries()) {
			String data = e.getId() + "," + e.getLemmaWrittenForm() + "," + e.getPos();
//			System.err.println(data);
			FileUtils.write(outFile, data + "\n", "UTF-8", true);
		}

	}

}
