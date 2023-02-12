package nlp4j.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Utilities for Command Line Interface
 * </pre>
 * 
 * created on 2021-05-01
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class ConsoleUtils {

	/**
	 * @param how_to_use of How to use CLI command
	 * @param args       input from CLI
	 */
	static public void printHowToUse(String how_to_use, String[] args) {
		System.err.println("How to use:\n" + how_to_use);
	}

	/**
	 * <pre>
	 * input : ["-dir","/usr/local/nlp4j","-message","hello"]<br>
	 * output : {"-dir":["/usr/local/nlp4j"],"-message":["hello"]}<br>
	 * </pre>
	 * 
	 * created on 2021-05-01
	 * 
	 * @param args input
	 * @return Map of parameters
	 * @since 1.3.1.0
	 */
	static public Map<String, List<String>> parseParams(String[] args) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();

		ArrayList<String> values = new ArrayList<>();
		String key = null;

		for (String arg : args) {
			if (arg.startsWith("-")) {
				key = arg;
				values = new ArrayList<>();
				map.put(key, values);
			} else {
				if (key != null) {
					map.get(key).add(arg);
				}
			}

		}

		return map;
	}

}
