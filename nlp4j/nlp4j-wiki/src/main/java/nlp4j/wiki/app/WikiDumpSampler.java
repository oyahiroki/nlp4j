package nlp4j.wiki.app;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

import nlp4j.util.ArgUtils;
import nlp4j.util.IOUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;

public class WikiDumpSampler {

	/**
	 * <pre>
	 * version 1.0: Initial
	 * version 1.1: Filter function
	 * </pre>
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Map<String, String> options = ArgUtils.parseArgs(args);

		// 必須チェック
		if (!options.containsKey("input") || !options.containsKey("count")) {
			printUsage();
			System.exit(1);
		}

		// 値取得
		File inputFile = new File(options.get("input"));
		int count = Integer.parseInt(options.get("count"));
		String format = options.getOrDefault("format", "jsonl");
		String output = options.get("output");
//		String seed = options.get("seed");
//		String lang = options.get("lang");
		String filter = options.get("filter");

		// ===== ダミー処理 =====
		System.out.println("Input  : " + inputFile);
		System.out.println("Count  : " + count);
//		System.out.println("Format : " + format);
		System.out.println("Output : " + output);
//		System.out.println("Seed   : " + seed);
//		System.out.println("Lang   : " + lang);
		System.out.println("Filter : " + filter);

		// TODO: 本処理

		// Dump File

		try ( //
				PrintWriter pw = (output == null) ? IOUtils.pwSystemOut() : IOUtils.printWriter(new File(output)); //
				WikiDumpReader reader = new WikiDumpReader(inputFile);//
		) {

			WikiPageHandlerWithPrintWriter handler = new WikiPageHandlerWithPrintWriter(pw, count,filter);

			reader.read(handler);
		} catch (BreakException e) {
			System.err.println("Message: " + e.getMessage());
			System.err.println("OK");
		}

	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("  java -jar wikidump-sampler.jar --input <file> --count <number> [options]");
		System.out.println();
		System.out.println("Options:");
//		System.out.println("  --format <jsonl|csv>   Output format (default: jsonl)");
		System.out.println("  --output <file>        Output file (default: stdout)");
//		System.out.println("  --seed <number>        Random seed");
//		System.out.println("  --lang <code>          Language (e.g., ja, en)");
		System.out.println("  --filter <expr>        Filter condition");
	}

}
