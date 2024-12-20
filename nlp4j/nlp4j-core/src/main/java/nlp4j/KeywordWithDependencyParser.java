package nlp4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nlp4j.impl.DefaultKeyword;

/**
 * <pre>
 * Parser for keyword with dependency
 * </pre>
 * 
 * created on 2023-02-16
 * 
 * @author Hiroki Oya
 *
 */
public class KeywordWithDependencyParser {

	private static final String LINK = " ... ";

	static public List<Keyword> flatten(KeywordWithDependency kwdRoot, Set<String> targetUPOS) {
		List<Keyword> kwds = new ArrayList<Keyword>();
		flatten(kwdRoot, kwds, targetUPOS, 0);
		return kwds;
	}

	static public List<Keyword> flatten(KeywordWithDependency kwdRoot) {
		List<Keyword> kwds = new ArrayList<Keyword>();
		flatten(kwdRoot, kwds, null, 0);
		return kwds;
	}

	private static void flatten(KeywordWithDependency kwdRoot, List<Keyword> kwds, Set<String> targetUPOS, int depth) {

		if (targetUPOS == null || targetUPOS.contains(kwdRoot.getUPos())) {
			Keyword k = (new KeywordBuilder())//
					.lex(kwdRoot.getLex()) //
					.facet(kwdRoot.getFacet())//
					.sentenceIndex(kwdRoot.getSentenceIndex()) //
					.upos(kwdRoot.getUPos()) //
					.sequence(kwdRoot.getSequence()) //
					.namespace(kwdRoot.getNamespance()) //
					.build();
			k.setSentenceIndex(kwdRoot.getSentenceIndex());
			kwds.add(k);

//			kwds.add((DefaultKeyword) kwdRoot);
		}

		List<KeywordWithDependency> cc = kwdRoot.getChildren();
		if (cc != null) {
			for (KeywordWithDependency c : cc) {
				flatten(c, kwds, targetUPOS, (depth + 1));
			}
		}

		if (depth == 0) {
			kwds.sort(new Comparator<Keyword>() {
				@Override
				public int compare(Keyword o1, Keyword o2) {
					int i1 = o1.getSequence();
					int i2 = o2.getSequence();
					return (i1 - i2);
				}
			});
		}

		return;
	}

	static public List<Keyword> parse(KeywordWithDependency kwdRoot, String... ss) {
		boolean reverse = false;
		return parse(kwdRoot, reverse, ss);
	}

	/**
	 * Parse dependencies as keyword
	 * 
	 * @param kwdRoot
	 * @param reverse
	 * @param ss
	 * @return
	 */
	static public List<Keyword> parse(KeywordWithDependency kwdRoot, boolean reverse, String... ss) {

		Set<String> sss = (ss != null && ss.length != 0) ? new HashSet<String>(Arrays.asList(ss)) : null;

		List<KeywordWithDependency> cc = kwdRoot.getChildren();

		List<Keyword> kwds = new ArrayList<>();

		for (KeywordWithDependency c : cc) {

			if (sss == null || (c.getRelation() != null && sss.contains(c.getRelation()))) {
				String lex2 = null;
				if (reverse == false) {
					lex2 = c.getLex() + LINK + kwdRoot.getLex();
				} else {
					lex2 = kwdRoot.getLex() + LINK + c.getLex();
				}

				Keyword kwd = new DefaultKeyword();
				{
					kwd.setLex(lex2);
					kwd.setFacet("pattern." + c.getRelation());
					kwd.setSentenceIndex(c.getSentenceIndex());
				}
				kwds.add(kwd);
			}

			{
				List<Keyword> kwds2 = parse(c, reverse, ss);
				if (kwds2 != null && kwds2.size() > 0) {
					kwds.addAll(kwds2);
				}
			}

		}

		return kwds;

	}

}
