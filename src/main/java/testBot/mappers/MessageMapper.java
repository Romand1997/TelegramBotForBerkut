package testBot.mappers;

import org.springframework.stereotype.Component;
import testBot.DTO.MessagesResponse;
import testBot.models.Message;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageMapper {
    public List<MessagesResponse> messagesToMessagesResponse(List<Message> messages) {
        List<MessagesResponse> messagesResponses = new ArrayList<>();
        for (Message message : messages) {
            messagesResponses.add(new MessagesResponse(message.getBody(), message.getCreatedAt()));
        }
        return messagesResponses;
    }
}
