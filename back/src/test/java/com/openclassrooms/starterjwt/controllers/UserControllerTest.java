package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import static org.hamcrest.Matchers.equalTo;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER", username = "test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserDetails mockUserDetails;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 17, 20);
        mockUser = new User(
            3L,
            "test",
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
        //WHEN
        Mockito.when(userService.findById(id)).thenReturn(this.mockUser);
        //THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(this.mockUser.getEmail()))
            .andExpect(jsonPath("$.lastName").value(this.mockUser.getLastName())
        );
        //return 404 when user not fount
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/{id}", 5)
            )
            .andExpect(status().isNotFound()
        );
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        //GIVEN
        Long id = 3L;
        //WHEN
        Mockito.when(userService.findById(id)).thenReturn(this.mockUser);
        //THEN
        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk()
        );
        // return not authorized when same user as connected
        mockUser.setEmail("user@test.com");
        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is(equalTo(401))
        );
        //return 404 when user not fount
        this.mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user/{id}", 5))
            .andExpect(status().isNotFound()
        );
        //throw an error when user id is not integer
        this.mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/user/{id}", "id"))
            .andExpect(status().isBadRequest()
        );
    }
}
