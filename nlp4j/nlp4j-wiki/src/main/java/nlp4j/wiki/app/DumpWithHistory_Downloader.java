package nlp4j.wiki.app;

import java.io.File;
import java.net.URL;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.http.FileDownloader;

public class DumpWithHistory_Downloader {

	public static void main(String[] args) throws Exception {

		String lang = "ja";
		String media = "wiki";
		String version = "20250901";

		String filter_regex = ".*history.*7z";

		{
			String baseurl = "https://dumps.wikimedia.org/" + lang + media + "/" + version + "/";
			String url = baseurl + "dumpstatus.json";
			File dir = new File("/usr/local/" + "wiki" + "/" + lang + media + "/" + version);
//			File outFile = new File("/usr/local/" + "wiki" + "/" + lang + media + "/" + version + "/dumpstatus.json");
			File outFile = File.createTempFile("nlp4j-", "dumpstatus.json");
			FileDownloader.download(url, outFile, true);
			System.err.println(outFile.getAbsolutePath());
			String json = org.apache.commons.io.FileUtils.readFileToString(outFile, "UTF-8");
			JsonObject jo = (new Gson()).fromJson(json, JsonObject.class);

			JsonObject files = jo.get("jobs").getAsJsonObject().get("metahistory7zdump").getAsJsonObject().get("files")
					.getAsJsonObject();
			Set<String> keys = files.keySet();
			for (String key : keys) {
//				System.err.println(key);
				JsonObject f = files.get(key).getAsJsonObject();
				URL full_url = (new URL(new URL(baseurl), f.get("url").getAsString()));

				if (full_url.toString().matches(filter_regex)) {
					System.err.println(full_url);
					File outFile2 = new File(dir, full_url.getFile());
					FileDownloader.download(full_url.toString(), outFile2, false);
				}

			}
		}

	}

}
