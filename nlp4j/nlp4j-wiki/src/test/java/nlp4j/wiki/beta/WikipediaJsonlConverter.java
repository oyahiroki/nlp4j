package nlp4j.wiki.beta;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class WikipediaJsonlConverter {
	public static void main(String[] args) throws Exception {

		// サンプル入力
		String title = "青函トンネル";

		String wikiText = """
				{{Infobox tunnel
				| name = '''青函トンネル'''
				| image = [[ファイル:Seikan_tonneru_aomori.JPG|270px|thumb|青函トンネル入口]]
				| caption = [[青森県]][[今別町]]側の入口
				| location = [[青森県]][[東津軽郡]][[今別町]] - [[北海道]][[上磯郡]][[知内町]]
				| type = 鉄道[[トンネル]]
				| length = {{convert|53.85|km|mi|abbr=on}}
				| opened = {{Start date|1988|3|13}}
				| owner = [[鉄道建設・運輸施設整備支援機構]]
				| operator = [[北海道旅客鉄道]]（JR北海道）
				| note = {{lang|en|Seikan Tunnel}}<ref>参考文献</ref>
				}}
				[[ファイル:Seikan Tunnel profile diagram.svg|270px|thumb|縦断図]]
				'''青函トンネル'''（せいかんトンネル）または'''青函隧道'''（せいかんずいどう）は、[[本州]]の[[青森県]][[東津軽郡]][[今別町]]と[[北海道]][[上磯郡]][[知内町]]を結ぶ鉄道[[トンネル]]である。
				[[日本鉄道建設公団]]によって建設され、後身の[[鉄道建設・運輸施設整備支援機構]]が保有し、[[北海道旅客鉄道]]（JR北海道）が管理および列車運行を行っている。
				[[Category:鉄道トンネル]]
				""";

		WikiPage page = WikiPageProcessor.processPage(title, wikiText);

		// 標準出力
		System.out.println("=== title ===");
		System.out.println(page.title());

		System.out.println("\n=== plain_text ===");
		System.out.println(page.plainText());

		System.out.println("\n=== infobox ===");
		for (Map.Entry<String, String> e : page.infobox().entrySet()) {
			System.out.println(e.getKey() + " = " + e.getValue());
		}

		System.out.println("\n=== jsonl ===");
		String jsonLine = JsonlWriter.toJsonLine(page);
		System.out.println(jsonLine);

		// ファイル出力
		Path out = Path.of("wikipedia_sample.jsonl");
		JsonlWriter.writePages(List.of(page), out);
		System.out.println("\nJSONL written to: " + out.toAbsolutePath());
	}
}
