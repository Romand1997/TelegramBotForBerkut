package testBot.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import testBot.DTO.MessagesResponse;
import testBot.mappers.MessageMapper;
import testBot.models.Message;
import testBot.models.User;
import testBot.repositories.MessageRepository;
import testBot.services.MessageService;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    @Override
    public void save(Message message, User user) {
        message.setCreatedAt(LocalDateTime.now());
        message.setUser(user);
        messageRepository.save(message);
    }
    @Override
    public List<MessagesResponse> findByUser(User user) {
        List<Message> messages = messageRepository.findByUser(user);
        return messageMapper.messagesToMessagesResponse(messages);
    }
}
