package nlp4j.twtr.crawler;

import java.util.ArrayList;

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

	private boolean debugEnabled = false;
	private String oAuthConsumerKey = null;
	private String oAuthConsumerSecret = null;
	private String oAuthAccessToken = null;
	private String oAuthAccessTokenSecret = null;
	private String query = null;

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);

		if (key.equals("debugEnabled")) {
			this.debugEnabled = Boolean.parseBoolean(value);
		} else if (key.equals("oauth.consumerKey")) {
			this.oAuthConsumerKey = value;
		} else if (key.equals("oauth.consumerSecret")) {
			this.oAuthConsumerSecret = value;
		} else if (key.equals("oauth.accessToken")) {
			this.oAuthAccessToken = value;
		} else if (key.equals("oauth.accessTokenSecret")) {
			this.oAuthAccessTokenSecret = value;
		} else if (key.equals("query")) {
			this.query = value;
		}

	}

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
		query.setCount(100);

		ArrayList<Document> docs = new ArrayList<>();

		try {
			QueryResult result = twitter.search(query);

			for (Status status : result.getTweets()) {

				Document doc = new DefaultDocument();

				String statusJSON = TwitterObjectFactory.getRawJSON(status);

//				System.err.println(JsonUtils.prettyPrint(statusJSON));
				doc.putAttribute("rawjson", statusJSON);

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

		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return docs;
	}

}