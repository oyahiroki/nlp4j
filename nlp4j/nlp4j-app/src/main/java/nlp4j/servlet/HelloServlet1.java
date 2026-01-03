package nlp4j.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet1
 */
@WebServlet("/helloservlet1")
public class HelloServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HelloServlet1() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		{ // Read resource properties file ...
			Properties prop = new Properties();
			try (InputStream inputStream = HelloServlet1.class.getResourceAsStream("/hello-java-jettyapp.properties")) {

				prop.load(inputStream);
				System.out.println("hello " + prop.getProperty("testkey", "xxx"));

			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		} // ... Read resource properties file

		response.getWriter().append("Served at: ").append(request.getContextPath()).append(" " + new Date());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
