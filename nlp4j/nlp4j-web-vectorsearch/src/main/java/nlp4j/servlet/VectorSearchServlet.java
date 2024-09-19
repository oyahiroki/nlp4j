package nlp4j.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.servlet.util.ServletUtils;
import nlp4j.solr.search.SolrSearchClient;

/**
 * ベクトル検索を実行する
 */
public class VectorSearchServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VectorSearchServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/javascript; charset=utf-8");
		response.setCharacterEncoding("UTF-8");

		String solr_endPoint = Config.SOLR_ENDPOINT;
		String solr_collection = Config.SOLR_COLLECTION;

		try (PrintWriter pw = response.getWriter();) {

			Document doc = (new DocumentBuilder()).build();

			try {
				doc = ServletUtils.parse(request);
			} catch (IOException e) {
				response.setStatus(400); // Bad Requiest
				return;
			}

			try {
				logger.info("embedding...");
				{
					EmbeddingAnnotator ann = new EmbeddingAnnotator();
					logger.info(ann.toString());
					ann.setProperty("target", "text");
					ann.annotate(doc);
					System.err.println(doc.getAttributeAsListNumbers("vector").size());

				}
				logger.info("embedding...done");
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
				return;
			}

			logger.info("searching ...");
			{
				try (SolrSearchClient client = new SolrSearchClient.Builder(solr_endPoint).build()) {
					JsonObject responseObject = client.searchByVector(solr_collection, doc);

					removeVectorData(responseObject);
					if (logger.isDebugEnabled()) {
						logger.debug(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));
					}
					pw.println(responseObject.toString());
				} catch (IOException | RemoteSolrException e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
			}
			logger.info("searching ... done");

			response.setStatus(200);

			pw.flush();
		}
	}

	private void removeVectorData(JsonObject solrResponseObject) {
		{
			solrResponseObject.remove("responseHeader");
			solrResponseObject.addProperty("responseheader", "removed");
		}
		{
			JsonArray docs = solrResponseObject.get("response").getAsJsonObject().get("docs").getAsJsonArray();
			for (int n = 0; n < docs.size(); n++) {
				docs.get(n).getAsJsonObject().remove("vector");
			}
		}
	}

}
