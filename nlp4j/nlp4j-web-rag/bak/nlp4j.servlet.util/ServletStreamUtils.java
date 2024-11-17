package nlp4j.servlet.util;

import java.io.PrintWriter;

import com.google.gson.JsonObject;

public class ServletStreamUtils {

	static public void printMessageStream(PrintWriter pw, String msg) {
		JsonObject jo = new JsonObject();
		jo.addProperty("type", "message");
		jo.addProperty("value", msg);
		pw.write("data: " + jo.toString() + "\n\n");
		pw.flush();
	}

}
