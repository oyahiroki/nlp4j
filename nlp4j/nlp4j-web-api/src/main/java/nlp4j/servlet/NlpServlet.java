package nlp4j.servlet;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

/**
 * Servlet implementation class NlpServlet
 */
@WebServlet(urlPatterns = "/nlp")
public class NlpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NlpServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// gson
		Gson gson = new Gson();

		// request body json
		JsonObject requestJson = null;

		Document doc = null;

		String contentType = request.getContentType();

		// if(request body is JSON)
		if (contentType.equals("text/json") //
				|| contentType.equals("application/json") //
				|| contentType.startsWith("text/json") //
				|| contentType.startsWith("application/json")) {
			String json = request.getReader().lines().collect(Collectors.joining("\r\n"));
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

//		{
//			DocumentAnnotator annotator = new YJpMaAnnotator();
//			try {
//				annotator.annotate(doc);
//			} catch (Exception e) {
//				e.printStackTrace();
//				response.setStatus(500); // Server Error
//				response.getWriter().println("Server Error");
//				return;
//			}
//		}

		response.setContentType("application/javascript; charset=utf-8");
		response.setStatus(200);

		response.getWriter().println(JsonUtils.prettyPrint(DocumentUtil.toJsonString(doc)));
		response.getWriter().close();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/javascript; charset=utf-8");
		response.setStatus(200);

		Document doc = new DefaultDocument();
		response.getWriter().println(JsonUtils.prettyPrint(DocumentUtil.toJsonString(doc)));
		response.getWriter().close();
	}

}
