package io.github.techietotoro;

import java.util.ArrayList;

public class Feed {

	private final String title;
	private final String link;
	private final String description;
	private final String language;
	private final String copyright;
	private final String pubDate;

	private final ArrayList<FeedItem> items = new ArrayList<FeedItem>();

	public Feed(String newTitle, String newLink, String newDescription, String newLanguage, String newCopyright, String newPubdate)
	{
		title = newTitle;
		link = newLink;
		description = newDescription;
		language = newLanguage;
		copyright = newCopyright;
		pubDate = newPubdate;
	}

	public static Feed newFromURL(String url)
	{
		return null;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	public String getLanguage() {
		return language;
	}

	public ArrayList<FeedItem> getItems() {
		return items;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getPubDate() {
		return pubDate;
	}
}
