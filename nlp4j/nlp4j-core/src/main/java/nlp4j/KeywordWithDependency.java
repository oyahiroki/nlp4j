package nlp4j;

import java.util.List;

/**
 * 係り受けの依存関係をセットできるキーワードです。<br>
 * Keyword with Dependency.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface KeywordWithDependency extends Keyword, Cloneable {

	/**
	 * ノードのサイズ
	 * 
	 * @return
	 * @since 1.3.7.8
	 */
	public int size();

	/**
	 * 係り受けのラベル
	 * 
	 * @return dependency label
	 * @since 1.2.1.0
	 */
	public String getRelation();

	/**
	 * 係り受けのラベル
	 * 
	 * @param relation 係り受けのラベル
	 * @since 1.2.1.0
	 */
	public void setRelation(String relation);

	/**
	 * 依存関係の子ノードを追加します。子ノードには親ノードへの依存関係がセットされます。<br>
	 * Add a child keyword. A parent keyword is set to child keyword.
	 * 
	 * @param keyword キーワード
	 */
	public void addChild(KeywordWithDependency keyword);

	/**
	 * 依存関係の子ノードを追加します。子ノードには親ノードへの依存関係はセットされません。<br>
	 * Add a child keywod without set a parent to child keyword.
	 * 
	 * @param keyword キーワード
	 */
	public void addChildOnly(KeywordWithDependency keyword);

	/**
	 * 依存関係のあるキーワードをリストとして返します。<br>
	 * Return keywords as a list from dependency keywords.
	 * 
	 * @return 依存関係のあるキーワード
	 */
	public List<KeywordWithDependency> asList();

	/**
	 * 依存関係のある子ノードのキーワードを返します。
	 * 
	 * @return 子ノード
	 */
	public List<KeywordWithDependency> getChildren();

	/**
	 * もっとも親の位置にあるキーワード（ルートキーワード）からの深さの位置を返します。
	 * 
	 * @return キーワードの深さ
	 */
	public int getDepth();

	/**
	 * 親の依存関係のあるキーワードを返します。
	 * 
	 * @return キーワード
	 */
	public KeywordWithDependency getParent();

	/**
	 * 階層の数を指定して親の依存関係のあるキーワードを返します。例えば２の場合、２段階親のキーワードを返します。
	 * 
	 * @param depth 深さ
	 * @return キーワード
	 */
	public KeywordWithDependency getParent(int depth);

	/**
	 * 親の依存関係にあるキーワードで最も親（ルート）に位置するキーワードを返します。このキーワードが最も親のキーワードである場合 null を返します。
	 * 
	 * @return キーワード
	 */
	public KeywordWithDependency getRoot();

	/**
	 * 文の中での出現順（連番）を返します。
	 * 
	 * @return キーワード
	 */
	public int getSequence();

	/**
	 * 子の依存関係にあるキーワードがあるかどうかを返します。
	 * 
	 * @return 子の依存関係にあるキーワードがあるかどうか
	 */
	public boolean hasChild();

	/**
	 * 親の依存関係にあるキーワードがあるかどうかを返します。
	 * 
	 * @return 親の依存関係にあるキーワードがあるかどうか
	 */
	public boolean hasParent();

	/**
	 * 最も親の依存関係にあるキーワードを返します。このキーワードが最も親の場合 null を返します。
	 * 
	 * @return 最も親の依存関係にあるキーワード
	 */
	public boolean isRoot();

	/**
	 * 親の依存関係にあるキーワードをセットします。親のキーワードにはこのキーワードが子キーワードとしてセットされます。
	 * 
	 * @param parent 親の依存関係にあるキーワード
	 */
	public void setParent(KeywordWithDependency parent);

	/**
	 * 親の依存関係にあるキーワードをセットします。親のキーワードにはこのキーワードが子キーワードとしてセットされません。
	 * 
	 * @param parent 親の依存関係にあるキーワード
	 */
	public void setParentOnly(KeywordWithDependency parent);

	/**
	 * 文の中での出現順（連番）をセットします。
	 * 
	 * @param sequence 連番
	 */
	public void setSequence(int sequence);

	/**
	 * キーワードの依存関係を文字列で表現します。
	 * 
	 * @return キーワードの依存関係
	 */
	public String toStringAsDependencyList();

	/**
	 * キーワードの依存関係を文字列で表現します。
	 * 
	 * @return キーワードの依存関係
	 */
	public String toStringAsDependencyTree();

	/**
	 * キーワードの依存関係をXML形式の文字列で表現します。
	 * 
	 * @return キーワードの依存関係
	 */
	public String toStringAsXml();

	/**
	 * 階層を指定してキーワードの依存関係をXML形式の文字列で表現します。
	 * 
	 * @param depth このオブジェクトの階層の深さ
	 * @return 文字列
	 */
	Object toStringAsXml(int depth);

}
