package nlp4j.webcrawler.nhtsa;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.crawler.AbstractCrawler;
import nlp4j.crawler.Crawler;
import nlp4j.impl.DefaultDocument;
import nlp4j.webcrawler.AbstractWebCrawler;

/**
 * since 20210213
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 */
public class NhtsaCrawler extends AbstractWebCrawler implements Crawler {

	static String[] headers;

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static List<String> makesFilterList;

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

//		String makesFilter = "NISSAN,TESLA";
//		makesFilterList = Arrays.asList(makesFilter.split(","));
	}

	private String charset = "UTF-8";
	private String encoding = "UTF-8";

	// private static final int minDate = 20000101;
	private int minDate = Integer.MIN_VALUE;

	boolean trim = true;

	private final String zipEntryFileName = "FLAT_CMPL.txt";

	@Override
	public List<Document> crawlDocuments() {

		String input = super.prop.getProperty("input");

		File inputFile = new File(input);

		try {
			if (inputFile.exists() == true) {
				String ext = FilenameUtils.getExtension(inputFile.getAbsolutePath());
				if (ext.equals("zip")) {
					logger.info("Read ZIP: " + inputFile.getAbsolutePath());
					return readAsZip(inputFile);
				} //
				else if (ext.equals("txt")) {
					logger.info("Read TXT: " + inputFile.getAbsolutePath());
					return readAsTxt(inputFile);
				} //
				else {
					return new ArrayList<Document>();
				}
			} //
			else {
				logger.warn("File Not Found: " + inputFile.getAbsolutePath());
				return new ArrayList<Document>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Document>();
		}

	}

	private List<Document> read(InputStream is, String encoding) throws IOException {

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

			if (makesFilterList == null || makesFilterList.contains(data[3])) {

				Document doc = new DefaultDocument();

				for (int n = 0; n < headers.length; n++) {
					if (data.length >= n + 1) {
						String key = headers[n];
						String value = data[n];
						doc.putAttribute(key, value);
					}
				}

				try {
					int dateAsInt = Integer.parseInt(doc.getAttributeAsString("DATEA"));
					if (dateAsInt < minDate) {
						continue;
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					continue;
				}

				docs.add(doc);

//				System.err.println(data[3]); // MAKES

				if (docs.size() % 1000 == 0) {
					logger.info("Reading docs: " + String.format("%,d", docs.size()));
				}

				count2++;
			}

			if (makes.contains(data[3]) == false) {
				makes.add(data[3]);
			}
		}

//		System.err.println(count);
//		System.err.println(count2);

		Collections.sort(makes);

		// PRINT MAKES
		System.err.println(Arrays.toString(makes.toArray(new String[0])));

		return docs;

	}

	private List<Document> readAsTxt(File inputFile) throws IOException {

		try (FileInputStream fis = new FileInputStream(inputFile);) {
			return read(fis, encoding);
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

				if (zipentry.getName().equals(zipEntryFileName)) {

					return read(zis, encoding);

				}
			}

			return null;

		} catch (IOException e) {
			throw e;
		}

	}

	@Override
	public void setProperty(String key, String value) {

		if (key == null || value == null) {
			return;
		}

		super.setProperty(key, value);

		if (key.equals("minDate")) {
			try {
				this.minDate = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} //
		else if (key.equals("MAKETXT")) {
			makesFilterList = Arrays.asList(value.split(","));
		}

	}

}
