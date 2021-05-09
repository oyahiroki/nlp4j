/**
 * 
 */
package nlp4j.pattern;

import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-03
 */
public class KeywordRule extends DefaultKeywordWithDependency {

	private String id;

//	private String lexPattern;

	@Override
	protected KeywordRule clone() {

		KeywordRule c = (KeywordRule) super.clone();

		if (this.id != null) {
			c.id = new String(this.id);
		}

//		if (this.lexPattern != null) {
//			c.lexPattern = new String(this.lexPattern);
//		}

//		c.hitKeyword = super.hitKeyword;
		c.hitKeyword = null;

		return c;
	}

	public boolean matchLex(String lex) {

		String ruleLex = this.lex;

		// REGEX
		if (ruleLex.startsWith("/") && ruleLex.endsWith("/") && ruleLex.length() > 2) {
			String ruleRegex = ruleLex.substring(1, ruleLex.length() - 1);
			return lex.matches(ruleRegex);
		} //
		else {
			return lex.equals(ruleLex);
		}
	}

	@Override
	public boolean match(Keyword rule) {

		boolean b = super.match(rule);

		System.err.println(b);

//		return super.match(rule);
		if (rule == null) {
			return true;
		}

		if (rule.getFacet() != null) {
			if (this.getFacet() == null) {
				return false;
			} else {
				if (this.getFacet().equals(rule.getFacet()) == false) {
					return false;
				}
			}
		}

//		if (rule.getLex() != null) {
//			if (this.getLex() == null) {
//				return false;
//			}
//			// ELSE(lex is not null)
//			else {
//
//				// REGEX
//				if (rule.getLex().startsWith("/") && rule.getLex().endsWith("/") && rule.getLex().length() > 2) {
//					String ruleRegex = rule.getLex().substring(1, rule.getLex().length() - 1);
//					if (this.getLex().matches(ruleRegex) == false) {
//						return false;
//					}
//				} //
//				else {
//					if (this.getLex().equals(rule.getLex()) == false) {
//						return false;
//					}
//				}
//
//			}
//		}
		
		if (this.lex != null) {
			if (rule.getLex() == null) {
				return false;
			}
			// ELSE(lex is not null)
			else {

				// REGEX
				if (this.lex.startsWith("/") && this.lex.endsWith("/") && this.lex.length() > 2) {
					String ruleRegex = this.lex.substring(1, this.lex.length() - 1);
					if (rule.getLex().matches(ruleRegex) == false) {
						return false;
					}
				} //
				else {
					if (rule.getLex().equals(this.lex) == false) {
						return false;
					}
				}

			}
		}

		if (rule.getUPos() != null) {
			if (this.getUPos() == null) {
				return false;
			} else {
				if (this.getUPos().equals(rule.getUPos()) == false) {
					return false;
				}

			}
		}

//		if (rule instanceof DefaultKeyword) {
//			if (((DefaultKeyword) rule).hitKeyword == null) {
//				((DefaultKeyword) rule).hitKeyword = this;
//			}
//		}

		if (rule instanceof KeywordRule) {
			if (((KeywordRule) rule).hitKeyword == null) {
				((KeywordRule) rule).hitKeyword = this;
			}
		}

		return true;
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

//	public String getLexPattern() {
//		return lexPattern;
//	}

	public void setId(String id) {
		this.id = id;
	}

//	public void setLexPattern(String lexPattern) {
//		this.lexPattern = lexPattern;
//	}

	@Override
	public String toString() {
//		return "KeywordRule [id=" + id + ", lexPattern=" + lexPattern + ", facet=" + facet + ", upos=" + upos + "]";
		return "KeywordRule [id=" + id + ", facet=" + facet + ", upos=" + upos + "]";
	}

}
