package testBot.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import testBot.DTO.LoginResponse;
import testBot.exceptions.*;
import testBot.models.Role;
import testBot.models.User;
import testBot.repositories.RoleRepository;
import testBot.repositories.UserRepository;
import testBot.services.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(login));
    }

    public void existByLogin(String login) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new UserFoundException("User already exists");
        }
    }

    @Override
    public User save(String login, String name, String password, String rePassword) {
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
        return userRepository.save(user);
    }

    public LoginResponse checkRegistration(String login, String name, String password, String rePassword) {
        existByLogin(login);
        save(login, name, password, rePassword);
        return authenticateAndGenerateToken(login, password);
    }

    public LoginResponse authenticateAndGenerateToken(String login, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(userDetails.getUsername());
        String fullName = findByLogin(login).getFullName();
        return new LoginResponse(fullName, login, token);
    }

    public void checkTelegramChatId(User user) {
        if (user.getTelegramChatId() == null) {
            throw new TelegramChatIdException("Telegram chat id is null");
        }
    }

}



