package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER", username = "test@test.io")
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.now();
        mockUser = new User(
            3L,
            "test@test.io",
            "last name",
            "first name",
            "********",
            false,
            date,
            date
        );
    }

    @Test
    public void shouldReturnUser() throws Exception {
        //GIVEN
        Long id = 3L;
        //WHEN user does not exist
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/user/{id}", id)
                )
                .andExpect(status().isNotFound()
            );
        //WHEN
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/user/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(mockUser.getEmail()))
                .andExpect(jsonPath("$.lastName").value(mockUser.getLastName())
            );

        //WHEN user id is not integer
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/{id}", "fsd")
                )
                .andExpect(status().isBadRequest()
            );
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        //GIVEN
        Long id = 3L;
        //WHEN user does not exist
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/api/user/{id}", id)
                )
                .andExpect(status().isNotFound()
            );
        //WHEN
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/api/user/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()
            );
        // return not authorized when same user as connected
        mockUser.setEmail("user@test.com");
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/api/user/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(equalTo(401))
            );
        //WHEN user id is not integer
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/api/user/{id}", "id")
                )
                .andExpect(status().isBadRequest()
            );
    }
}
