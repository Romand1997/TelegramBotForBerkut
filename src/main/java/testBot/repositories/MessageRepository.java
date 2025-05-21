package testBot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import testBot.models.Message;
import testBot.models.User;

import java.util.List;
@Repository
@Transactional
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUser(User user);
}
