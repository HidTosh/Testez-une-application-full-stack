package com.openclassrooms.starterjwt.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    UserRepository userRepository;

    @Test
    public void shouldLoginUser() throws Exception {
        //GIVEN
        UserDetailsImpl mockUserDetails = userDetails();
        User mockUser = createUser();
        String requestBody = objectMapper.writeValueAsString(loginRequest());
        String mockJwtToken = "shdytvpbimjznsavcxf";
        TestingAuthenticationProvider provider = new TestingAuthenticationProvider();
        TestingAuthenticationToken token = new TestingAuthenticationToken(mockUserDetails, "ROLE_USER");
        Authentication authentication = provider.authenticate(token);
        //WHEN
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(mockJwtToken);
        Mockito.when(userRepository.findByEmail(mockUserDetails.getUsername())).thenReturn(Optional.of(mockUser));
        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/auth/login")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(mockJwtToken))
                .andExpect(jsonPath("$.username").value(mockUserDetails.getUsername())
            );
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        //GIVEN
        String requestBody = objectMapper.writeValueAsString(signupRequest());
        // when user do not exist
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/auth/register")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!")
            );
        //WHEN user already exist
        Mockito.when(userRepository.existsByEmail("toto3@toto.com")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/auth/register")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!")
            );
    }

    private LoginRequest loginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        return loginRequest;
    }

    private SignupRequest signupRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("toto");
        signupRequest.setFirstName("toto");
        signupRequest.setEmail("toto3@toto.com");
        signupRequest.setPassword("test!1234");
        return signupRequest;
    }

    private UserDetailsImpl userDetails() {
        return new UserDetailsImpl(
                1L,
                "yogauser",
                "first name",
                "last name",
                true,
                "test!1234"
        );
    }
    private User createUser() {
        return new User(
            1L,
            "yogauser",
            "last name",
            "first name",
            "test!1234",
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }
}
