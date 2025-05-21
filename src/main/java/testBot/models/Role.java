package testBot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table (name = "roles")
@Getter
@Setter
public class Role extends BaseEntity implements GrantedAuthority {
    @Column
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
