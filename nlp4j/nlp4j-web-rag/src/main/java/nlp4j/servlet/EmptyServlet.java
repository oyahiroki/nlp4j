package nlp4j.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

/**
 * Servlet implementation class NlpServlet
 */
public class EmptyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmptyServlet() {
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

			try {
				Document doc = ServletUtils.parse(request);
				response.setStatus(200);
				pw.println(JsonUtils.prettyPrint(DocumentUtil.toJsonString(doc)));
			} catch (IOException e) {
				response.setStatus(400); // Bad Requiest
				return;
			}

			{
//				DocumentAnnotator annotator = new YJpMaAnnotator();
//				try {
//					annotator.annotate(doc);
//				} catch (Exception e) {
//					e.printStackTrace();
//					response.setStatus(500); // Server Error
//					response.getWriter().println("Server Error");
//					return;
//				}
			}

//			response.setContentType("application/javascript; charset=utf-8");

		}

	}

}
