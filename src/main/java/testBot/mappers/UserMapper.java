package testBot.mappers;

import org.springframework.stereotype.Component;
import testBot.DTO.LoginResponse;
import testBot.models.User;

@Component
public class UserMapper {
    public LoginResponse userToLoginResponse(User user) {
        return new LoginResponse(user.getFullName(), user.getLogin());
    }
}
