package com.adapter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class PersonHandler extends DefaultHandler {

	private Person currPerson;
	private StringBuilder content;
	private XMLReader reader;
	private SAXHandler parentHandler;

	public PersonHandler(XMLReader reader, SAXHandler parentHandler, String name) {
		this.reader = reader;
		this.parentHandler = parentHandler;
		this.currPerson = new Person(name);
		this.content = new StringBuilder();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.content.setLength(0);
		System.out.println("PersonHandler::Start Element: " + qName);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("age".equals(qName)) {
			this.currPerson.setAge(this.content.toString());
		} else if ("city".equals(qName)) {
			this.currPerson.setCity(this.content.toString());
		} else if ("person".equals(qName)) {
			this.parentHandler.addPerson(this.currPerson);
			this.reader.setContentHandler(this.parentHandler);
		}
		System.out.println("PersonHandler::End Element: " + qName);
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {
		this.content.append(ch, start, length);
	}

}