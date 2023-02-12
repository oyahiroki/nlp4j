package nlp4j.counter;

import java.util.ArrayList;

/**
 * created on 2021-07-13
 * 
 * @author Hiroki Oya
 * @since 1.3.2
 */
public class DocumentCounterBuilder {

	ArrayList<String> facetFilter;
	String keyField;

	protected DocumentCounterBuilder() {
		super();
	}

	/**
	 * @return new Builder
	 */
	static public DocumentCounterBuilder create() {
		return new DocumentCounterBuilder();
	}

	public DocumentCounterBuilder idField(String field) {
		this.keyField = field;
		return this;
	}

	/**
	 * @param facet of keyword for filtering
	 * @return Builder
	 */
	public DocumentCounterBuilder faceFilter(String facet) {
		if (this.facetFilter == null) {
			this.facetFilter = new ArrayList<>();
		}
		this.facetFilter.add(facet);
		return this;
	}

	/**
	 * @return new DocumentCounter with configuration
	 */
	public DocumentCounter build() {
		DocumentCounter counter = new DocumentCounter();
		if (this.facetFilter != null) {
			for (String facet : this.facetFilter) {
				counter.setFacetFilter(facet);
			}
		}
		if (this.keyField != null) {
			counter.setKeyField(this.keyField);
		}
		return counter;
	}

}
