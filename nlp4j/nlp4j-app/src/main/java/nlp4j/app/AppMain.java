package nlp4j.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import nlp4j.servlet.HelloServlet1;
import nlp4j.servlet.HelloServlet2EventStream;
import nlp4j.servlet.HelloServlet3;
import nlp4j.servlet.NlpServlet;
import nlp4j.servlet.NlpServlet2;

public class AppMain {
	public static void main(String[] args) throws Exception {
		boolean devMode = false;
		final String contextPath = "/"
//				+ "nlp4j-app"
				+ "";
		final int port = 8080;

		for (String arg : args) {
			if (arg.equals("--dev")) {
				devMode = true;
			}
		}

		// Jettyサーバーをポート8080で作成
		Server server = new Server(port);

		// WebAppContextを設定
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath(contextPath);
		webapp.setWelcomeFiles(new String[] { "index.html", "index.jsp" });

		if (devMode) {
			webapp.setResourceBase("src/main/webapp");
		} //
		else {
			System.err.println(AppMain.class.getClassLoader().getResource("webapp").toExternalForm());
			webapp.setResourceBase(AppMain.class.getClassLoader().getResource("webapp").toExternalForm());
		}

		// JSPサポートを追加
//		webapp.addServlet(new ServletHolder(new JspServlet()), "*.jsp");

		// デフォルトサーブレットを設定（静的ファイル用）
		ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
		if (devMode) {
			defaultServlet.setInitParameter("resourceBase", "src/main/webapp");
		} else {
			defaultServlet.setInitParameter("resourceBase", webapp.getResourceBase());
		}
		defaultServlet.setInitParameter("dirAllowed", "true");
		webapp.addServlet(defaultServlet, "/");

		// Servletを追加
		webapp.addServlet(new ServletHolder(new HelloServlet1()), "/helloservlet1");
		webapp.addServlet(new ServletHolder(new HelloServlet2EventStream()), "/helloservlet2");
		webapp.addServlet(new ServletHolder(new HelloServlet3()), "/helloservlet3");
		webapp.addServlet(new ServletHolder(new NlpServlet()), "/nlp");
		webapp.addServlet(new ServletHolder(new NlpServlet2()), "/nlp2");

		// サーバーにWebAppContextを設定
		server.setHandler(webapp);

		System.out.println("http://localhost:" + port + contextPath + "index.html");
		// サーバーを起動
		server.start();
		server.join();

	}
}
