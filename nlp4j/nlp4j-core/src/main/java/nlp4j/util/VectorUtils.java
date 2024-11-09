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

	/**
	 * コサイン類似度を計算するメソッド
	 * 
	 * @since 1.3.7.15
	 */
	public static double cosineSimilarity(double[] vectorA, double[] vectorB) {

		if (vectorA.length != vectorB.length) {
			throw new IllegalArgumentException("vector_length_different");
		}

		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;

		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}

		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

}
