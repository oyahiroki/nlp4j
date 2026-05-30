package nlp4j.lucene9;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Schema definition for Lucene documents.
 *
 * This class is intentionally small. It maps logical field names to
 * FieldTypeDef definitions.
 */
public class SearchSchema {

	private final Map<String, FieldTypeDef> fields = new LinkedHashMap<>();

	public SearchSchema add(String fieldName, FieldTypeDef fieldTypeDef) {
		if (fieldName == null || fieldName.isBlank()) {
			throw new IllegalArgumentException("fieldName must not be blank");
		}
		if (fieldTypeDef == null) {
			throw new IllegalArgumentException("fieldTypeDef must not be null");
		}
		if (fields.containsKey(fieldName)) {
			throw new IllegalArgumentException("Field already exists: " + fieldName);
		}

		fields.put(fieldName, fieldTypeDef);
		return this;
	}

	public FieldTypeDef get(String fieldName) {
		FieldTypeDef def = fields.get(fieldName);
		if (def == null) {
			throw new IllegalArgumentException("Unknown field: " + fieldName);
		}
		return def;
	}

	public boolean contains(String fieldName) {
		return fields.containsKey(fieldName);
	}

	public Set<String> fieldNames() {
		return Collections.unmodifiableSet(fields.keySet());
	}

	public Map<String, FieldTypeDef> asMap() {
		return Collections.unmodifiableMap(fields);
	}

	public SearchDocumentBuilder document() {
		return new SearchDocumentBuilder(this);
	}
}