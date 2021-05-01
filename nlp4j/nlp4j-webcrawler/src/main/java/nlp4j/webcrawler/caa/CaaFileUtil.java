package nlp4j.webcrawler.caa;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CaaFileUtil {

	public static String getFolderName(int n) {
		int range = 1000;

		int base = n / range;

		int base1 = base * range;

		int base2 = (base + 1) * range - 1;

		return String.format("%08d-%08d", base1, base2);
	}

	/**
	 * Parse document id number from file name<br>
	 * Example in : out_00000027000.html<br>
	 * Example out : 27000
	 * 
	 * @param fileName
	 * @return id number
	 */
	static public int getIdNum(String fileName) {
		String id = fileName.substring(4, 15);
		int idNum = Integer.parseInt(id);
		return idNum;
	}

	public static void main(String[] args) {
		String dir = "C:/usr/local/nlp4j/collections/caa/data/html";

		File dirBase = new File(dir);

		for (File file : dirBase.listFiles()) {
			if (file.isDirectory()) {
				continue;
			} else {
				System.err.println(file.getName());

				String name = file.getName();

				int idNum = getIdNum(name);

				System.err.println(idNum);

				String folderName = getFolderName(idNum);

				System.err.println(" -> " + folderName);

				File folder = new File(dirBase, folderName);
				if (folder.exists() == false) {
					boolean mkdir = folder.mkdir();
					System.err.println("mkdir: " + mkdir);
				}

				File moveTo = new File(folder, name);

				try {
					Files.move(file.toPath(), moveTo.toPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}

			}
		}

	}

}
