package nlp4j.wiki.entity;

public abstract class AbstractWikiEntity implements WikiEntity {

	String text;
	String name;

	public boolean isEmpty() {
		if (this.text == null) {
			return true;
		} else {
			return this.text.trim().isEmpty();
		}
	}

	public boolean isTemplate() {
		if (this.text != null && this.text.length() > 2 && this.text.startsWith("{{")) {
			return true;
		} else {
			return false;
		}
	}

	public String getText() {
		return text;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		if (text == null) {
			return getClass().getName() + " " + "[text=" + text + "]";
		} else {
			return getClass().getName() + " " + "[text=" + text.replace("\n", "\\n").replace("\r", "\\r") + "]";
		}
	}

}
