package com.example.hejiayuan.vocabulary.utils;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParserFactory;

public class XMLParser {
    public SAXParserFactory factory = null;
    public XMLReader reader = null;

    public XMLParser() {
        try {
            factory = SAXParserFactory.newInstance();
            reader = factory.newSAXParser().getXMLReader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseXml(DefaultHandler content, InputSource inSource) {
        if (inSource == null)
            return;
        try {
            reader.setContentHandler(content);
            reader.parse(inSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
