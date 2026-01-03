package nlp4j.servlet;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.KeywordWithDependency;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;
import nlp4j.util.KeywordWithDependencyUtils;
import nlp4j.yhoo_jp.YJpDaAnnotatorV2;
import nlp4j.yhoo_jp.YJpMaAnnotator;

/**
 * Servlet implementation class NlpServlet
 */
public class NlpServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NlpServlet2() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// gson
//		Gson gson = new Gson();
		// request body json
//		JsonObject requestJson = null;

		Document doc = null;

		String contentType = request.getContentType();

		// if(request body is JSON)
		if (contentType.equals("text/json") //
				|| contentType.equals("application/json") //
				|| contentType.startsWith("text/json") //
				|| contentType.startsWith("application/json")) {
			String json = request.getReader().lines().collect(Collectors.joining("\r\n"));
			System.err.println("DEBUG: " + json);
			// JSON -> Document
			try {
				doc = DocumentUtil.parseFromJson(json);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(400); // Bad Requiest
				response.getWriter().println("Invalid Json");
				return;
			}
		}

		if (doc == null) {
			response.setStatus(400); // Bad Requiest
			return;
		}

		{
			YJpDaAnnotatorV2 annotator = new YJpDaAnnotatorV2();
			try {
				annotator.annotate(doc);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500); // Server Error
				response.getWriter().println("Server Error");
				return;
			}
		}

		String jsonDocString = DocumentUtil.toJsonString(doc);

		JsonObject jsonDocObj = (new Gson()).fromJson(jsonDocString, JsonObject.class);

		for (KeywordWithDependency kwd : doc.getKeywords(KeywordWithDependency.class)) {
			JsonObject json = KeywordWithDependencyUtils.toJson(kwd);
			jsonDocObj.add("dependency", json);
		}

		response.setContentType("application/javascript; charset=utf-8");
		response.setStatus(200);

		response.getWriter().println(JsonUtils.prettyPrint(jsonDocObj));
		response.getWriter().close();
	}

}
