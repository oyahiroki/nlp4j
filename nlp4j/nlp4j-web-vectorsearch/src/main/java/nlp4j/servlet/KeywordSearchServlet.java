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
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.servlet.util.ServletUtils;
import nlp4j.solr.search.SolrSearchClient;

/**
 * キーワード検索を実行する
 */
public class KeywordSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KeywordSearchServlet() {
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

			//
			// ...
			//

			logger.info("searching ...");
			{
				try (SolrSearchClient client = new SolrSearchClient.Builder(solr_endPoint).build()) {

					// Azure Search 形式の検索リクエストパラメータ
					JsonObject query_azure = new JsonObject();
					{
						query_azure.addProperty("search", "text_txt_ja:" + doc.getText());
						query_azure.addProperty("select", "score,id,text_txt_ja");
					}

					JsonObject responseObject = new JsonObject();

					// 検索
					JsonObject solrResponseObject = client.search(solr_collection, query_azure);

					responseObject.addProperty("type", "solr");
					responseObject.add("response", solrResponseObject);

					System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));

					pw.println(responseObject.toString());

				} catch (IOException | RemoteSolrException e) {
					System.err.println("SKIP THIS TEST: " + e.getMessage());
				}
			}
			logger.info("searching ... done");

			response.setStatus(200);
			pw.flush();

		} // END_OF_TRY()
	} // END_OF_doPost()
} // END_OFF_CLASS
