package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class LoginRequestTest {
    @InjectMocks
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
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
