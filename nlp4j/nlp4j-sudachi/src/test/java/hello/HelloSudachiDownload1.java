package hello;

import java.io.IOException;

import nlp4j.http.FileDownloader;

public class HelloSudachiDownload1 {

	public static void main(String[] args) throws IOException {
		String url = "http://sudachi.s3-website-ap-northeast-1.amazonaws.com/sudachidict/"
				+ "sudachi-dictionary-20230110-core.zip";
		FileDownloader.download(url);
	}

}
