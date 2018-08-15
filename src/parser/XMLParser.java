package parser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Michael James Siers
 * The XMLParser is an abstract class allowing for generic implementation of parsers
 * that use common xml parsing functions. 
 * @see ModelParser.java
 * @see LevelParser.java
 * @see WarriorParser.java
 */
public abstract class XMLParser {
	protected Document doc;
	
	/**
	 * Constructor that takes a filename as input then initializes the document
	 * object with data from that file.
	 * @param filename the filename of the file being parsed
	 */
	public XMLParser(String filename) {
		DocumentBuilder dBuilder = null;
		File file = new File(filename);
		
		if (!file.exists()) {
			System.out.println("Filename: " + filename + " not found.");
		}
		else {
			try {
				dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
			try {
				doc = dBuilder.parse(file);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
			buildObject();
		}
	}
	
	/**
	 * Builds an object from the parsed information. 
	 */
	public abstract void buildObject();
}
