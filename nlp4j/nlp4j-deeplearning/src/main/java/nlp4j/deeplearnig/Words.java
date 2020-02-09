package nlp4j.deeplearnig;

import java.util.ArrayList;
import java.util.List;

public class Words {

	List<String> positiveList = new ArrayList<String>();
	List<String> negativeList = new ArrayList<String>();

	static public Words getNew() {
		return new Words();
	}

	public Words positive(String word) {
		positiveList.add(word);
		return this;
	}

	public Words negative(String word) {
		negativeList.add(word);
		return this;
	}

	protected List<String> getPositiveList() {
		return positiveList;
	}

	protected List<String> getNegativeList() {
		return negativeList;
	}

}
