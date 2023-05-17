package nlp4j.util;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class CollectionUtilsTestCase extends TestCase {

	public void testPartition001() {

		List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");

		List<List<String>> list2 = CollectionUtils.partition(list, 2);

		for (List<String> ss : list2) {
			System.err.println(ss.size());
		}

	}

}
