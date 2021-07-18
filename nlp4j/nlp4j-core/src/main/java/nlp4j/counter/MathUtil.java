package nlp4j.counter;

public class MathUtil {

	/**
	 * @param x
	 * @return
	 */
	public static double log2(double x) {
		// 特殊な値
		if (Double.isNaN(x) || x < 0.0) {
			return Double.NaN;
		}
		if (x == Double.POSITIVE_INFINITY) {
			return Double.POSITIVE_INFINITY;
		}
		if (x == 0.0) {
			return Double.NEGATIVE_INFINITY;
		}
		// ここから
		int k = Math.getExponent(x);
		if (k < Double.MIN_EXPONENT) {
			// 非正規化数は取扱い注意！
			k = Math.getExponent(x * 0x1.0p52) - 52;
		}
		if (k < 0) {
			k++;
		}
		double s = Math.scalb(x, -k);
		final double LOG2_E = 1.4426950408889634;
		return k + LOG2_E * Math.log(s);
	}

}
