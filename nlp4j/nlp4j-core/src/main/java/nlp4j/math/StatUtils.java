package nlp4j.math;

import java.util.List;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.RankingAlgorithm;
import org.apache.commons.math3.stat.ranking.TiesStrategy;

import nlp4j.util.CollectionUtils;

/**
 * <pre>
 * 数値の集合に対する統計的操作を提供するユーティリティクラス。
 * Provides utility methods for working with collections of numeric values.
 * 
 *  created on: 2025-2-26
 * </pre>
 * 
 * @since 1.3.7.16
 */
public class StatUtils {

	/**
	 * <pre>
	 * 与えられたスコアの配列に対して、値の小さい順に順位をつける。
	 * Performs a rank transformation on the input data, returning an array of
	 * ranks ordered from the smallest to the largest values.
	 * </pre>
	 * 
	 * @param scores 数値の配列。An array of numeric scores.
	 * @return 順位の配列。An array of ranks.
	 */
	static public double[] rank(double[] scores) {
		RankingAlgorithm rankingAlgorithm = new NaturalRanking();
		// A,B,C に対するスコアの順位
		double[] rank = rankingAlgorithm.rank(scores);
		return rank;
	}

	/**
	 * <pre>
	 * 与えられたスコアの配列に対して、値の大きい順に順位をつける。
	 * Performs a rank transformation on the input data, returning an array of
	 * ranks ordered from the largest to the smallest values.
	 * </pre>
	 * 
	 * @param scores 数値の配列。An array of numeric scores.
	 * @return 順位の配列。An array of ranks.
	 */
	static public double[] rank_reversed(double[] scores) {
		reverse(scores);
		return rank(scores);
	}

	/**
	 * <pre>
	 * 配列の各要素の符号を反転させるヘルパーメソッド。
	 * A helper method that inverts the sign of each element in the array.
	 * </pre>
	 * 
	 * @param scores 数値の配列。An array of numeric scores.
	 */
	private static void reverse(double[] scores) {
		for (int n = 0; n < scores.length; n++) {
			scores[n] = -scores[n];
		}
	}

	/**
	 * <pre>
	 * 与えられたスコアの配列に対して、最大戦略で値の小さい順に順位をつける。
	 * Performs a rank transformation using the maximum strategy on ties, returning an array of
	 * ranks ordered from the smallest to the largest values.
	 * </pre>
	 * 
	 * @param scores 数値の配列。An array of numeric scores.
	 * @return 順位の配列。An array of ranks.
	 */
	static public double[] rank_TiesMaxStragegy(double[] scores) {
		RankingAlgorithm rankingAlgorithm = new NaturalRanking(TiesStrategy.MAXIMUM);
		// A,B,C に対するスコアの順位
		double[] rank = rankingAlgorithm.rank(scores);
		return rank;
	}

	/**
	 * <pre>
	 * 与えられたスコアの配列に対して、最大戦略で値の大きい順に順位をつける。
	 * Performs a rank transformation using the maximum strategy on ties, returning an array of
	 * ranks ordered from the largest to the smallest values.
	 * </pre>
	 * 
	 * @param scores 数値の配列。An array of numeric scores.
	 * @return 順位の配列。An array of ranks.
	 */
	static public double[] rank_TiesMaxStragegy_reversed(double[] scores) {
		reverse(scores);
		return rank_TiesMaxStragegy(scores);
	}

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
	 * ピアソンの積率相関係数(-1.0 <= corr <= 1.0)を計算します。
	 * Calculates Pearson's product-moment correlation coefficient.
	 * </pre>
	 * 
	 * @param dd1 the first data set as an array of doubles
	 * @param dd2 the second data set as an array of doubles
	 * @return the Pearson's correlation coefficient (-1.0 <= corr <= 1.0)
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
