package nlp4j.math;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.util.DoubleUtils;

public class StatUtilsTestCase extends TestCase {

	public void testRank001() {
		double[] scores = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] rank = StatUtils.rank(scores);
		System.out.println(DoubleUtils.toPlainString(rank));
	}

	public void testRank_TiesMaxStragegy001() {
		double[] scores = { 10, 20, 30, 40, 40 };
		double[] rank = StatUtils.rank_TiesMaxStragegy(scores);
		System.out.println(DoubleUtils.toPlainString(rank));
	}

	public void testRank_TiesMaxStragegy_reversed001() {
		double[] scores = { 10, 20, 30, 40, 40 };
		double[] rank = StatUtils.rank_TiesMaxStragegy_reversed(scores);
		double[] expected = { 5.0, 4.0, 3.0, 2.0, 2.0 };
		System.out.println(DoubleUtils.toPlainString(rank));
		assertEquals(DoubleUtils.toPlainString(expected), DoubleUtils.toPlainString(rank));
	}

	public void testRank_reversed_001() {
		double[] scores = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] rank = StatUtils.rank_reversed(scores);
		System.out.println(DoubleUtils.toPlainString(rank));
	}

	public void testRank_TiesMaxStrategy_001() {
		double[] scores = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] rank = StatUtils.rank_TiesMaxStragegy(scores);
		System.out.println(DoubleUtils.toPlainString(rank));
	}

	public void testSpearmansCorrelationDoubleArrayDoubleArray001() {
		double[] dd1 = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] dd2 = { 50, 80, 50, 80, 60, 90, 50, 80, 90 };
		double corr = StatUtils.spearmansCorrelation(dd1, dd2);
		System.out.println(DoubleUtils.toPlainString(corr));
	}

	public void testSpearmansCorrelationDoubleArrayDoubleArray002() {
		double[] dd1 = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] dd2 = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double corr = StatUtils.spearmansCorrelation(dd1, dd2);
		System.out.println(DoubleUtils.toPlainString(corr));
	}

	public void testSpearmansCorrelationDoubleArrayDoubleArray003() {
		double[] dd1 = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] dd2 = { 20 * 20, 30 * 30, 50 * 50, 60 * 60, 70 * 70, 70 * 70, 80 * 80, 80 * 80, 80 * 80 };
		double corr = StatUtils.spearmansCorrelation(dd1, dd2);
		System.out.println(DoubleUtils.toPlainString(corr));
	}

	public void testSpearmansCorrelationListOfDoubleListOfDouble001() {
	}

	public void testPearsonsCorrelationDoubleArrayDoubleArray001() {
		double[] dd1 = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] dd2 = { 50, 80, 50, 80, 60, 90, 50, 80, 90 };
		double corr = StatUtils.pearsonsCorrelation(dd1, dd2);
		System.out.println(DoubleUtils.toPlainString(corr));
	}

	/**
	 * 相関係数 (完全に相関している)
	 */
	public void testPearsonsCorrelationDoubleArrayDoubleArray002() {
		double[] dd1 = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] dd2 = { 20 * 2, 30 * 2, 50 * 2, 60 * 2, 70 * 2, 70 * 2, 80 * 2, 80 * 2, 80 * 2 };
		double corr = StatUtils.pearsonsCorrelation(dd1, dd2);
		System.out.println(DoubleUtils.toPlainString(corr));
	}

	/**
	 * 相関係数 (y = x * x)
	 */
	public void testPearsonsCorrelationDoubleArrayDoubleArray003() {
		double[] dd1 = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] dd2 = { 20 * 20, 30 * 30, 50 * 50, 60 * 60, 70 * 70, 70 * 70, 80 * 80, 80 * 80, 80 * 80 };
		double corr = StatUtils.pearsonsCorrelation(dd1, dd2);
		System.out.println(DoubleUtils.toPlainString(corr));
	}

	public void testPearsonsCorrelationListOfDoubleListOfDouble001() {
		List<Double> dd1 = DoubleUtils.toDoubleList(20, 30, 50, 60, 70, 70, 80, 80, 80);
		List<Double> dd2 = DoubleUtils.toDoubleList(50, 80, 50, 80, 60, 90, 50, 80, 90);
		double corr = StatUtils.pearsonsCorrelation(dd1, dd2);
		System.out.println(DoubleUtils.toPlainString(corr));
	}

}
