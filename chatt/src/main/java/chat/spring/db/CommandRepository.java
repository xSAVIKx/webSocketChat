package chat.spring.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chat.spring.model.CommandPojo;

@Repository
public interface CommandRepository extends JpaRepository<CommandPojo, Long> {

}
