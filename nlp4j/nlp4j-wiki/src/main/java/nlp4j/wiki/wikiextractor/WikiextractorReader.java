package nlp4j.wiki.wikiextractor;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class WikiextractorReader {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public void convert(File wikiextractorFileOrDir, File outFile) throws IOException {

		if (wikiextractorFileOrDir.isDirectory()) {
			IOFileFilter fileFilter = new IOFileFilter() {
				@Override
				public boolean accept(File file) {
					if (file.getName().startsWith("wiki_")) {
						return true;
					}
					return false;
				}

				@Override
				public boolean accept(File dir, String name) {
					if (name.startsWith("wiki_")) {
						return true;
					}
					return false;
				}
			};
			IOFileFilter dirFilter = new IOFileFilter() {
				@Override
				public boolean accept(File file) {
					return true;
				}

				@Override
				public boolean accept(File dir, String name) {
					return true;
				}
			};
			Collection<File> files = FileUtils.listFiles(wikiextractorFileOrDir, fileFilter, dirFilter);
			for (File file : files) {
				convertFile(file, outFile);
			}
		} else {
			convertFile(wikiextractorFileOrDir, outFile);
		}
	}

	private void convertData(String lineJSON, File outFile) throws IOException {
		Gson gson = new Gson();
		JsonObject jo = gson.fromJson(lineJSON, JsonObject.class);
		{
			String s1 = jo.get("text").getAsString();
			{
				String s2 = org.apache.commons.text.StringEscapeUtils.unescapeXml(s1);
				jo.addProperty("text", s2);
			}
		}
		FileUtils.write(outFile, jo.toString() + "\n", "UTF-8", true);
	}

	public void convertFile(File inFile, File outFile) throws IOException {
		logger.info("Processing: " + inFile.getAbsolutePath());
		String s = FileUtils.readFileToString(inFile, "UTF-8");
		for (String line : s.split("\n")) {
			convertData(line, outFile);
		}
	}

}
