package nlp4j.http;

public class Base64Response {

	private String mimeType;
	private String data;

	public Base64Response(String mimeType, String data) {
		super();
		this.mimeType = mimeType;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
