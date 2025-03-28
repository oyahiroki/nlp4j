package nlp4j.pattern;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeyword;
import nlp4j.node.Node;
import nlp4j.node.NodeKeyword;

/**
 * created on 2021-05-04
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class PatternMatcher {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * {.*?} にマッチする Matcher
	 */
	static private java.util.regex.Pattern matchPattern //
			= java.util.regex.Pattern.compile("\\{.*?\\}");

	/**
	 * @param targetKeyword
	 * @param rulePattern
	 * @return Hit Keywords
	 */
	static public List<Keyword> match(KeywordWithDependency targetKeyword, Pattern rulePattern) {

		NodeKeyword<KeywordWithDependency> targetKeywordNode = new NodeKeyword<>(targetKeyword);

		// 検索対象のNodeをバラバラにする
		List<Node<Object>> targetKeywordNodeCloned = targetKeywordNode.clonePatterns();

		logger.debug("target nodes: " + targetKeywordNodeCloned.size());

		List<Keyword> ret = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();

		for (Node<Object> targetKwd : targetKeywordNodeCloned) {

			Pattern rulePatternCloned = rulePattern.clone();

			KeywordRule ruleKeyword = rulePatternCloned.getKeywordRule();

			NodeKeyword<KeywordWithDependency> ruleNode = new NodeKeyword<>(ruleKeyword);

			boolean b = ruleNode.matchAll(targetKwd);

			logger.debug("matchAll=" + b);

			if (b == true) {

				String ruleFacet = rulePatternCloned.getFacet();
				String rulePatternValue = rulePatternCloned.getValue();
				logger.debug("rulePatternValue=" + rulePatternValue);

				Matcher matcher = matchPattern.matcher(rulePatternValue);

				int begin = -1;
				int end = -1;

				while (matcher.find() == true) {
					// example_of_group: {0.lex}
					String group = matcher.group();
					logger.debug("group=" + group);

					// example_of_id: 0
					String id = group.substring(1, group.indexOf("."));
					// example_of_att: lex
					String att = group.substring(group.indexOf(".") + 1, group.length() - 1);

					logger.debug("id=" + id + ",att=" + att);

					Keyword hitKeyword = rulePatternCloned.getKeyword(id);

					logger.debug(hitKeyword);

					if (hitKeyword != null) {

						if (hitKeyword.getBegin() >= 0) {
							if (begin == -1 || (begin > hitKeyword.getBegin())) {
								begin = hitKeyword.getBegin();
							}
						}
						if (hitKeyword.getEnd() > 0) {
							if (end < hitKeyword.getEnd()) {
								end = hitKeyword.getEnd();
							}
						}

						String v = null;
						{
							if ("lex".equals(att)) {
								v = hitKeyword.getLex();
							} //
							else if ("str".equals(att)) {
								v = hitKeyword.getStr();
							} //
							else if ("relation".equals(att) //
									&& (hitKeyword instanceof KeywordWithDependency)) {
								KeywordWithDependency kwdwd = ((KeywordWithDependency) hitKeyword);
								v = kwdwd.getRelation();
							}
						}

						if (v != null) {
							rulePatternValue = rulePatternValue.replace(group, v);
						}
					}
				}

				logger.debug("facet=" + ruleFacet + ",patternValue=" + rulePatternValue);

				if (values.contains(rulePatternValue) == false) {
					values.add(rulePatternValue);
					DefaultKeyword newKw = new DefaultKeyword();
					{
						newKw.setFacet(ruleFacet);
						newKw.setLex(rulePatternValue);
						newKw.setBegin(begin);
						newKw.setEnd(end);
						newKw.setSentenceIndex(targetKeyword.getSentenceIndex());
					}
					ret.add(newKw);
				} // END OF IF

			} // END OF (MATCHALL == TRUE)

		} // END OF FOR EACH TARGET NODE

		return ret;
	}

}
