package hello.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class HelloJsoupMain001 {

	public static void main(String[] args) {

		String html = "" //
				+ "<html>\r\n" //
				+ "<body>\r\n" //
				+ "<h1 id=\"title\">TITLE</h1>\r\n" //
				+ "</body>\r\n" //
				+ "</html>\r\n" //
				+ ""; //

		org.jsoup.nodes.Document document = Jsoup.parse(html); // throws IOException

		{
			Elements els = document.select("#title");
			System.out.println(els.size()); // 1
			System.out.println(els); // <h1 id="title">TITLE</h1>
			System.out.println(els.get(0).text()); // TITLE
			System.out.println(els.get(0).attr("id")); // title
		}

	}

}
