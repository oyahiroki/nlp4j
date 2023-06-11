package nlp4j;

/**
 * created on: 2022-06-24
 * 
 * @author Hiroki Oya
 */
public abstract class AbstractKeyword implements Keyword {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id = null;

	/**
	 * begin
	 */
	protected int begin = BEGIN_INIT;

	/**
	 * correlation
	 */
	protected double correlation = CORRELATION_INIT;

	/**
	 * count
	 */
	protected long count = COUNT_INIT;

	/**
	 * count
	 */
	protected long count2 = COUNT_INIT;

	/**
	 * end
	 */
	protected int end = END_INIT;

	/**
	 * facet
	 */
	protected String facet;

	/**
	 * hitKeyword
	 */
	protected Keyword hitKeyword = null;
	/**
	 * lex
	 */
	protected String lex;
	/**
	 * namespace
	 */
	protected String namespace;
	/**
	 * paragraph
	 */
	protected int paragraphIndex = -1;
	/**
	 * reading: 読み
	 */
	protected String reading;
	/**
	 * sentence
	 */
	protected int sentenceIndex = -1;
	/**
	 * sequence: 連番
	 */
	protected int sequence = SEQUENCE_INIT;
	/**
	 * str
	 */
	protected String str;
	/**
	 * upos: Universal Part of Speech
	 */
	protected String upos;
	/**
	 * TRUE: (facet is same) and (lex is same)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Keyword) {
			Keyword kw = (Keyword) obj;
			// IF(BOTH FACET IS NULL)
			if (this.facet == null && kw.getFacet() == null) {
				return this.lex.equals(kw.getLex());
			} //
				// ELSE_IF(LEX OR FACE IS NULL)
			else if (this.lex == null || kw.getLex() == null || this.facet == null || kw.getFacet() == null) {
				return false;
			} //
				// ELSE -> SAME FACET AND SAME LEX
			else {
				return this.facet.equals(kw.getFacet()) && this.lex.equals(kw.getLex());
			}
		} //
		else {
			return super.equals(obj);
		}
	}
	@Override
	public int getBegin() {
		return begin;
	}

	@Override
	public double getCorrelation() {
		return this.correlation;
	}

	@Override
	public long getCount() {
		return this.count;
	}

	@Override
	public long getCount2() {
		return this.count2;
	}

	@Override
	public int getEnd() {
		return end;
	}

	@Override
	public String getFacet() {
		return facet;
	}

	public String getId() {
		return id;
	}

	@Override
	public String getLex() {
		return lex;
	}

	@Override
	public String getNamespance() {
		return this.namespace;
	}

	@Override
	public String getReading() {

		return this.reading;
	}

	/**
	 * @return 連番
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * @return the str
	 */
	public String getStr() {
		return str;
	}

	@Override
	public String getUPos() {
		return this.upos;
	}

	/**
	 *
	 */
	public boolean match(Keyword rule) {

		if (rule == null) {
			return true;
		}

		// ignore begin
		// ignnore correlation
		// ignore count
		// ignore end

		// FACET
		if (rule.getFacet() != null //
				&& this.getFacet() != null //
				&& this.getFacet().equals(rule.getFacet()) == false) {
			return false;
		}

		// LEX
		if (rule.getLex() != null //
				&& this.getLex() != null //
				&& this.getLex().equals(rule.getLex()) == false) {
			return false;
		}

		// UPOS
		if (rule.getUPos() != null //
				&& this.getUPos() != null //
				&& this.getUPos().equals(rule.getUPos()) == false) {
			return false;
		}

		return true;
	}

	@Override
	public void setBegin(int begin) {
		this.begin = begin;
	}

	@Override
	public void setCorrelation(double d) {
		this.correlation = d;
	}

	@Override
	public void setCount(long count) {
		this.count = count;
	}

	@Override
	public void setCount2(long count2) {
		this.count2 = count2;
	}

	@Override
	public void setEnd(int end) {
		this.end = end;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setLex(String lex) {
		this.lex = lex;
	}

	@Override
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public void setReading(String reading) {
		this.reading = reading;
	}

	/**
	 * 連番を返します。
	 * 
	 * @param sequence 連番
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;

	}

	/**
	 * @param str the str to set
	 */
	public void setStr(String str) {
		this.str = str;
	}

	@Override
	public void setUPos(String upos) {
		this.upos = upos;
	}

	@Override
	public String toString() {
		return //
		this.lex //
				+ " [" //
//				+ "sequence=" + sequence //
				+ "facet=" + facet //
				+ ", lex=" + lex //
				+ ", str=" + str //
//				+ ", reading="+ reading  //
//				+ ", count=" + count  //
//				+ ", begin=" + begin  //
//				+ ", end=" + end  //
//				+ ", correlation=" + correlation //
				+ "]";
	}
}
