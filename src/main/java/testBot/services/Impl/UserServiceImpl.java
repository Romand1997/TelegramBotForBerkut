package testBot.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import testBot.DTO.LoginResponse;
import testBot.exceptions.PasswordException;
import testBot.exceptions.TelegramChatIdException;
import testBot.exceptions.UserFoundException;
import testBot.mappers.UserMapper;
import testBot.models.Role;
import testBot.models.User;
import testBot.repositories.RoleRepository;
import testBot.repositories.UserRepository;
import testBot.services.UserService;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }
    public void existByLogin(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            throw new UserFoundException("User already exists");
        }
    }

    @Override
    public User save(String login, String name, String password, String rePassword) {
        if (!password.equals(rePassword)) {
            throw new PasswordException("Password are not same");
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(name);
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        user.setRoles(roles);
        user.setToken(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public LoginResponse checkLogin(String login, String password) {
        User user = findByLogin(login);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return userMapper.userToLoginResponse(user);
        }
        throw new PasswordException("Password incorrect");
    }

    public LoginResponse checkRegistration(String login, String name, String password, String rePassword) {
        existByLogin(login);
        User user = save(login, name, password, rePassword);
        return userMapper.userToLoginResponse(user);
    }

    public void checkTelegramChatId(User user) {
        if (user.getTelegramChatId() == null) {
            throw new TelegramChatIdException("Telegram chat id is null");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = findByLogin(login);
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.getRoles()
        );
    }
}


