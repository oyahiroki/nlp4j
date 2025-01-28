package nlp4j.converter;

import java.io.File;

public class Csv2Jsonl_Main {

	public static void main(String[] args) throws Exception {

		if (args == null || args.length != 2) {
			System.err.println("args[0]: INPUT CSV file");
			System.err.println("args[1]: OUTPUT JSON LINE file");
			return;
		}

		File file1 = new File(args[0]);
		File file2 = new File(args[1]);

		Csv2Jsonl.convert(file1, file2);

	}

}
