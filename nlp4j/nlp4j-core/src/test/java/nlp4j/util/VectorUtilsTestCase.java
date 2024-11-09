package nlp4j.util;

import java.util.Arrays;

import junit.framework.TestCase;

public class VectorUtilsTestCase extends TestCase {

	public void testCosineSimilarity() throws Exception {
		double[] targetVector = { 1.0, 2.0, 3.0 };
		double[][] otherVectors = { { 4.0, 5.0, 6.0 }, { 1.0, 0.0, 1.0 }, { 7.0, 8.0, 9.0 } };

		System.out.println("ターゲットベクトルとのコサイン類似度:");
		for (int i = 0; i < otherVectors.length; i++) {
			double similarity = VectorUtils.cosineSimilarity(targetVector, otherVectors[i]);
			System.out.printf("ベクトル %d: %.4f%n", i + 1, similarity);
		}
	}

	public void testNormalize001() {
		double[] array = { 1.0, 2.0, 3.0 }; // 例として3次元ベクトル
		VectorUtils.normalize(array);

		double[] expected = { 0.2672612419124244, 0.5345224838248488, 0.8017837257372732 };

		// 結果を出力
		System.out.println("Normalized vector:");

		for (double value : array) {
			System.out.println(value);
		}

		assertTrue(Arrays.equals(array, expected));
	}

	public void testNormalize002() {
		double[] array = { 1.0, 0.0, 0.0 }; // 例として3次元ベクトル
		VectorUtils.normalize(array);

		double[] expected = { 1.0, 0.0, 0.0 };

		// 結果を出力
		System.out.println("Normalized vector:");

		for (double value : array) {
			System.out.println(value);
		}

		assertTrue(Arrays.equals(array, expected));
	}

	public void testNormalize003() {
		double[] array = { 0.01, 0.0, 0.0 }; // 例として3次元ベクトル
		VectorUtils.normalize(array);

		double[] expected = { 1.0, 0.0, 0.0 };

		// 結果を出力
		System.out.println("Normalized vector:");

		for (double value : array) {
			System.out.println(value);
		}

		assertTrue(Arrays.equals(array, expected));
	}

}
