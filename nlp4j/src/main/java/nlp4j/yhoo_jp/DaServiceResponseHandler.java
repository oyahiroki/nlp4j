/**
 * 
 */
package nlp4j.yhoo_jp;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import nlp4j.KeywordWithDependency;
import nlp4j.impl.KeywordWithDependencyImpl;
import nlp4j.xml.AbstractXmlHandler;

/**
 * @author oyahiroki
 *
 */
public class DaServiceResponseHandler extends AbstractXmlHandler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	KeywordWithDependencyImpl kwd;

	HashMap<String, KeywordWithDependencyImpl> map = new HashMap<String, KeywordWithDependencyImpl>();

	StringBuilder surface = new StringBuilder();

	KeywordWithDependency root;

	int sequence = 0;

	public KeywordWithDependency getRoot() {
		return root;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		logger.debug(super.getPath());
		logger.debug(super.getText());

		if ("ResultSet/Result/ChunkList/Chunk/Id".equals(super.getPath())) {
			String id = super.getText();
			kwd = new KeywordWithDependencyImpl();
			kwd.setSequence(sequence);
			map.put(id, kwd);
		} //
		else if ("ResultSet/Result/ChunkList/Chunk/Dependency".equals(super.getPath())) {
			String dependencyKey = super.getText();
			kwd.setDependencyKey(dependencyKey);
		} //

		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem/Surface".equals(super.getPath())) {
			surface.append(super.getText());
		} //
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList".equals(super.getPath())) {
			kwd.setStr(surface.toString());
			surface = new StringBuilder();
		} //
		else if ("ResultSet/Result/ChunkList/Chunk".equals(super.getPath())) {
			sequence++;
		} //
		else if ("ResultSet".equals(super.getPath())) {
			for (KeywordWithDependencyImpl kwd : map.values()) {
				if (map.get(kwd.getDependencyKey()) != null) {
					kwd.setParent(map.get(kwd.getDependencyKey()));
				}
			}
			root = map.get("0").getRoot();
		}

		super.endElement(uri, localName, qName);
	}

}
