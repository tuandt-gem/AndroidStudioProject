package com.adapter;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

	private XMLReader reader;
	public List<Person> people = new ArrayList<Person>();

	public static void main(String[] args) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			SAXHandler tester = new SAXHandler(saxParser.getXMLReader());
			saxParser.parse("workbook.xml", tester);
			System.out.println(tester.people);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SAXHandler(XMLReader reader) {
		this.reader = reader;
	}

	public Person addPerson(Person person) {
		this.people.add(person);
		return person;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("person".equals(qName)) {
			String name = attributes.getValue("name");
			this.reader.setContentHandler(new PersonHandler(this.reader, this,
					name));
		}
		System.out.println("Start Element: " + qName);
	}
}