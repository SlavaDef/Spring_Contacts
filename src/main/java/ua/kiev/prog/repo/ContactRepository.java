package ua.kiev.prog.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    // JPQL знайти контакти що належать певній групі
    // @Param("group") оцей параметр передати :group цьому
   // Pageable pageable при додаванні цього інтерфейсу стає можливим вибірка даних по сторінково
    @Query("SELECT c FROM Contact c WHERE c.group = :group")
    List<Contact> findByGroup(@Param("group") Group group, Pageable pageable);

    // порахувати скільки в певній групі контактів
    @Query("SELECT COUNT(c) FROM Contact c WHERE c.group = :group")
    long countByGroup(@Param("group") Group group);

    // пошук контактів за імям
    @Query("SELECT c FROM Contact c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    List<Contact> findByPattern(@Param("pattern") String pattern, Pageable pageable);

    // List<Contact> findByNameOrEmailOrderById(String name, String email);
    // List<Contact> findByNameAndEmail(String name, String email);
}
