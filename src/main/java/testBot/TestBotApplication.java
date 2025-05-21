package testBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import testBot.config.TelegramBot;

@SpringBootApplication
public class TestBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestBotApplication.class, args);
	}
	@Bean
	public TelegramBotsApi telegramBotsApi(TelegramBot bot) throws TelegramApiException {
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(bot);
		return botsApi;
	}
}
