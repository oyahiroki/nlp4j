package nlp4j.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * </pre>
 * 
 * created on 2022-01-16
 * 
 * @author Hiroki Oya
 */
public class FacetUtils {

	/**
	 * <pre>
	 * </pre>
	 * 
	 * created on 2021-01-16
	 * 
	 * @param path
	 * @return
	 */
	static public List<String> splitFacetPath(String path) {
		int idx = 0;
		int idx1 = 0;
		List<String> ss = new ArrayList<>();
		if (path == null || path.isEmpty()) {
			return ss;
		} else {
			while ((idx = path.indexOf(".", idx1)) != -1) {
				ss.add(path.substring(0, idx));
				idx1 = idx + 1;
				if (idx1 >= path.length()) {
					break;
				}
			}
			ss.add(path);
			return ss;
		}
	}

}
