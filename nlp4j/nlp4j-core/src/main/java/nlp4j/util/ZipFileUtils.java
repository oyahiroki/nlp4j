package nlp4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

/**
 * created on: 2023-05-29
 * 
 * @author Hiroki Oya
 *
 */
public class ZipFileUtils {

	/**
	 * @param zipFile     to be extracted
	 * @param charsetName entry name charset
	 * @param entry       file entry in zip file
	 * @param outputFile  file to be created
	 * @throws IOException
	 */
	static public void extract(File zipFile, String charsetName, String entry, File outputFile) throws IOException {
		Charset charset = Charset.forName(charsetName);
		try ( //
				FileInputStream fis = new FileInputStream(zipFile); //
				ZipInputStream zis = new ZipInputStream(fis, charset); //
		) {
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				if (ze.getName().equals(entry)) {
					FileUtils.copyInputStreamToFile(zis, outputFile);
					break;
				}
			}
		}

	}

}
