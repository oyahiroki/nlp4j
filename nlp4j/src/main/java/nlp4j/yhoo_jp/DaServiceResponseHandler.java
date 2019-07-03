/**
 * 
 */
package nlp4j.yhoo_jp;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import nlp4j.AbstractXmlHandler;

/**
 * @author oyahiroki
 *
 */
public class DaServiceResponseHandler extends AbstractXmlHandler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		logger.debug(super.getPath());
		logger.debug(super.getText());

		super.endElement(uri, localName, qName);
	}

}
