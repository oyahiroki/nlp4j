package examples;

import java.io.File;
import java.net.URL;
import java.util.List;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

public class Example010_ReadFileAsDocument {

	public static void main(String[] args) throws Exception {

		{ // 同期読み込み DocumentUtil.read()
			{ // Plain Text JSONLines
				File file = new File("src/test/resources/examples/example.jsonl");
				List<Document> docs = DocumentUtil.read(file);
				print(docs);
			}
			System.out.println("--------");
			{ // GZip JSON Lines
				File file = new File("src/test/resources/examples/example.jsonl.gz");
				List<Document> docs = DocumentUtil.read(file);
				print(docs);
			}
			System.out.println("--------");
			{ // Plain Text CSV
				System.out.println("src/test/resources/examples/example_utf8bom.csv");
				File file = new File("src/test/resources/examples/example_utf8bom.csv");
				List<Document> docs = DocumentUtil.read(file);
				print(docs);
			}
			System.out.println("--------");
			{ // Plain Text CSV
				System.out.println("src/test/resources/examples/example_utf8bom.csv.gz");
				File file = new File("src/test/resources/examples/example_utf8bom.csv.gz");
				List<Document> docs = DocumentUtil.read(file);
				print(docs);
			}
			System.out.println("--------");
		}

		{ // 非同期読み込み DocumentUtil.stream()
			{ // Plain Text JsonLines
				File file = new File("src/test/resources/examples/example.jsonl");
				DocumentUtil.stream(file).forEach(d -> {
					System.out.println(d.toString());
				});

			}
			System.out.println("--------");
			{ // GZip JSON Lines
				File file = new File("src/test/resources/examples/example.jsonl.gz");
				DocumentUtil.stream(file).forEach(d -> {
					System.out.println(d.toString());
				});

			}
			System.out.println("--------");
			{ // CSV
				File file = new File("src/test/resources/examples/example_utf8bom.csv");
				DocumentUtil.stream(file).forEach(d -> {
					System.out.println(d.toString());
				});

			}
			System.out.println("--------");
			{ // Gzip CSV
				File file = new File("src/test/resources/examples/example_utf8bom.csv.gz");
				DocumentUtil.stream(file).forEach(d -> {
					System.out.println(d.toString());
				});

			}

			System.out.println("--------");
			{ // URL of JSON
				System.out.println("https://nlp4j.sakura.ne.jp/test/example.jsonl");
				URL url = new URL("https://nlp4j.sakura.ne.jp/test/example.jsonl");
				DocumentUtil.stream(url).forEach(d -> {
					System.out.println(d.toString());
				});
			}
			System.out.println("--------");
			{ // URL of JSON
				System.out.println("https://nlp4j.sakura.ne.jp/test/example.jsonl.gz");
				URL url = new URL("https://nlp4j.sakura.ne.jp/test/example.jsonl.gz");
				DocumentUtil.stream(url).forEach(d -> {
					System.out.println(d.toString());
				});
			}
			System.out.println("--------");
			{ // URL of CSV
				System.out.println("https://nlp4j.sakura.ne.jp/test/example_utf8bom.csv");
				URL url = new URL("https://nlp4j.sakura.ne.jp/test/example_utf8bom.csv");
				DocumentUtil.stream(url).forEach(d -> {
					System.out.println(d.toString());
				});
			}
			System.out.println("--------");
			{ // URL of CSV Gzip
				System.out.println("https://nlp4j.sakura.ne.jp/test/example_utf8bom.csv.gz");
				URL url = new URL("https://nlp4j.sakura.ne.jp/test/example_utf8bom.csv.gz");
				DocumentUtil.stream(url).forEach(d -> {
					System.out.println(d.toString());
				});
			}
			System.out.println("--------");

		}

	}

	private static void print(List<Document> docs) {
		for (Document doc : docs) {
			System.out.println(DocumentUtil.toJsonPrettyString(doc));
		}
	}

}
