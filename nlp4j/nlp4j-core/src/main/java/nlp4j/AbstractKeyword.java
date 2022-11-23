package nlp4j;

/**
 * created at: 2022-06024
 * 
 * @author Hiroki Oya
 *
 */
public abstract class AbstractKeyword implements Keyword {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int begin = BEGIN_INIT;
	protected double correlation = CORRELATION_INIT;
	protected long count = COUNT_INIT;
	protected int end = END_INIT;
	protected String facet;
	protected String lex;
	protected String namespace;
	protected String reading;
	protected int sequence = SEQUENCE_INIT;
	protected String str;
	protected String upos;
	protected Keyword hitKeyword = null;

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
	public int getEnd() {
		return end;
	}

	@Override
	public String getFacet() {
		return facet;
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
	public void setEnd(int end) {
		this.end = end;
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
