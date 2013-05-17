/* Based off of
 * the DropboxMirror project
 * by yetanotherx
 */

package io.github.techietotoro;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beust.jcommander.JCommander;

import com.yetanotherx.reddit.RedditPlugin;
import com.yetanotherx.reddit.api.data.LinkData;
import com.yetanotherx.reddit.api.modules.RedditCore;
import com.yetanotherx.reddit.api.modules.RedditLink;
import com.yetanotherx.reddit.api.modules.RedditSubreddit;
import com.yetanotherx.reddit.exception.APIException;
import com.yetanotherx.reddit.http.request.Request;
import com.yetanotherx.reddit.http.request.WebRequest;
import com.yetanotherx.reddit.http.response.JSONResult;
import com.yetanotherx.reddit.http.response.Response;
import com.yetanotherx.reddit.redditbot.http.Transport;
import com.yetanotherx.reddit.util.LinkType;
import com.yetanotherx.reddit.util.MapNode;

/**
 * Hello world!
 *
 */
public class App extends RedditPlugin
{
	private Parameters params;

	public App(Parameters params) 
	{
		this.params = params;
	}

	public void run() throws InterruptedException
	{
		RedditCore.newFromUserAndPass(this, params.username, params.password).doLogin();
		String subName = params.subreddit;
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
				boolean alreadyPosted = checkDuplicate(subName, title, content);

				if (!alreadyPosted) {
					Thread.sleep(2000);
					try {
						RedditLink.doSubmit(this, title, content, subName, LinkType.SELF);
						System.out.println("Posted!");
					} catch (APIException e) {
						System.out.println("An APIException occurred... :(");
						//System.out.println(sub.getSubredditData().getDisplayName());
						e.printStackTrace();
					}
				}

				Thread.sleep(2000);


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

	private boolean checkDuplicate(String subName, String title, String content) {
		boolean result = false;
		RedditSubreddit sub = RedditSubreddit.newFromName(this, subName);

		Transport transport = this.getTransport();
		Request request = new WebRequest(this);
		request.setURL(this.getRedditURL() + sub.getSubredditData().getURL() + "new.json?limit=50");
		transport.setRequest(request);

		Response response = transport.sendURL();
		JSONResult json = response.getJSONResult();

		for( MapNode node : json.getMapNodeList("data/children") ) {
			LinkData data = LinkData.newInstance(node.getMapNode("data"));
			RedditLink link = RedditLink.newFromLink(this, data);
			if (link != null) {
				//System.out.println("Link was not null! Attempting iteration.");
				//check if the titles and content are the same
				result = result || (title.toLowerCase().contains(link.getLinkData().getTitle().toLowerCase()));
				result = result || (title.toLowerCase().contains(link.getLinkData().getSelfText().toLowerCase()));
			}
			else {
				System.out.println("Link was null! Ignoring.");
			}
		}

		return result;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Java-CongressBot v1.2.1 by /u/techietotoro";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.2.1";
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

