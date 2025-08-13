package nlp4j.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>
 * A utility class for downloading files from a given URL using Apache
 * HttpClient. Supports optional progress listener callbacks for tracking
 * download progress.
 * </p>
 * <p>
 * 指定したURLからファイルをダウンロードするためのユーティリティクラスです。 Apache HttpClient
 * を使用し、進捗状況を通知するリスナーをオプションでサポートします。
 * </p>
 */
public class FileDownloader {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * <p>
	 * A listener interface for receiving progress updates during file download.
	 * </p>
	 * <p>
	 * ファイルダウンロード中の進捗更新を受け取るためのリスナーインターフェースです。
	 * </p>
	 */
	@FunctionalInterface
	public interface DownloadProgressListener {
		/**
		 * Called when the download starts. <br>
		 * ダウンロード開始時に呼び出されます。
		 *
		 * @param url        URL to be downloaded. / ダウンロード対象のURL
		 * @param totalBytes Total bytes to read (-1 if unknown). / 総バイト数（不明な場合は -1）
		 */
		default void onStart(String url, long totalBytes) {
		}

		/**
		 * Called to report download progress. <br>
		 * ダウンロード進捗を通知するために呼び出されます。
		 *
		 * @param bytesRead  Total bytes read so far. / これまでに読み込まれた総バイト数
		 * @param totalBytes Total bytes to read (-1 if unknown). / 総バイト数（不明な場合は -1）
		 * @param percent    Download percentage (-1 if total size unknown). /
		 *                   ダウンロードの進捗率（総サイズ不明時は -1）
		 */
		void onProgress(long bytesRead, long totalBytes, double percent);

		/**
		 * Called when the download completes successfully. <br>
		 * ダウンロードが正常に完了したときに呼び出されます。
		 *
		 * @param file Output file that was downloaded. / ダウンロードされた出力ファイル
		 */
		default void onComplete(File file) {
		}
	}

	/**
	 * Downloads a file from the given URL to the current working directory. <br>
	 * 指定URLからファイルをカレントディレクトリにダウンロードします。
	 *
	 * @param url URL string to download from. / ダウンロード元のURL文字列
	 * @throws IOException if an error occurs. / エラーが発生した場合
	 */
	static public void download(String url) throws IOException {
		try {
			new URL(url); // Basic validity check / URL妥当性の簡易チェック
		} catch (MalformedURLException e) {
			throw new IOException("Invalid URL: " + url);
		}
		String fileName = url.substring(url.lastIndexOf('/') + 1);
		if (fileName == null || fileName.isEmpty()) {
			throw new IOException("file name is empty");
		}
		File outFile = new File(fileName);
		boolean overwrite = false;
		download(url, outFile, overwrite, null);
	}

	/**
	 * Downloads a file from the given URL to the specified output file. <br>
	 * 指定URLから指定された出力ファイルにダウンロードします。
	 *
	 * @param url       URL string to download from. / ダウンロード元のURL文字列
	 * @param outFile   Output file path. / 出力ファイルのパス
	 * @param overwrite If {@code true}, overwrite existing file. /
	 *                  {@code true}の場合、既存ファイルを上書き
	 * @throws IOException if an error occurs. / エラーが発生した場合
	 */
	public static void download(String url, File outFile, boolean overwrite) throws IOException {
		download(url, outFile, overwrite, null);
	}

	/**
	 * Downloads a file from the given URL to the specified output file, optionally
	 * reporting progress via the provided listener. <br>
	 * 指定URLから指定された出力ファイルにダウンロードし、オプションでリスナーを通じて進捗を通知します。 <br>
	 * If Content-Length is unknown, progress is reported every fixed number of
	 * bytes (default: 1MB). <br>
	 * Content-Length が不明な場合は、一定バイトごと（デフォルト: 1MB）に進捗を通知します。
	 *
	 * @param url       URL string to download from. / ダウンロード元のURL文字列
	 * @param outFile   Output file path. / 出力ファイルのパス
	 * @param overwrite If {@code true}, overwrite existing file. /
	 *                  {@code true}の場合、既存ファイルを上書き
	 * @param listener  Progress listener (nullable). / 進捗リスナー（null可）
	 * @throws IOException if an error occurs. / エラーが発生した場合
	 */
	public static void download(String url, File outFile, boolean overwrite, DownloadProgressListener listener)
			throws IOException {

		// Interval for progress reporting when total size unknown / 総サイズ不明時の進捗通知間隔
		final long UNKNOWN_SIZE_NOTIFY_INTERVAL = 1L * 1024 * 1024; // 1MB

		if (!overwrite && outFile.exists()) {
			throw new IOException("File already exists: " + outFile.getAbsolutePath());
		}

		try (final CloseableHttpClient client = HttpClients.createDefault();
				final CloseableHttpResponse response = client.execute(new HttpGet(url))) {

			final int status = response.getCode();
			if (status >= 200 && status < 300) {
				final HttpEntity entity = response.getEntity();
				final long total = (entity != null && entity.getContentLength() >= 0) ? entity.getContentLength() : -1;

				// Notify start / 開始通知
				if (listener != null) {
					listener.onStart(url, total);
				}

				// Create parent directories if needed / 必要に応じて親ディレクトリを作成
				File parent = outFile.getAbsoluteFile().getParentFile();
				if (parent != null) {
					Files.createDirectories(parent.toPath());
				}

				try (InputStream in = (entity != null) ? entity.getContent() : InputStream.nullInputStream();
						OutputStream out = Files.newOutputStream(outFile.toPath(), StandardOpenOption.CREATE,
								StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {

					byte[] buf = new byte[8192];
					long readTotal = 0;
					int r;
					int lastPercentNotified = -1; // Last integer percent notified / 最後に通知した整数％
					long lastUnknownSizeNotified = 0; // Last bytes count notified when total unknown /
														// 総サイズ不明時に最後に通知したバイト数

					while ((r = in.read(buf)) != -1) {
						out.write(buf, 0, r);
						readTotal += r;

						if (listener != null) {
							if (total > 0) {
								int percentInt = (int) Math.floor(readTotal * 100.0 / total);
								if (percentInt > lastPercentNotified) {
									lastPercentNotified = percentInt;
									listener.onProgress(readTotal, total, (double) percentInt);
								}
							} else {
								// Unknown total size: notify every fixed bytes / 総サイズ不明時：一定バイトごとに通知
								if (readTotal - lastUnknownSizeNotified >= UNKNOWN_SIZE_NOTIFY_INTERVAL) {
									lastUnknownSizeNotified = readTotal;
									listener.onProgress(readTotal, -1, -1);
								}
							}
						}
					}
					out.flush();

					// Ensure final 100% notification / 最終的に100％を通知
					if (listener != null && total > 0 && lastPercentNotified < 100) {
						listener.onProgress(total, total, 100.0);
					}
				}

				// Notify complete / 完了通知
				if (listener != null) {
					listener.onComplete(outFile);
				}

				logger.info("Downloaded: " + outFile.getAbsolutePath());
			} else {
				throw new IOException("Unexpected response status: " + status);
			}
		}
	}

}
