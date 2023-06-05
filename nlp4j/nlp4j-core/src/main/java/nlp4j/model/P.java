package nlp4j.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Pair of Object
 * 
 * @author Hiroki Oya
 *
 */
public class P {

	String value;
	int count = 0;
	double ratio = 0.0;

	Map<String, P> children = new HashMap<>();

	public P(String v) {
		super();
		this.value = v;
		this.count = 1;
	}

	public P(String v, int count) {
		super();
		this.value = v;
		this.count = count;
	}

	public double getRatio() {
		return ratio;
	}

	private void calcRatio() {
		int countAll = 0;
		for (P c : this.children.values()) {
			countAll += c.getCount();
		}
		for (P c : this.children.values()) {
//			System.err.println("calc: " + value + "," + c.getValue());
			double r = (double) c.getCount() / (double) countAll;
			c.ratio = r;
		}

	}

	public String getValue() {
		return value;
	}

	public int getCount() {
		return count;
	}

	public void addCount() {
		this.count++;
		calcRatio();
	}

	public P addChild(P p) {
		P c = new P(p.value);
		this.children.put(c.value, c);
		calcRatio();
		return c;
	}

	@Override
	public String toString() {
		return "P [value=" + value + ", count=" + count + ", children.size()=" + children.size() + "]";
	}

	public P hasChild(P p) {
		return this.children.get(p.value);
	}

	public Collection<P> getChildren() {
		return children.values();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof P) {
			String v = ((P) obj).getValue();
			if (this.value != null) {
				return this.value.equals(v);
			} //
			else {
				return false;
			}
		}
		return false;
	}

}
