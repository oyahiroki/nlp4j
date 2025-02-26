package nlp4j.math;

import java.util.List;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import nlp4j.util.CollectionUtils;

/**
 * <pre>
 * Provides utility methods for working with collections
 * created on: 2025-2-26
 * </pre>
 * 
 * @since 1.3.7.16
 */
public class StatUtils {

	/**
	 * <pre>
	 * スピアマンの順位相関係数を計算します。
	 * Calculates Spearman's rank correlation coefficient.
	 * created on: 2025-2-26
	 * </pre>
	 * 
	 * @param dd1 the first data set as an array of doubles
	 * @param dd2 the second data set as an array of doubles
	 * @return the Spearman's rank correlation coefficient
	 * @since 1.3.7.16
	 */
	static public double spearmansCorrelation(double[] dd1, double[] dd2) {
		SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
		double correlation = spearmansCorrelation.correlation(dd1, dd2);
		return correlation;
	}

	/**
	 * <pre>
	 * リスト形式のデータセットからスピアマンの順位相関係数を計算します。
	 * Calculates Spearman's rank correlation coefficient from data sets given as lists.
	 * created on: 2025-2-26
	 * </pre>
	 * 
	 * @param dd1 the first data set as a list of Doubles
	 * @param dd2 the second data set as a list of Doubles
	 * @return the Spearman's rank correlation coefficient
	 * @since 1.3.7.16
	 */
	static public double spearmansCorrelation(List<Double> dd1, List<Double> dd2) {
		return spearmansCorrelation( //
				CollectionUtils.toArray(dd1), //
				CollectionUtils.toArray(dd2) //
		);
	}

	/**
	 * <pre>
	 * ピアソンの積率相関係数を計算します。
	 * Calculates Pearson's product-moment correlation coefficient.
	 * </pre>
	 * 
	 * @param dd1 the first data set as an array of doubles
	 * @param dd2 the second data set as an array of doubles
	 * @return the Pearson's correlation coefficient
	 * @since 1.3.7.16
	 */
	static public double pearsonsCorrelation(double[] dd1, double[] dd2) {
		PearsonsCorrelation corr = new PearsonsCorrelation();
		return corr.correlation(dd1, dd2);
	}

	/**
	 * <pre>
	 * リスト形式のデータセットからピアソンの積率相関係数を計算します。
	 * Calculates Pearson's product-moment correlation coefficient from data sets given as lists.
	 * </pre>
	 * 
	 * @param dd1 the first data set as a list of Doubles
	 * @param dd2 the second data set as a list of Doubles
	 * @return the Pearson's correlation coefficient
	 * @since 1.3.7.16
	 */
	static public double pearsonsCorrelation(List<Double> dd1, List<Double> dd2) {
		return pearsonsCorrelation( //
				CollectionUtils.toArray(dd1), //
				CollectionUtils.toArray(dd2) //
		);
	}

}
