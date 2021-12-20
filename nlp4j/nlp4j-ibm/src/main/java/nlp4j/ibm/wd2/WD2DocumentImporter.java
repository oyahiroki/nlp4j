package nlp4j.ibm.wd2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.discovery.v2.Discovery;
import com.ibm.watson.discovery.v2.model.AddDocumentOptions;
import com.ibm.watson.discovery.v2.model.DocumentAccepted;

import nlp4j.AbstractDocumentImporter;
import nlp4j.Document;
import nlp4j.DocumentImporter;
import nlp4j.util.DocumentUtil;

public class WD2DocumentImporter extends AbstractDocumentImporter implements DocumentImporter {

	// DISCOVERY_APIKEY
	// DISCOVERY_URL
	// projectId
	// collectionId
	private String projectId;
	private String collectionId;
	private String DISCOVERY_APIKEY;
	private String DISCOVERY_URL;

	@Override
	public void importDocument(Document doc) throws IOException {

		String fileName;

		if (doc.getAttribute("filename") != null) {
			fileName = doc.getAttributeAsString("filename");
		} else {
			fileName = "hello" + System.currentTimeMillis() + ".json";
		}

		JsonObject jsonObj = DocumentUtil.toJsonObject(doc);
		jsonObj.remove("keywords");

		{
			IamAuthenticator authenticator = new IamAuthenticator(this.DISCOVERY_APIKEY);
			Discovery v2Discovery = new Discovery("2020-08-30", authenticator);
			v2Discovery.setServiceUrl(this.DISCOVERY_URL);
			AddDocumentOptions options = new AddDocumentOptions.Builder() //
					.projectId(this.projectId) //
					.collectionId(this.collectionId) //
					.file(new ByteArrayInputStream(jsonObj.toString().getBytes(StandardCharsets.UTF_8))) //
					.filename(fileName) //
					.fileContentType("application/json") //
					.build(); //

			DocumentAccepted response = v2Discovery.addDocument(options).execute().getResult();

			System.err.println(response.getStatus());
			System.err.println(response);

		}

	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if ("projectId".equals(key)) {
			this.projectId = value;
		} //
		else if ("collectionId".equals(key)) {
			this.collectionId = value;
		} //
		else if ("DISCOVERY_APIKEY".equals(key)) {
			this.DISCOVERY_APIKEY = value;
		} //
		else if ("DISCOVERY_URL".equals(key)) {
			this.DISCOVERY_URL = value;
		}

	}

	@Override
	public void commit() throws IOException {
	}

	@Override
	public void close() {
	}

}
