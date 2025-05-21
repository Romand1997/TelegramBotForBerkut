package testBot.exceptions;

public class TelegramChatIdException extends RuntimeException {
    public TelegramChatIdException(String message) {
        super(message);
    }
}
