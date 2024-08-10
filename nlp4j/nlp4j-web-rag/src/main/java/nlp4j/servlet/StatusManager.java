package nlp4j.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StatusManager {

	private static final ConcurrentHashMap<String, List<String>> statusMap = new ConcurrentHashMap<>();

	public static void addStatus(String clientId, String status) {
		statusMap.computeIfAbsent(clientId, k -> new ArrayList<>()).add(status);
	}

	public static List<String> getStatuses(String clientId) {
		return statusMap.getOrDefault(clientId, new ArrayList<>());
	}

	public static void clearStatuses(String clientId) {
		statusMap.remove(clientId);
	}
}
