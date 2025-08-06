package work202508.zipcode;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import nlp4j.tuple.Pair;
import nlp4j.util.CsvUtils;
import nlp4j.util.IOUtils;
import nlp4j.util.UnicodeUtils;

/**
 * <pre>
 * 
 * https://www.post.japanpost.jp/zipcode/dl/utf-zip.html
 * 7. 都道府県名
 * 8. 市区町村名
 * 9. 町域名
 * 
 * 
 * </pre>
 * 
 */
public class ProcessZipCode {

	public static void main(String[] args) throws Exception {
		File f = new File("r:/utf_ken_all.csv");

		try (InputStream is //
				= IOUtils.inputStream(f) // 1.3.7.18
		) {

			CSVParser parser = CSVParser.parse(is, //
					StandardCharsets.UTF_8, //
					CSVFormat.EXCEL.builder().build() //
			);

			final Set<String> set6 = new LinkedHashSet<String>();
			final Set<String> set7 = new LinkedHashSet<String>();
			final Set<String> set8 = new LinkedHashSet<String>();

			String s7 = null;
			String s8 = null;

			for (CSVRecord r : parser.getRecords()) {
//				System.err.println(r.size());
				{
//					System.err.println(r.get(6)); // 都道府県名
					{
						set6.add(r.get(6));
					}

				}
				{
//					System.err.println(r.get(7));
					set7.add(r.get(7));
					{
						String common = StringUtils.getCommonPrefix(s7, r.get(7));
						if (common != null && common.length() > 0 && common.length() != r.get(7).length()) {
							{
								String s7a = s7.substring(common.length());
//								System.err.println("* " + s7a);
								set7.add(s7a);
							}
							{
								String s2 = r.get(7).substring(common.length());
//								System.err.println("# " + common);
//								System.err.println("# " + s2);
								set7.add(common);
								set7.add(s2);
							}
						}
					}
					s7 = r.get(7);
				}
				{

					// 以下に掲載がない場合
					if ("以下に掲載がない場合".equals(r.get(8))) {
						s8 = null;
					} else {

						String s8b = r.get(8);

						s8b = UnicodeUtils.nfkc(s8b);

						s8b = s8b.replace("（", "(").replace("）", ")").replace("〜", "～");

						int idx = s8b.indexOf("(");
						if (idx != -1) {
							s8b = s8b.substring(0, idx).trim();
						}
//						System.err.println(s8b);
						set8.add(s8b);

						{
							String common = StringUtils.getCommonPrefix(s8, s8b);
							if (common != null && common.length() > 0 && common.length() != s8b.length()) {
								{
									String s8c = s8b.substring(common.length());
//									System.err.println("* " + s8c);
									set7.add(s8c);
								}
								{
									String s8d = s8b.substring(common.length());
//									System.err.println("# " + common);
//									System.err.println("# " + s8d);
									set7.add(common);
									set7.add(s8d);
								}
							}
						}
						s8 = s8b;
					}
//					System.err.println("--");
				}

			}

			Pair<PrintWriter, File> p = IOUtils.pwTemp();
			PrintWriter pw = p.getLeft();
			File temp = p.getRight();

//			System.err.println("--");
			for (String s : set6) {
				pw.println(s);
			}
//			System.err.println("--");
			for (String s : set7) {
				pw.println(s);
			}
//			System.err.println("--");
			for (String s : set8) {
				pw.println(s);
			}
//			System.err.println("--");

			System.err.println(temp.getAbsolutePath());

		}

	}

}
