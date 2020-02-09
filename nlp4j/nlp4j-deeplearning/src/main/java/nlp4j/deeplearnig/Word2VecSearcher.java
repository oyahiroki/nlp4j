package nlp4j.deeplearnig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

public class Word2VecSearcher {

	WordVectors vec;

	public Word2VecSearcher(File modelFile) {

		try {
			vec = WordVectorSerializer.loadTxtVectors(modelFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Collection<String> words() {
		Collection<String> words = vec.vocab().words();
		return words;
	}

	public double similarity(String word1, String word2) {
		double similarity = vec.similarity(word1, word2);
		return similarity;
	}

	public Collection<String> nearest(String word, int top) {
		Collection<String> words = vec.wordsNearest(word, top);
		return words;
	}

	public Collection<String> nearest(Words words, int top) {
		List<String> positiveList = words.getPositiveList();
		List<String> negativeList = words.getNegativeList();
		Collection<String> nearestList = vec.wordsNearest(positiveList, negativeList, top);
		return nearestList;
	}

}
