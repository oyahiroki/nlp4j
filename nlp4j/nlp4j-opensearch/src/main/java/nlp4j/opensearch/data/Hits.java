package nlp4j.opensearch.data;

import java.util.List;

import com.google.gson.JsonArray;

public class Hits {

	private Total total;
	private double max_score;
//	private List<Hit> hits;
	
	JsonArray hits;

	public JsonArray getHits() {
		return hits;
	}

	public void setHits(JsonArray hits) {
		this.hits = hits;
	}

	public Total getTotal() {
		return total;
	}

	public void setTotal(Total total) {
		this.total = total;
	}

	public double getMax_score() {
		return max_score;
	}

	public void setMax_score(double max_score) {
		this.max_score = max_score;
	}

//	public List<Hit> getHits() {
//		return hits;
//	}
//
//	public void setHits(List<Hit> hits) {
//		this.hits = hits;
//	}

}
