package testBot.services;

import testBot.DTO.MessagesResponse;
import testBot.models.Message;
import testBot.models.User;
import java.util.List;

public interface MessageService {
     void save(Message message, User user);
     List<MessagesResponse> findByUser(User user);
}
