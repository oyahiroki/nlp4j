package nlp4j.dictionary;

import java.util.List;

import nlp4j.Keyword;

public class UserDictionaryStringMatcherTestMain {

	public static void main(String[] args) {
			String s = "私は日本IBMで仕事をしています．日本アイビーエムです．日本アイ・ビー・エムです．テストです．";
			String[] ee = { "日本ＩＢＭ", "日本MS", "日本HP" };
	
			// 文字列マッチでユーザー辞書を適用する
			UserDictionaryStringMatcher dic = new UserDictionaryStringMatcher();
	
			for (String e : ee) {
	//			System.err.println(e);
				dic.put("user.dic", e);
			}
			{
				dic.put("user.dic2", "日本IBM", "日本アイビーエム", "日本アイ・ビー・エム");
			}
			{
				dic.put("user.dic2", "日本アイ・ビー・エム", "日本アイビーエム");
			}
			{
				dic.put("user.others", "テスト");
			}
			{
				dic.put("user.others", "は日");
			}
	
			List<Keyword> kwds = dic.parse(s);
			for (Keyword kwd : kwds) {
				System.out.println(kwd.getBegin() + "," + kwd.getEnd() + "," + kwd.getFacet() + "," + kwd.getLex());
			}
	
		}

}
