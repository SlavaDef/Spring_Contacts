package ua.kiev.prog.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.kiev.prog.models.Contact;
import ua.kiev.prog.models.Group;

import java.util.List;

// DAO
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("FROM Group g WHERE g.name = :name")
    Group findGroupByName(@Param("name") String name);
}
