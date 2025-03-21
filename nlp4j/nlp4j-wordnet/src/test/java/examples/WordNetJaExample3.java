package examples;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import nlp4j.wordnetja.WordNetJa;

/**
 * <pre>
 * created_at 2021-12-10
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class WordNetJaExample3 {

	public static void main(String[] args) throws Exception {

		String fileName = "files/jpn_wn_lmf.xml.gz";
		File file = new File(fileName);

		WordNetJa db = new WordNetJa(file);

		List<String> ss = db.getWrittenFormList();

		ss.sort(Comparator.naturalOrder());

		int count = 1;

		for (String item : ss) {
			System.err.println(count + "," + item);
			count++;
		}

	}

}
