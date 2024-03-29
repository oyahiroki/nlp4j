package nlp4j.impl;

import java.io.IOException;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;

/**
 * ドキュメントをインデックスにインポートするクラスです。<br>
 * Document Importer for document index.
 * 
 * @author Hiroki Oya
 * @since 1.0
 */
public class DefaultDocumentImporter extends AbstractDocumentImporter implements DocumentImporter {

	@Override
	public void setProperty(String key, String value) {
		System.err.println("key=" + key + ",value=" + value);
	}

	@Override
	public void importDocument(Document doc) throws IOException {
		System.err.println("import: " + doc);
	}

	@Override
	public void commit() {
		System.err.println("Commit!");

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
