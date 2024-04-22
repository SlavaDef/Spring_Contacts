package ua.kiev.prog.config;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactParserFromServak {

    Group group2 = new Group();

    private static final String CONTAKTS_URL = "https://iportal.com.ua/wp-content/uploads/Contacts.xml";
    private static final String CONTAKTS_URL2 = "https://senior-pomidor.com.ua/Contacts25.xml";
    List<String> red = new ArrayList<>();
    // метод зчитає з сайту xml файл і запише все у String чи StringBuilder
    public String readerXml() throws Exception {
        String res = null;
        URL url = new URL(CONTAKTS_URL2);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader(conn.getInputStream()))) {
            // StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                red.add(line);
                res += line;
             //   System.out.println(line);
                //  result.append(line);
            }
            //  res = result.toString();
        }

        return res;
        //   File file = new File(Url);
        //  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        //  return dbFactory.newDocumentBuilder().parse(file);
    }

       public List<Contact> parseContacts(Node contactsNode) {
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


       public Node getContactNode(Document document) {


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

    public  Document buildDocument() throws Exception {

        File file = new File(readerXml());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        return dbFactory.newDocumentBuilder().parse(file);
    }

    public static void main(String[] args) throws Exception {
        ContactParserFromServak contactParser = new ContactParserFromServak();
        contactParser.readerXml();
        List<String> set = contactParser.red;
        System.out.println(set);
      //  List<Contact> contactList = contactParser.parseContacts(contactParser.getContactNode(contactParser.buildDocument()));
      //  System.out.println(contactList);

        //  List<Contact> contacts = contactParser.parseContacts(contactParser.getContactNode(contactParser.buildDocument()));
        // System.out.println(contacts);

        //  System.out.println(contactParser.buildDocument());


    }
}
