package nlp4j.solr;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class ReadFileMainCountAll {

	public static void main(String[] args) throws Exception {
		// "C:/usr/local/doshisha/iip_lab/共起概念ベース構築（完成版）/共起概念ベース構築（完成版）/wikipedia_for_kyouki_serasera.txt"

		String fileName = "C:/usr/local/doshisha/iip_lab/共起概念ベース構築（完成版）/共起概念ベース構築（完成版）/"
				+ "wikipedia_for_kyouki_serasera.txt";

		try (LineIterator it = FileUtils.lineIterator(new File(fileName), "UTF-8");) {
			int count = 0;
			while (it.hasNext()) {
				count++;
				it.nextLine();
			}
			System.err.println(count);
		}

	}

}
