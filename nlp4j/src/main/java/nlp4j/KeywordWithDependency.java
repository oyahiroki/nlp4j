package nlp4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 係り受けの依存関係をセットできるキーワードです。
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface KeywordWithDependency extends Keyword {

	/**
	 * 依存関係の子ノードを追加します。子ノードには親ノードへの依存関係がセットされます。
	 * 
	 * @param keyword
	 */
	public void addChild(KeywordWithDependency keyword);

	/**
	 * 依存関係の子ノードを追加します。子ノードには親ノードへの依存関係はセットされません。
	 * 
	 * @param keyword
	 */
	public void addChildOnly(KeywordWithDependency keyword);

	/**
	 * 依存関係のあるキーワードをリストとして返します。
	 * 
	 * @return 依存関係のあるキーワード
	 */
	public List<KeywordWithDependency> asList();

	/**
	 * 依存関係のある子ノードのキーワードを返します。
	 * 
	 * @return 子ノード
	 */
	public ArrayList<KeywordWithDependency> getChildren();

	/**
	 * もっとも親の位置にあるキーワード（ルートキーワード）からの深さの位置を返します。
	 * 
	 * @return
	 */
	public int getDepth();

	/**
	 * 親の依存関係のあるキーワードを返します。
	 * 
	 * @return
	 */
	public KeywordWithDependency getParent();

	/**
	 * 階層の数を指定して親の依存関係のあるキーワードを返します。例えば２の場合、２段階親のキーワードを返します。
	 * 
	 * @param depth 深さ
	 * @return
	 */
	public KeywordWithDependency getParent(int depth);

	/**
	 * 親の依存関係にあるキーワードで最も親（ルート）に位置するキーワードを返します。このキーワードが最も親のキーワードである場合 null を返します。
	 * 
	 * @return
	 */
	public KeywordWithDependency getRoot();

	/**
	 * 文の中での出現順（連番）を返します。
	 * 
	 * @return
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

	Object toStringAsXml(int i);

}
