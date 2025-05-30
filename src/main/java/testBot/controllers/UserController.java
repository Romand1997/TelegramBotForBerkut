package testBot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import testBot.DTO.LoginRequest;
import testBot.DTO.LoginResponse;
import testBot.DTO.RegisterRequest;
import testBot.DTO.RegisterResponse;
import testBot.exceptions.LoginException;
import testBot.exceptions.PasswordException;
import testBot.exceptions.UserFoundException;
import testBot.models.User;
import testBot.services.Impl.JWTService;
import testBot.services.Impl.UserServiceImpl;
import testBot.services.Impl.ValidateService;

import java.security.Principal;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final ValidateService validateService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            validateService.validateRegister(request);
            RegisterResponse response = userService.checkRegistration(request.getLogin(), request.getFullName(), request.getPassword(), request.getRePassword());
            return ResponseEntity.ok(response);
        } catch (LoginException | PasswordException | UserFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            validateService.validateLogin(request);
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
            );
            String login = userService.getLoginByAuth(auth);
            String token = jwtService.generateToken(login);
            LoginResponse response = userService.generateResponse(login, token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (LoginException | PasswordException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/getToken")
    public ResponseEntity<?> getToken(Principal principal) {
        try {
            User user = userService.findByLogin(principal.getName());
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", user.getToken()));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}