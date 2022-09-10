package nlp4j.solr.importer;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

public class TestMain {

	public static void main(String[] args) throws Exception {
		String s = "{\"id\":\"14\",\"title\":\"EU (曖昧さ回避)\",\"wikilink_abstract\":[],\"wikilink_all\":[\"欧州連合\",\"ユローパ島\",\"FIPS\",\"アメリカ合衆国\",\"Europa Universalis\",\"パラドックスインタラクティブ\",\"学警狙撃\",\"エントロピア・ユニバース\",\"拡張世界\",\"スター・ウォーズ\",\"エディンバラ大学\",\"愛媛大学\",\"エモリー大学\",\"エロン大学\",\"実行ユニット\",\"エクアトリアナ航空\",\"衝鋒隊 (香港)\",\"香港警察\",\"ガリシア統一左翼\",\"統一左翼 (スペイン)\",\"ガリシア州\",\"エフエム愛媛\",\"ユウロピウム\",\"ユーロピニジン\",\"ユーフォニアム\",\".eu\",\"国別ドメイン\",\"バスク語\",\"ISO 639\",\"ㅡ\",\"ハングル\"],\"templates\":[],\"text_abstract\":\"EU\"}";

		Document doc = DocumentUtil.parseFromJson(s);

		System.err.println(DocumentUtil.toJsonPrettyString(doc));

	}

}
