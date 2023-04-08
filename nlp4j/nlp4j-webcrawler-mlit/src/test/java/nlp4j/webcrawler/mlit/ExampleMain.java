package nlp4j.webcrawler.mlit;

public class ExampleMain {

	/**
	 * <pre>
	 * 日本の国土交通省「自動車のリコール・不具合情報」からデータをダウンロードする
	 * https://carinf.mlit.go.jp/jidosha/carinf/opn/index.html
	 * </pre>
	 * 
	 * @param args なし
	 * @throws Exception 例外発生時
	 */
	public static void main(String[] args) throws Exception {

		String[] params = { "2022/10/01", "2022/10/31", "./mlit_carinfo-202210_json.txt" };

		P101MlitCarInfoDownloadMain.main(params);

	}

}
