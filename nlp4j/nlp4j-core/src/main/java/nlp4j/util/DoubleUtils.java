package nlp4j.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.3.7.13
 */
public class DoubleUtils {

	/**
	 * @since 1.3.7.13
	 */
	static public String toPlainString(double[] dd) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < dd.length; i++) {
			builder.append(StringUtils.toString(dd[i]));
			// 最後の要素以外にはカンマとスペースを追加
			if (i < dd.length - 1) {
				builder.append(", ");
			}
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @since 1.3.7.16
	 */
	static public String toPlainString(double d) {
		return StringUtils.toString(d);
	}

	/**
	 * @since 1.3.7.16
	 */
	static public String toString(double d) {
		return StringUtils.toString(d);
	}

	/**
	 * @since 1.3.7.16
	 */
	static public String toString(double[] dd) {
		return toPlainString(dd);
	}

	/**
	 * @since 1.3.7.13
	 */
	static public List<Float> toFloatList(double[] dd) {
		List<Float> list = new ArrayList<Float>();
		for (int n = 0; n < dd.length; n++) {
			list.add((float) dd[n]);
		}
		return list;
	}

	/**
	 * @since 1.3.7.13
	 */
	static public List<Float> toFloatList(List<Number> dd) {
		List<Float> list = new ArrayList<Float>();
		for (int n = 0; n < dd.size(); n++) {
			list.add(dd.get(n).floatValue());
		}
		return list;
	}

	/**
	 * @since 1.3.7.14
	 */
	public static float[] toFloatArray(List<Number> dd) {
		float[] ff = new float[dd.size()];
		for (int n = 0; n < dd.size(); n++) {
			ff[n] = dd.get(n).floatValue();
		}
		return ff;
	}

}
