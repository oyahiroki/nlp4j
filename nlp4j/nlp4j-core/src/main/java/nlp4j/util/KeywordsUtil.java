package nlp4j.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nlp4j.Keyword;

/**
 * created on 2022-11-19
 * 
 * @author Hiroki Oya
 */
public class KeywordsUtil {

	/**
	 * 
	 * @param kwds
	 * @param facet
	 * @return
	 * @since 1.3.7.12
	 */
	static public boolean isSameFacet(List<Keyword> kwds, String facet) {

		for (Keyword kwd : kwds) {
			String f = kwd.getFacet();
			if (f == null || f.equals(facet) == false) {
				return false;
			}
		}
		return true;
	}

	static public void println(List<Keyword> kwds) {
		println(kwds, new PrintWriter(System.err, true));
	}

	static public void println(List<Keyword> kwds, PrintWriter pw) {
		for (Keyword kwd : kwds) {
			pw.println(kwd.toString());
		}
	}

	static public void sortByCorrelationAsc(List<Keyword> kwds) {
		Collections.sort(kwds, new Comparator<Keyword>() {
			@Override
			public int compare(Keyword o1, Keyword o2) {
				double d = o1.getCorrelation() - o2.getCorrelation();
				if (d == 0.0) {
					return 0;
				} //
				else if (d > 0.0) {
					return 1;
				} //
				else {
					return -1;
				}
			}
		});
	}

	static public void sortByCorrelationDesc(List<Keyword> kwds) {
		Collections.sort(kwds, new Comparator<Keyword>() {
			@Override
			public int compare(Keyword o1, Keyword o2) {
				double d = o2.getCorrelation() - o1.getCorrelation();
				if (d == 0.0) {
					return 0;
				} //
				else if (d > 0.0) {
					return 1;
				} //
				else {
					return -1;
				}
			}
		});
	}

	static public void sortByCountAsc(List<Keyword> kwds) {
		Collections.sort(kwds, new Comparator<Keyword>() {
			@Override
			public int compare(Keyword o1, Keyword o2) {
				long diff = (o1.getCount() - o2.getCount());
				return (int) diff;
			}
		});
	}

	static public void sortByCountDesc(List<Keyword> kwds) {
		Collections.sort(kwds, new Comparator<Keyword>() {
			@Override
			public int compare(Keyword o1, Keyword o2) {
				long diff = (o1.getCount() - o2.getCount()) * -1;
				return (int) diff;
			}
		});
	}

	static public String toLex(List<Keyword> kwds, String delimiter) {
		if (kwds == null || kwds.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Keyword kwd : kwds) {
			if (sb.length() > 0) {
				sb.append(delimiter);
			}
			sb.append(kwd.getLex());
		}
		return sb.toString();
	}

	static public String[] toLexArray(List<Keyword> kwds) {
		if (kwds == null) {
			return null;
		}
		if (kwds.size() == 0) {
			return new String[0];
		}
		String[] ss = new String[kwds.size()];
		for (int n = 0; n < kwds.size(); n++) {
			ss[n] = kwds.get(n).getLex();
		}
		return ss;
	}

	/**
	 * @param kwds
	 * @return List of Keyword lex
	 * @since 1.3.7.12
	 */
	static public List<String> toLexList(List<Keyword> kwds) {
		if (kwds == null) {
			return null;
		}
		if (kwds.size() == 0) {
			return new ArrayList<>();
		}
		List<String> list = new ArrayList<>();
		for (int n = 0; n < kwds.size(); n++) {
			list.add(kwds.get(n).getLex());
		}
		return list;
	}

	/**
	 * @param kwds
	 * @return Set of Keyword lex
	 * @since 1.3.7.12
	 */
	static public Set<String> toLexSet(List<Keyword> kwds) {
		if (kwds == null) {
			return null;
		}
		if (kwds.size() == 0) {
			return new HashSet<>();
		}
		Set<String> list = new HashSet<>();
		for (int n = 0; n < kwds.size(); n++) {
			list.add(kwds.get(n).getLex());
		}
		return list;
	}

	public static boolean isSameLexSet(List<Keyword> kwds1, List<Keyword> kwds2) {
		if (kwds1.size() == kwds2.size()) {
			Set<String> set1 = toLexSet(kwds1);
			Set<String> set2 = toLexSet(kwds2);
			Iterator<String> it = set1.iterator();
			while (it.hasNext()) {
				if (set2.contains(it.next()) == false) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static int countSameLex(List<Keyword> kwds1, List<Keyword> kwds2) {
		int count = 0;
		{
			Set<String> set1 = toLexSet(kwds1);
			Set<String> set2 = toLexSet(kwds2);
			Iterator<String> it = set1.iterator();
			while (it.hasNext()) {
				if (set2.contains(it.next()) == true) {
					count++;
				}
			}
		}
		return count;
	}

}
