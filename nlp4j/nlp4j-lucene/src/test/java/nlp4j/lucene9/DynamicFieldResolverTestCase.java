package nlp4j.lucene9;

import junit.framework.TestCase;

/**
 * JUnit3 test case for {@link DynamicFieldResolver}.
 */
public class DynamicFieldResolverTestCase extends TestCase {

	private DynamicFieldResolver resolver;

	@Override
	protected void setUp() throws Exception {
		resolver = new DynamicFieldResolver();
	}

	/** *_dt → DATE */
	public void test_dt_suffix() {
		FieldTypeDef def = resolver.resolve("created_dt");
		assertEquals(FieldTypeDef.Kind.DATE, def.kind());
		assertTrue(def.is_stored());
		assertTrue(def.is_aggregatable());
	}

	/** *_i → INTEGER */
	public void test_i_suffix() {
		FieldTypeDef def = resolver.resolve("year_i");
		assertEquals(FieldTypeDef.Kind.INTEGER, def.kind());
		assertTrue(def.is_stored());
		assertTrue(def.is_aggregatable());
	}

	/** *_l → LONG */
	public void test_l_suffix() {
		FieldTypeDef def = resolver.resolve("count_l");
		assertEquals(FieldTypeDef.Kind.LONG, def.kind());
		assertTrue(def.is_stored());
		assertTrue(def.is_aggregatable());
	}

	/** *_d → DOUBLE */
	public void test_d_suffix() {
		FieldTypeDef def = resolver.resolve("price_d");
		assertEquals(FieldTypeDef.Kind.DOUBLE, def.kind());
		assertTrue(def.is_stored());
		assertTrue(def.is_aggregatable());
	}

	/** suffix なし → KEYWORD */
	public void test_no_suffix_keyword() {
		FieldTypeDef def = resolver.resolve("category");
		assertEquals(FieldTypeDef.Kind.KEYWORD, def.kind());
		assertTrue(def.is_stored());
		assertTrue(def.is_aggregatable());
	}

	/** _dt suffix は _d より優先される（year_dt は DATE） */
	public void test_dt_priority_over_d() {
		FieldTypeDef def = resolver.resolve("year_dt");
		assertEquals(FieldTypeDef.Kind.DATE, def.kind());
	}

	/** multiValued フラグが伝播されること */
	public void test_multiValued() {
		FieldTypeDef def = resolver.resolve("tags");
		FieldTypeDef multi = def.multiValued(true);
		assertTrue(multi.is_multiValued());
		assertEquals(FieldTypeDef.Kind.KEYWORD, multi.kind());
	}
}
