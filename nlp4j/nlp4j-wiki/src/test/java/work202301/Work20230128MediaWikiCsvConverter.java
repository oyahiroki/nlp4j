package work202301;

import nlp4j.wiki.converter.MediaWikiCsvConverter;

public class Work20230128MediaWikiCsvConverter {

	public static void main(String[] args) throws Exception {

		String param0 = "/usr/local/wiki/jawiki/20221101/" //
				+ "jawiki-20221101-pages-articles-multistream.xml.bz2";
		String param1 = "R:/" + "jawiki-20221101-pages-articles-multistream-abstract.csv";
//		String param2 = "1000";
		String param2 = "-1";

		String[] params = { param0, param1, param2 };

		MediaWikiCsvConverter.main(params);

	}

}
