package nlp4j.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created on: 2023-10-03
 * 
 * @author Hiroki Oya
 *
 */
public class LimitedLineBufferedReader extends BufferedReader {
	// 読み込み最大行数
	private int maxLines;
	// 読み込んだファイルの行数
	private int linesRead;

	public LimitedLineBufferedReader(InputStreamReader in, int maxLines) {
		super(in);
		this.maxLines = maxLines;
		this.linesRead = 0;
	}

	@Override
	public String readLine() throws IOException {
		if (linesRead < maxLines) {
			String line = super.readLine();
			if (line != null) {
				linesRead++;
				return line;
			}
		}
		return null;
	}

}
