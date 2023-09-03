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
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    UserRepository userRepository;

    private User mockUser;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldLoginUser() throws Exception {
        LoginRequest loginRequest = loginRequest();
        String requestBody = mapper.writeValueAsString(loginRequest);
        UserDetailsImpl userDetails = userDetails();
        String mockJwtToken = "shdytvpbimjznsavcxf";

        TestingAuthenticationProvider provider = new TestingAuthenticationProvider();
        TestingAuthenticationToken token = new TestingAuthenticationToken(
            userDetails,
            "ROLE_USER"
        );
        Authentication authentication = provider.authenticate(token);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        authentication.setAuthenticated(true);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(mockJwtToken);


        mockUser = createUser(1L, "yogauser", "last name", "first name", "test!1234", true);

        Mockito.when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(mockUser));

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value(mockJwtToken))
            .andExpect(jsonPath("$.username").value(userDetails.getUsername())
        );
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        SignupRequest signupRequest = signupRequest();
        String requestBody = mapper.writeValueAsString(signupRequest);
        // when user do not exist
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/register")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("User registered successfully!")
        );
        // when user already exist
        Mockito.when(userRepository.existsByEmail("toto3@toto.com")).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders
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

    private SignupRequest signupRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("toto");
        signupRequest.setFirstName("toto");
        signupRequest.setEmail("toto3@toto.com");
        signupRequest.setPassword("test!1234");
        return signupRequest;
    }

    private com.openclassrooms.starterjwt.models.User createUser(
            Long id,
            String email,
            String lastName,
            String firstName,
            String password,
            Boolean isAdmin
    ) {
        com.openclassrooms.starterjwt.models.User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setAdmin(isAdmin);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
