package nlp4j.util;

import java.util.Map;

import junit.framework.TestCase;

/**
 * Created on 2024-08-04
 * 
 * @since 1.3.7.13
 * 
 */
public class MapBuilderTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = MapBuilder.class;

	public void test001() throws Exception {
		Map<String, String> map = (new MapBuilder<String, String>()).put("aaa", "xxx").build();

		String expected = "xxx";
		String actual = map.get("aaa");

		assertEquals(expected, actual);
	}

}
