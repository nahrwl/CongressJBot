package io.github.techietotoro;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.net.MalformedURLException;
import java.net.URL;

public class XMLParser {
	static final String TITLE = "title_without_number";
	static final String TYPE = "bill_resolution_type";
	static final String NUMBER = "display_number";
	static final String DATE = "current_status_date";
	static final String ID = "id";
	static final String LINK = "link";
	static final String CURRENT_STATUS = "current_status_description";
	
	final URL url;

	public XMLParser(String feedUrl) {
	    try {
	      this.url = new URL(feedUrl);
	    } catch (MalformedURLException e) {
	      throw new RuntimeException(e);
	    }
	}

	public void parse() {
		try {
			 
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(url.openStream());
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			Node objects = doc.getElementsByTagName("objects").item(0);
			NodeList nList = objects.getChildNodes();
		 
			System.out.println("----------------------------");
		 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					// Change these to all the Item properties (title, status, etc.)
					System.out.println("Title: " + eElement.getElementsByTagName(TITLE).item(0).getTextContent());
					System.out.println("Type: " + eElement.getElementsByTagName(TYPE).item(0).getTextContent());
					System.out.println("Number: " + eElement.getElementsByTagName(NUMBER).item(0).getTextContent());
					System.out.println("Date: " + eElement.getElementsByTagName(DATE).item(0).getTextContent());
		 
				}
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }

	}
	
}
