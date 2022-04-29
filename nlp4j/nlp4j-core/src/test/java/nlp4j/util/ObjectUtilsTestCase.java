package nlp4j.util;

import java.util.ArrayList;

import junit.framework.TestCase;

public class ObjectUtilsTestCase extends TestCase {

	public void testGetByteSize001() {
		String s = "ABC";
		int size = ObjectUtils.getByteSize(s);
		System.err.println("001:" + size); // 10
	}

	public void testGetByteSize002() {
		Integer obj = 0;
		int size = ObjectUtils.getByteSize(obj);
		System.err.println("002:" + size); // 81
	}

	public void testGetByteSize003() {
		int obj = 0;
		int size = ObjectUtils.getByteSize(obj);
		System.err.println("003:" + size); // 81
	}

	public void testGetByteSize004() {
		ArrayList<String> obj = new ArrayList<>();
		int size = ObjectUtils.getByteSize(obj);
		System.err.println("004:" + size); // 58
	}

	public void testGetByteSize005() {
		ArrayList<String> obj = new ArrayList<>();
		obj.add("TEST");
		obj.add("TEST");
		obj.add("TEST");
		int size = ObjectUtils.getByteSize(obj);
		System.err.println("005:" + size); // 75
	}

	public void testGetByteSize006() {
		ObjectUtilsTestCase obj = new ObjectUtilsTestCase();
		int size = ObjectUtils.getByteSize(obj);
		System.err.println("006:" + size); // -1
	}
}
