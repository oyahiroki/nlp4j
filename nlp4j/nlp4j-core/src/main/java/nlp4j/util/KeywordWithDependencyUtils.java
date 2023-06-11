package nlp4j.util;

import java.nio.file.ClosedFileSystemException;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.KeywordWithDependencyParser;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * <pre>
 * Extract Keyword with dependency
 * </pre>
 * 
 * created on 2023-04-02
 * 
 * @author Hiroki Oya
 *
 */
public class KeywordWithDependencyUtils {

	static public List<Keyword> extract(KeywordWithDependency kw) {
		return KeywordWithDependencyParser.parse(kw);
	}

	public static JsonObject toJson(KeywordWithDependency kwdWithDependency) {

		JsonArray nodes = new JsonArray();
		JsonArray edges = new JsonArray();

		setIds(kwdWithDependency, nodes, edges, 0);
		toJson(kwdWithDependency, nodes, edges);

		JsonObject network = new JsonObject();
		network.add("nodes", nodes);
		network.add("edges", edges);

		return network;

	}

	private static int setIds(KeywordWithDependency kwdWithDependency, JsonArray nodes, JsonArray edges, int id) {
		kwdWithDependency.setId("" + id);
		for (KeywordWithDependency child : kwdWithDependency.getChildren()) {
			id++;
			int i = setIds(child, nodes, edges, id);
			id = i;
		}
		return id;
	}

	private static void toJson(KeywordWithDependency kwdWithDependency, JsonArray nodes, JsonArray edges) {

		{
			JsonObject node = new JsonObject();
			{
				node.addProperty("id", Integer.parseInt(kwdWithDependency.getId()));
				node.addProperty("label", kwdWithDependency.getLex());
				{
					JsonObject obj = new JsonObject();
					obj.addProperty("upos", kwdWithDependency.getUPos());
					obj.addProperty("facet", kwdWithDependency.getFacet());
					obj.addProperty("lex", kwdWithDependency.getLex());
					obj.addProperty("str", kwdWithDependency.getStr());
					node.add("obj", obj);
				}
			}
			nodes.add(node);
		}

		for (KeywordWithDependency child : kwdWithDependency.getChildren()) {
			{
				String thisid = kwdWithDependency.getId();
				String childid = child.getId();
				if (thisid != null && childid != null) {
					JsonObject edge = new JsonObject();
					edge.addProperty("from", Integer.parseInt(thisid));
					edge.addProperty("to", Integer.parseInt(childid));
					edges.add(edge);
				}
			}
			toJson(child, nodes, edges);
		}

	}

}
