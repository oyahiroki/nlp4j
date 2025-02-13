package nlp4j.util;

import java.util.Map;

import junit.framework.TestCase;
import nlp4j.tuple.Pair;

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

	public void test010() throws Exception {
		Map<Character, Character> map = MapBuilder.of(Pair.of('(', ')'), Pair.of('{', '}'));
		for (char key : map.keySet()) {
			char value = map.get(key);
			System.out.println("key: " + key + ",value: " + value);
		}
	}

}
