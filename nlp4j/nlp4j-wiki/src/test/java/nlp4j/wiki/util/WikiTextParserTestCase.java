package nlp4j.wiki.util;

import junit.framework.TestCase;

public class WikiTextParserTestCase extends TestCase {

	public void testParse001() throws Exception {
		String s = "{{Otheruses}}\r\n" //
				+ "{{複数の問題\r\n" //
				+ "| 出典の明記 = 2021年3月\r\n" //
				+ "| 更新 = 2021年3月\r\n" //
				+ "}}\r\n" //
				+ "[[ファイル:Менська районна гимназія; 13.08.19.jpg|200px|サムネイル|右]] \r\n" //
				+ "[[ファイル:Larkmead School, Abingdon, Oxfordshire.png|thumb|right|250px|[[イングランド]]の[[オックスフォードシャー州]]にある学校課（[[2007年]]）。]]\r\n" //
				+ "'''学校'''（がっこう）または'''スクール'''（英語: <span lang=\"en-us\" dir=\"ltr\">School</span>）は、[[幼児]]・[[児童]]・[[生徒]]・[[学生]]などに対する[[教育]][[制度]]の中核的な役割を果たす機関。また、その施設。[[学園]]、[[学院]]などもほぼ同様の意味を持つ。\r\n" //
				+ "\r\n" //
				+ "== 概説 ==\r\n" //
				+ "学校制度は[[社会システム]]の1つである教育制度の中心的システムの一つである<ref name=\"syakai11\">高橋靖直編『学校制度と社会 第二版』玉川大学出版局、2007年、11頁</ref>。社会的作用・社会的活動としての教育は、個人、家庭、小集団、地域社会、国家社会などにもみられるが、現代国家では学校が教育制度の中核的役割を担っている<ref name=\"syakai10\">高橋靖直編『学校制度と社会 第二版』玉川大学出版局、2007年、10頁</ref>。\r\n" //
				+ "\r\n" //
				+ "";
		WikiTextParser.parse(s);
	}

}
