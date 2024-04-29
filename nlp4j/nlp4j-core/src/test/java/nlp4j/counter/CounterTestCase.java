package nlp4j.counter;

import java.io.PrintWriter;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordBuilder;
import nlp4j.util.IOUtils;

public class CounterTestCase extends TestCase {

	public void testAddAllT001() {
		Counter<String> counter1 = new Counter<>();
		counter1.add("aaa", 1);
		counter1.add("bbb", 1);

		Counter<String> counter2 = new Counter<>();
		counter2.add("aaa", 2);

		counter1.addAll(counter2);

		{
			int count = counter1.getCount("aaa");
			int expected = 3;
			assertEquals(expected, count);
		}
		{
			int count = counter1.getCount("bbb");
			int expected = 1;
			assertEquals(expected, count);
		}
	}

	public void testAddT001() {
		Counter<String> counter = new Counter<>();
		counter.add("aaa");

		int count = counter.getCount("aaa");

		int expected = 1;
		assertEquals(expected, count);
	}

	public void testAddT002() {
		Counter<String> counter = new Counter<>();
		counter.add("aaa", 3);

		int count = counter.getCount("aaa");

		int expected = 3;
		assertEquals(expected, count);
	}

	public void testGetCountAll001() {
		Counter<String> counter = new Counter<>();
		counter.add("aaa", 3);
		counter.add("bbb", 3);

		int count = counter.getCountAll();
		int expected = 6;
		assertEquals(expected, count);
	}

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

	public void testCounter002() {

		String s1 = "202001";
		String s2 = "202002";
		String s3 = "202012";

		Counter<String> counter = new Counter<>();
		counter.add(s1);
		counter.add(s2);
		counter.add(s3);

		System.err.println(counter.getObjectList());
	}

	public void testCounter003() {

		int a = 1;
		int b = 2;
		int c = 3;

		Counter<Integer> counter = new Counter<>();
		{
			counter.add(b);
			counter.add(b);
			counter.add(b);
			counter.add(b);
			counter.add(b);
		}
		{
			counter.add(a);
			counter.add(a);
		}
		{
			counter.add(c);
			counter.add(c);
			counter.add(c);
		}
		System.err.println("---");
		for (Count<Integer> ct : counter.getCountList()) {
			System.err.println(ct);
		}
		System.err.println("---");
		for (Count<Integer> ct : counter.getCountListSorted()) {
			System.err.println(ct);
		}
		System.err.println("---");
		System.err.println(counter.getObjectList());
	}

	public void testCounter004() {
		Counter<Keyword> counter = new Counter<>();
		Keyword kwd1 = (new KeywordBuilder()).facet("noun").lex("test").build();
		Keyword kwd2 = (new KeywordBuilder()).facet("noun").lex("test").build();
		Keyword kwd3 = (new KeywordBuilder()).facet("noun").lex("test1").build();
		counter.add(kwd1);
		counter.add(kwd2);
		counter.add(kwd3);
		for (Count<Keyword> ct : counter.getCountListSorted()) {
			System.err.println(ct);
		}
	}

	public void testPrint001() {
		String s1 = "aaa";
		String s2 = "bbb";
		String s3 = "ccc";

		PrintWriter pw = IOUtils.pwSystemErr();

		Counter<String> counter = new Counter<>();
		counter.add(s1); // 1
		counter.add(s1); // 2
		counter.add(s1); // 3
		counter.add(s2); // 1
		counter.add(s2); // 2
		counter.add(s3); // 1

		counter.print(pw);
	}

	public void testPrintValues001() {
		String s1 = "aaa";
		String s2 = "bbb";
		String s3 = "ccc";

		Counter<String> counter = new Counter<>();
		counter.add(s1); // 1
		counter.add(s1); // 2
		counter.add(s1); // 3
		counter.add(s2); // 1
		counter.add(s2); // 2
		counter.add(s3); // 1

		counter.printValues(",");
	}

	public void testTop001() {
		Counter<String> counter = new Counter<>();
		counter.add("aaa");
		counter.add("bbb");
		counter.add("bbb");
		counter.add("bbb");
		counter.add("ccc");
		counter.add("ccc");

		List<Count<String>> list = counter.top(1);

		for (Count<String> v : list) {
			System.err.println(v);
		}

		int expected_size = 1;
		String expected_value = "bbb";
		int expected_count = 3;

		assertEquals(expected_size, list.size());
		assertEquals(expected_value, list.get(0).getValue());
		assertEquals(expected_count, list.get(0).getCount());

	}

	public void testTop002() {
		Counter<String> counter = new Counter<>();
		counter.add("aaa");
		counter.add("bbb");
		counter.add("bbb");
		counter.add("bbb");
		counter.add("ccc");
		counter.add("ccc");

		List<Count<String>> list = counter.top(5);

		for (Count<String> v : list) {
			System.err.println(v);
		}

		int expected_size = 3;
		String expected_value = "bbb";
		int expected_count = 3;

		assertEquals(expected_size, list.size());
		assertEquals(expected_value, list.get(0).getValue());
		assertEquals(expected_count, list.get(0).getCount());

	}

	public void testTop003() {
		Counter<String> counter = new Counter<>();
		counter.add("aaa");
		counter.add("bbb");
		counter.add("bbb");
		counter.add("bbb");
		counter.add("ccc");
		counter.add("ccc");

		List<Count<String>> list = counter.top(0);

		for (Count<String> v : list) {
			System.err.println(v);
		}

		int expected_size = 0;
//		String expected_value = "bbb";
//		int expected_count = 3;

		assertEquals(expected_size, list.size());
//		assertEquals(expected_value, list.get(0).getValue());
//		assertEquals(expected_count, list.get(0).getCount());

	}

	public void testToString001() {
		String s1 = "aaa";
		String s2 = "bbb";
		String s3 = "ccc";

		Counter<String> counter = new Counter<>();
		counter.add(s1); // 1
		counter.add(s1); // 2
		counter.add(s1); // 3
		counter.add(s2); // 1
		counter.add(s2); // 2
		counter.add(s3); // 1

		System.out.println(counter.toString());
	}

}
