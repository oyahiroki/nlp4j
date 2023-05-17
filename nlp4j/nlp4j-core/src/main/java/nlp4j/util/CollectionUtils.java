package nlp4j.util;

import java.util.List;

import org.apache.commons.collections4.ListUtils;

public class CollectionUtils {

	public static <T> List<List<T>> partition(List<T> origin, int size) {
		return ListUtils.partition(origin, size);
	}
}
