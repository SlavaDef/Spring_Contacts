package ua.kiev.prog.config;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ContactParserFromServak {

    public Document buildDocument() throws Exception {

        File file = new File("Contacts.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        return dbFactory.newDocumentBuilder().parse(file);
    }
}
