package nlp4j.wiki.pageview;

import junit.framework.TestCase;
import nlp4j.counter.Count;
import nlp4j.counter.Counter;

public class PageViewCounterTestCase extends TestCase {

	public void testGet001() throws Exception {
		PageViewCounter ppc = new PageViewCounter();

		String url = "https://dumps.wikimedia.org/other/pageviews/2024/2024-03/pageviews-20240317-050000.gz";
		String domain = "ja";

		Counter<String> count = ppc.get(url, domain);

		System.out.println(count.getCountListSorted().get(0));
		System.out.println(count.getCount("メインページ"));
	}

	public void testGet002() throws Exception {

		String url1 = "https://dumps.wikimedia.org/other/pageviews/2024/2024-03/pageviews-20240317-050000.gz";
		String url0 = "https://dumps.wikimedia.org/other/pageviews/2024/2024-03/pageviews-20240317-040000.gz";
		String domain = "ja";

		PageViewCounter ppc1 = new PageViewCounter();
		PageViewCounter ppc0 = new PageViewCounter();
		System.err.println("Dowonloading : " + url1);
		Counter<String> count1 = ppc1.get(url1, domain);
		System.err.println("Dowonloading : " + url0);
		Counter<String> count0 = ppc0.get(url0, domain);

		System.out.println(count1.getCountListSorted().get(0));
		System.out.println(count1.getCount("メインページ"));
		System.out.println(count0.getCountListSorted().get(0));
		System.out.println(count0.getCount("メインページ"));

		int count = 0;

		for (Count<String> c : count1.getCountListSorted()) {

//			System.out.println(c);
			{
				int c0 = count0.getCount(c.getValue());
//				System.out.println(c0);
				int diff = c.getCount() - c0;
				System.out.println(
						c.getValue() + "," + c.getCount() + "," + c0 + ",(" + ((diff > 0) ? "+" : "") + diff + ")");
			}

			count++;
			if (count > 10) {
				break;
			}
		}

	}

}
