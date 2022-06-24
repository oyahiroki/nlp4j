package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import nlp4j.Document;

public class RunnableNLPMain {

	public static void main(String[] args) throws Exception {

		long time1 = System.currentTimeMillis();

		List<String> ss = new ArrayList<>();

		List<Document> docs = new ArrayList<>();

		for (int n = 0; n < 1000; n++) {
			ss.add("テスト " + n);
		}

		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Future<Document>> taskList = new ArrayList<Future<Document>>();

		try {
			for (int n = 0; n < ss.size(); n++) {
				String s = ss.get(n);
				{
					RunnableNLP p1 = new RunnableNLP();
					p1.setText(s);

					taskList.add(executor.submit(p1));

					p1.close();
				}

			}

			for (Future<Document> f : taskList) {
				Document doc = f.get();
				docs.add(doc);
			}

		} finally {
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.MINUTES);
		}

		long time2 = System.currentTimeMillis();

		System.err.println(time2 - time1);

		System.err.println(docs.size());
	}

}
