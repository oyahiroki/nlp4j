package hello;

import java.io.File;

import nlp4j.util.ZipFileUtils;

public class HelloSudachiDictionaryExtract {

	public static void main(String[] args) throws Exception {
		File zipFile = new File("sudachi-dictionary-20230110-core.zip");
		String entryName = "sudachi-dictionary-20230110/system_core.dic";
		File outFile = new File("out.dic");
		ZipFileUtils.extract(zipFile, "MS932", entryName, outFile);

	}

}
