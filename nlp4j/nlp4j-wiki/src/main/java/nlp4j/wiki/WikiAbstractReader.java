package nlp4j.wiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.wiki.util.MediaWikiFileUtils;

public class WikiAbstractReader {

	enum Status {
		FEED, DOC, TITLE, URL, ABSTRACT, LINKS, SUBLINK, CLOSE_DOC
	}

	File file;

	public WikiAbstractReader(File file) {
		this.file = file;
	}

	DocumentHandler handler;

	public void setHandler(DocumentHandler handler) {
		this.handler = handler;
	}

	Document doc;

	public void read() throws IOException {

		if (file == null || file.exists() == false || file.canRead() == false) {
			throw new FileNotFoundException();
		}

		Path p = this.file.toPath();

		try (InputStream is = Files.newInputStream(p);
				GZIPInputStream gzis = new GZIPInputStream(is);
				InputStreamReader isr = new InputStreamReader(gzis, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr);) {
			String s;

			Status status = Status.FEED;

			while ((s = br.readLine()) != null) {

//				System.err.println(s);
//				System.err.println("STATUS=" + status.toString());

				if (status == Status.FEED) {
					status = Status.DOC;
					continue;
				} //
				else if (status == Status.DOC) {
					this.doc = new DefaultDocument();
					status = Status.TITLE;
					continue;
				} //
				else if (status == Status.TITLE) {
					// <title>Wikipedia: アンパサンド</title>
					String title = s.substring(7, s.length() - 8);
//					System.err.println("TITLE=" + title);
					doc.putAttribute("title", title);
					status = Status.URL;
					continue;
				} //
				else if (status == Status.URL) {
					String url = s.substring(5, s.length() - 6);
//					System.err.println(url);
					this.doc.putAttribute("url", url);
					status = Status.ABSTRACT;
					continue;
				} //
				else if (status == Status.ABSTRACT) {
					if (s.startsWith("<abstract>")) {
						String a = s.substring(10, s.length() - 11);
//						System.err.println(a);
						this.doc.putAttribute("abstract", a);
						status = Status.LINKS;
						continue;
					}
					// no abstarct
					else {
						status = Status.LINKS;
						continue;
					}
				} //
				else if (status == Status.LINKS) {

					status = Status.SUBLINK;
					continue;
				} //
				else if (status == Status.SUBLINK) {
					if (s.startsWith("<sublink")) {
						continue;
					}
					// </links>
					else {
						status = Status.CLOSE_DOC;
						continue;
					}

				} else if (status == Status.CLOSE_DOC) {
					status = Status.DOC;
					if (this.handler != null) {
						try {
							handler.read(doc);
						} catch (BreakException e) {
							break;
						}
					}
					continue;
				} //
				else {
					// ???
				}

				// <feed>
				// <doc>
				// <title></title>
				// <url>
				// <abstract>
				// <links>
				// sublink ...
				// </links>
				// </doc>

			}

		}

	}

	public static void main(String[] args) throws Exception {
		File abstFile = MediaWikiFileUtils.getAbstarctFile("/usr/local/data/wiki/20220501/", "ja", "wiki", "20220501");
//		System.err.println(abstFile.exists());

		DocumentHandler handler = new DocumentHandler() {
			int count = 0;

			@Override
			public void read(Document document) throws BreakException {
				String abs = document.getAttributeAsString("abstract");
				if (abs != null && abs.length() > 32) {
					abs = abs.substring(0, 32);
				}
				System.err.println(count + " " + document.getAttribute("title") + ":" + abs);
				count++;
				if (count > 10000) {
					throw new BreakException();
				}
			}
		};

		WikiAbstractReader reader = new WikiAbstractReader(abstFile);
		reader.setHandler(handler);
		reader.read();

	}

}
