package nlp4j;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * ドキュメントインポーターのインターフェイスクラスです。<br>
 * Interface class for document importer.
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public interface DocumentImporter {

	/**
	 * ドキュメントをインポートしてコミットします。<br>
	 * Import a document and commit.
	 * 
	 * @param doc インポート対象のドキュメント
	 * @throws IOException 例外発生時にスローされる
	 */
	public void importDocumentAndCommit(Document doc) throws IOException;

	/**
	 * 複数のドキュメントをインポートします。<br>
	 * Import multiple documents.
	 * 
	 * @param docs インポート対象のドキュメント
	 * @throws IOException 例外発生時にスローされる
	 */
	public void importDocuments(List<Document> docs) throws IOException;

	/**
	 * 単一のドキュメントをインポートします。<br>
	 * Import a single document.
	 * 
	 * @param doc インポート対象のドキュメント
	 * @throws IOException 例外発生時にスローされる
	 * @throws Exception 
	 */
	public void importDocument(Document doc) throws IOException;

	/**
	 * インポートしたドキュメントをコミットします。<br>
	 * Commit a imported document.
	 * 
	 * @throws IOException 例外発生時にスローされる
	 */
	public void commit() throws IOException;

	/**
	 * プロパティをセットします。<br>
	 * Set properties.
	 * 
	 * @param prop プロパティ
	 */
	public void setProperties(Properties prop);

	/**
	 * プロパティをセットします。<br>
	 * Set a property key and value.
	 * 
	 * @param key   プロパティのキー
	 * @param value プロパティの値
	 */
	public void setProperty(String key, String value);

	/**
	 * インデックスとの接続を閉じます。<br>
	 * Close a connection of a index.
	 */
	public void close();

}