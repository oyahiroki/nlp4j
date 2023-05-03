package nlp4j.counter;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordBuilder;

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

}
