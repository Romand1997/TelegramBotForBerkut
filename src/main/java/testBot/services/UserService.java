package testBot.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import testBot.models.User;

public interface UserService extends UserDetailsService {
     User findByLogin(String login);
     User save(String login, String name, String password, String rePassword);
}
