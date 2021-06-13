/**
 * 
 */
package nlp4j.pattern;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeyword;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-03
 */
public class KeywordRule extends DefaultKeywordWithDependency {

	private String id;

	private String lang;

	@Override
	protected KeywordRule clone() {

		KeywordRule c = (KeywordRule) super.clone();

		if (this.id != null) {
			c.id = new String(this.id);
		}

		if (this.lang != null) {
			c.lang = new String(this.lang);
		}

		c.hitKeyword = null;

		return c;
	}

	public Keyword getHitKeyword() {
		return super.hitKeyword;
	}

	public String getId() {
		return id;
	}

	public String getLang() {
		return lang;
	}

	@Override
	public boolean match(Keyword target) {

		if (target == null) {
			return true;
		}

//		this.children
//		this.correlation
//		this.facet
//		this.lex
//		this.reading
//		this.relation
//		this.str
//		this.upos

//		if (target.getFacet() != null) {
//			if (this.getFacet() == null) {
//			} else {
//				if (this.getFacet().equals(target.getFacet()) == false) {
//					return false;
//				}
//			}
//		}
		if (this.facet != null) {
			if (target.getFacet() == null) {
				return false;
			} //
			else {
				boolean b = this.facet.equals(target.getFacet());
				if (b == false) {
					return false;
				}
			}
		}

		if (this.lex != null) {
			if (target.getLex() == null) {
				return false;
			}
			// ELSE(lex is not null)
			else {
				// REGEX
				if (this.lex.startsWith("/") && this.lex.endsWith("/") && this.lex.length() > 2) {
					String ruleRegex = this.lex.substring(1, this.lex.length() - 1);
					if (target.getLex().matches(ruleRegex) == false) {
						return false;
					}
				} //
				else {
					if (target.getLex().equals(this.lex) == false) {
						return false;
					}
				}

			}
		}

		if (this.reading != null) {
			if (target.getReading() == null) {
				return false;
			} //
			else {
				boolean b = this.reading.equals(target.getReading());
				if (b == false) {
					return false;
				}
			}
		}

		if (this.relation != null) {
			if (target instanceof KeywordWithDependency == false) {
				return false;
			} //
			else {
				KeywordWithDependency tgt = (KeywordWithDependency) target;
				if (tgt.getRelation() == null) {
					return false;
				} //
				else {
					boolean b = this.relation.equals(tgt.getRelation());
					if (b == false) {
						return false;
					}
				}
			}

		}

//		if (this.facet != null) {
//			if (this.facet.equals(target.getFacet()) == false) {
//				return false;
//			}
//		}

		if (this.str != null) {
			if (target.getStr() == null) {
				return false;
			} //
			else {
				boolean b = this.str.equals(target.getStr());
				if (b == false) {
					return b;
				}
			}
		}

		if (target.getUPos() != null) {
			if (this.getUPos() == null) {
			} //
			else {
				if (this.getUPos().equals(target.getUPos()) == false) {
					return false;
				}
			}
		}

		if (this.upos != null) {
			if (target.getUPos() == null) {
				return false;
			} //
			else {
				boolean b = this.upos.equals(target.getUPos());
				if (b == false) {
					return false;
				}
			}
		}

		// hit is true

		if (target instanceof KeywordRule) {
			throw new RuntimeException();
//			if (((KeywordRule) target).hitKeyword == null) {
//				((KeywordRule) target).hitKeyword = this;
//			}
		}

		// 20210509
		{
			this.hitKeyword = target;
		}

		return true;
	}

	public void setHitKeywordNull() {
		super.hitKeyword = null;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public String getLexPattern() {
//		return lexPattern;
//	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	public String toString() {
		return "KeywordRule [" //
				+ "lang=" + lang + ", " //
				+ "id=" + id + ", " //
				+ "relation=" + relation + ", " //
				+ "facet=" + facet + ", " //
				+ "lex=" + lex + ", " //
				+ "reading=" + reading + ", " //
				+ "str=" + str + ", " //
				+ "upos=" + upos + "" //
				+ "]";
	}

//	public void setLexPattern(String lexPattern) {
//		this.lexPattern = lexPattern;
//	}

//	@Override
//	public String toString() {
////		return "KeywordRule [id=" + id + ", lexPattern=" + lexPattern + ", facet=" + facet + ", upos=" + upos + "]";
//		return "KeywordRule [id=" + id + ", facet=" + facet + ", upos=" + upos + "]";
//	}

}
