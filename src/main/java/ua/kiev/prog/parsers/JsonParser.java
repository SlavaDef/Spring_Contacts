package ua.kiev.prog.parsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class JsonParser {

    private static final String GROUP_NAME = "name";
    private static final String CONTACTS_ARR = "contacts";


// метод читає json фаіл з самого проєкту далі створюємо обьект контакт з отриманими полями
    public static List<Contact> parsejson(String fileName) {
        Group group = new Group();
        List<Contact> contactList = new ArrayList<>();
        JSONParser parser = new JSONParser(); // залежність json-simple

        try (FileReader fr = new FileReader(fileName)) { // reading from file "Contacts.json"
            JSONObject groupObject = (JSONObject) parser.parse(fr);
            String groupName = (String) groupObject.get(GROUP_NAME); // отримали імя групи groupObject.get

            JSONArray contacts = (JSONArray) groupObject.get(CONTACTS_ARR); // JSONArray отримали масив контактів
            group.setName(groupName);
            for (Object contact : contacts) { // для кожного обьекту з масива контактів
                JSONObject contactObject = (JSONObject) contact; // тут один із 5 обектів
                String nameCon = (String) contactObject.get("name");
                String surname = (String) contactObject.get("surname");
                String phone = (String) contactObject.get("phone");
                String email = (String) contactObject.get("email");
                Contact contact1 = new Contact(group, nameCon, surname, phone, email);
                contactList.add(contact1);
            }
            // group.setContacts(contactList);
            return contactList;

        } catch (Exception e) {
            System.out.println("Parsing error" + e);
        }
        return null;
    }
}
