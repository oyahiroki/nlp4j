package nlp4j.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatusServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static private String msg = null;

	static public void setMessage(String message) {
		msg = message;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		HttpSession session = request.getSession();
//		String sessionId = session.getId();
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		while (true) {
//			Integer progress = HeavyProcessServlet.getProgress(sessionId);
//			if (progress != null) {
//				response.getWriter().write("data: Progress: " + progress + "%\n\n");
//				response.getWriter().flush();
//
//				if (progress >= 100) {
//					break; // 処理完了時にループを終了
//				}
//			}

			if (msg != null) {
				synchronized (msg) {
					response.getWriter().write("data: " + msg + "\n\n");
					response.getWriter().flush();
				}
			}
			msg = null;

			try {
				Thread.sleep(100); // ステータスのポーリング間隔
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
