package nlp4j.model;

public class NGramLanguageModelMain {

	public static void main(String[] args) throws Exception {

		NGramLanguageModel model = new NGramLanguageModel();
		model.add("吾輩 は 猫 で ある");
		model.add("吾輩 は 猫 で はない");
		model.add("吾輩 が ネコ で ある");
		model.add("吾輩 は 犬 で ある");

		model.print();

		System.err.println(model.pull("吾輩 は").toString());
		;
		model.pull("吾輩");
		model.pull("猫");

		{

			// Pair<Pair<String, Integer>, Set<Pair<String, Integer>>> tree = new
			// MutablePair<>();
			//
			// List<Pair<String, Integer>> list = new ArrayList<>();
			//
			// {
			// Pair<String, Integer> p = new MutablePair<String, Integer>("a", 1);
			// list.add(p);
			// }
			// Pair<String, Integer> x = new MutablePair<String, Integer>("a", 1);
			//
			// boolean b = list.contains(x);
			//
			// System.err.println(b);
		}

	}

}
