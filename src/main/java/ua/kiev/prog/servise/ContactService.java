package ua.kiev.prog.servise;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.parsers.ContactParserFromProject;
import ua.kiev.prog.parsers.JsonParser;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;
import ua.kiev.prog.repo.ContactRepository;
import ua.kiev.prog.repo.GroupRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ua.kiev.prog.constants.Constants.SCHOOL;
import static ua.kiev.prog.parsers.JsonParser.parsejson;


// c -> s -> r -> DB

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final GroupRepository groupRepository;

    public ContactService(ContactRepository contactRepository,
                          GroupRepository groupRepository) {
        this.contactRepository = contactRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }

    @Transactional
    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    // видалити контакти за масивом їх id
    @Transactional
    public void deleteContacts(long[] idList) {
        for (long id : idList)
            contactRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Group> findGroups() {
        return groupRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public List<Contact> findByGroup(Group group, Pageable pageable) {
        return contactRepository.findByGroup(group, pageable);
    }

    @Transactional(readOnly = true)
    public long countByGroup(Group group) {
        return contactRepository.countByGroup(group);
    }

    @Transactional(readOnly = true)
    public List<Contact> findByPattern(String pattern, Pageable pageable) {
        return contactRepository.findByPattern(pattern, pageable);
    }

    // підрахунок всіх обьектів
    @Transactional(readOnly = true)
    public long count() {
        return contactRepository.count();
    }

    @Transactional(readOnly = true)
    public Group findGroup(long id) {
        return groupRepository.findById(id).get();
    }

    @Transactional
    public void deleteGroup(String name) {
        if (groupRepository.findGroupByName(name) == null){
            return;
        }
        groupRepository.delete(groupRepository.findGroupByName(name));

    }

    @Transactional
    public void downloadContacts(String url) throws Exception {
        if(url.length()<8){
            return;
        }
        ContactParserFromProject parser = new ContactParserFromProject();
        Contact contact;
        parser.universalParser(url);
        List<Contact> contacts =
                parser.parseContacts(parser.getContactNode(parser.buildDocument(parser.getNewFile())));
        addGroup(parser.getGroup2());
        for (Contact c : contacts) {
            contact = new Contact(parser.getGroup2(), c.getName(), c.getSurname(), c.getEmail(), c.getPhone());
            addContact(contact);
        }
    }

    @Transactional
    public void transfer(Long id, Group group) {
        Optional<Contact> contact = contactRepository.findById(id);
        contact.get().setGroup(group);
        contactRepository.save(contact.get());
    }


    @Transactional
    public void reset() {
        groupRepository.deleteAll();
        contactRepository.deleteAll();

        Group group = new Group("Test");
        Contact contact;

        addGroup(group);

    //    Group group1 = parsejson(SCHOOL).get(1).getGroup();
       /// addGroup(group1);
        List<Contact> contacts = parsejson(SCHOOL);
       // addGroup();
        for (Contact c : Objects.requireNonNull(contacts)) {
            addGroup(c.getGroup());
            Contact contact1 = new Contact(c.getGroup(), c.getName(), c.getSurname(),
                    c.getPhone(), c.getEmail());
            addContact(contact1);
        }
        for (int i = 0; i < 10; i++) {
            contact = new Contact(group, "Other" + i, "OtherSurname" + i, "7654321" + i, "user" + i + "@other.com");
            addContact(contact);
        }
    }
}
