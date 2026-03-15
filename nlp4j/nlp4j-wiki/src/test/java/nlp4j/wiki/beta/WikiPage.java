package nlp4j.wiki.beta;

import java.util.Map;

public record WikiPage(String title, String plainText, Map<String, String> infobox) {
}
