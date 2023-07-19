package nlp4j.util;

import java.awt.Desktop;
import java.net.URI;

public class DesktopUtils {

	/**
	 * <pre>
	 * ウェブブラウザでURLを開く
	 * Open the URL in a web browser
	 * </pre>
	 * 
	 * @param url
	 */
	static public void borwse(String url) {
		Desktop desktop = Desktop.getDesktop();
		try {
			URI uri = new URI(url);
			desktop.browse(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
