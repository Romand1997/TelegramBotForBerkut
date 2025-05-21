package testBot;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testBot.services.Impl.JWTService;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    @Mock
    private SecretKey secretKey;

    private JWTService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JWTService(Keys.secretKeyFor(SignatureAlgorithm.HS256), 3600000);
    }

    @Test
    void generateToken_shouldContainUsername() {
        String token = jwtService.generateToken("user123");
        String username = jwtService.extractUsername(token);
        assertEquals("user123", username);
    }

    @Test
    void isTokenValid_validToken_returnsTrue() {
        String token = jwtService.generateToken("user");
        assertTrue(jwtService.isTokenValid(token, "user"));
    }

    @Test
    void isTokenValid_wrongUser_returnsFalse() {
        String token = jwtService.generateToken("user");
        assertFalse(jwtService.isTokenValid(token, "other"));
    }
}

