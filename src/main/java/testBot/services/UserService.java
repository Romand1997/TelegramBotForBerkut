package testBot.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import testBot.models.User;

public interface UserService {
     User findByLogin(String login);
     User save(String login, String name, String password, String rePassword);
}
