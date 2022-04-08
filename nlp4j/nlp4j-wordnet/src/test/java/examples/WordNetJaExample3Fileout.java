package examples;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import nlp4j.wordnetja.WordNetJa;

/**
 * <pre>
 * created_at 2021-12-10
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class WordNetJaExample3Fileout {

	public static void main(String[] args) throws Exception {

		String fileName = "files/jpn_wn_lmf.xml.gz";
		File file = new File(fileName);

		WordNetJa db = new WordNetJa(file);

		File outFile = new File("files/files_items.txt");

		List<String> ss = db.getWrittenFormList();

		ss.sort(Comparator.naturalOrder());

		for (String item : ss) {
			FileUtils.write(outFile, item + "\n", "UTF-8", true);
		}

	}

}
