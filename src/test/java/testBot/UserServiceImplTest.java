package testBot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import testBot.DTO.LoginResponse;
import testBot.DTO.RegisterResponse;
import testBot.exceptions.PasswordException;
import testBot.exceptions.TelegramChatIdException;
import testBot.exceptions.UserFoundException;
import testBot.models.Role;
import testBot.models.User;
import testBot.repositories.RoleRepository;
import testBot.repositories.UserRepository;
import testBot.services.Impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByLogin_userExists_returnsUser() {
        User user = new User();
        when(userRepository.findByLogin("test")).thenReturn(Optional.of(user));
        assertEquals(user, userService.findByLogin("test"));
    }

    @Test
    void findByLogin_userNotFound_throwsException() {
        when(userRepository.findByLogin("none")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.findByLogin("none"));
    }

    @Test
    void existByLogin_userExists_throwsUserFoundException() {
        when(userRepository.findByLogin("test")).thenReturn(Optional.of(new User()));
        assertThrows(UserFoundException.class, () -> userService.existByLogin("test"));
    }

    @Test
    void save_passwordsMismatch_throwsPasswordException() {
        assertThrows(PasswordException.class, () -> userService.save("login", "name", "123", "321"));
    }

    @Test
    void checkRegistration_valid_returnsResponse() {
        when(userRepository.findByLogin("new")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_USER")).thenReturn(new Role());
        when(passwordEncoder.encode("123")).thenReturn("encoded");

        RegisterResponse response = userService.checkRegistration("new", "Name", "123", "123");

        assertEquals("Name", response.getFullName());
        assertEquals("new", response.getLogin());
    }

    @Test
    void getLoginByAuth_returnsUsername() {
        UserDetails details = new org.springframework.security.core.userdetails.User("login", "pass", List.of());
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(details);
        assertEquals("login", userService.getLoginByAuth(auth));
    }

    @Test
    void generateResponse_valid_returnsLoginResponse() {
        User user = new User();
        user.setFullName("Full");
        when(userRepository.findByLogin("user")).thenReturn(Optional.of(user));
        LoginResponse response = userService.generateResponse("user", "token");
        assertEquals("Full", response.getFullName());
        assertEquals("user", response.getLogin());
        assertEquals("token", response.getToken());
    }

    @Test
    void checkTelegramChatId_null_throwsException() {
        User user = new User();
        assertThrows(TelegramChatIdException.class, () -> userService.checkTelegramChatId(user));
    }

    @Test
    void loadUserByUsername_valid_returnsUserDetails() {
        User user = new User();
        user.setLogin("login");
        user.setPassword("pass");
        user.setRoles(List.of());
        when(userRepository.findByLogin("login")).thenReturn(Optional.of(user));

        UserDetails details = userService.loadUserByUsername("login");

        assertEquals("login", details.getUsername());
        assertEquals("pass", details.getPassword());
    }
}

