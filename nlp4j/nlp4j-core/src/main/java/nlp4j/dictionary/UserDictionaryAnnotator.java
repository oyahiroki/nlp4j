package nlp4j.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordBuilder;
import nlp4j.util.FileUtils;
import nlp4j.util.TextUtils;

public class UserDictionaryAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	// 見出し(Uppercase) -> List<facet>
	Map<String, List<String>> dic = new HashMap<>();

	@Override
	public void annotate(Document doc) throws Exception {
		List<Keyword> kwds2 = new ArrayList<>();
		for (Keyword kwd : doc.getKeywords()) {
			String lex = kwd.getLex().trim();
			lex = TextUtils.nfkc(lex);
			lex = lex.toUpperCase();

			String facet = kwd.getFacet();

			if (this.dic.containsKey(lex)) {
				List<String> facets = this.dic.get(lex);
				if (facets.contains(facet) == false) {
					for (String facet2 : facets) {
						Keyword kwd2 = (new KeywordBuilder()).lex(lex).facet(facet2)
								.namespace(this.getClass().getName()).build();
						kwds2.add(kwd2);
					}
				}
			}

		}
		doc.addKeywords(kwds2);
	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if ("dictionary".equals(key)) {

			File file = new File(value);
			String name = file.getName();
			String facet = (name.indexOf(".") != -1) ? name.substring(0, name.lastIndexOf(".")) : "dic";
			System.err.println("facet=" + facet);

			try {
				BufferedReader br = FileUtils.openTextFileAsBufferedReader(value);
				String word;
				while ((word = br.readLine()) != null) {
					System.err.println(word);

					word = TextUtils.nfkc(word.trim());
					word = word.toUpperCase();

					if (word.isEmpty() == true) {
						continue;
					}

					if (this.dic.containsKey(word) == false) {
						this.dic.put(word, new ArrayList<>());
					}

					if (this.dic.get(word).contains(facet) == false) {
						this.dic.get(word).add(facet);
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
