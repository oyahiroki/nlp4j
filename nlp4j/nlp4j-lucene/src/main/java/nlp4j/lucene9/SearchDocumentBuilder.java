package nlp4j.lucene9;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.KnnFloatVectorField;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.SortedSetDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.BytesRef;

/**
 * Builds a Lucene Document from SearchSchema.
 *
 * Example:
 *
 * Document doc = schema.document() .put("id", "1") .put("category", "greeting")
 * .put("content", "Hello Lucene") .put("created_at", 1710000000000L)
 * .putVector("vector", new float[] { ... }) .build();
 */
public class SearchDocumentBuilder {

	private final SearchSchema schema;
	// List to allow multiple values for the same field name (multi-valued fields)
	private final List<Map.Entry<String, Object>> values = new ArrayList<>();

	public SearchDocumentBuilder(SearchSchema schema) {
		if (schema == null) {
			throw new IllegalArgumentException("schema must not be null");
		}
		this.schema = schema;
	}

	public SearchDocumentBuilder put(String fieldName, String value) {
		putValue(fieldName, value);
		return this;
	}

	public SearchDocumentBuilder put(String fieldName, long value) {
		putValue(fieldName, Long.valueOf(value));
		return this;
	}

	public SearchDocumentBuilder put(String fieldName, int value) {
		putValue(fieldName, Long.valueOf(value));
		return this;
	}

	public SearchDocumentBuilder putVector(String fieldName, float[] vector) {
		putValue(fieldName, vector);
		return this;
	}

	public SearchDocumentBuilder putValue(String fieldName, Object value) {
		if (!schema.contains(fieldName)) {
			throw new IllegalArgumentException("Unknown field: " + fieldName);
		}
		if (value == null) {
			throw new IllegalArgumentException("value must not be null: " + fieldName);
		}

		values.add(new AbstractMap.SimpleImmutableEntry<>(fieldName, value));
		return this;
	}

	public Document build() {
		Document doc = new Document();

		for (Map.Entry<String, Object> entry : values) {
			String fieldName = entry.getKey();
			Object value = entry.getValue();
			FieldTypeDef def = schema.get(fieldName);

			addFields(doc, fieldName, def, value);
		}

		return doc;
	}

	private void addFields(Document doc, String fieldName, FieldTypeDef def, Object value) {
		switch (def.kind()) {
		case KEYWORD:
			addKeywordField(doc, fieldName, def, value);
			break;
		case TEXT:
			addTextField(doc, fieldName, def, value);
			break;
		case LONG:
			addLongField(doc, fieldName, def, value);
			break;
		case KNN_VECTOR:
			addKnnVectorField(doc, fieldName, def, value);
			break;
		case STORED_ONLY:
			addStoredOnlyField(doc, fieldName, def, value);
			break;
		default:
			throw new IllegalStateException("Unsupported field kind: " + def.kind());
		}
	}

	private void addKeywordField(Document doc, String fieldName, FieldTypeDef def, Object value) {
		String text = asString(fieldName, value);

		Field.Store store = def.is_stored() ? Field.Store.YES : Field.Store.NO;

		// Exact match search + optionally stored result value
		doc.add(new StringField(fieldName, text, store));

		// Aggregation / sort
		if (def.is_aggregatable() || def.is_sortable()) {
			if (def.is_multiValued()) {
				// Multi-valued: SortedSetDocValuesField allows multiple values per field per document
				doc.add(new SortedSetDocValuesField(fieldName, new BytesRef(text)));
			} else {
				// Single-valued: SortedDocValuesField
				doc.add(new SortedDocValuesField(fieldName, new BytesRef(text)));
			}
		}
	}

	private void addTextField(Document doc, String fieldName, FieldTypeDef def, Object value) {
		String text = asString(fieldName, value);

		Field.Store store = def.is_stored() ? Field.Store.YES : Field.Store.NO;

		// Full-text search + optionally stored result value
		doc.add(new TextField(fieldName, text, store));

		if (def.is_aggregatable() || def.is_sortable()) {
			throw new IllegalArgumentException("TEXT field cannot be aggregatable/sortable in this MVP: " + fieldName
					+ ". Use keyword field for aggregation/sort.");
		}
	}

	private void addLongField(Document doc, String fieldName, FieldTypeDef def, Object value) {
		long number = asLong(fieldName, value);

		// Range query
		if (def.is_range()) {
			doc.add(new LongPoint(fieldName, number));
		}

		// Sort / numeric aggregation foundation
		if (def.is_sortable() || def.is_aggregatable()) {
			doc.add(new NumericDocValuesField(fieldName, number));
		}

		// Stored result value
		if (def.is_stored()) {
			doc.add(new StoredField(fieldName, number));
		}
	}

	private void addKnnVectorField(Document doc, String fieldName, FieldTypeDef def, Object value) {
		float[] vector = asFloatArray(fieldName, value);

		if (vector.length != def.get_dimension()) {
			throw new IllegalArgumentException("Vector dimension mismatch. field=" + fieldName + ", expected="
					+ def.get_dimension() + ", actual=" + vector.length);
		}

		doc.add(new KnnFloatVectorField(fieldName, vector, def.vectorSimilarityFunction()));

		// Usually vectors are not stored because they are large.
		// If you need to return the vector itself, store it separately as JSON or
		// binary.
		if (def.is_stored()) {
			doc.add(new StoredField(fieldName + "_stored", toJsonArrayString(vector)));
		}
	}

	private String asString(String fieldName, Object value) {
		if (value instanceof String s) {
			return s;
		}
		throw new IllegalArgumentException("Field must be String: " + fieldName);
	}

	private long asLong(String fieldName, Object value) {
		if (value instanceof Long l) {
			return l.longValue();
		}
		if (value instanceof Integer i) {
			return i.longValue();
		}
		if (value instanceof Number n) {
			return n.longValue();
		}
		throw new IllegalArgumentException("Field must be long-compatible number: " + fieldName);
	}

	private float[] asFloatArray(String fieldName, Object value) {
		if (value instanceof float[] vector) {
			return vector;
		}
		throw new IllegalArgumentException("Field must be float[]: " + fieldName);
	}

	private String toJsonArrayString(float[] vector) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < vector.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(vector[i]);
		}
		sb.append("]");
		return sb.toString();
	}

	/**
		* Adds a stored-only field that is not searchable, sortable, or aggregatable.
		* Only stores the value for retrieval in search results.
		*
		* @param doc the Document to add the field to
		* @param fieldName the name of the field
		* @param def the field type definition
		* @param value the value to store (String or Long)
		*/
	private void addStoredOnlyField(Document doc, String fieldName, FieldTypeDef def, Object value) {
		if (value instanceof String text) {
			doc.add(new StoredField(fieldName, text));
		} else if (value instanceof Long number) {
			doc.add(new StoredField(fieldName, number.longValue()));
		} else if (value instanceof Integer number) {
			doc.add(new StoredField(fieldName, number.longValue()));
		} else if (value instanceof Number number) {
			doc.add(new StoredField(fieldName, number.longValue()));
		} else {
			throw new IllegalArgumentException(
					"STORED_ONLY field must be String or Number: " + fieldName + ", type=" + value.getClass());
		}
	}
}