package ua.kiev.prog.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class JsonParser {

    private static final String GROUP_NAME = "name";
    private static final String CONTACTS_ARR = "contacts";
    public List<Contact> contactList = new ArrayList<>();

    public Group parse() {
        Group group = new Group();
        JSONParser parser = new JSONParser(); // залежність json-simple

        try (FileReader fr = new FileReader("Contacts.json")) {
            JSONObject groupObject = (JSONObject) parser.parse(fr);
            String groupName = (String) groupObject.get(GROUP_NAME); // отримали імя групи groupObject.get

            JSONArray contacts = (JSONArray) groupObject.get(CONTACTS_ARR); // JSONArray отримали масив контактів
            
            for (Object contact : contacts) { // для кожного обьекту з масива контактів
                JSONObject contactObject = (JSONObject) contact; // тут один із 5 обектів
                String nameCon = (String) contactObject.get("name");
                String surname = (String) contactObject.get("surname");
                String phone = (String) contactObject.get("phone");
                String email = (String) contactObject.get("email");
                Contact contact1 = new Contact(nameCon, surname, phone, email);
                contactList.add(contact1);

            }
            group.setName(groupName);
            // group.setContacts(contactList);
            return group;

        } catch (Exception e) {
            System.out.println("Parsing error" + e);
        }
        return null;
    }
}
