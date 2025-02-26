package nlp4j.util;

import java.util.List;

import org.apache.commons.collections4.ListUtils;

/**
 * Created on: 2023-5-13
 */
public class CollectionUtils {

	/**
	 * Partitions a list into consecutive sublists of the specified size.
	 * 
	 * @param origin the original list to be partitioned
	 * @param size   the desired size of each sublist
	 * @param <T>    the type of elements in the list
	 * @return a list of sublists, each with a length up to the specified size
	 */
	public static <T> List<List<T>> partition(List<T> origin, int size) {
		return ListUtils.partition(origin, size);
	}

	/**
	 * <pre>
	 * Converts a list of Double objects into an array of primitive doubles.
	 * Created on: 2025-2-26
	 * </pre>
	 * 
	 * @param dd1 the list of Double objects to be converted
	 * @return an array containing the primitive double values of the list
	 */
	static public double[] toArray(List<Double> dd1) {
		return dd1.stream().mapToDouble(Double::doubleValue).toArray();
	}
}
