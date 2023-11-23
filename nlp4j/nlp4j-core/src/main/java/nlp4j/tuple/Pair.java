package nlp4j.tuple;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * @author Hiroki Oya
 * @since 1.3.7.12
 * @param <L>
 * @param <R>
 */
public class Pair<L, R> extends org.apache.commons.lang3.tuple.Pair<L, R> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ImmutablePair<L, R> pair;

	public static <L, R> Pair<L, R> of(final L left, final R right) {
		return new Pair<>(left, right);
	}

	public Pair(final L left, final R right) {
		pair = new ImmutablePair<>(left, right);
	}

	@Override
	public L getLeft() {
		return pair.getLeft();
	}

	@Override
	public R getRight() {
		return pair.getRight();
	}

	@Override
	public R setValue(R value) {
		return this.pair.setValue(value);
	}

}
