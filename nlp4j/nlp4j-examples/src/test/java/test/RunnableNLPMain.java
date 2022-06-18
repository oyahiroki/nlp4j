package test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import nlp4j.Document;

public class RunnableNLPMain {

	public static void main(String[] args) throws Exception {

		long time1 = System.currentTimeMillis();

		ExecutorService executor = Executors.newFixedThreadPool(3);

		RunnableNLP p1 = new RunnableNLP("今日はいい天気です");
		RunnableNLP p2 = new RunnableNLP("明日は晴れるかな");
		RunnableNLP p3 = new RunnableNLP("私は歩いて学校に行きます");

		try {
			Future<Document> d1 = executor.submit(p1);
			Future<Document> d2 = executor.submit(p2);
			Future<Document> d3 = executor.submit(p3);

		} finally {
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.MINUTES);
		}

		p1.close();
		p2.close();
		p3.close();

		long time2 = System.currentTimeMillis();

		System.err.println(time2 - time1);
	}

}
