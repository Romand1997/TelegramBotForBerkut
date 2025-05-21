package testBot.services.Impl;

import org.springframework.stereotype.Service;
import testBot.DTO.LoginRequest;
import testBot.DTO.RegisterRequest;
import testBot.exceptions.LoginException;
import testBot.exceptions.PasswordException;

import java.util.Objects;

@Service
public class ValidateService {
    public void validateLogin(LoginRequest request) {
        if (Objects.isNull(request.getLogin()) || request.getLogin().isEmpty()){
            throw new LoginException("Login is empty");
        }
        if (Objects.isNull(request.getPassword()) || request.getPassword().isEmpty()){
            throw new PasswordException("Password is empty");
        }
    }
    public void validateRegister(RegisterRequest request) {
        if (Objects.isNull(request.getLogin()) || request.getLogin().isEmpty()){
            throw new LoginException("Login is empty");
        }
        if (Objects.isNull(request.getFullName()) || request.getFullName().isEmpty()){
            throw new LoginException("Name is empty");
        }
        if (Objects.isNull(request.getPassword()) || request.getPassword().isEmpty()){
            throw new LoginException("Password is empty");
        }
        if (Objects.isNull(request.getRePassword()) || request.getRePassword().isEmpty()){
            throw new PasswordException("RePassword is empty");
        }
    }
}
