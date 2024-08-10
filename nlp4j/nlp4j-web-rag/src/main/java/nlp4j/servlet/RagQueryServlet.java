package nlp4j.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.annotator.KeywordFacetMappingAnnotator;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.solr.importer.SolrDocumentImporterVector;
import nlp4j.solr.search.SolrSearchClient;

/**
 * Servlet implementation class NlpServlet
 */
public class RagQueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RagQueryServlet() {
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

		try (PrintWriter pw = response.getWriter();) {

			String endPoint = "http://localhost:8983/solr/";
			String collection = "sandbox";

			Document doc = (new DocumentBuilder()).build();

			try {
				doc = ServletUtils.parse(request);
			} catch (IOException e) {
				response.setStatus(400); // Bad Requiest
				return;
			}

			try {
				{ // Kuromoji
					KuromojiAnnotator ann = new KuromojiAnnotator();
					ann.setProperty("target", "text");
					ann.annotate(doc);
				}
				{ // Keyword Facet Mapping for Solr
					KeywordFacetMappingAnnotator ann = new KeywordFacetMappingAnnotator();
					ann.setProperty("mapping", KeywordFacetMappingAnnotator.DEFAULT_MAPPING);
					ann.annotate(doc);
				}
				StatusServlet.setMessage("embedding...");
				{
					DocumentAnnotator ann = new EmbeddingAnnotator();
					ann.setProperty("target", "text");
					ann.annotate(doc);
					System.err.println(doc.getAttributeAsListNumbers("vector").size());

				}
				StatusServlet.setMessage("embedding...done");
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
				return;
			}

			StatusServlet.setMessage("searching...");
			{
				try (SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build()) {
					JsonObject responseObject = client.searchByVector(collection, doc);
					{
						{
							responseObject.remove("responseHeader");
							responseObject.addProperty("responseheader", "removed");
						}
						{
							JsonArray docs = responseObject.get("response").getAsJsonObject().get("docs")
									.getAsJsonArray();
							for (int n = 0; n < docs.size(); n++) {
								docs.get(n).getAsJsonObject().remove("vector");
							}
						}
					}

					System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));

					pw.println(responseObject.toString());

				} catch (IOException | RemoteSolrException e) {
					System.err.println("SKIP THIS TEST: " + e.getMessage());
				}
			}
			StatusServlet.setMessage("searching...done");

//			response.setContentType("application/javascript; charset=utf-8");
			response.setStatus(200);

//			for (int n = 0; n < 3; n++) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
			pw.flush();
			StatusServlet.setMessage("");

//			response.getWriter().println(JsonUtils.prettyPrint(DocumentUtil.toJsonString(doc)));

		}

	}

}
