package conversion;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.wordnetja.LexicalEntry;
import nlp4j.wordnetja.Synset;
import nlp4j.wordnetja.WordNetJa;

public class WordNetConverterGlosses {

	public void convert(File inFile, File licenseFile, File outFile) throws IOException {
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

		WordNetJa db = new WordNetJa(inFile);

//		for (LexicalEntry le : db.getLexicalEntries()) {
//			System.err.println(le.getLemmaWrittenForm());
//		}

		System.err.println(db.getLexicalEntries().size());

		int max = db.getLexicalEntries().size();

		// FOR_EACH(LexicalEntries)
		for (int n = 0; n < max; n++) {

			System.err.println("processing ... " + n + " / " + max + " ("
					+ String.format("%.2f", ((double) n / (double) max) * 100));

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

		} // END_OF FOR_EACH(LexicalEntries)

	}

}
