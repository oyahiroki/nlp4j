package nlp4j.tuple;

import org.apache.commons.lang3.tuple.ImmutableTriple;

/**
 * @author Hiroki Oya
 * @since 1.3.7.12
 * @param <L>
 * @param <M>
 * @param <R>
 */
public class Triple<L, M, R> extends org.apache.commons.lang3.tuple.Triple<L, M, R> {

	private static final long serialVersionUID = 1L;

	ImmutableTriple<L, M, R> triple;

	public static <L, M, R> Triple<L, M, R> of(final L left, final M middle, final R right) {
		return new Triple<>(left, middle, right);
	}

	public Triple(final L left, final M middle, final R right) {
		this.triple = new ImmutableTriple<L, M, R>(left, middle, right);
	}

	@Override
	public L getLeft() {
		return this.triple.getLeft();
	}

	@Override
	public M getMiddle() {
		return this.triple.getMiddle();
	}

	@Override
	public R getRight() {
		return this.triple.getRight();
	}

	@Override
	public String toString() {
		return this.triple.toString();
	}

}
