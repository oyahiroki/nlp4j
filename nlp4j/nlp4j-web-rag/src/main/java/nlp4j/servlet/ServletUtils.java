package nlp4j.servlet;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

public class ServletUtils {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Parse Document from HTTP Request Body JSON
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	static public Document parse(HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("UTF-8");
		String contentType = request.getContentType();

		// if(request body is JSON)
		if (contentType.equals("text/json") //
				|| contentType.equals("application/json") //
				|| contentType.startsWith("text/json") //
				|| contentType.startsWith("application/json")) {
			String json = request.getReader().lines().collect(Collectors.joining("\r\n"));
			logger.debug("DEBUG: " + json);
			// JSON -> Document
			try {
				Document doc = DocumentUtil.parseFromJson(json);
				return doc;
			} catch (Exception e) {
//				e.printStackTrace();
//				response.setStatus(400); // Bad Requiest
//				response.getWriter().println("Invalid Json");
//				return;
				throw new IOException();
			}
		} else {
			throw new IOException();
		}
	}

}
