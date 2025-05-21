package testBot.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String login;
    private String password;
    private String rePassword;
}