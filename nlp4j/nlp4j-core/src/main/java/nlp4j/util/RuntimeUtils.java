package nlp4j.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/**
 * created on 2022-05-09
 * 
 * @author Hiroki Oya
 *
 */
public class RuntimeUtils {

	private RuntimeUtils() {
	}

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

	public static boolean isMaxMemoryAtLeast(int megabytes) {

		long required = megabytes * 1024L * 1024L;

		long actual = Runtime.getRuntime().maxMemory();

		return actual >= required;
	}

	/**
	 * -Xms の値をバイト単位で返す 指定されていない場合は Runtime.totalMemory() を返す
	 */
	public static long getXms() {

		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();

		List<String> args = bean.getInputArguments();

		for (String arg : args) {
			if (arg.startsWith("-Xms")) {
				return parseMemorySize(arg.substring(4));
			}
		}

		// 明示指定がない場合
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * "512m", "2g", "1024k" をバイトへ変換
	 */
	private static long parseMemorySize(String value) {

		value = value.trim().toLowerCase();

		if (value.endsWith("g")) {
			return Long.parseLong(value.substring(0, value.length() - 1)) * 1024L * 1024L * 1024L;
		}

		else if (value.endsWith("m")) {
			return Long.parseLong(value.substring(0, value.length() - 1)) * 1024L * 1024L;
		}

		else if (value.endsWith("k")) {
			return Long.parseLong(value.substring(0, value.length() - 1)) * 1024L;
		}

		else {
			return Long.parseLong(value);
		}
	}

}
