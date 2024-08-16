package nlp4j.servlet;

import java.util.Date;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.util.DateUtils;

public class ChatHistory {

	JsonObject history;

	public ChatHistory() {
		history = new JsonObject();
		String ts = DateUtils.toISO8601(new Date());
		history.addProperty("conversation_id", ts + "_" + (new Random()).nextInt(10000));
		history.addProperty("timestamp", ts);
		history.addProperty("description", "this object is chat history of bot and user");
		JsonArray messages = new JsonArray();
		history.add("messages", messages);
	}

	public void addBotMessage(String bot_id, String msg) {
		JsonArray messages = history.get("messages").getAsJsonArray();
		{
			JsonObject message = new JsonObject();
			{
				message.addProperty("bot_id", bot_id);
				message.addProperty("message", msg);
				message.addProperty("timestamp", DateUtils.toISO8601(new Date()));
			}
			messages.add(message);
		}
	}

	public void addUserMessage(String user_id, String msg) {
		JsonArray messages = history.get("messages").getAsJsonArray();
		{
			JsonObject message = new JsonObject();
			{
				message.addProperty("user_id", user_id);
				message.addProperty("message", msg);
				message.addProperty("timestamp", DateUtils.toISO8601(new Date()));
			}
			messages.add(message);
		}
	}

	/**
	 *
	 */
	public String toString() {
		if (this.history == null) {
			return "null";
		}
		return this.history.toString();
	}

}
