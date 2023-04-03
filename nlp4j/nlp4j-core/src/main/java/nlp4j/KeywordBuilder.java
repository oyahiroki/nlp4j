package nlp4j;

import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @since 1.3.7.5
 */
public class KeywordBuilder implements Builder<Keyword> {

	Keyword kwd;

	public KeywordBuilder() {
		this.kwd = new DefaultKeyword();
	}

	public KeywordBuilder begin(int begin) {
		this.kwd.setBegin(begin);
		return this;
	}

	@Override
	public Keyword build() {
		return this.kwd;
	}

	public KeywordBuilder correlation(double correlation) {
		this.kwd.setCorrelation(correlation);
		return this;
	}

	public KeywordBuilder count(long count) {
		this.kwd.setCount(count);
		return this;
	}

	public KeywordBuilder end(int end) {
		this.kwd.setEnd(end);
		return this;
	}

	public KeywordBuilder facet(String facet) {
		this.kwd.setFacet(facet);
		return this;
	}

	public KeywordBuilder flag(boolean flag) {
		this.kwd.setFlag(flag);
		return this;
	}

	public KeywordBuilder lex(String lex) {
		this.kwd.setLex(lex);
		return this;
	}

	public KeywordBuilder namespace(String namespace) {
		this.kwd.setNamespace(namespace);
		return this;
	}

	public KeywordBuilder paragraphIndex(int idx) {
		this.kwd.setParagraphIndex(idx);
		return this;
	}

	public KeywordBuilder reading(String reading) {
		this.kwd.setReading(reading);
		return this;
	}

	public KeywordBuilder sentenceIndex(int idx) {
		this.kwd.setSentenceIndex(idx);
		return this;
	}

	public KeywordBuilder str(String str) {
		this.kwd.setStr(str);
		return this;
	}

	public KeywordBuilder upos(String upos) {
		this.kwd.setUPos(upos);
		return this;
	}

}
