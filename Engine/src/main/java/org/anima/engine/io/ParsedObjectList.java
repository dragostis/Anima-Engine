package org.anima.engine.io;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public abstract class ParsedObjectList<T> {
    private Element elements;
    private String tag;

    public ParsedObjectList(Parser parser, String tag) {
        elements = parser.getElement();
        this.tag = tag;
    }

    public List<T> getList() {
        List<T> list = new ArrayList<T>();

        NodeList nodeList = elements.getElementsByTagName(tag);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            Parser parser = new Parser(element);

            T object = getObject(parser);

            list.add(object);
        }

        return list;
    }

    public abstract T getObject(Parser parser);
}
