package ua.kiev.prog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kiev.prog.models.Group;

// DAO
public interface GroupRepository extends JpaRepository<Group, Long> {
}
