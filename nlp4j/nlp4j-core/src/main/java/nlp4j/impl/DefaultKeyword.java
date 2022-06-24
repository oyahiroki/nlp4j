package nlp4j.impl;

import java.util.Objects;

import nlp4j.AbstractKeyword;
import nlp4j.Keyword;
import nlp4j.UPOS20;

/**
 * ドキュメントのキーワードとなるクラスです。<br>
 * Keyword class of a document.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class DefaultKeyword extends AbstractKeyword implements Keyword, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean flag = false;

	/**
	 * Default constructor
	 * 
	 * @since 1.2.1.0
	 */
	public DefaultKeyword() {
		super();
	}

	/**
	 * @param begin 開始位置
	 * @param end   終了位置
	 * @param facet ファセット
	 * @param lex   正規形
	 * @param str   表出文字
	 */
	public DefaultKeyword(int begin, int end, String facet, String lex, String str) {
		super();
		this.begin = begin;
		this.end = end;
		this.facet = facet;
		this.lex = lex;
		this.str = str;
	}

	/**
	 * @param namespace 名前空間
	 * @param begin     開始位置
	 * @param end       終了位置
	 * @param facet     ファセット
	 * @param lex       正規形
	 * @param str       表出文字
	 * @since 1.3.1
	 */
	public DefaultKeyword(String namespace, int begin, int end, String facet, String lex, String str) {
		super();
		this.begin = begin;
		this.end = end;
		this.facet = facet;
		this.lex = lex;
		this.str = str;
	}

	/**
	 * @param facet ファセット
	 * @param lex   正規形
	 * @since 1.3.2
	 */
	public DefaultKeyword(String facet, String lex) {
		super();
		this.facet = facet;
		this.lex = lex;
		this.str = lex;
	}

	public void addBeginEnd(int n) {
		this.begin += n;
		this.end += n;
	}

	@Override
	protected DefaultKeyword clone() {

		try {
			DefaultKeyword c = (DefaultKeyword) super.clone();
			{
				c.begin = this.begin;
				c.correlation = this.correlation;
				c.count = this.count;
				if (this.facet != null) {
					c.facet = new String(this.facet);
				}
				c.flag = this.flag;
				if (this.lex != null) {
					c.lex = new String(this.lex);
				}
				if (this.namespace != null) {
					c.namespace = new String(this.namespace);
				}
				if (this.reading != null) {
					c.reading = new String(this.reading);
				}
				c.sequence = this.sequence;
				if (this.str != null) {
					c.str = this.str;
				}
				if (this.upos != null) {
					c.upos = this.upos;
				}
			}
			return c;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	/**
	 * @return true (Check for lex, facet)
	 */
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj instanceof Keyword) {
			Keyword kw = (Keyword) obj;

			if (this.facet == null && kw.getFacet() == null) {
				return this.lex.equals(kw.getLex());
			} //
			else if (this.lex == null || kw.getLex() == null || this.facet == null || kw.getFacet() == null) {
				return false;
			} //
			else {
				return this.facet.equals(kw.getFacet()) && this.lex.equals(kw.getLex());
			}
		} //
		else {
			return super.equals(obj);
		}

	}

	@Override
	public String get(String attribute) {

		if (attribute == null) {
			return null;
		} //
		else if ("facet".equals(attribute)) {
			return facet;
		} //
		else if ("lex".equals(attribute)) {
			return lex;
		} //
		else if ("str".equals(attribute)) {
			return str;
		} //
		else if ("reading".equals(attribute)) {
			return reading;
		} //
		else if ("count".equals(attribute)) {
			return "" + count;
		} //
		else if ("begin".equals(attribute)) {
			return "" + begin;
		} //
		else if ("end".equals(attribute)) {
			return "" + end;
		} //
		else if ("correlation".equals(attribute)) {
			return "" + correlation;
		} //
		else if ("sequence".equals(attribute)) {
			return "" + sequence;
		} //
		else {
			return null;
		}
	}

	/**
	 * フラグを返します。
	 * 
	 * @return フラグ
	 * @since 1.3
	 */
	@Override
	public boolean getFlag() {
		return this.flag;
	}

	@Override
	public int hashCode() {
		if (this.lex == null || lex == null || this.facet == null || facet == null) {
			return super.hashCode();
		} else {
//			return (facet + lex).hashCode();
			return Objects.hash(facet, lex); // since 1.3.1
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#setFacet(java.lang.String)
	 */
	@Override
	public void setFacet(String facet) {
		this.facet = facet;

		if (UPOS20.fromPOSJA(facet) != null) {
			this.upos = UPOS20.fromPOSJA(facet);
		}
	}

	@Override
	public void setFlag(boolean b) {
		this.flag = b;

	}

	/**
	 * @return キーワードの詳細情報文字列
	 * @since 1.3
	 */
	public String toStringDetail() {
		return this.lex + " [sequence=" + sequence + ", facet=" + facet + ", upos=" + upos + ", lex=" + lex + ", str="
				+ str + ", reading=" + reading + ", count=" + count + ", begin=" + begin + ", end=" + end
				+ ", correlation=" + correlation + "]";
	}

}
