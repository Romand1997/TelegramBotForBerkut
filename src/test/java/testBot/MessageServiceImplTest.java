package testBot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testBot.DTO.MessagesResponse;
import testBot.mappers.MessageMapper;
import testBot.models.Message;
import testBot.models.User;
import testBot.repositories.MessageRepository;
import testBot.services.Impl.MessageServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void testSave() {
        User user = new User();
        Message message = new Message();

        messageService.save(message, user);

        assertEquals(user, message.getUser());
        assertNotNull(message.getCreatedAt());
        verify(messageRepository).save(message);
    }

    @Test
    void testFindByUser() {
        User user = new User();
        List<Message> messages = List.of(new Message());
        List<MessagesResponse> response = List.of(new MessagesResponse());

        when(messageRepository.findByUser(user)).thenReturn(messages);
        when(messageMapper.messagesToMessagesResponse(messages)).thenReturn(response);

        List<MessagesResponse> result = messageService.findByUser(user);

        assertEquals(response, result);
        verify(messageRepository).findByUser(user);
        verify(messageMapper).messagesToMessagesResponse(messages);
    }
}
