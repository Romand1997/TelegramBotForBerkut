package testBot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import testBot.repositories.UserRepository;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    UserRepository userRepository;
    @Value("${chat.bot.name}")
    public String botName;
    @Value("${chat.bot.token}")
    public String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String token = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            userRepository.findByToken(token).ifPresent(user -> {
                user.setTelegramChatId(chatId.toString());
                userRepository.save(user);
                try {
                    sendText(String.valueOf(chatId), "Token привязан к вашему аккаунту.");
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void sendText(String chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage(chatId, text);
        execute(message);
    }

    public void sendDuplicateText(String chatId, String text, String userName) throws TelegramApiException {
        String fullText = userName + ", я получил от тебя сообщение:\n" + text;
        sendText(chatId, fullText);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
