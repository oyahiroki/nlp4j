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
public class WordNetJaConvert1Glosses {

	public static void main(String[] args) throws Exception {

		String fileName = "/usr/local/data/wordnetja/" + "jpn_wn_lmf.xml.gz";
		File file = new File(fileName);

		String outFileName = "/usr/local/data/wordnetja/" + "jpn_wn_lmf_glosses_json.txt";
		File outFile = new File(outFileName);

		if (outFile.exists()) {
			System.err.println("Exists: " + outFile.getAbsolutePath());
			return;
		}

		String licenseFileName = "/usr/doshisha/iip/data/wordnetja/license.txt";
		File licenseFile = new File(licenseFileName);
		String license = FileUtils.readFileToString(licenseFile, "UTF-8");
		{
			JsonObject doc = new JsonObject();
			doc.addProperty("author", "Hiroki Oya");
			doc.addProperty("author_url", "https://nlp4j.org");
			doc.addProperty("url",
					"https://github.com/oyahiroki/nlp4j-resources/blob/main/wordnet/jpn_wn_lmf_glosses_json.txt");
			doc.addProperty("url_raw",
					"https://raw.githubusercontent.com/oyahiroki/nlp4j-resources/main/wordnet/jpn_wn_lmf_glosses_json.txt");
			doc.addProperty("license_url", "http://compling.hss.ntu.edu.sg/wnja/license.txt");
			doc.addProperty("license", license);
			FileUtils.write(outFile, doc.toString() + "\n", "UTF-8", true);
		}

		WordNetJa db = new WordNetJa(file);

//		for (LexicalEntry le : db.getLexicalEntries()) {
//			System.err.println(le.getLemmaWrittenForm());
//		}

		System.err.println(db.getLexicalEntries().size());

		int max = 100;

		max = db.getLexicalEntries().size();

		for (int n = 0; n < max; n++) {

			JsonObject doc = new JsonObject();

			LexicalEntry entry = db.getLexicalEntries().get(n);
			{
				doc.addProperty("id", entry.getId());
			}
			String str = entry.getLemmaWrittenForm();
			{
				doc.addProperty("item", str);
			}
			String pos = entry.getPos();
			{
				doc.addProperty("pos", pos);
			}

			List<Synset> synsets = db.getLinkedSynsets(entry);
			{
				JsonArray glosses = new JsonArray();
				synsets.stream() //
						.filter(o -> o.getDefinition_gloss() != null) //
						.forEach(syn -> {
							glosses.add(syn.getDefinition_gloss());
						});
				doc.add("glosses", glosses);
			}

//			System.err.println(str + "," + pos);
			{ // 複数synsetの同義語を混ぜる
				Set<String> used = new HashSet<String>();
				JsonArray synonyms = new JsonArray();
				db.getLexicalEntriesInSameSynset(entry).stream()//
						.filter(o -> str.equals(o.getLemmaWrittenForm()) == false) //
						.filter(o -> used.contains(o.getLemmaWrittenForm()) == false) // FIX:20220829
						.forEach(et -> {
//					System.err.println("同義語:" + et.getLemmaWrittenForm());
							synonyms.add(et.getLemmaWrittenForm());
							used.add(et.getLemmaWrittenForm());
						});
				doc.add("synonyms", synonyms);
			}

			// NEW:20220829
			{ // TODO 複数synsetを混ぜない同義語リスト
				JsonArray synonyms2 = new JsonArray();
				for (List<LexicalEntry> le : db.getLexicalEntriesInSameSynsetAsListList(entry)) {
					JsonArray synonyms = new JsonArray();
					for (LexicalEntry lee : le) {
						if (str.equals(lee.getLemmaWrittenForm())) {
							continue;
						}
						synonyms.add(lee.getLemmaWrittenForm());
					}
					synonyms2.add(synonyms);
				}
				doc.add("synonyms2", synonyms2);
			}
//			System.err.println(JsonUtils.prettyPrint(doc));

			FileUtils.write(outFile, doc.toString() + "\n", "UTF-8", true);

		}

	}

}
