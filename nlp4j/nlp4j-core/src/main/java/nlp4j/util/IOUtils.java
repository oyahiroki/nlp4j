package nlp4j.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;

import nlp4j.io.DevNullOutputStream;
import nlp4j.io.DevNullPrintWriter;
import nlp4j.io.MultiIOException;
import nlp4j.io.MultiOutputStream;
import nlp4j.io.MultiWriter;
import nlp4j.io.NoCloseWriter;
import nlp4j.tuple.Pair;

/**
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public class IOUtils {

	/**
	 * created on: 2024-08-26
	 * 
	 * @param file
	 * @return BufferedReader
	 * @throws IOException
	 * @since 1.3.7.14
	 */
	static public BufferedReader br(File file) throws IOException {
		return IOUtils.bufferedReader(file);
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	static public BufferedReader br(URL url) throws IOException {
		if (url.toString().endsWith("gz")) {
			BufferedReader br = new BufferedReader( //
					new InputStreamReader( //
							new GZIPInputStream( //
									url.openStream()),
							StandardCharsets.UTF_8));
			return br;

		} else {
			BufferedReader br = new BufferedReader( //
					new InputStreamReader( //
							url.openStream(), StandardCharsets.UTF_8));
			return br;

		}
	}

	/**
	 * created on: 2024-08-26
	 * 
	 * @param (gzip File) or (plain text File)
	 * @return BufferedReader
	 * @throws IOException
	 * @since 1.3.7.14
	 */
	static public BufferedReader bufferedReader(File file) throws IOException {
		if (file.exists() == false) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
		// OPEN AS ZGIP
		if (file.getAbsolutePath().endsWith(".gz")) {
			BufferedReader br = new BufferedReader( //
					new InputStreamReader( //
							new GZIPInputStream( //
									new FileInputStream(file)),
							StandardCharsets.UTF_8));
			return br;
		}
		// OPEN AS PLAIN TEXT
		else {
			BufferedReader br = new BufferedReader( //
					new InputStreamReader( //
							new FileInputStream(file), StandardCharsets.UTF_8));
			return br;
		}
	}

	/**
	 * created on: 2023-10-22
	 * 
	 * @return Empty PrintWriter
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter empty() throws IOException {
		return new DevNullPrintWriter(new DevNullOutputStream());
	}

	/**
	 * Flush writers
	 * 
	 * @param ww
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public void flush(Writer... ww) throws IOException {
		MultiIOException ee = new MultiIOException();
		for (Writer w : ww) {
			try {
				w.flush();
			} catch (IOException e) {
				ee.addException(e);
			}
		}
		if (ee.size() > 0) {
			throw ee;
		}
	}

	/**
	 * Flush and Close writers
	 * 
	 * @param ww
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public void flushClose(Writer... ww) throws IOException {
		MultiIOException ee = new MultiIOException();
		for (Writer w : ww) {
			try {
				w.flush();
				w.close();

			} catch (IOException e) {
				ee.addException(e);
			}
		}
		if (ee.size() > 0) {
			throw ee;
		}
	}

	/**
	 * created on: 2025-03-16
	 * 
	 * @param (gzip File) or (plain text File)
	 * @return InputStream of File
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	static public InputStream inputStream(File file) throws IOException {
		if (file.exists() == false) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
		// OPEN AS ZGIP
		if (file.getAbsolutePath().endsWith(".gz")) {
			InputStream is = new GZIPInputStream(new FileInputStream(file));
			return is;
		}
		// OPEN AS PLAIN TEXT
		else {
			return new FileInputStream(file);
		}
	}

	/**
	 * created on: 2023-10-22
	 * 
	 * @return Empty PrintWriter
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter printWriter() throws IOException {
		return empty();
	}

	/**
	 * @param file
	 * @return PrintWriter (output=file, append=true, autoflush=true, charset=UTF-8)
	 * @throws IOException
	 * @since 1.3.7.9
	 */
	static public PrintWriter printWriter(File file) throws IOException {
		boolean append = true;
		Charset charset = StandardCharsets.UTF_8;
		boolean autoflush = true;
		return printWriter(file, append, charset, autoflush);
	}

	/**
	 * @param file      : directory created if not exists
	 * @param append
	 * @param charset
	 * @param autoflush
	 * @return
	 * @throws IOException
	 * @since 1.3.7.9
	 */
	static public PrintWriter printWriter(File file, boolean append, Charset charset, boolean autoflush)
			throws IOException {

		// file.isFile() はファイルが存在しない場合は false を返す

		// 2023-08-14
		if (file.getParentFile().exists() == false) {
			FileUtils.forceMkdirParent(file);
		}

		return new PrintWriter( //
				new OutputStreamWriter( //
						new FileOutputStream(file, append) //
						, charset) // OutputStreamWriter
				, //
				autoflush); //
	}

	/**
	 * @param outFile
	 * @param ps
	 * @return
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter printWriter(File outFile, PrintStream ps) throws IOException {
		boolean append = true;
		boolean autoflush = true;
		Writer w1 = new PrintWriter(
				new OutputStreamWriter(new FileOutputStream(outFile, append), StandardCharsets.UTF_8), autoflush);
		Writer w2 = new NoCloseWriter(ps);
		Writer w = new MultiWriter(w1, w2);
		PrintWriter pw = new PrintWriter(w);
		return pw;
	}

	/**
	 * @param outputStreams
	 * @return
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter printWriter(OutputStream... outputStreams) throws IOException {
		PrintWriter pw = new PrintWriter(new MultiOutputStream(outputStreams));
		return pw;
	}

	/**
	 * @param fileName
	 * @return PrintWriter (output=file, append=true, charset=UTF-8, autoFlush=true)
	 * @throws IOException
	 * @since 1.3.7.8
	 */
	static public PrintWriter printWriter(String fileName) throws IOException {
		File file = new File(fileName);
		boolean append = true;
		Charset charset = StandardCharsets.UTF_8;
		boolean autoflush = true;
		return printWriter(file, append, charset, autoflush);
	}

	/**
	 * created on: 2023-10-22
	 * 
	 * @return Empty PrintWriter
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter pw() throws IOException {
		return empty();
	}

	/**
	 * @param file
	 * @return PrintWriter (output=file, append=true, autoflush=true, charset=UTF-8)
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter pw(File file) throws IOException {
		return printWriter(file);
	}

	/**
	 * @param outFile
	 * @param ps
	 * @return
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter pw(File outFile, PrintStream ps) throws IOException {
		return printWriter(outFile, ps);
	}

	/**
	 * @param outputStreams
	 * @return
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter pw(OutputStream... outputStreams) throws IOException {
		return printWriter(outputStreams);
	}

	/**
	 * Short name of {@link IOUtils#printWriter(String) }
	 * 
	 * @param fileName of print writer
	 * @return
	 * @throws IOException
	 * @since 1.3.7.9
	 */
	static public PrintWriter pw(String fileName) throws IOException {
		return printWriter(fileName);
	}

	/**
	 * @return System Error
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter pwSystemErr() {
		return new PrintWriter(System.err, true);
	}

	/**
	 * @return System Out
	 * @throws IOException
	 * @since 1.3.7.12
	 */
	static public PrintWriter pwSystemOut() {
		return new PrintWriter(System.out, true);
	}

	/**
	 * @return
	 * @throws IOException
	 * @since 1.3.7.15
	 */
	static public Pair<PrintWriter, File> pwTemp() throws IOException {
		return pwTemp("nlp4j-", ".txt");
	}

	/**
	 * @param prefix
	 * @param suffix
	 * @return
	 * @throws IOException
	 * @since 1.3.7.15
	 */
	static public Pair<PrintWriter, File> pwTemp(String prefix, String suffix) throws IOException {
		File tempFile = File.createTempFile(prefix, suffix);
		PrintWriter pw = pw(tempFile);
		return new Pair<PrintWriter, File>(pw, tempFile);
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
	 * @param url
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	static public List<String> read(URL url) throws IOException {

		return stream(url).toList();
	}

	/**
	 * @param br
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	static public Stream<String> stream(BufferedReader br) throws IOException {
		return br.lines().map(line -> {
			try {
				return line;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).onClose(() -> {
			try {
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	static public Stream<String> stream(File file) throws IOException {
		BufferedReader br = br(file);
		return stream(br);
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * @since 1.3.7.18
	 */
	static public Stream<String> stream(URL url) throws IOException {
		BufferedReader br = br(url);
		return stream(br);
	}

}
