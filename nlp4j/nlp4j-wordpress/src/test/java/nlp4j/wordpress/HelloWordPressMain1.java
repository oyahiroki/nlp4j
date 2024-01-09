package nlp4j.wordpress;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.http.HttpClient5;
import nlp4j.impl.DefaultNlpServiceResponse;

public class HelloWordPressMain1 {

	public static void main(String[] args) throws IOException {
		// 参考になるページ
		// https://ja.wp-api.org/reference/posts/
		// https://toranoana-lab.hatenablog.com/entry/2022/12/04/100000
		// https://posipochi.com/2021/05/09/wp-rest-api-python/

		String user = System.getProperty("NLP4J_WORDPRESS_APIUSER");
		String pass = System.getProperty("NLP4J_WORDPRESS_APIKEY");
		String endpoint = System.getProperty("NLP4J_WORDPRESS_APIENDPOINT");

		if (pass == null) {
			System.err.println("SET NLP4J_WORDPRESS_APIKEY");
			return;
		}

//		-DNLP4J_WORDPRESS_APIKEY=yourpassword
//		-DNLP4J_WORDPRESS_APIUSER=apiusername
//		-DNLP4J_WORDPRESS_APIENDPOINT=https://yourhost

		String token = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
//		String token = new String(Base64.getEncoder().encode((user + ":" + pass).getBytes()));

		System.err.println("token: " + token);

		Map<String, String> headers = new LinkedHashMap<>();
		headers.put("Authorization", "Basic " + token);

		JsonObject request_body = new JsonObject();
		request_body.addProperty("title", "Hello from API");
		request_body.addProperty("status", "publish"); // draft, publish
		
//		request_body.addProperty("content", "" //
//				+ "<!-- wp:heading {\"level\":2} -->\n" //
//				+ "<h2>ここに見出しが入ります</h2>\n" //
//				+ "<!-- /wp:heading -->\n" //
//				+ "\n" //
//				+ "<!-- wp:paragraph -->\n" //
//				+ "<p>ここに本文が入ります その2</p>\n" //
//				+ "<!-- /wp:paragraph -->\n" //
//				+ ""); //
		
		request_body.addProperty("content", "<div id=\"chart_div\" style=\"width: 100%; height: 400px;\"></div>"
				+ "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"
				+ "<!-- 棒グラフ -->"
				+ "<script type=\"text/javascript\">"
				+ ""
				+ "      google.charts.load('current', {'packages':['bar']});"
				+ "      google.charts.setOnLoadCallback(drawChart);"
				+ ""
				+ "      function drawChart() {"
				+ "        var data = google.visualization.arrayToDataTable(["
				+ "        ['月', 'アクセス数'],"
				+ "        ['1月', 498],"
				+ "        ['2月', 367],"
				+ "        ['3月', 565],"
				+ "        ['4月', 610],"
				+ "        ['5月', 749],"
				+ "        ['6月', 722],"
				+ "        ['7月', 954],"
				+ "        ['8月', 1340],"
				+ "        ['9月', 1504],"
				+ "        ['10月', 1843],"
				+ "        ['11月', 1780],"
				+ "        ['12月', 2139]"
				+ "        ]);"
				+ ""
				+ "        var options = {"
				+ "          chart: {"
				+ "            title: 'ブログの月間アクセス数',"
				+ "            subtitle: '***blog.com',"
				+ "          },"
				+ "          legend: {"
				+ "            position: 'none',"
				+ "          },"
				+ "          hAxis: {"
				+ "            title: '2021年'"
				+ "          },"
				+ "          vAxis: {"
				+ "            format: 'decimal',"
				+ "          },"
				+ "          bar: {"
				+ "            groupWidth: \"90%\","
				+ "          }"
				+ "        };"
				+ ""
				+ "        var chart = new google.charts.Bar(document.getElementById('chart_div'));"
				+ "        chart.draw(data, google.charts.Bar.convertOptions(options));"
				+ "      }"
				+ "</script>"
				+ ""); //
		
		JsonArray categories = new JsonArray();
		categories.add(1);
		request_body.add("categories", categories);
		// categories: [n]
		// tags: [n]
		request_body.addProperty("slug", "pre_open");

		String url = endpoint + "/wp-json/wp/v2/posts/";

		System.err.println("url: " + url);

		try (HttpClient5 client = new HttpClient5();) {
			DefaultNlpServiceResponse response = client.post(url, headers, request_body.toString());
			System.err.println(response.getResponseCode());
		}

	}

}
