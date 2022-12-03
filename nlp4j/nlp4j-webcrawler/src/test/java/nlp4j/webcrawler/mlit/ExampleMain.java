package nlp4j.webcrawler.mlit;

public class ExampleMain {

	public static void main(String[] args) throws Exception {

		String[] params = { "2022/10/01", "2022/10/31", "./mlit_carinfo-202210_json.txt" };

		P101MlitCarInfoDownloadMain.main(params);

	}

}
