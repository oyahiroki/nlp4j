package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;

/**
 * @author Hiroki Oya
 *
 */
public class FileUtils {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @param gzipTextOrPlainTextFile
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	public static Stream<String> stream(File gzipTextOrPlainTextFile) throws IOException {
		// gzipなどに対応
		BufferedReader br = nlp4j.util.FileUtils.openTextFileAsBufferedReader(gzipTextOrPlainTextFile, "UTF-8");
		return nlp4j.util.IOUtils.stream(br);
	}

	public static Stream<String> stream(URL url) throws IOException {
		// gzipなどに対応
		BufferedReader br = nlp4j.util.IOUtils.br(url);
		return nlp4j.util.IOUtils.stream(br);
	}

	/**
	 * Checks if the specified file or its parent directory exists. This method logs
	 * the existence of each parent directory in the file path hierarchy. The check
	 * proceeds up the file hierarchy until an existing file or directory is found.
	 *
	 * @param f The file to check for existence. If the file does not exist, the
	 *          method will check its parent directories.
	 * @since 1.3.7.14
	 * 
	 */
	static public void checExists(File f) throws IOException {
		File p = f;
		boolean found = f.exists();
		while ((p.getParentFile()) != null) {
			boolean exists = p.exists();
			logger.info(p.getAbsolutePath() + " : " + exists);
			if (exists) {
				break;
			}
			p = p.getParentFile();
		}
		if (found == false) {
			FileNotFoundException e = new FileNotFoundException(f.getAbsolutePath());
			throw e;
		}
	}

	/**
	 * <pre>
	 * 指定されたファイル名を持つファイルの存在をチェックします。
	 * ファイルが存在しない場合、FileNotFoundExceptionをスローします。
	 * </pre>
	 * 
	 * @param file チェックするファイル
	 * @throws IOException ファイルが存在しない場合にスローされる例外
	 */
	static public void check(File file) throws IOException {
		checExists(file);
	}

	/**
	 * <pre>
	 * 指定されたファイル名を持つファイルの存在をチェックします。
	 * ファイルが存在しない場合、FileNotFoundExceptionをスローします。
	 * </pre>
	 * 
	 * @param fileName チェックするファイルの名前
	 * @throws IOException ファイルが存在しない場合にスローされる例外
	 */
	static public void check(String fileName) throws IOException {
		File file = new File(fileName);
		checExists(file);
	}

	/**
	 * @param fromFiles
	 * @param toFile
	 * @return the number of bytes copied, or -1 if greater than Integer.MAX_VALUE.
	 * @throws IOException
	 * @since 1.3.7.9
	 */
	static public int concat(List<File> fromFiles, File toFile) throws IOException {
		List<FileInputStream> fiss = new ArrayList<>();
		for (File fromFile : fromFiles) {
			fiss.add(new FileInputStream(fromFile));
		}
		// the number of bytes copied, or -1 if greater than Integer.MAX_VALUE.
		int number_of_bytes_copied = -1;
		try (SequenceInputStream sis = new SequenceInputStream(Collections.enumeration(fiss));) {
			number_of_bytes_copied = IOUtils.copy(sis, new FileOutputStream(toFile));
			return number_of_bytes_copied;
		}
	}

	/**
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @since 1.3.7.16
	 */
	static public boolean exists(String fileName) throws IOException {
		return (new File(fileName)).exists();
	}

	/**
	 * Returns the absolute pathname string of this abstract pathname.
	 * 
	 * @param fileName
	 * @return
	 * @since 1.3.7.16
	 */
	static public String getAbsolutePath(String fileName) {
		return (new File(fileName)).getAbsolutePath();
	}

	/**
	 * @since 1.3.7.12
	 * @param jsonFile
	 * @param csvFile
	 * @throws IOException
	 */
	static public void jsonToCsv(File jsonFile, File csvFile) throws IOException {

		ArrayList<Document> docs = new ArrayList<>();

		// 形態素解析済みの文書を読み込む
		docs.addAll(DocumentUtil.readFromLineSeparatedJson(jsonFile));

		PrintWriter pw = nlp4j.util.IOUtils.printWriter(csvFile);

		DocumentsUtils.printAsCsv(docs, pw);

	}

	/**
	 * Returns line count of a text file
	 * 
	 * @param textFileUTF8 in UTF-8 text file
	 * @return
	 * @throws IOException
	 */
	static public long lineCount(File textFileUTF8) throws IOException {
		long lineCount = 0;
		try (BufferedReader br = new BufferedReader(
//				new FileReader(textFileUTF8, StandardCharsets.UTF_8)
				new InputStreamReader(new FileInputStream(textFileUTF8), StandardCharsets.UTF_8))

		) {
			while (br.readLine() != null) {
				lineCount++;
			}
		}
		return lineCount;
	}

	/**
	 * Returns line count of a text file in GZip
	 * 
	 * @param fileGZipText
	 * @return
	 * @throws IOException
	 */
	static public long lineCountGZipTextFile(File fileGZipText) throws IOException {
		long lineCount = 0;
		try (BufferedReader br = GZIPFileUtils.openGZIPFileAsBufferedReader(fileGZipText)) {
			while (br.readLine() != null) {
				lineCount++;
			}
		}
		return lineCount;
	}

	/**
	 * Returns new PrintWriter of UTF-8
	 * 
	 * @param fileName *.gz | *.txt
	 * @return
	 * @throws IOException
	 * @since 1.3.7.6
	 */
	static public PrintWriter newPrintWriter(String fileName) throws IOException {
		return new PrintWriter(
				new OutputStreamWriter(new FileOutputStream(new File(fileName)), StandardCharsets.UTF_8));
	}

	/**
	 * @param gzipTextOrPlainTextFile
	 * @return
	 * @throws IOException
	 */
	static public BufferedReader openTextFileAsBufferedReader(File gzipTextOrPlainTextFile) throws IOException {
		return openTextFileAsBufferedReader(gzipTextOrPlainTextFile, StandardCharsets.UTF_8);
	}

	/**
	 * Returns BufferedReader of text file (plain or gzip)
	 * 
	 * @param gzipTextOrPlainTextFile plain text file OR gzip text file
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	static public BufferedReader openTextFileAsBufferedReader(File gzipTextOrPlainTextFile, Charset encoding)
			throws IOException {

		if (gzipTextOrPlainTextFile.getName().endsWith(".gz")) {
			return new BufferedReader(
					new InputStreamReader(new GZIPInputStream(new FileInputStream(gzipTextOrPlainTextFile)), encoding));

		} else {
			return new BufferedReader(new InputStreamReader((new FileInputStream(gzipTextOrPlainTextFile)), encoding));

		}

	}

	/**
	 * @param gzipTextOrPlainTextFile *.gz | *.txt
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	static public BufferedReader openTextFileAsBufferedReader(File gzipTextOrPlainTextFile, String encoding)
			throws IOException {
		return openTextFileAsBufferedReader(gzipTextOrPlainTextFile, Charset.forName(encoding));
	}

	/**
	 * @param gzipTextOrPlainTextFileName *.gz | *.txt
	 * @return
	 * @throws IOException
	 * @since 1.3.7.8
	 * 
	 */
	static public BufferedReader openTextFileAsBufferedReader(String gzipTextOrPlainTextFileName) throws IOException {
		return openTextFileAsBufferedReader(new File(gzipTextOrPlainTextFileName), StandardCharsets.UTF_8);
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	static public List<String> read(File file) throws IOException {
		return stream(file).toList();
	}

	/**
	 * @param (gzip file) or (plain text file)
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	static public List<String> readLines(File file) throws IOException {

		return stream(file).toList();

//		List<String> list = new ArrayList<String>();
//		try (BufferedReader br = nlp4j.util.IOUtils.bufferedReader(file)) {
//			String s;
//			while ((s = br.readLine()) != null) {
//				list.add(s);
//			}
//		}
//		return list;
	}

	static public void write(File file, Collection<String> data, String charsetName, boolean append)
			throws IOException {
		StringBuilder sbData = new StringBuilder();
		for (String d : data) {
			sbData.append(d + "\n");
		}
		org.apache.commons.io.FileUtils.write(file, sbData.toString(), charsetName, append);
	}

}
