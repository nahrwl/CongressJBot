/* Based off of
 * the DropboxMirror project
 * by yetanotherx
 */

package io.github.techietotoro;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beust.jcommander.JCommander;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import com.yetanotherx.reddit.RedditPlugin;
import com.yetanotherx.reddit.api.modules.RedditCore;
import com.yetanotherx.reddit.api.modules.RedditLink;
import com.yetanotherx.reddit.exception.APIException;
import com.yetanotherx.reddit.util.LinkType;

public class App extends RedditPlugin
{
	private final String DATABASE = "heroku_app15671465";
	private final String COLLECTION = "guids";
	private final String GUID_ID = "guid";

	private MongoClient mongo;

	private Parameters params;

	public App(Parameters params) 
	{
		this.params = params;
	}

	public void run() throws InterruptedException
	{
		RedditCore.newFromUserAndPass(this, params.username, params.password).doLogin();
		String subName = params.subreddit;
		String dbURI = params.database;

		MongoClientURI mongoClientURI = new MongoClientURI(dbURI);
		try {
			this.mongo = new MongoClient(mongoClientURI);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		RSSFeedParser parser = new RSSFeedParser(params.feed);
		params = null;

		Feed feed = parser.readFeed();
		ArrayList<FeedItem> items = feed.getItems();

		for (FeedItem item : items)
		{
			if (item != null) {
				String title = "No Title";
				String link = "";
				String description = "No Description";

				if (item.getTitle() != null) title = item.getTitle();
				if (item.getLink() != null) link = item.getLink();
				if (item.getDescription() != null) description = item.getDescription();

				//put it all together in a nicey nice way
				StringBuilder builder = new StringBuilder();
				builder.append(description);
				builder.append("\n\n");
				builder.append("[Govtrack.us Summary](");
				builder.append(link);
				builder.append(")");
				String content = builder.toString();

				//check if the link as already been posted
				//boolean alreadyPosted = checkDuplicate(item.getGuid());
				boolean alreadyPosted = false;
				if (!alreadyPosted) {
					Thread.sleep(2000);
					try {
						RedditLink.doSubmit(this, title, content, subName, LinkType.SELF);
						this.insertEntry(item.getGuid());
						System.out.println("Posted!");
					} catch (APIException e) {
						System.out.println("An APIException occurred... :(");
						//System.out.println(sub.getSubredditData().getDisplayName());
						e.printStackTrace();
					}
				}

				Thread.sleep(2000); // no need to rush


			}
		}

		// RedditLink.doSubmit(this, "First post from bot!", "I'm trying to make a nice bot for some guys at futuristparty. Except I don't know python so I can't copy congressbot.", "cswider", LinkType.SELF);

		/*Transport transport = this.getTransport();
        Request request = new WebRequest(this);
        request.setURL(this.getRedditURL() + csSub.getSubredditData().getURL() + HTTPUtils.urlEncode("new.json"));
        transport.setRequest(request);

        Response response = transport.sendURL();
        JSONResult json = response.getJSONResult();

        for( MapNode node : json.getMapNodeList("data/children") ) {
            LinkData data = LinkData.newInstance(node.getMapNode("data"));
            RedditLink link = RedditLink.newFromLink(this, data);
            if (link != null) {
            	System.out.println("Link was not null! Attempting iteration.");
            	CommentData[] comments = link.getComments();
            	for (CommentData c : comments)
            	{
            		String text = c.getText();
            		if (text.matches(".*llama.*"))
            		{
            			try {
            				RedditComment newComment = RedditComment.newFromComment(this, c);
            				newComment.doReply("Hey, this comment contains 'llama' somewhere! This means you are officially awesome!");
            				System.out.println("Replied!");
            			} catch (Exception e) {
            				// something went wrong
            				System.out.println("Error in replying to llama comment.");
            			}
            			Thread.sleep(2000);
            		}
            		System.out.println("Iterated.");
            	}
            }
            else {
            	System.out.println("Link was null! Ignoring.");
            }
        }*/




	}

	private boolean checkDuplicate(String guid) {
		boolean result = false;

		DB db = mongo.getDB(DATABASE);
		DBCollection table = db.getCollection(COLLECTION);
		BasicDBObject document = new BasicDBObject();
		document.put(GUID_ID, guid);

		if (table.count() > 0) {
			DBCursor cursor = table.find(document);
			if (cursor.hasNext()) {
				result = true;
			}
		}

		return result;
	}

	private void insertEntry(String guid) {
		DB db = mongo.getDB(DATABASE);
		DBCollection table = db.getCollection(COLLECTION);
		BasicDBObject document = new BasicDBObject();
		document.put(GUID_ID, guid);
		table.insert(document);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Java-CongressBot v1.3.0 by /u/techietotoro";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.3.0";
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Parameters params = new Parameters();
		JCommander jc = new JCommander(params, args);
		try {
			new App(params).run();
		} catch (InterruptedException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}

