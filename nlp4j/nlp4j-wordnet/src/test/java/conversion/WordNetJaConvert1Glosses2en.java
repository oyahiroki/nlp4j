package conversion;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.wordnetja.LexicalEntry;
import nlp4j.wordnetja.Synset;
import nlp4j.wordnetja.WordNetJa;

/**
 * <pre>
 * created_at 2021-12-10
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class WordNetJaConvert1Glosses2en {

	public static void main(String[] args) throws Exception {

		WordNetConverterGlosses converter = new WordNetConverterGlosses();

		String dir = "/usr/local/wordnet/en/";

		String fileName = dir + "english-wordnet-2022.xml.gz";
		File inFile = new File(fileName);

		String licenseFileName = dir + "license.txt";
		File licenseFile = new File(licenseFileName);

		String outFileName = dir + "eng_wn_lmf_glosses_json_" + System.currentTimeMillis() + ".txt";
		File outFile = new File(outFileName);
		if (outFile.exists()) {
			System.err.println("Exists: " + outFile.getAbsolutePath());
			return;
		}

		converter.convert(inFile, licenseFile, outFile);

	}

}
