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
		double[] dd2 = new double[dd1.length];
		for (int n = 0; n < dd1.length; n++) {
			dd2[n] = dd1[n] * dd1[n];
		}
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
		double[] dd2 = new double[dd1.length];
		for (int n = 0; n < dd1.length; n++) {
			dd2[n] = dd1[n] * 2.0;
		}
		double corr = StatUtils.pearsonsCorrelation(dd1, dd2);
		System.out.println(DoubleUtils.toPlainString(corr));
	}

	/**
	 * 相関係数 (y = x * x)
	 */
	public void testPearsonsCorrelationDoubleArrayDoubleArray003() {
		double[] dd1 = { 20, 30, 50, 60, 70, 70, 80, 80, 80 };
		double[] dd2 = new double[dd1.length];
		for (int n = 0; n < dd1.length; n++) {
			dd2[n] = dd1[n] * dd1[n];
		}
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
