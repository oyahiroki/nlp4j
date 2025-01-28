package nlp4j.converter;

import java.io.File;
import java.io.IOException;

public interface Converter {

	public void convert(File jsonl_file, File csv_file) throws IOException;

}
