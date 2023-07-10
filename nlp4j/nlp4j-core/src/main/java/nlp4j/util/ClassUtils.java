package nlp4j.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * @author Hiroki Oya
 * @since 1.3.7.9
 *
 */
public class ClassUtils {

	public static File getLocation(Class<?> cls) {
		ProtectionDomain pd = cls.getProtectionDomain();
		CodeSource cs = pd.getCodeSource();
		URL location = cs.getLocation();
		URI uri;
		try {
			uri = location.toURI();
			File file = new File(uri);
			return file;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

}
