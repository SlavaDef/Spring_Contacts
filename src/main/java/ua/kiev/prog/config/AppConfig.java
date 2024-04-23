package ua.kiev.prog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;
import ua.kiev.prog.parsers.ContactParserFromProject;
import ua.kiev.prog.servise.ContactService;

import java.util.List;

import static ua.kiev.prog.constants.Constants.CONTAKTS_URL2;

// DB -> JDBC -> JPA(H): E..E -> DAO..DAO / Repository -> S..S -> C...C -> DTO -> View / React / Vue

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**") // якщо йде запит на такі файли
                .addResourceLocations("/WEB-INF/static/"); //  кажемо де шукати
    }

    @Bean
    public CommandLineRunner demo(final ContactService contactService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                Group group = new Group("Test");
                Contact contact;
                ContactParserFromProject test = new ContactParserFromProject();
                test.universalParser(CONTAKTS_URL2);
                contactService.addGroup(group);

                List<Contact> contacts = test.parseContacts(test.getContactNode(test.buildDocument(test.getNewFile())));
                contactService.addGroup(test.getGroup2());
                for (Contact c : contacts) {
                    contact = new Contact(test.getGroup2(), c.getName(),c.getSurname(),c.getPhone(),c.getEmail());
                    contactService.addContact(contact);
                }

                for (int i = 0; i < 10; i++) {
                    contact = new Contact(group, "Other" + i, "OtherSurname" + i, "7654321" + i, "user" + i + "@other.com");
                    contactService.addContact(contact);
                }
            }
        };
    }
}
