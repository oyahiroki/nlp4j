package nlp4j.util;

import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.impl.DefaultKeywordWithDependency;

public class KeywordWithDependencyUtilsTestCase extends TestCase {

	public void testExtract001() throws Exception {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("a");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("b");
			kwd1.addChild(kwd2);
		}
		DefaultKeywordWithDependency kwd3 = new DefaultKeywordWithDependency();
		{
			kwd3.setLex("c");
			kwd1.addChild(kwd3);
		}
		DefaultKeywordWithDependency kwd4 = new DefaultKeywordWithDependency();
		{
			kwd4.setLex("d");
			kwd3.addChild(kwd4);
		}
		DefaultKeywordWithDependency kwd5 = new DefaultKeywordWithDependency();
		{
			kwd5.setLex("e");
			kwd3.addChild(kwd5);
		}

		System.err.println(kwd1.toStringAsXml());

		for (Keyword kwd : KeywordWithDependencyUtils.extract(kwd1)) {
			System.err.println("kwd: " + kwd.toString());
		}
	}

	public void testToJson001() throws Exception {
		DefaultKeywordWithDependency kwd1 = new DefaultKeywordWithDependency();
		{
			kwd1.setLex("a");
		}
		DefaultKeywordWithDependency kwd2 = new DefaultKeywordWithDependency();
		{
			kwd2.setLex("b");
			kwd1.addChild(kwd2);
		}
		DefaultKeywordWithDependency kwd3 = new DefaultKeywordWithDependency();
		{
			kwd3.setLex("c");
			kwd1.addChild(kwd3);
		}
		DefaultKeywordWithDependency kwd4 = new DefaultKeywordWithDependency();
		{
			kwd4.setLex("d");
			kwd3.addChild(kwd4);
		}
		DefaultKeywordWithDependency kwd5 = new DefaultKeywordWithDependency();
		{
			kwd5.setLex("e");
			kwd3.addChild(kwd5);
		}

		System.err.println(kwd1.toStringAsXml());

		for (Keyword kwd : KeywordWithDependencyUtils.extract(kwd1)) {
			System.err.println("kwd: " + kwd.toString());
		}

		JsonObject json = KeywordWithDependencyUtils.toJson(kwd1);

		System.err.println(JsonUtils.prettyPrint(json));

	}
}
