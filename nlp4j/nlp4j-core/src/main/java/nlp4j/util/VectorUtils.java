package nlp4j.util;

/**
 * @since 1.3.7.13
 */
public class VectorUtils {

	/**
	 * @since 1.3.7.13
	 */
	public static void normalize(double[] vector) {
		double norm = 0;

		// ノルムの計算
		for (double value : vector) {
			norm += value * value;
		}
		norm = Math.sqrt(norm);

		// ベクトルの正規化
		for (int i = 0; i < vector.length; i++) {
			vector[i] = vector[i] / norm;
		}
	}

}
