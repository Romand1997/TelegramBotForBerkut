package testBot.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class MessagesResponse {
    private String text;
    private LocalDateTime createdAt;
}
