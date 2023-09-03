package com.openclassrooms.starterjwt.payload.request;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class LoginRequestTest {
    @InjectMocks
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 17, 20);
        loginRequest = new LoginRequest();
    }
    @Test
    public void testGetSetEmail() {
        loginRequest.setEmail("test@test.io");
        assertThat(loginRequest.getEmail()).isEqualTo("test@test.io");
    }

    @Test
    public void testGetSetPassword() {
        loginRequest.setPassword("my password");
        assertThat(loginRequest.getPassword()).isEqualTo("my password");
    }
}
