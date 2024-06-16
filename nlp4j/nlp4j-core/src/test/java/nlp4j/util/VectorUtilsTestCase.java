package nlp4j.util;

import java.util.Arrays;

import junit.framework.TestCase;

public class VectorUtilsTestCase extends TestCase {

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
