package ua.kiev.prog.config;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ContactParserFromProject {

 public Group group2 = new Group();


    public ContactParserFromProject() {
    }

    // метод для зчитування директорії
    public  Document buildDocument() throws Exception {

        File file = new File("contacts444.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        return dbFactory.newDocumentBuilder().parse(file);
    }

    // метод для парсингу саме контактів те що в тегу контакти
    public  List<Contact> parseContacts(Node contactsNode) {
         List<Contact> contacts = new ArrayList<>();
        NodeList contactList = contactsNode.getChildNodes();
        // цей фор проходить всередині контактів і отримує всю інфу про поля контактів
        for (int i = 0; i < contactList.getLength(); i++) {
            if (contactList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (!contactList.item(i).getNodeName().equals("element")) {
                continue;
            }
            String name = "";
            String surname = "";
            String phone = "";
            String email = "";

            NodeList elementsChilds = contactList.item(i).getChildNodes();

            for (int j = 0; j < elementsChilds.getLength(); j++) {
                if (elementsChilds.item(j).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                switch (elementsChilds.item(j).getNodeName()) {
                    case "name": {
                        name = elementsChilds.item(j).getTextContent();
                        break;
                    }
                    case "surname": {
                        surname = elementsChilds.item(j).getTextContent();
                        break;
                    }
                    case "phone": {
                        phone = elementsChilds.item(j).getTextContent();
                        break;
                    }
                    case "email": {
                        email = elementsChilds.item(j).getTextContent();
                        break;
                    }
                }
            }

            Contact contact = new Contact(name, surname, phone, email);


             contacts.add(contact);
             }
             return contacts;
        }


    public  Node getContactNode(Document document) {


        try {
            document = buildDocument();
        } catch (Exception e) {
            System.out.println("Pars exeption " + e.toString());
            return null;
        }

        Node rootNode = document.getFirstChild();
        NodeList list = rootNode.getChildNodes();


        Node contactNode = null;
        String groupName = null;

        for (int i = 0; i < list.getLength(); i++) {

            if (list.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            switch (list.item(i).getNodeName()) {
                case "name": {
                    groupName = list.item(i).getTextContent(); // витягли груп нейм
                    break;
                }

                case "contact": {
                    contactNode = list.item(i);
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + list.item(i).getNodeName());
            }
        }

        if (contactNode == null) {
            return null;
        }

        group2.setName(groupName);


        return contactNode;
    }
}
