package nlp4j.nhtsa;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import nlp4j.Document;
import nlp4j.crawler.AbstractCrawler;
import nlp4j.crawler.Crawler;
import nlp4j.impl.DefaultDocument;

public class NhtsaCrawler extends AbstractCrawler implements Crawler {

	static String[] headers;
	static {
		String header = "CMPLID,ODINO,MFR_NAME,MAKETXT,MODELTXT," //
				+ "YEARTXT,CRASH,FAILDATE,FIRE,INJURED," //
				+ "DEATHS,COMPDESC,CITY,STATE,VIN," //
				+ "DATEA,LDATE,MILES,OCCURENCES,CDESCR," //
				+ "CMPL_TYPE,POLICE_RPT_YN,PURCH_DT,ORIG_OWNER_YN,ANTI_BRAKES_YN," //
				+ "CRUISE_CONT_YN,NUM_CYLS,DRIVE_TRAIN,FUEL_SYS,FUEL_TYPE," //
				+ "TRANS_TYPE,VEH_SPEED,DOT,TIRE_SIZE,LOC_OF_TIRE," //
				+ "TIRE_FAIL_TYPE,ORIG_EQUIP_YN,MANUF_DT,SEAT_TYPE,RESTRAINT_TYPE," //
				+ "DEALER_NAME,DEALER_TEL,DEALER_CITY,DEALER_STATE,DEALER_ZIP," //
				+ "PROD_TYPE,REPAIRED_YN,MEDICAL_ATTN,VEHICLES_TOWED_YN"; //

		headers = header.split(",");

		String makesFilter = "NISSAN,TESLA";
		makesFilterList = Arrays.asList(makesFilter.split(","));
	}

	String charset = "UTF-8";
	String encoding = "UTF-8";

	String fileName = "FLAT_CMPL.txt";

	boolean trim = true;

	static List<String> makesFilterList;

	@Override
	public List<Document> crawlDocuments() {

		String input = super.prop.getProperty("input");

		File inputFile = new File(input);

		try {
			if (inputFile.exists() == true) {

				String ext = FilenameUtils.getExtension(inputFile.getAbsolutePath());
				if (ext.equals("zip")) {
					return readAsZip(inputFile);
				} //
				else if (ext.equals("txt")) {
					return readAsTxt(inputFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private List<Document> process(InputStream is, String encoding) throws IOException {

		ArrayList<Document> docs = new ArrayList<>();

		List<String> lines = IOUtils.readLines(is, encoding);

		int count = 0;
		int count2 = 0;

		ArrayList<String> makes = new ArrayList<>();

		for (String line : lines) {
			count++;
			String[] data = line.split("\t");

			for (int n = 0; n < data.length; n++) {
				if (data[n] != null) {
					data[n] = data[n].trim();
				}
			}

			if (makesFilterList.contains(data[3])) {

				Document doc = new DefaultDocument();

				for (int n = 0; n < headers.length; n++) {
					if (data.length >= n + 1) {
						String key = headers[n];
						String value = data[n];
						doc.putAttribute(key, value);
					}
				}

				docs.add(doc);

				System.err.println(data[3]);
				count2++;
			}

			if (makes.contains(data[3]) == false) {
				makes.add(data[3]);
			}
		}

		System.err.println(count);
		System.err.println(count2);
		Collections.sort(makes);
		System.err.println(Arrays.toString(makes.toArray(new String[0])));

		return docs;

	}

	private List<Document> readAsTxt(File inputFile) throws IOException {

		try (FileInputStream fis = new FileInputStream(inputFile);) {
			return process(fis, encoding);
		} catch (IOException e) {
			throw e;
		}

	}

	private List<Document> readAsZip(File inputFile) throws IOException {

		try (FileInputStream fis = new FileInputStream(inputFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ZipInputStream zis = new ZipInputStream(bis, Charset.forName(charset));) {

			ZipEntry zipentry;
			while ((zipentry = zis.getNextEntry()) != null) {

				System.err.println(zipentry.getName());

				if (zipentry.getName().equals(fileName)) {

					return process(zis, encoding);

				}
			}

			return null;

		} catch (IOException e) {
			throw e;
		}

	}

	public static void main(String[] args) {

		NhtsaCrawler crawler = new NhtsaCrawler();

//		crawler.setProperty("input", "C:/usr/local/nlp4j/collections/nhtsa/data/FLAT_CMPL.zip");
//		crawler.setProperty("input", "C:/usr/local/nlp4j/collections/nhtsa/data/FLAT_CMPL_1000.txt");
		crawler.setProperty("input", "C:/usr/local/nlp4j/collections/nhtsa/data/FLAT_CMPL_100.txt");

		List<Document> docs = crawler.crawlDocuments();
		for (Document doc : docs) {
//			System.err.println(doc.getAttribute("COMPDESC"));
			System.err.println(doc.getAttribute("CDESCR"));

		}
	}

}
