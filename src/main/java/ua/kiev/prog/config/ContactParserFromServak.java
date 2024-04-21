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
import java.util.List;

public class ContactParserFromServak {

    Group group2 = new Group();

    private static final String CONTAKTS_URL = "https://drive.google.com/file/d/1ktx3fgvHQJ320onMw70vdaglZ1IK-WpJ/view?usp=sharing";

    public String buildDocument() throws Exception {
        String res = null;
        URL url = new URL(CONTAKTS_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader(conn.getInputStream()))) {
            // StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                res += line;
                //  result.append(line);
            }
            //  res = result.toString();
        }

        return res;
        //   File file = new File(Url);
        //  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        //  return dbFactory.newDocumentBuilder().parse(file);
    }

    /*   public List<Contact> parseContacts(Node contactsNode) {
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
   */
    public static void main(String[] args) throws Exception {
        ContactParserFromServak contactParser = new ContactParserFromServak();
        //  List<Contact> contacts = contactParser.parseContacts(contactParser.getContactNode(contactParser.buildDocument()));
        // System.out.println(contacts);

        //  System.out.println(contactParser.buildDocument());

        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            URL url = new URL(CONTAKTS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();

                File file = new File("contacts3.json"); // новий файл куди записуємо
                outputStream = new FileOutputStream(file);

                int bytesRead = -1; // -1 це кінець файлу зчитуємо дані по 1мб
                byte[] buffer = new byte[1024];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

            }
            //  conn.setRequestMethod("GET");

        } catch (IOException e) {
            System.out.println("Internet connection error" + e.toString());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                System.out.println("Error closing input stream" + e.toString());
            }

        }
    }
}
