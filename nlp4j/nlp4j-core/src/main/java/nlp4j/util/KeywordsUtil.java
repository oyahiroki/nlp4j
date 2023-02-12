package nlp4j.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nlp4j.Keyword;

/**
 * created on 2022-11-19
 * 
 * @author Hiroki Oya
 */
public class KeywordsUtil {

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

}
