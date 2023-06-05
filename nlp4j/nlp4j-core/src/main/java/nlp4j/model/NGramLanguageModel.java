package nlp4j.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nlp4j.counter.Count;
import nlp4j.counter.Counter;

public class NGramLanguageModel {

	int gram_size = 3;
	private Map<P, List<P>> tree = new HashMap<>();

	/**
	 * 文書セット全体での語彙をカウントする
	 */
	private Counter<String> counter = new Counter<>();

	private P root = null;

	public NGramLanguageModel() {
		P p = new P("[root]");
		this.root = p;
		this.tree.put(p, new ArrayList<>());

	}

	/**
	 * @param text_space_sparated 学習対象のテキスト
	 */
	public void add(String text_space_sparated) {
		// Not process empty text
		if (text_space_sparated == null || text_space_sparated.isEmpty()) {
			return;
		}

		String[] ww = ("[BOS] " + text_space_sparated + " [EOS]").split(" ");

//		System.err.println("ww: " + Arrays.toString(ww));

		for (int n = 0; n < ww.length; n++) {
			counter.add(ww[n]);
		}

		for (int n = 0; n < (ww.length - gram_size + 1); n++) {
			String[] ss = new String[this.gram_size];
			for (int x = 0; (x < gram_size) && (x < ss.length); x++) {
				ss[x] = ww[n + x];
			}
			append(ss);
		}

	}

	public void append(String[] ss) {

//		System.err.println("append: " + Arrays.toString(ss));

		P ptr = this.root;

		// 深さ n
		for (int n = 0; (n < ss.length); n++) {

			String v = ss[n];

			P p = new P(v);

			// found
			P pp;
			if ((pp = ptr.hasChild(p)) != null) {
				pp.addCount();
				ptr = pp;
			}
			// not found
			else {
				ptr = ptr.addChild(p);
			}

			if (v.equals("[EOS]")) {
				break;
			}
		}

//		print();

	}

	public void print() {
		print(new PrintWriter(System.err, true));
	}

	public void print(PrintWriter pw) {
		pw.println("<tree>");
		print(pw, this.root, 0);
		pw.println("</tree>");
		pw.println("<count>");
		for (Count<String> c : counter.getCountListSorted()) {
			pw.println(c.getValue() + "," + c.getCount()+",");
		}
		pw.println("</count>");
	}

	public void print(PrintWriter pw, P p, int i) {
		String tab = StringUtils.repeat("\t", i);
		pw.println(tab + p.value + "," + p.count);
		for (P c : p.getChildren()) {
			print(pw, c, i + 1);
		}

	}

	public Collection<P> pull(P ptr, String[] ww, int depth) {
		P ptr2;
		if ((ptr2 = ptr.hasChild(new P(ww[depth]))) != null) {
//			System.err.println("found " + ptr2.value + "," + ptr2.count + "," + ptr2.ratio);
			if (depth < ww.length - 1) {
				Collection<P> cc = pull(ptr2, ww, depth + 1);
				if (cc != null) {
					return cc;
				}
			} //
			else {
				for (P p : ptr2.getChildren()) {
					System.err.println(Arrays.toString(ww) + " → " + p.value + "," + p.ratio);
				}
				return ptr2.getChildren();
			}
		} else {
//			System.err.println("not found");
		}
		return null;
	}

	public Collection<P> pull(String text) {
		String[] w = text.split(" ");
		return pull(this.root, w, 0);
	}

}
