package nlp4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.crawler.Crawler;
import nlp4j.impl.DefaultDocumentAnnotatorPipeline;

/**
 * Crawl, Annotate, Import Documents.
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class DocumentProcessor {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	Properties props;
	ArrayList<Document> docs;

	/**
	 * @param props
	 */
	public DocumentProcessor(Properties props) {

		this.props = props;

		{
			ClassLoader cl = ClassLoader.getSystemClassLoader();

			System.err.println(cl.getClass().getCanonicalName());

			try {
				if (cl instanceof URLClassLoader) {
					URL[] urls = ((URLClassLoader) cl).getURLs();
					logger.info("class loader URLs:");
					for (URL url : urls) {
						logger.info(url.getFile());
					}
				} //
				else {
					System.err.println("classloader: " + cl.getClass().getCanonicalName());
				}
			} catch (ClassCastException ex) {

			}
		}

		checkClassExists(props);

	}

	private void checkClassExists(Properties props) {
		// crawler[n].name
		for (int n = 0; props.getProperty("crawler[" + n + "].name") != null; n++) {
			String classNameKey = "crawler[" + n + "].name";
			String className = props.getProperty(classNameKey);
			try {
				Class<?> clazz = Class.forName(className);
				logger.info("CLASS FOUND for crawler: " + clazz.getName());
			} catch (ClassNotFoundException e) {
				logger.info("CLASS NOT FOUND for crawler: " + className);
				throw new RuntimeException(e);
			}
		}

		// annotator[n].name
		for (int n = 0; props.getProperty("annotator[" + n + "].name") != null; n++) {
			String classNameKey = "annotator[" + n + "].name";
			String className = props.getProperty(classNameKey);
			try {
				Class<?> clazz = Class.forName(className);
				logger.info("CLASS FOUND for annotator: " + clazz.getName());
			} catch (ClassNotFoundException e) {
				logger.info("CLASS NOT FOUND for annotator: " + className);
				throw new RuntimeException(e);
			}
		}

		// importer[n].name
		for (int n = 0; props.getProperty("importer[" + n + "].name") != null; n++) {
			String classNameKey = "importer[" + n + "].name";
			String className = props.getProperty(classNameKey);

			try {
				Class<?> clazz = Class.forName(className);
				logger.info("CLASS FOUND for importer: " + clazz.getName());
			} catch (ClassNotFoundException e) {
				logger.info("CLASS NOT FOUND for importer: " + className);
				throw new RuntimeException(e);
			}
		}
	}

	public ArrayList<Document> getDocs() {
		return docs;
	}

	/**
	 * Crawl Documents
	 * 
	 * @throws Exception
	 */
	public List<Document> crawlDocuments() throws Exception {

		ArrayList<Crawler> crawlers = new ArrayList<>();

		// crawler[n].name
		for (int n = 0; props.getProperty("crawler[" + n + "].name") != null; n++) {
			String crawlerNameKey = "crawler[" + n + "].name";
			String crawlerName = props.getProperty(crawlerNameKey);

			Crawler crawler = null;

			try {
				crawler = (Crawler) Class.forName(crawlerName).newInstance();
				crawlers.add(crawler);
			} catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}

			// crawler[n].param[m].key
			// crawler[n].param[m].value
			for (int m = 0; m < 10; m++) {
				String paramBase = "crawler[" + n + "].param[" + m + "]";
				if (props.getProperty(paramBase + ".key") != null) {
					String key = props.getProperty(paramBase + ".key");
					String value = props.getProperty(paramBase + ".value");
					crawler.setProperty(key, value);
				}
			}
		} // END FOR

		docs = new ArrayList<>();

		for (Crawler crawler : crawlers) {
			docs.addAll(crawler.crawlDocuments());
		}

		logger.info("Crawled documents: " + docs.size());

		return docs;
	}

	/**
	 * Annotate Documents. Add metadata to documents.
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public List<Document> annotateDocuments() throws Exception {

		DocumentAnnotatorPipeline pipeline = new DefaultDocumentAnnotatorPipeline();

		initAnnotators(pipeline); // throws ReflectiveOperationException, ClassNotFoundException

		if (props.getProperty("pipeline.processpath") == null
				|| props.getProperty("pipeline.processpath").equals("1") == false) {
			// Annotatorsごとに処理
			pipeline.annotate(docs);
		} else {
			// Documentごとに処理
			for (int n = 0; n < docs.size(); n++) {
				if (n % 10 == 1) {
					logger.info("Processing ... " + (n + 1));
				}
				pipeline.annotate(docs.get(n));
			}
		}

		int countDoc = 0;

		for (Document doc : docs) {

			countDoc++;

			if (countDoc % 10 == 1) {
				logger.info("Processing ... " + countDoc);
			}

			for (String key : doc.getAttributeKeys()) {
				logger.debug(key + "=" + doc.getAttribute(key));
			}

			logger.debug(doc.getAttribute("description"));

			for (Keyword kwd : doc.getKeywords()) {
				logger.debug("Keyword=" + kwd);
			}
		}

		return docs;

	}

	private void initAnnotators(DocumentAnnotatorPipeline pipeline) throws Exception {

		// annotator[n].name
		for (int n = 0;; n++) {
			String crawlerNameKey = "annotator[" + n + "].name";
			if (props.getProperty(crawlerNameKey) == null) {
				break;
			} //

			String crawlerName = props.getProperty(crawlerNameKey);

			DocumentAnnotator annotator = null;
			try {
				annotator = (DocumentAnnotator) Class.forName(crawlerName).newInstance();
				pipeline.add(annotator);
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				throw e;
			} catch (ClassNotFoundException e) {
				logger.error("CONFIGURATION ERROR: Class Not Found: " + e.getMessage());
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				throw e;
			}
			// annotator[n].param[m].key
			// annotator[n].param[m].value
			for (int m = 0;; m++) {
				String paramBase = "annotator[" + n + "].param[" + m + "]";
				if (props.getProperty(paramBase + ".key") == null) {
					break;
				} //
					// ELSE (annotator[n].param[m] != null) THEN
				else {
					String key = props.getProperty(paramBase + ".key");
					String value = props.getProperty(paramBase + ".value");
					annotator.setProperty(key, value);
				}
			}
		}
	}

	/**
	 * Import documents to document repository, database, index, etc.
	 * 
	 * @throws Exception
	 */
	public void importDocuments() throws Exception {
		for (int n = 0; n < 10; n++) {

			String importerObjName = "importer[" + n + "]";
			String crawlerNameKey = "importer[" + n + "].name";

			if (props.getProperty(crawlerNameKey) != null) {

				String importerName = props.getProperty(crawlerNameKey);

				String endPoint = null;
				for (int m = 0; m < 100; m++) {
					String paramBase = importerObjName + ".param[" + m + "]";
					if (props.getProperty(paramBase + ".key") == null) {
						break;
					} else {
						if (props.getProperty(paramBase + ".key").equals("endPoint") == false) {
							continue;
						} else {
							endPoint = props.getProperty(paramBase + ".value");
						}
					}
				}

				Object[] oo = { endPoint };

				DocumentImporter importer = null;
				try {
					Class<?> importerClass = Class.forName(importerName);
					Class<?>[] types = {};
					Constructor<?> constructor = importerClass.getConstructor(types);
					importer = (DocumentImporter) constructor.newInstance();
					logger.info("Importer instance created. " + importer.getClass().getName());
				} catch (Throwable e) {
					e.printStackTrace();
					return;
				}

				{ // Initialize importer

					for (int m = 0;; m++) {
						String key1 = importerObjName + ".param[" + m + "].key";
						String key2 = importerObjName + ".param[" + m + "].value";
						if (props.get(key1) == null) {
							break;
						} //
						else {
							String value1 = props.getProperty(key1);
							String value2 = props.getProperty(key2);
							importer.setProperty(value1, value2);
						}
					}

					for (Object key : props.keySet()) {
						String k = (String) key;

						if (k.startsWith(importerObjName)) {
							Object value = props.get(key);
							String valueS = (String) value;

							importer.setProperty(k.substring(importerObjName.length() + 1), valueS);
						}
					}

				}

				int success = 0;
				int failed = 0;
				int all = docs.size();
				ArrayList<String> failedReport = new ArrayList<>();
				for (Document doc : docs) {
					try {
						importer.importDocument(doc);
						importer.commit();
						success++;
					} catch (IOException e) {
						e.printStackTrace();
						failed++;
						String report = "failed," //
								+ failed + "," //
								+ doc.getId() + "," //
								+ doc.getAttribute("resource") + "," //
								+ doc.getAttribute("filename") + "," //
								+ doc.getAttribute("recordnumber") //
//								+ "," + doc.toString() //
						;//
						failedReport.add(report);
					}
					logger.info("all=" + all + ",success=" + success + ",failed=" + failed);
				}
				importer.close();
				if (failedReport.size() > 0) {
					System.err.println("Failed Report:");
					for (String s : failedReport) {
						System.err.println(s);
					}
				} else {
					System.err.println("NO Failed Report.");
				}
			}
		} // End of Importers

	}

	/**
	 * @param args args[0] File path of property
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		logger.info("Start");
		logger.info("args: " + Arrays.toString(args));

		if (args == null || args.length != 1) {
			System.err.println("How to use this class...");
			System.err.println(DocumentProcessor.class.getName() + " properties_file_path");
			return;
		}

		String propFileName = args[0];

		Properties props = new Properties();
		File propsFile = new File(propFileName);

		if (propsFile.exists() == false) {
			System.err.println("ERROR - NOT FOUND: " + propsFile.getAbsolutePath());
			return;
		}
		if (propsFile.isFile() == false) {
			System.err.println("ERROR - NOT FILE: " + propsFile.getAbsolutePath());
			return;
		}

		try (FileInputStream fis = new FileInputStream(propsFile);
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader br = new BufferedReader(isr)) {

			props.load(br); // throws IOException

		}

//mojibake		props.load(new FileInputStream(propsFile)); // throws IOException

		DocumentProcessor dc = new DocumentProcessor(props);

		// crawl documents
		dc.crawlDocuments();
		// annotate documents
		dc.annotateDocuments();
		// import documents
		dc.importDocuments();

	}

}
