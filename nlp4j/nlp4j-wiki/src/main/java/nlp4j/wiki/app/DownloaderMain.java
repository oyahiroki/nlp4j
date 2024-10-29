package nlp4j.wiki.app;

import nlp4j.wiki.MediaWikiDownloader;

public class DownloaderMain {

	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 4) {
			System.err.println("args[0]: lang    : en | ja | ... ");
			System.err.println("args[1]: media   : wiki | wiktionary");
			System.err.println("args[2]: version : 20240401");
			System.err.println("args[3]: dir     : /usr/local/wiki/");
			return;
		}
		String[] lang = args[0].split(",");
		String[] media = args[1].split(",");
		String[] version = args[2].split(",");
		String dir = args[3];
		for (String l : lang) {
			for (String m : media) {
				for (String v : version) {
					MediaWikiDownloader dl = (new MediaWikiDownloader.Builder()) //
							.version(v) //
							.outdir("" + dir + "" + l + m + "/" + v + "") //
							.language(l) //
							.media(m) //
							.build();
					dl.crawlDocuments();

				}
			}
		}
	}

}
