package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

/**
 * Created on: 2025-09-23
 * 
 * @since 1.3.7.19
 */
public class SevenZUtils {

	static public InputStream readAsInputStream(File f) throws IOException {

		SevenZFile sevenZFile = new SevenZFile(f);

		SevenZArchiveEntry entry = sevenZFile.getNextEntry();
		if (entry != null && !entry.isDirectory()) {

//			InputStream is = SevenZUtils.readAsInputStream(file);

			InputStream is = new InputStream() {
				private final byte[] single = new byte[1];

				@Override
				public int read(byte[] b, int off, int len) throws IOException {
					// SevenZFile は read(b, off, len) を持つので、これを使うと効率的
					return sevenZFile.read(b, off, len);
				}

				@Override
				public int read() throws IOException {
					int n = read(single, 0, 1);
					return (n == -1) ? -1 : (single[0] & 0xff);
				}

				@Override
				public void close() throws IOException {
					// Reader を閉じるとここに到達 → SevenZFile も確実に閉じる
					sevenZFile.close();
				}
			};

			return is;
		} else {
			throw new IOException("No 7z entry: " + f.getAbsolutePath());
		}

	}

}
