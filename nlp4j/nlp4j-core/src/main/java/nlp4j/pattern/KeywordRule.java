/**
 * 
 */
package nlp4j.pattern;

import nlp4j.Keyword;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-03
 */
public class KeywordRule extends DefaultKeywordWithDependency {

	private String id;

	private String lexPattern;

	@Override
	protected KeywordRule clone() {
		
		KeywordRule c = (KeywordRule) super.clone();
		
		if (this.id != null) {
			c.id = new String(this.id);
		}
		
		if (this.lexPattern != null) {
			c.lexPattern = new String(this.lexPattern);
		}
		
//		c.hitKeyword = super.hitKeyword;
		c.hitKeyword = null;
		
		return c;
	}

	public Keyword getHitKeyword() {
		return super.hitKeyword;
	}

	public void setHitKeywordNull() {
		super.hitKeyword = null;
	}

	public String getId() {
		return id;
	}

	public String getLexPattern() {
		return lexPattern;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLexPattern(String lexPattern) {
		this.lexPattern = lexPattern;
	}

	@Override
	public String toString() {
		return "KeywordRule [id=" + id + ", lexPattern=" + lexPattern + ", facet=" + facet + ", upos=" + upos + "]";
	}

}
