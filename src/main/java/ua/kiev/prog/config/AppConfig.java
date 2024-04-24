package ua.kiev.prog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.servise.ContactService;

import java.util.List;
import java.util.Objects;

import static ua.kiev.prog.constants.Constants.*;
import static ua.kiev.prog.parsers.JsonParser.parsejson;

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
                Contact contact;
                List<Contact> contacts;
                // додаємо контакти з json файлу
                contacts = parsejson(FAMILY);
                if(contacts.size()>0){
                    contactService.addGroup(contacts.get(0).getGroup());
                }

                for (Contact c : Objects.requireNonNull(contacts)) {

                    contact = new Contact(c.getGroup(), c.getName(), c.getSurname(),
                            c.getEmail(), c.getPhone());

                    contactService.addContact(contact);
                }
                contacts.clear();

                contacts = parsejson(SCHOOL);
                if(contacts.size()>0){
                    contactService.addGroup(contacts.get(0).getGroup());
                }

                for (Contact c2 : Objects.requireNonNull(contacts)) {
                    contact = new Contact(c2.getGroup(), c2.getName(), c2.getSurname(),
                            c2.getEmail(), c2.getPhone());

                    contactService.addContact(contact);
                }
            }
        };
    }
}
