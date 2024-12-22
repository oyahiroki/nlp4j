package nlp4j.twtr.crawler;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.InvalidPropertyException;
import nlp4j.crawler.AbstractCrawler;
import nlp4j.crawler.Crawler;
import nlp4j.impl.DefaultDocument;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Twitter検索を利用して文書をクロールします。<br>
 * プロパティ<br>
 * debugEnabled<br>
 * oauth.consumerKey<br>
 * oauth.consumerSecret<br>
 * oauth.accessToken<br>
 * oauth.accessTokenSecret<br>
 * query 検索クエリー<br>
 * 
 * @author Hiroki Oya
 * @since 1.0
 *
 */
public class TwitterCrawler extends AbstractCrawler implements Crawler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private boolean debugEnabled = false;
	private String oAuthConsumerKey = null;
	private String oAuthConsumerSecret = null;
	private String oAuthAccessToken = null;
	private String oAuthAccessTokenSecret = null;
	private String query = null;

	File outFile = null;

	private final int COUNT = 100;
	private final int LOOP_MAX = 15;

	long maxId = -1;

	@Override
	public ArrayList<Document> crawlDocuments() {

		if (this.oAuthConsumerKey == null //
				|| this.oAuthConsumerSecret == null //
				|| this.oAuthAccessToken == null //
				|| this.oAuthAccessTokenSecret == null //
				|| this.query == null) {
			throw new InvalidPropertyException("something", null);
		}

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb //
				.setJSONStoreEnabled(true) //
				.setDebugEnabled(this.debugEnabled) //
				.setOAuthConsumerKey(this.oAuthConsumerKey) //
				.setOAuthConsumerSecret(this.oAuthConsumerSecret) //
				.setOAuthAccessToken(this.oAuthAccessToken) //
				.setOAuthAccessTokenSecret(this.oAuthAccessTokenSecret);

		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		Query query = new Query(this.query);
		{
			query.setCount(COUNT);
			if (this.maxId != -1) {
				query.setSinceId(maxId);
				logger.info("query_sinceid: " + this.maxId);
			}
		}

		ArrayList<Document> docs = new ArrayList<>();

		Gson gson = new Gson();

		try {

			for (int count = 0; count < LOOP_MAX; count++) {

				logger.info("Search count: " + (count + 1));
				logger.info("query: " + query.toString());

				QueryResult result = twitter.search(query);

				logger.info("Tweets Count: " + result.getCount());

				for (Status status : result.getTweets()) {

					Document doc = new DefaultDocument();

					{
						String statusJSON = TwitterObjectFactory.getRawJSON(status);
//						System.err.println(JsonUtils.prettyPrint(statusJSON));
//						doc.putAttribute("rawjson", statusJSON); // 1.0
						JsonObject tweet = gson.fromJson(statusJSON, JsonObject.class);
						if (tweet.get("id") != null) {
							long latestid = tweet.get("id").getAsLong();
							if (latestid > this.maxId) {
								this.maxId = latestid;
							}
						}
						doc.putAttribute("rawjson", tweet); // 1.1
						if (this.outFile != null) {
							try {
								boolean append = true;
								String encoding = "UTF-8";
								String data = statusJSON + "\n";
								FileUtils.write(outFile, data, encoding, append);
							} catch (IOException e) {
								logger.error(e.getMessage(), e);
							}
						}
					}

					doc.putAttribute("source", status.getSource());

					doc.putAttribute("user_followercount", status.getUser().getFollowersCount());
					doc.putAttribute("user_location", status.getUser().getLocation());
					doc.putAttribute("user_screenname", status.getUser().getScreenName());
					doc.putAttribute("user_id", status.getUser().getId());

					doc.putAttribute("text", status.getText());
					doc.putAttribute("id", status.getId());
					doc.putAttribute("created_at", status.getCreatedAt());
					doc.putAttribute("favorite_count", status.getFavoriteCount());
					doc.putAttribute("retweet_count", status.getRetweetCount());
					// System.out.println("@" + status.getUser().getScreenName() + ":" +
					// status.getText());

					docs.add(doc);
				}

				if (result.hasNext()) {
					query = result.nextQuery();
				}
				//
				else {
					break;
				}

			}

		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return docs;
	}

	/**
	 * @param key   debugEnabled | oauth.consumerKey | oauth.consumerSecret |
	 *              oauth.accessToken | oauth.accessTokenSecret
	 * @param value
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if (key.equals("debugEnabled")) {
			this.debugEnabled = Boolean.parseBoolean(value);
		} //
		else if (key.equals("oauth.consumerKey")) {
			this.oAuthConsumerKey = value;
		} //
		else if (key.equals("oauth.consumerSecret")) {
			this.oAuthConsumerSecret = value;
		} //
		else if (key.equals("oauth.accessToken")) {
			this.oAuthAccessToken = value;
		} //
		else if (key.equals("oauth.accessTokenSecret")) {
			this.oAuthAccessTokenSecret = value;
		} //
		else if (key.equals("query")) {
			this.query = value;
		} //
		else if (key.equals("outfile")) {
			File file = new File(value);
			// IF(OUTFILE NOT EXIST) THEN
			if (file.exists() == false) {
				File parentDir = file.getParentFile();
				if (parentDir.exists() == false) {
					try {
						FileUtils.forceMkdir(parentDir);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			// ELSE (OUTFILE EXIST)
			else {
				// CHECK MAX ID OF TWEETS FOR NEXT QUERY
				List<String> lines;
				try {
					lines = FileUtils.readLines(file, "UTF-8");
					for (String line : lines) {
						Gson gson = new Gson();
						JsonObject tweet = gson.fromJson(line, JsonObject.class);
						if (tweet == null) {
							logger.warn("file may not be JSON");
						} else {
							long id = tweet.get("id").getAsLong();
							if (id > this.maxId) {
								this.maxId = id;
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				logger.info("maxId: " + this.maxId);
			}
			this.outFile = file;
		}

	}

}
