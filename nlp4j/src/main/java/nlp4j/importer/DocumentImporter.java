package nlp4j.importer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import nlp4j.Document;

/**
 * ドキュメントインポーターのインターフェイスクラスです。<br/>
 * Interface class for document importer.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public interface DocumentImporter {

	/**
	 * ドキュメントをインポートしてコミットします。<br/>
	 * Import a document and commit.
	 * 
	 * @param doc
	 * @throws IOException
	 */
	public void importDocumentAndCommit(Document doc) throws IOException;

	/**
	 * 複数のドキュメントをインポートします。<br/>
	 * Import multiple documents.
	 * 
	 * @param docs
	 * @throws IOException
	 */
	public void importDocuments(List<Document> docs) throws IOException;

	/**
	 * 単一のドキュメントをインポートします。<br/>
	 * Import a single document.
	 * 
	 * @param doc
	 * @throws IOException
	 */
	public void importDocument(Document doc) throws IOException;

	/**
	 * インポートしたドキュメントをコミットします。<br/>
	 * Commit a imported document.
	 * 
	 * @throws IOException
	 */
	public void commit() throws IOException;

	/**
	 * プロパティをセットします。<br/>
	 * Set properties.
	 * 
	 * @param prop
	 */
	public void setProperties(Properties prop);

	/**
	 * プロパティをセットします。<br/>
	 * Set a property key and value.
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value);

	/**
	 * インデックスとの接続を閉じます。<br/>
	 * Close a connection of a index.
	 */
	public void close();

}