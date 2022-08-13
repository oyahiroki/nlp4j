package nlp4j.util;

/**
 * created_at: 2022-05-09
 * 
 * @author Hiroki Oya
 *
 */
public class RuntimeUtils {

	/**
	 * @return Memory Info like "free=2,141,192,192 total=2,147,483,648
	 *         max=12,811,501,568 used=6,291,456 ratio=0.29 "
	 */
	static public String getMemoryInfo() {

		long free = Runtime.getRuntime().freeMemory();
		long total = Runtime.getRuntime().totalMemory();
		long max = Runtime.getRuntime().maxMemory();
		long used = total - free;
		double ratio = ((double) used / (double) total) * 100;

		return String.format( //
				"free=%,d " //
						+ "total=%,d " //
						+ "max=%,d " //
						+ "used=%,d " //
						+ "ratio=%.2f",
				free, total, max, used, ratio) //
		;
	}

}
