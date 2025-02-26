package nlp4j.llm.embeddings;

public class EmbeddingResponse {
	private String message;
	private String time;
	private String text;
	private double[] embeddings;

	// GetterとSetter（必要に応じて）
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double[] getEmbeddings() {
		return embeddings;
	}

	public void setEmbeddings(double[] embeddings) {
		this.embeddings = embeddings;
	}
}