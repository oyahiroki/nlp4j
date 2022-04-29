package nlp4j.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectUtils {

	static public int getByteSize(Object obj) {

		if (obj instanceof Serializable) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(bos);
				oos.writeObject(obj);
				oos.flush();
				byte[] b = bos.toByteArray();
				return b.length;
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			} finally {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			return -1;
		}

	}

}
