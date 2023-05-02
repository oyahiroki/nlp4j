package nlp4j.wiki.infobox;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * created_on: 2023-05-02
 * 
 * @author Hiroki Oya
 *
 */
public class Infobox {

	private String name;
	private Map<String, String> keyvalue = new LinkedHashMap<>();

	public Infobox(String name, Map<String, String> keyvalue) {
		super();
		this.name = name;
		this.keyvalue = keyvalue;
	}

	public Map<String, String> getKeyvalue() {
		return keyvalue;
	}

	public String getName() {
		return name;
	}

	public void setKeyvalue(Map<String, String> keyvalue) {
		this.keyvalue = keyvalue;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Infobox [name=" + name + ", keyvalue=" + keyvalue.toString().replace("\n", "\\n") + "]";
	}

}
