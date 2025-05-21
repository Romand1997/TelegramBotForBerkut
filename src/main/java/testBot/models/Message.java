package testBot.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table (name = "messages")
@Entity
@Getter
@Setter
public class Message extends BaseEntity {

    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}