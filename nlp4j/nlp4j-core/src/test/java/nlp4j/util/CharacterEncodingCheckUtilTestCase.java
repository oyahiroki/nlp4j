package nlp4j.util;

import junit.framework.TestCase;

public class CharacterEncodingCheckUtilTestCase extends TestCase {

	public void testCanEncode001() {
		CharacterEncodingCheckUtil u = new CharacterEncodingCheckUtil("MS932");
		boolean b = u.canEncode("あ");
		boolean expected = true;

		assertEquals(expected, b);
	}

	public void testCanEncode002() {
		CharacterEncodingCheckUtil u = new CharacterEncodingCheckUtil("MS932");
		boolean b = u.canEncode("𠮷野家");
		boolean expected = false;

		assertEquals(expected, b);
	}

}
