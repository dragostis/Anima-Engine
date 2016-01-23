package org.anima.engine.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class Parser {
    private Element element;

    public Parser(InputStream inputStream) throws ParserConfigurationException, SAXException,
            IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);

        element = document.getDocumentElement();
    }

    public Parser(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public Parser getParser(String tag) {
        Element element;

        NodeList nodeList = this.element.getElementsByTagName(tag);

        element = (Element) nodeList.item(0);

        return new Parser(element);
    }

    public String getString(String tag) {
        String string;

        NodeList nodeList = element.getElementsByTagName(tag);

        string = nodeList.item(0).getFirstChild().getNodeValue();

        return string;
    }

    public int getInt(String tag) {
        return Integer.parseInt(getString(tag));
    }

    public float getFloat(String tag) {
        return Float.parseFloat(getString(tag));
    }
}
