package ua.kiev.prog.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kiev.prog.servise.ContactService;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static ua.kiev.prog.constants.Constants.*;

@Controller
public class MyController {

    private static final Logger LOGGER = LogManager.getLogger(MyController.class);


    private final ContactService contactService;

    public MyController(ContactService contactService) {
        this.contactService = contactService;
    }

    // required = false параметр не обовяковий якщо не вказати то за замовч defaultValue = "0"
    // Integer page - номер сторінки
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "0") Integer page) {
        if (page < 0) page = 0;

        List<Contact> contacts = contactService // отримаємо 6 обьектів на сторінці
                //  тут вже юзаємо Pageable - PageRequest.of клас який реалізує інтерфейс Pageable
                // page - номер сторінки, ITEMS_PER_PAGE - скільки на сторінці показувати
                // Sort.Direction.DESC сортіровка по спаданню,"id" - по чому сортуємо
                // завдяки такому сортуванню нові контакти завжди будуть першими (по id)
                .findAll(PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));
        // в модель кладемо список всіх груп
        model.addAttribute("groups", contactService.findGroups());
        // в модель кладемо колекцію контактів
        model.addAttribute("contacts", contacts);
        // в модель кладемо getPageCount() метод рахує скільки сторінок треба а jsp вже малює
        model.addAttribute("allPages", getPageCount());

        return "index"; // переходимо на сторінку index.jsp
    }

    @GetMapping("/reset")
    public String reset() {
        contactService.reset();
        return "redirect:/"; // перенаправити в корінь додатка редірект на вказаний єндпоинт
    }

    @GetMapping("/contact_add_page")
    public String contactAddPage(Model model) {
        // на сторінку передаємо список всіх груп
        model.addAttribute("groups", contactService.findGroups());
        return "contact_add_page";
    }

    @GetMapping("/group_add_page")
    public String groupAddPage() {
        return "group_add_page";
    }

    @GetMapping("/group/{id}")
    public String listGroup(
            @PathVariable(value = "id") long groupId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            Model model) {
        Group group = (groupId != DEFAULT_GROUP_ID) ? contactService.findGroup(groupId) : null;
        if (page < 0) page = 0;
        // вибірка контактів з конкретної групи
        List<Contact> contacts = contactService
                .findByGroup(group, PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));

        model.addAttribute("groups", contactService.findGroups());
        model.addAttribute("contacts", contacts);
        model.addAttribute("byGroupPages", getPageCount(group));
        model.addAttribute("groupId", groupId);

        return "index";
    }

    @PostMapping(value = "/search")
    public String search(@RequestParam String pattern,
                         @RequestParam(required = false, defaultValue = "0") Integer page, Model model) {
        if (page < 0) page = 0;
        model.addAttribute("groups", contactService.findGroups());
        model.addAttribute("allPages", getPageCount());
        model.addAttribute("contacts", contactService.findByPattern(pattern,
                PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id") /* HW */));
       // дороблено відображення педжинаціїї
        return "index";
    }

    @GetMapping("/group/delete")
    public String groupDel() {
        return "group_delete";
    }

    @PostMapping("/group/delete")
    public String deleteGroup(@RequestParam(value = "name") String name) {
        contactService.deleteGroup(name);
        return "redirect:/";
    }

    @PostMapping(value = "/contact/delete")
    public ResponseEntity<Void> delete(
            @RequestParam(value = "toDelete[]", required = false) long[] toDelete) {
        if (toDelete != null && toDelete.length > 0)
            contactService.deleteContacts(toDelete);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // повертає сторінку з вибором групи і імям користувача якого треба перенести в іншу групу
    @GetMapping("/contact_transfer_page")
    public String contactTransferPage(Model model) {
        model.addAttribute("groups", contactService.findGroups());
        return "contact_transfer_page";
    }

    // встановлення для збереженого контакту нової групи яку вибере користувача і збереження цього користувача в базу
    @PostMapping("/contact/transfer")
    public String transfer(@RequestParam(value = "group") long groupId,
                           @RequestParam String name) {
        Group group = (groupId != DEFAULT_GROUP_ID) ? contactService.findGroup(groupId) : null;
        Contact contact = contactService.transferByName(name);
        contact.setGroup(group);
        contactService.addContact(contact);

        return "redirect:/";

    }

    @PostMapping(value = "/contact/add") //  long groupId id групи в яку передаємо контакт
    public String contactAdd(@RequestParam(value = "group") long groupId,
                             @RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam String phone,
                             @RequestParam String email) {
        // за id шукаю групу в базі якщо такої не має то ретурн 0
        Group group = (groupId != DEFAULT_GROUP_ID) ? contactService.findGroup(groupId) : null;
        // створили ентіті і зберігли його в базу
        Contact contact = new Contact(group, name, surname, phone, email);
        contactService.addContact(contact);

        return "redirect:/"; // перенаправлення на головну
    }

    // додавання групи + редирект на головну
    @PostMapping(value = "/group/add")
    public String groupAdd(@RequestParam String name) {
        contactService.addGroup(new Group(name));
        return "redirect:/";
    }

    // return page з силками на сервак з меню для url та двома кнопками
    @GetMapping("/download")
    public String downloadFrom() {
        return "download_page";
    }

    @PostMapping("/download") // TODO за замовчуванням повертається по кнопці тільки одна з двох груп
    public String download(@RequestParam(required = false, defaultValue = CONTAKTS_URL)
                           String url) throws Exception {

        LOGGER.info("URL: " + url);
        contactService.downloadContacts(url);

        return "redirect:/";
    }

    private long getPageCount() { // метод рахує скільки треба сторінок
        long totalCount = contactService.count();
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }

    private long getPageCount(Group group) { // метод рахує скільки треба сторінок для певної групи
        long totalCount = contactService.countByGroup(group);
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }
}
