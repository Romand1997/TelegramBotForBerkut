package testBot.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import testBot.DTO.LoginResponse;
import testBot.DTO.RegisterResponse;
import testBot.exceptions.*;
import testBot.models.User;
import testBot.repositories.RoleRepository;
import testBot.repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(login));
    }

    public void existByLogin(String login) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new UserFoundException("User already exists");
        }
    }


    public void save(String login, String name, String password, String rePassword) {
        if (!password.equals(rePassword)) {
            throw new PasswordException("Passwords do not match");
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(name);
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER")));
        user.setToken(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public RegisterResponse checkRegistration(String login, String name, String password, String rePassword) {
        existByLogin(login);
        save(login, name, password, rePassword);
        return new RegisterResponse(login, password);
    }

    public String getLoginByAuth(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userDetails.getUsername();

    }

    public LoginResponse generateResponse(String login, String token) {
        String fullName = findByLogin(login).getFullName();
        return new LoginResponse(fullName, login, token);
    }

    public void checkTelegramChatId(User user) {
        if (user.getTelegramChatId() == null) {
            throw new TelegramChatIdException("Telegram chat id is null");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow();
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.getRoles()
        );
    }

}



