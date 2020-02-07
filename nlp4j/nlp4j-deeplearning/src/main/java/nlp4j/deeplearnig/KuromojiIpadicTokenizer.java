package nlp4j.deeplearnig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.krmj.annotator.KuromojiAnnotator;

/**
 * @author Hiroki Oya
 * @since 1.0.0.0
 *
 */
public class KuromojiIpadicTokenizer implements Tokenizer {

	// TODO Multithread
	static DocumentAnnotator annotator = new KuromojiAnnotator(); // 形態素解析

	static {
		annotator.setProperty("target", "text");

	}

	List<Keyword> kwds = null;

	private int index;

	ArrayList<String> facets = null;

	/**
	 * key<br>
	 * "facets" : "名詞,形容詞,動詞" comma separated values
	 * 
	 * @param key   name of key
	 * @param value to be set
	 */
	public void setProperty(String key, String value) {
		if ("facet".equals(key)) {
			new ArrayList<String>(Arrays.asList(value.split(",")));
		}
	}

	/**
	 * Constructor
	 * 
	 * @param toTokenize Text to be tokenized
	 */
	public KuromojiIpadicTokenizer(String toTokenize) {

		Document doc = new DefaultDocument();
		doc.setText(toTokenize);
		try {
			annotator.annotate(doc);
			kwds = new ArrayList<>();
			for (Keyword kwd : doc.getKeywords()) {
				String facet = kwd.getFacet();

				if (facets == null) {
					kwds.add(kwd);
				} else {
					if (facets.contains(facet)) {
						kwds.add(kwd);
					}
				}
			}
			index = (kwds.isEmpty()) ? -1 : 0;
		} catch (Exception e) {
			e.printStackTrace();
			index = -1;
		}
	}

	public int countTokens() {
		return kwds.size();
	}

	public List<String> getTokens() {
		List<String> ret = new ArrayList<String>();
		while (hasMoreTokens()) {
			ret.add(nextToken());
		}
		return ret;
	}

	public boolean hasMoreTokens() {
		if (index < 0) {
			return false;
		} else {
			return index < kwds.size();
		}
	}

	public String nextToken() {
		if (index < 0) {
			return null;
		}
		Keyword kwd = kwds.get(index);
		index++;
		return kwd.getLex();
	}

	public void setTokenPreProcessor(TokenPreProcess preProcess) {
		// do nothing
	}
}
