package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RunnableNLPMain {

	public static void main(String[] args) throws Exception {

		long time1 = System.currentTimeMillis();

		RunnableNLP command = new RunnableNLP();

		ExecutorService executor = Executors.newFixedThreadPool(3);
		try {
			executor.execute(command);
			executor.execute(command);
			executor.execute(command);
			executor.execute(command);
			executor.execute(command);
			executor.execute(command);
			executor.execute(command);
			executor.execute(command);
			executor.execute(command);
			executor.execute(command);
		} finally {
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.MINUTES);
		}

		long time2 = System.currentTimeMillis();

		System.err.println(time2 - time1);
	}

}
