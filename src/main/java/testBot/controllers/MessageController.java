package testBot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import testBot.DTO.MessagesResponse;
import testBot.config.TelegramBot;
import testBot.exceptions.TelegramChatIdException;
import testBot.models.Message;
import testBot.models.User;
import testBot.services.Impl.MessageServiceImpl;
import testBot.services.Impl.UserServiceImpl;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageServiceImpl messageService;
    private final UserServiceImpl userService;
    private final TelegramBot telegramBot;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> sendMessage(Principal principal, @RequestBody Message message) {
        try {
            User user = userService.findByLogin(principal.getName());
            messageService.save(message, user);
            userService.checkTelegramChatId(user);
            telegramBot.sendDuplicateText(user.getTelegramChatId(), message.getBody(), user.getFullName());
            return ResponseEntity.status(HttpStatus.OK).body("Сообщение отправлено");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (TelegramChatIdException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }

    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> getMessages(Principal principal) {
        try {
            User user = userService.findByLogin(principal.getName());
            List<MessagesResponse> messages = messageService.findByUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}