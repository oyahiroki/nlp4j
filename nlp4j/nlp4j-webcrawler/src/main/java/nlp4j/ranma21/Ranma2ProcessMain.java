package nlp4j.ranma21;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Ranma2ProcessMain {

	public static void main(String[] args) throws IOException {

		int storyTotal = 1;

		for (int n = 1; n <= 38; n++) {
			String data = FileUtils.readFileToString(new File("file/ranma/ranma_" + n + ".txt"), "UTF-8");
//			System.err.println(data);

			String[] ss = data.split("(▼|●)");

			int story = 1;
			for (String s : ss) {
				// 第N話
				if (s.startsWith("第")) {
					System.err.println(storyTotal + "," + n + "," + story + "," + s.split("／")[1]);
					storyTotal++;
					story++;
				}
			}

//			break;

		}

	}

}
