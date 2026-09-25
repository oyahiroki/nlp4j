/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
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
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
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
 * Utility class providing factory methods for common I/O operations.
 *
 * <p>
 * All methods are static. The class covers three main areas:
 * </p>
 * <ul>
 * <li><b>Reading</b> — {@link #bufferedReader(File)}, {@link #br(File)},
 * {@link #stream(File)}, {@link #read(File)} open plain-text, gzip
 * ({@code .gz}), and 7-Zip ({@code .7z}) files transparently.</li>
 * <li><b>Writing</b> — {@link #printWriter(File)}, {@link #pw(File)} and their
 * overloads create append-mode, UTF-8, auto-flush {@link PrintWriter}
 * instances, with optional fan-out to a {@link PrintStream} or multiple
 * {@link OutputStream}s.</li>
 * <li><b>Resource management</b> — {@link #flush(Writer...)},
 * {@link #flushClose(Writer...)} operate on multiple writers at once,
 * collecting all {@link IOException}s into a single {@link MultiIOException}
 * rather than stopping at the first failure.</li>
 * </ul>
 *
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public class IOUtils {

	private IOUtils() {
	}

	/**
	 * Short alias for {@link #bufferedReader(File)}.
	 *
	 * @param file the file to open; must not be {@code null}
	 * @return a {@link BufferedReader} over the file contents
	 * @throws IOException if the file does not exist or cannot be read
	 * @since 1.3.7.14
	 */
	static public BufferedReader br(File file) throws IOException {
		return IOUtils.bufferedReader(file);
	}

	/**
	 * Opens a {@link BufferedReader} over a URL, transparently decompressing gzip
	 * content when the URL path ends with {@code .gz}. The reader uses
	 * {@link StandardCharsets#UTF_8}.
	 *
	 * @param url the URL to open; must not be {@code null}
	 * @return a {@link BufferedReader} over the URL contents
	 * @throws IOException if the URL cannot be opened or the stream is not valid
	 *                     gzip data (when applicable)
	 * @since 1.3.7.18
	 */
	static public BufferedReader br(URL url) throws IOException {
		if (url.getPath().endsWith(".gz")) {
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
	 * Opens a {@link BufferedReader} over {@code file} using the named charset.
	 * Delegates to {@link #bufferedReader(File, Charset)}. The file format is
	 * detected automatically by extension ({@code .gz}, {@code .7z}, or plain
	 * text).
	 *
	 * @param file    the file to open; must not be {@code null}
	 * @param charset IANA charset name (e.g. {@code "UTF-8"}, {@code "Shift_JIS"})
	 * @return a {@link BufferedReader} decoded with the specified charset
	 * @throws IOException if the file does not exist or cannot be read
	 * @since 1.3.7.19
	 */
	static public BufferedReader bufferedReader(File file, String charset) throws IOException {
		return bufferedReader(file, Charset.forName(charset));
	}

	/**
	 * Opens a {@link BufferedReader} over {@code file} using the given charset,
	 * automatically detecting the file format by extension:
	 * <ul>
	 * <li>{@code .gz} — decompressed with {@link GZIPInputStream}</li>
	 * <li>{@code .7z} — first entry extracted via {@code SevenZUtils}</li>
	 * <li>otherwise — opened as a plain text file</li>
	 * </ul>
	 *
	 * @param file    the file to open; must not be {@code null}
	 * @param charset the character encoding to use for decoding
	 * @return a {@link BufferedReader} over the (possibly decompressed) content
	 * @throws FileNotFoundException if {@code file} does not exist
	 * @throws IOException           if the file cannot be read or decompressed
	 * @since 1.3.7.19
	 */
	static public BufferedReader bufferedReader(File file, Charset charset) throws IOException {

		if (file.exists() == false) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
		// OPEN AS GZIP
		if (file.getAbsolutePath().endsWith(".gz")) {
			FileInputStream fis = new FileInputStream(file);
			try {
				BufferedReader br = new BufferedReader( //
						new InputStreamReader( //
								new GZIPInputStream(fis), charset));
				return br;
			} catch (IOException e) {
				fis.close();
				throw e;
			}
		}
		// OPEN AS 7z
		else if (file.getAbsolutePath().endsWith(".7z")) {
			// SevenZUtils.readAsInputStream() opens its own SevenZFile internally
			// and closes it when the returned InputStream is closed.
			InputStream is = SevenZUtils.readAsInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
			return reader;
		}
		// OPEN AS PLAIN TEXT
		else {
			BufferedReader br = new BufferedReader( //
					new InputStreamReader( //
							new FileInputStream(file), charset));
			return br;
		}
	}

	/**
	 * Opens a {@link BufferedReader} over {@code file} using UTF-8 encoding,
	 * automatically detecting the file format by extension. Equivalent to
	 * {@code bufferedReader(file, StandardCharsets.UTF_8)}.
	 *
	 * @param file a plain-text, gzip ({@code .gz}), or 7-Zip ({@code .7z}) file;
	 *             must not be {@code null}
	 * @return a UTF-8 {@link BufferedReader} over the file contents
	 * @throws FileNotFoundException if {@code file} does not exist
	 * @throws IOException           if the file cannot be read or decompressed
	 * @since 1.3.7.14
	 */
	static public BufferedReader bufferedReader(File file) throws IOException {
		return bufferedReader(file, StandardCharsets.UTF_8);
	}

	/**
	 * Returns a {@link PrintWriter} that silently discards all output (backed by
	 * {@link DevNullOutputStream}). Useful as a no-op placeholder where a
	 * {@link PrintWriter} is required.
	 *
	 * @return a discard-all {@link PrintWriter}; never {@code null}
	 * @throws IOException never thrown; declared for API consistency
	 * @since 1.3.7.12
	 */
	static public PrintWriter empty() throws IOException {
		return new DevNullPrintWriter(new DevNullOutputStream());
	}

	/**
	 * Flushes all supplied writers, collecting any {@link IOException}s. Every
	 * writer is attempted regardless of previous failures; all exceptions are
	 * aggregated and thrown together as a {@link MultiIOException} at the end.
	 *
	 * @param ww the writers to flush; must not be {@code null}
	 * @throws MultiIOException if one or more writers throw during flush
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
	 * Flushes and then closes all supplied writers. Both operations are attempted
	 * on every writer regardless of earlier failures; in particular,
	 * {@code close()} is always called even when {@code flush()} throws. All
	 * exceptions are aggregated and thrown together as a {@link MultiIOException}
	 * at the end.
	 *
	 * @param ww the writers to flush and close; must not be {@code null}
	 * @throws MultiIOException if one or more writers throw during flush or close
	 * @since 1.3.7.12
	 */
	static public void flushClose(Writer... ww) throws IOException {
		MultiIOException ee = new MultiIOException();
		for (Writer w : ww) {
			try {
				w.flush();
			} catch (IOException e) {
				ee.addException(e);
			}
			try {
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
	 * Opens {@code file} as a raw {@link InputStream}, automatically detecting the
	 * file format by extension:
	 * <ul>
	 * <li>{@code .gz} — wrapped in {@link GZIPInputStream}</li>
	 * <li>{@code .7z} — first entry extracted via {@code SevenZUtils}</li>
	 * <li>otherwise — plain {@link FileInputStream}</li>
	 * </ul>
	 *
	 * @param file a plain-text, gzip ({@code .gz}), or 7-Zip ({@code .7z}) file;
	 *             must not be {@code null}
	 * @return an {@link InputStream} over the (possibly decompressed) bytes
	 * @throws FileNotFoundException if {@code file} does not exist
	 * @throws IOException           if the file cannot be opened or decompressed
	 * @since 1.3.7.18
	 */
	static public InputStream inputStream(File file) throws IOException {
		if (file.exists() == false) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
		// OPEN AS GZIP
		if (file.getAbsolutePath().endsWith(".gz")) {
			FileInputStream fis = new FileInputStream(file);
			try {
				return new GZIPInputStream(fis);
			} catch (IOException e) {
				fis.close();
				throw e;
			}
		}
		// OPEN AS 7Z
		else if (file.getAbsolutePath().endsWith(".7z")) {
			InputStream is = SevenZUtils.readAsInputStream(file);
			return is;
		}
		// OPEN AS PLAIN TEXT
		else {
			return new FileInputStream(file);
		}
	}

	/**
	 * Returns a {@link PrintWriter} that silently discards all output. Equivalent
	 * to {@link #empty()}.
	 *
	 * @return a discard-all {@link PrintWriter}; never {@code null}
	 * @throws IOException never thrown; declared for API consistency
	 * @since 1.3.7.12
	 */
	static public PrintWriter printWriter() throws IOException {
		return empty();
	}

	/**
	 * Opens {@code file} for writing in append mode with UTF-8 encoding and
	 * auto-flush enabled. The parent directory is created if it does not exist.
	 * Equivalent to {@code printWriter(file, true, UTF_8, true)}.
	 *
	 * @param file the output file; must not be {@code null}
	 * @return a {@link PrintWriter} with append=true, autoflush=true, charset=UTF-8
	 * @throws IOException if the file cannot be opened or the parent directory
	 *                     cannot be created
	 * @since 1.3.7.9
	 */
	static public PrintWriter printWriter(File file) throws IOException {
		boolean append = true;
		Charset charset = StandardCharsets.UTF_8;
		boolean autoflush = true;
		return printWriter(file, append, charset, autoflush);
	}

	/**
	 * Opens {@code file} for writing with full control over append mode, character
	 * encoding, and auto-flush behaviour. The parent directory is created
	 * automatically if it does not exist.
	 *
	 * @param file      the output file; must not be {@code null}
	 * @param append    {@code true} to append to an existing file, {@code false} to
	 *                  overwrite
	 * @param charset   the character encoding to use
	 * @param autoflush {@code true} to flush the stream after every
	 *                  {@code println}, {@code printf}, or {@code format} call
	 * @return a configured {@link PrintWriter}
	 * @throws IOException if the file cannot be opened or the parent directory
	 *                     cannot be created
	 * @since 1.3.7.9
	 */
	static public PrintWriter printWriter(File file, boolean append, Charset charset, boolean autoflush)
			throws IOException {

		// file.isFile() はファイルが存在しない場合は false を返す

		// 2023-08-14
		if (file.getParentFile() != null && file.getParentFile().exists() == false) {
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
	 * Opens {@code outFile} for writing (append mode, UTF-8, auto-flush) and
	 * simultaneously fans all output to {@code ps}. The parent directory is created
	 * if it does not exist.
	 *
	 * <p>
	 * Closing the returned {@link PrintWriter} closes the file writer but does
	 * <em>not</em> close {@code ps} (it is wrapped in a {@link NoCloseWriter}).
	 * </p>
	 *
	 * @param outFile the output file; must not be {@code null}
	 * @param ps      the {@link PrintStream} to mirror output to (e.g.
	 *                {@link System#err}); must not be {@code null}
	 * @return a {@link PrintWriter} that writes to both {@code outFile} and
	 *         {@code ps}
	 * @throws IOException if the file cannot be opened or the parent directory
	 *                     cannot be created
	 * @since 1.3.7.12
	 */
	static public PrintWriter printWriter(File outFile, PrintStream ps) throws IOException {
		boolean append = true;
		boolean autoflush = true;
		if (outFile.getParentFile() != null && outFile.getParentFile().exists() == false) {
			FileUtils.forceMkdirParent(outFile);
		}
		Writer w1 = new PrintWriter(
				new OutputStreamWriter(new FileOutputStream(outFile, append), StandardCharsets.UTF_8), autoflush);
		Writer w2 = new NoCloseWriter(ps);
		Writer w = new MultiWriter(w1, w2);
		PrintWriter pw = new PrintWriter(w);
		return pw;
	}

	/**
	 * Creates a {@link PrintWriter} that fans output to all supplied
	 * {@link OutputStream}s via a {@link MultiOutputStream}.
	 *
	 * @param outputStreams one or more target streams; must not be {@code null}
	 * @return a {@link PrintWriter} writing to all supplied streams
	 * @throws IOException never thrown; declared for API consistency
	 * @since 1.3.7.12
	 */
	static public PrintWriter printWriter(OutputStream... outputStreams) throws IOException {
		PrintWriter pw = new PrintWriter(new MultiOutputStream(outputStreams));
		return pw;
	}

	/**
	 * Opens the file identified by {@code fileName} for writing in append mode with
	 * UTF-8 encoding and auto-flush enabled. Equivalent to
	 * {@code printWriter(new File(fileName), true, UTF_8, true)}.
	 *
	 * @param fileName path of the output file; must not be {@code null}
	 * @return a {@link PrintWriter} with append=true, charset=UTF-8, autoFlush=true
	 * @throws IOException if the file cannot be opened or the parent directory
	 *                     cannot be created
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
	 * Short alias for {@link #empty()}. Returns a {@link PrintWriter} that silently
	 * discards all output.
	 *
	 * @return a discard-all {@link PrintWriter}; never {@code null}
	 * @throws IOException never thrown; declared for API consistency
	 * @since 1.3.7.12
	 */
	static public PrintWriter pw() throws IOException {
		return empty();
	}

	/**
	 * Short alias for {@link #printWriter(File)}. Opens {@code file} in append mode
	 * with UTF-8 and auto-flush.
	 *
	 * @param file the output file; must not be {@code null}
	 * @return a {@link PrintWriter} with append=true, autoflush=true, charset=UTF-8
	 * @throws IOException if the file cannot be opened
	 * @since 1.3.7.12
	 */
	static public PrintWriter pw(File file) throws IOException {
		return printWriter(file);
	}

	/**
	 * Short alias for {@link #printWriter(File, PrintStream)}. Opens
	 * {@code outFile} and mirrors all output to {@code ps}.
	 *
	 * @param outFile the output file; must not be {@code null}
	 * @param ps      the {@link PrintStream} to mirror output to; must not be
	 *                {@code null}
	 * @return a {@link PrintWriter} that writes to both {@code outFile} and
	 *         {@code ps}
	 * @throws IOException if the file cannot be opened
	 * @since 1.3.7.12
	 */
	static public PrintWriter pw(File outFile, PrintStream ps) throws IOException {
		return printWriter(outFile, ps);
	}

	/**
	 * Short alias for {@link #printWriter(OutputStream...)}. Creates a
	 * {@link PrintWriter} that fans output to all supplied streams.
	 *
	 * @param outputStreams one or more target streams; must not be {@code null}
	 * @return a {@link PrintWriter} writing to all supplied streams
	 * @throws IOException never thrown; declared for API consistency
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
	 * Returns an auto-flush {@link PrintWriter} backed by {@link System#err}. The
	 * returned writer does <em>not</em> own {@code System.err}; closing it will not
	 * close the underlying stream.
	 *
	 * @return a {@link PrintWriter} wrapping {@link System#err}
	 * @since 1.3.7.12
	 */
	static public PrintWriter pwSystemErr() {
		return new PrintWriter(new NoCloseWriter(System.err), true);
	}

	/**
	 * Returns an auto-flush {@link PrintWriter} backed by {@link System#out}. The
	 * returned writer does <em>not</em> own {@code System.out}; closing it will not
	 * close the underlying stream.
	 *
	 * @return a {@link PrintWriter} wrapping {@link System#out}
	 * @since 1.3.7.12
	 */
	static public PrintWriter pwSystemOut() {
		return new PrintWriter(new NoCloseWriter(System.out), true);
	}

	/**
	 * Creates a temporary file with the prefix {@code "nlp4j-"} and suffix
	 * {@code ".txt"} and returns a {@link PrintWriter} open on it together with the
	 * {@link File} handle. Equivalent to {@code pwTemp("nlp4j-", ".txt")}.
	 *
	 * @return a {@link Pair} of ({@link PrintWriter}, {@link File}) for the
	 *         temporary file; never {@code null}
	 * @throws IOException if the temporary file cannot be created
	 * @since 1.3.7.15
	 */
	static public Pair<PrintWriter, File> pwTemp() throws IOException {
		return pwTemp("nlp4j-", ".txt");
	}

	/**
	 * Creates a temporary file with the given {@code prefix} and {@code suffix} and
	 * returns a {@link PrintWriter} open on it together with the {@link File}
	 * handle.
	 *
	 * @param prefix the prefix string for the temporary file name; must have at
	 *               least three characters
	 * @param suffix the suffix string for the temporary file name (e.g.
	 *               {@code ".txt"})
	 * @return a {@link Pair} of ({@link PrintWriter}, {@link File}); never
	 *         {@code null}
	 * @throws IOException if the temporary file cannot be created
	 * @since 1.3.7.15
	 */
	static public Pair<PrintWriter, File> pwTemp(String prefix, String suffix) throws IOException {
		File tempFile = File.createTempFile(prefix, suffix);
		PrintWriter pw = pw(tempFile);
		return new Pair<PrintWriter, File>(pw, tempFile);
	}

	/**
	 * Creates a temporary file (prefix {@code "nlp4j-ioutils-temp-"}, suffix
	 * {@code ".txt"}) and returns a {@link PrintWriter} that fans all output to
	 * both the file and {@link System#err}. Equivalent to
	 * {@code pwSysErrTemp("nlp4j-ioutils-temp-", ".txt")}.
	 *
	 * @return a {@link Pair} of ({@link PrintWriter}, {@link File}); never
	 *         {@code null}
	 * @throws IOException if the temporary file cannot be created
	 * @since 1.3.7.19
	 */
	static public Pair<PrintWriter, File> pwSysErrTemp() throws IOException {
		String prefix = "nlp4j-ioutils-temp-";
		String suffix = ".txt";
		return pwSysErrTemp(prefix, suffix);
	}

	/**
	 * Creates a temporary file with the given {@code prefix} and {@code suffix} and
	 * returns a {@link PrintWriter} that fans all output to both the file and
	 * {@link System#err}.
	 *
	 * @param prefix the prefix string for the temporary file name
	 * @param suffix the suffix string for the temporary file name
	 * @return a {@link Pair} of ({@link PrintWriter}, {@link File}); never
	 *         {@code null}
	 * @throws IOException if the temporary file cannot be created
	 * @since 1.3.7.19
	 */
	static public Pair<PrintWriter, File> pwSysErrTemp(String prefix, String suffix) throws IOException {
		File tempFile = File.createTempFile(prefix, suffix);
		PrintWriter pw = printWriter(tempFile, System.err);
		return new Pair<PrintWriter, File>(pw, tempFile);
	}

	/**
	 * Reads all lines of {@code file} into a {@link List}, then closes the
	 * underlying reader. The file format is detected automatically (plain, gzip, or
	 * 7-Zip). Uses UTF-8 encoding.
	 *
	 * <p>
	 * For large files prefer {@link #stream(File)} to avoid loading the entire
	 * content into memory.
	 * </p>
	 *
	 * @param file the file to read; must not be {@code null}
	 * @return an list of lines; never {@code null}
	 * @throws IOException if the file cannot be read
	 * @since 1.3.7.18
	 */
	static public List<String> read(File file) throws IOException {
		try (Stream<String> s = stream(file)) {
			return s.collect(Collectors.toList());
		}
	}

	/**
	 * Reads all lines from {@code url} into a {@link List}, then closes the
	 * underlying reader. Gzip content ({@code .gz} URL path) is decompressed
	 * automatically. Uses UTF-8 encoding.
	 *
	 * <p>
	 * For large responses prefer {@link #stream(URL)} to avoid loading the entire
	 * content into memory.
	 * </p>
	 *
	 * @param url the URL to read; must not be {@code null}
	 * @return an immutable list of lines; never {@code null}
	 * @throws IOException if the URL cannot be opened or read
	 * @since 1.3.7.18
	 */
	static public List<String> read(URL url) throws IOException {
		try (Stream<String> s = stream(url)) {
			return s.collect(Collectors.toList());
		}
	}

	/**
	 * Wraps {@code br} in a lazy line {@link Stream}. The reader is closed
	 * automatically when the stream is closed (via the registered {@code onClose}
	 * handler), so callers should use the stream in a try-with-resources block or
	 * call {@link Stream#close()} explicitly.
	 *
	 * @param br the reader to stream; must not be {@code null}
	 * @return a sequential {@link Stream} of lines backed by {@code br}
	 * @throws IOException never thrown; declared for API consistency
	 * @since 1.3.7.18
	 */
	static public Stream<String> stream(BufferedReader br) throws IOException {
		return br.lines().onClose(() -> {
			try {
				br.close();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	/**
	 * Opens {@code file} and returns a lazy line {@link Stream}. The file format is
	 * detected automatically (plain, gzip, or 7-Zip); UTF-8 encoding is used. The
	 * underlying reader is closed when the stream is closed.
	 *
	 * <p>
	 * Use in a try-with-resources block:
	 * </p>
	 * 
	 * <pre>{@code
	 * try (Stream<String> lines = IOUtils.stream(file)) {
	 * 	lines.forEach(System.out::println);
	 * }
	 * }</pre>
	 *
	 * @param file the file to stream; must not be {@code null}
	 * @return a sequential {@link Stream} of lines
	 * @throws IOException if the file cannot be opened
	 * @since 1.3.7.18
	 */
	static public Stream<String> stream(File file) throws IOException {
		BufferedReader br = br(file);
		return stream(br);
	}

	/**
	 * Opens {@code url} and returns a lazy line {@link Stream}. Gzip content
	 * ({@code .gz} URL path) is decompressed automatically; UTF-8 encoding is used.
	 * The underlying reader is closed when the stream is closed.
	 *
	 * @param url the URL to stream; must not be {@code null}
	 * @return a sequential {@link Stream} of lines
	 * @throws IOException if the URL cannot be opened
	 * @since 1.3.7.18
	 */
	static public Stream<String> stream(URL url) throws IOException {
		BufferedReader br = br(url);
		return stream(br);
	}

}
