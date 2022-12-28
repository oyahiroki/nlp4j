package nlp4j.dictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DictionaryNode {

	// root
	// terminal

	List<DictionaryNode> children = new ArrayList<DictionaryNode>();

	Character c = null;

	public DictionaryNode() {

	}

	public DictionaryNode(char c) {
		this.c = c;
	}

	public void put(String s) {
		for (int n = 0; n < s.length(); n++) {
			char c = s.charAt(n);
			DictionaryNode cNode = new DictionaryNode(c);
			if (this.children.contains(cNode) == true) {
				DictionaryNode pt = this.children.get(this.children.indexOf(cNode));
			}else {
				
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DictionaryNode) {
			if (this.c != null && ((DictionaryNode) obj).c != null) {
				return (this.c == ((DictionaryNode) obj).c);
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

}
