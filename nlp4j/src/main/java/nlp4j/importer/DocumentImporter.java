package nlp4j.importer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import nlp4j.Document;

public interface DocumentImporter {

	public void importDocumentAndCommit(Document doc) throws IOException;

	public void importDocuments(List<Document> docs) throws IOException;

	public void importDocument(Document doc) throws IOException;

	public void commit() throws IOException;

	public void setProperties(Properties prop);

	public void setProperty(String key, String value);

	public void close();

}