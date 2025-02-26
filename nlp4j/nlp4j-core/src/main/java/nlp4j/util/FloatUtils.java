package nlp4j.util;

/**
 * @since 1.3.7.16
 */
public class FloatUtils {

	/**
	 * @since 1.3.7.16
	 */
	static public String toString(float f) {
		return StringUtils.toString(f);
	}

	/**
	 * @since 1.3.7.16
	 */
	static public String toString(float[] ff) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');

		for (int n = 0; n < ff.length; n++) {
			if (n > 0) {
				sb.append(", ");
			}
//			sb.append(Float.toString(ff[n]));
			sb.append(toString(ff[n]));
		}

		sb.append(']');
		return sb.toString();
	}

	/**
	 * @since 1.3.7.16
	 */
	public static String toPlainString(float[] ff) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < ff.length; i++) {
			builder.append(StringUtils.toString(ff[i]));
			// 最後の要素以外にはカンマとスペースを追加
			if (i < ff.length - 1) {
				builder.append(", ");
			}
		}
		builder.append("]");
		return builder.toString();
	}

}
