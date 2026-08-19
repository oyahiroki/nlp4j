package nlp4j.lucene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.KeywordBuilder;
import nlp4j.UPOS20;
import nlp4j.impl.DefaultDocument;
import nlp4j.opennlp.OpenNLPAnnotator;
import nlp4j.util.StringUtils;

public class EnglishSearchRecordEnricher implements SearchRecordEnricher {

	private static final Set<String> DEFAULT_WORD_FIELDS = Set.of("word", "word.noun", "word.verb", "word.adj",
			"word.adp", "word.aux", "word.sym", "word.propn", "word.num", "word.adv");

	@Override
	public void enrich(SearchRecord record) {
		if (record == null || record.getBody() == null || record.getBody().isEmpty()) {
			return;
		}

		try {
			nlp4j.Document doc = new DefaultDocument();

			String text = StringUtils.nfkc(record.getBody());
			doc.setText(text);

			OpenNLPAnnotator annotator = new OpenNLPAnnotator();
			{
				annotator.setProperty("target", "text");
				annotator.annotate(doc);
			}

			addCompoundNouns(doc);

			copyKeywords(doc, record);

		} catch (Exception e) {
			throw new LocalSearchException("text analysis failed: " + e.getMessage(), e);
		}

	}

	private void addCompoundNouns(Document doc) {
		String s = "";
		int count = 0;
		int begin = 0;
		String spacer = " "; // FOR Japanese "", FOR ENGLISH " "

		List<Keyword> keywords = new ArrayList<>();

		for (Keyword kwd : doc.getKeywords()) {

			if (kwd.getUPos() == null || kwd.getLex() == null || "*".equals(kwd.getLex())) {

				s = "";
				count = 0;
				begin = 0;
				continue;
			}

			if (UPOS20.NOUN.equals(kwd.getUPos())) {

				count++;
				
				if (count == 1) {
				    s = kwd.getLex();
				} else {
				    s += spacer + kwd.getLex();
				}
				count++;

				if (count == 1) {
					begin = kwd.getBegin();
				}

				if (count > 1) {
					Keyword keyword = new KeywordBuilder().lex(s).str(s).begin(begin).end(begin + s.length())
							.facet("word.noun").upos(UPOS20.NOUN).build();

					keywords.add(keyword);
				}

			} else {
				s = "";
				count = 0;
				begin = 0;
			}
		}

		doc.addKeywords(keywords);

	}

	private void copyKeywords(nlp4j.Document doc, SearchRecord record) {

		Set<String> registered = new HashSet<>();

		for (Keyword kw : doc.getKeywords()) {

			String upos = kw.getUPos();
			String lex = kw.getLex();

			if (upos == null || lex == null) {
				continue;
			}

			if (DEFAULT_WORD_FIELDS.contains("word." + upos.toLowerCase()) == false) {
				continue;
			}

			String wordField = toWordField(upos);

			String wordKey = wordField + "\t" + lex;

			if (registered.add(wordKey)) {
				record.addKeyword(wordField, lex, kw.getStr(), kw.getBegin(), kw.getEnd());
			}

			if (isContentWord(upos)) {

				String mainKey = "word\t" + lex;

				if (registered.add(mainKey)) {
					record.addKeyword("word", lex, kw.getStr(), kw.getBegin(), kw.getEnd());
				}
			}
		}
	}

	private String toWordField(String upos) {
		return "word." + upos.toLowerCase(Locale.ROOT);
	}

	private boolean isContentWord(String upos) {
		return UPOS20.NOUN.equals(upos) || UPOS20.PROPN.equals(upos) || UPOS20.VERB.equals(upos)
				|| UPOS20.ADJ.equals(upos);
	}

}
