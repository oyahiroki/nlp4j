package nlp4j.counter;

import java.util.List;

import junit.framework.TestCase;

public class CounterTestCase extends TestCase {

	public void testCounter001() {

		String s1 = "aaa";
		String s2 = "bbb";
		String s3 = "ccc";

		Counter<String> counter = new Counter<>();
		counter.add(s1);
		counter.add(s1);
		counter.add(s1);
		counter.add(s2);
		counter.add(s2);
		counter.add(s3);

		assertEquals(3, counter.getCount(s1));
		assertEquals(2, counter.getCount(s2));
		assertEquals(1, counter.getCount(s3));

		counter.getObjectListSorted();

		{
			List<Count<String>> counts = counter.getCountListSorted();
			for (Count<String> c : counts) {
				System.err.println(c);
			}
		}

		{
			List<String> ss = counter.getObjectList();
			for (String s : ss) {
				System.err.println(s);
			}
		}

	}

}
