/**
 * 
 */
package nlp4j.webcrawler.mlit;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.impl.DefaultDocument;
import nlp4j.webcrawler.AbstractWebCrawler;

/**
 * 国土交通省「自動車のリコール・不具合情報」をダウンロードする<br>
 * http://carinf.mlit.go.jp/jidosha/carinf/opn/index.html<br>
 * 電子計算機による情報解析（多数の著作物その他の大量の情報から， 当該情報を構成する言語，音，影像その他の要素に係る情報を抽出し，
 * 比較，分類その他の統計的な解析を行うことをいう。）を行うことを目的とする
 * 
 * @see "http://carinf.mlit.go.jp/jidosha/carinf/opn/index.html"
 * @author Hiroki Oya
 *
 */
public class MlitCarInfoCrawler extends AbstractWebCrawler implements Crawler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

	/**
	 * Validate param value
	 * 
	 * @param value to be set as date
	 * @return
	 */
	static boolean checkDateFormat(String value) {
		try {
			sdf.parse(value);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	String txtFrDat = null; // "2019/01/01";
	String txtToDat = null; // "2019/12/31";

	private String accessKey;

	int countCrawled = 0;

	List<Integer> hashCodes = new ArrayList<>();

	/**
	 * 国土交通省「自動車のリコール・不具合情報」をダウンロードする<br>
	 * http://carinf.mlit.go.jp/jidosha/carinf/opn/index.html<br>
	 * 電子計算機による情報解析（多数の著作物その他の大量の情報から， 当該情報を構成する言語，音，影像その他の要素に係る情報を抽出し，
	 * 比較，分類その他の統計的な解析を行うことをいう。）を行うことを目的とする <br>
	 * Default Constructor
	 */
	public MlitCarInfoCrawler() {
		super();
	}

	public String getAccessKey() throws IOException {
//		String url = "https://"
////				+ "carinf."
//				+ "www." //
//				+ "mlit.go.jp/jidosha/carinf/opn/index.html";
		String url = "https://www.mlit.go.jp/jidosha/carinf/rcl/cgi-bin/cis_search.cgi";
		org.jsoup.nodes.Document document = Jsoup.parse(new URL(url), 1000 * 10); // throws IOException

		String v = document.select("#inputForm").select("input[name=\"nccharset\"]").val();

		return v;
	}

	/**
	 * 国土交通省「自動車のリコール・不具合情報」をダウンロードする<br>
	 * http://carinf.mlit.go.jp/jidosha/carinf/opn/index.html<br>
	 * <br>
	 * 電子計算機による情報解析（多数の著作物その他の大量の情報から，<br>
	 * 当該情報を構成する言語，音，影像その他の要素に係る情報を抽出し， <br>
	 * 比較，分類その他の統計的な解析を行うことをいう。）を行うことを目的とする <br>
	 */
	@Override
	public ArrayList<Document> crawlDocuments() {

		ArrayList<Document> docs = new ArrayList<>();

		int countMax = 1;

		int page = 1;

		if (this.accessKey == null) {
			try {
				this.accessKey = getAccessKey(); // throws IOException
				// Example: (java.net.UnknownHostException: carinf.mlit.go.jp)
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				logger.error(e);
				throw new RuntimeException(e);
//				return docs;
			}
		}

		if (this.txtFrDat == null || this.txtToDat == null) {
			logger.warn("Parameter is not set: from_date=" + this.txtFrDat + ",to_date" + this.txtToDat);
			throw new RuntimeException("Parameter is not set: from_date=" + this.txtFrDat + ",to_date" + this.txtToDat);
		}

		for (int count = 0; count < countMax; count++) {

			// as of 2021/11/16
			// http://carinf.mlit.go.jp/jidosha/carinf/opn/search.html?
			// selCarTp=1
			// &lstCarNo=000
			// &txtFrDat=2021/04/01
			// &txtToDat=2021/05/31
			// &txtNamNm=
			// &txtMdlNm=
			// &txtEgmNm=
			// &chkDevCd=
			// &page=64

			String url = String.format("https://"
//					+ "carinf."
					+ "www." //
					+ "mlit.go.jp/jidosha/carinf/opn/search.html?" //
					+ "nccharset=%s" //
					+ "&selCarTp=1" //
					+ "&lstCarNo=000" //
					+ "&txtFrDat=%s" //
					+ "&txtToDat=%s" //
					+ "&txtNamNm=" //
					+ "&txtMdlNm=" //
					+ "&txtEgmNm=" //
					+ "&chkDevCd=" //
					+ "&page=%d" //
					+ "", this.accessKey, this.txtFrDat, this.txtToDat, page);

			logger.info("crawling: " + url);

			try {

				org.jsoup.nodes.Document document = Jsoup.parse(new URL(url), 1000 * 10); // throws IOException

				{ // 総ページ数の取得
					Elements elements = document.select("#p1 p");

					if (elements.size() == 0) {
						logger.info("page not returned info: " + url);
						break;
					}

					Element el = elements.get(0);
					String sMaxPages = el.text().trim().split(" ")[2];
					try {
						countMax = Integer.parseInt(sMaxPages);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}

				// ヘッダ名を動的に取得する

				Elements elements = document.select("#r1 tr");

				ArrayList<String> header = new ArrayList<>();

				// ヘッダー情報は各ページで動的に取得する
				for (int idx = 0; idx < elements.size(); idx++) {
					Element element = elements.get(idx);

					// ヘッダー行
					if (idx == 0) {
						// 番号
						{
							Elements ee = element.select("th:first-child");
							header.add(ee.text().trim());
						}

						Elements elDivs = element.select("div");

						for (int n = 0; n < elDivs.size(); n++) {
							Element elDiv = elDivs.get(n);
							header.add(elDiv.text().replace("\u3000", "").replace(" ", "").trim());
						}
					}
					// データ行
					else {

						Document doc = new DefaultDocument();

						// 番号
						{
							Elements ee = element.select("td:first-child");
							String key = header.get(0);
							String value = ee.text().trim();
							doc.putAttribute(key, value);
						}

						Elements elDivs = element.select("div");

						for (int n = 0; n < elDivs.size(); n++) {
							Element elDiv = elDivs.get(n);
							String key = header.get(n + 1);
							String value = elDiv.text().trim();
							doc.putAttribute(key, value);
						}

						docs.add(doc);

						{
							int hashCode = doc.toString().hashCode();
							if (this.hashCodes.contains(hashCode)) {
								logger.warn("Same content found: " + doc.toString());
							} //
							else {
								this.hashCodes.add(hashCode);
							}
						}

						logger.info("crawled=1,total=" + docs.size());
					}

				} // end of for

			}
			// on getting Page data
			catch (IOException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(1000 * 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			page++;

			if (super.prop.get("debug") != null && "true".equals(super.prop.get("debug"))) {
				if (count > 2) {
					break;
				}
			}

		} // end of for loop (page)

		return docs;
	}

	/**
	 * key:<br>
	 * from_date: 開始日(yyyy/MM/dd)<br>
	 * to_date: 終了日(yyyy/MM/dd)<br>
	 */
	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		if ("from_date".equals(key)) {
			if (checkDateFormat(value)) {
				this.txtFrDat = value;
			} else {
				// warn
			}
		} //
		else if ("to_date".equals(key)) {
			if (checkDateFormat(value)) {
				this.txtToDat = value;
			} else {
				// warn
			}
		} //
		else if ("accessKey".equals(key)) {
			this.accessKey = value;
		}

	}

}
