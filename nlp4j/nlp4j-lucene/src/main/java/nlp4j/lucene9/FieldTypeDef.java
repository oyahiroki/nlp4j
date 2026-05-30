package nlp4j.lucene9;

import org.apache.lucene.index.VectorSimilarityFunction;

/**
 * Logical field definition for LuceneLocalSearchApi.
 *
 * This class hides low-level Lucene field choices such as: - StringField -
 * TextField - StoredField - SortedDocValuesField - NumericDocValuesField -
 * LongPoint - KnnFloatVectorField
 */
public class FieldTypeDef {

	public enum Kind {
		KEYWORD, TEXT, LONG, KNN_VECTOR
	}

	private final Kind kind;

	private boolean stored;
	private boolean aggregatable;
	private boolean sortable;
	private boolean range;

	private int dimension = -1;
	private VectorSimilarityFunction vectorSimilarityFunction = VectorSimilarityFunction.COSINE;

	private FieldTypeDef(Kind kind) {
		this.kind = kind;
	}

	public static FieldTypeDef keyword() {
		return new FieldTypeDef(Kind.KEYWORD);
	}

	public static FieldTypeDef text() {
		return new FieldTypeDef(Kind.TEXT);
	}

	public static FieldTypeDef longNumber() {
		return new FieldTypeDef(Kind.LONG);
	}

	public static FieldTypeDef knnVector(int dimension) {
		if (dimension <= 0) {
			throw new IllegalArgumentException("dimension must be > 0");
		}

		FieldTypeDef def = new FieldTypeDef(Kind.KNN_VECTOR);
		def.dimension = dimension;
		return def;
	}

	public FieldTypeDef stored() {
		return stored(true);
	}

	public FieldTypeDef stored(boolean stored) {
		this.stored = stored;
		return this;
	}

	public FieldTypeDef aggregatable() {
		return aggregatable(true);
	}

	public FieldTypeDef aggregatable(boolean aggregatable) {
		this.aggregatable = aggregatable;
		return this;
	}

	public FieldTypeDef sortable() {
		return sortable(true);
	}

	public FieldTypeDef sortable(boolean sortable) {
		this.sortable = sortable;
		return this;
	}

	public FieldTypeDef range() {
		return range(true);
	}

	public FieldTypeDef range(boolean range) {
		this.range = range;
		return this;
	}

	public FieldTypeDef similarity(VectorSimilarityFunction vectorSimilarityFunction) {
		if (vectorSimilarityFunction == null) {
			throw new IllegalArgumentException("vectorSimilarityFunction must not be null");
		}
		this.vectorSimilarityFunction = vectorSimilarityFunction;
		return this;
	}

	public Kind kind() {
		return kind;
	}

	public boolean is_stored() {
		return stored;
	}

	public boolean is_aggregatable() {
		return aggregatable;
	}

	public boolean is_sortable() {
		return sortable;
	}

	public boolean is_range() {
		return range;
	}

	public int get_dimension() {
		return dimension;
	}

	public VectorSimilarityFunction vectorSimilarityFunction() {
		return vectorSimilarityFunction;
	}
}