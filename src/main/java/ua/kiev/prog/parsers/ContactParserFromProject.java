package ua.kiev.prog.parsers;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static ua.kiev.prog.constants.Constants.NEW_XML_FILE;

@Getter
@Setter
public class ContactParserFromProject {

    private final Group group2 = new Group();

    private String newFile;

    public ContactParserFromProject() {
    }

    // метод для зчитування директорії
    public  Document buildDocument(String path) throws Exception {

        File file = new File(path);
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
            document = buildDocument(newFile);
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

        group2.setName(groupName+"1");


        return contactNode;
    }

    // метод читає з сервака json/xml фаіл і записує данні у корінь проєкта створюючи новий json/xml файл
    public void universalParser(String urls) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        newFile = NEW_XML_FILE;

        try {
            URL url = new URL(urls); // тут треба json чи xml на серваці
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();

                File file = new File(newFile); // новий файл куди записуємо or contacts333.json
                outputStream = new FileOutputStream(file);

                int bytesRead = -1; // -1 це кінець файлу зчитуємо дані по 1мб
                byte[] buffer = new byte[1024];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

            }

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