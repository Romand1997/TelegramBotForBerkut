package testBot.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import testBot.DTO.LoginResponse;
import testBot.DTO.RegisterResponse;
import testBot.models.User;

public interface UserService extends UserDetailsService {
    User findByLogin(String login);

    void existByLogin(String login);

    void save(String login, String name, String password, String rePassword);

    RegisterResponse checkRegistration(String login, String name, String password, String rePassword);

    String getLoginByAuth(Authentication auth);

    LoginResponse generateResponse(String login, String token);

    void checkTelegramChatId(User user);

}
