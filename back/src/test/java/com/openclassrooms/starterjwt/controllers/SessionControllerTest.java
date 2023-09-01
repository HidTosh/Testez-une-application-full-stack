package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    Teacher teacher;

    @MockBean
    User user;

    @MockBean
    SessionService sessionService;

    private static ObjectMapper mapper = new ObjectMapper();

    private Session mockSession;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 13, 17);
        mockSession = new Session(
            3L,
            "session 1",
            new Date(),
            "my description",
            teacher, Arrays.asList(user),
            date,
            date
        );
    }
    @Test
    public void shouldReturnSessionList() throws Exception {
        //GIVEN
        List<Session> listSession = Arrays.asList(this.mockSession);
        //WHEN
        Mockito.when(sessionService.findAll()).thenReturn(listSession);
        //THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/session")
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(listSession.size()))
            .andExpect(jsonPath("$[0].id").value(this.mockSession.getId()))
            .andExpect(jsonPath("$[0].description").value(this.mockSession.getDescription())
        );
    }

    @Test
    public void shouldReturnSession() throws Exception {
        //GIVEN
        Long id = 3L;
        //WHEN
        Mockito.when(sessionService.getById(id)).thenReturn(this.mockSession);
        //THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/session/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(this.mockSession.getId()))
            .andExpect(jsonPath("$.name").value(this.mockSession.getName())
        );
        //return 404 when session not fount
        this.mockMvc.perform(MockMvcRequestBuilders
            .get("/api/session/{id}", 5))
            .andExpect(status().isNotFound()
        );
        //throw an error when session id is not integer
        this.mockMvc.perform(MockMvcRequestBuilders
            .get("/api/session/{id}", "id"))
            .andExpect(status().isBadRequest()
        );
    }

    @Test
    public void shouldCreateSession() throws Exception {
        //GIVEN
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 13, 17);
        List<Long> listUserId = Arrays.asList(user.getId());
        SessionDto mockSessionDto = new SessionDto(
            3L, "session 1", new Date(), 1L, "my description", listUserId, date, date
        );
        String requestBody = mapper.writeValueAsString(mockSessionDto);
        // WHEN
        Mockito.when(sessionService.create(this.mockSession)).thenReturn(this.mockSession);
        // THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(this.mockSession.getId()))
            .andExpect(jsonPath("$.name").value(this.mockSession.getName())
        );
    }

    @Test
    public void shouldUpdateSession() throws Exception {
        //GIVEN
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 13, 17);
        List<Long> listUserId = Arrays.asList(user.getId());
        SessionDto mockSessionDto = new SessionDto(
                3L, "session 5", new Date(), 1L, "description text", listUserId, date, date
        );
        String requestBody = mapper.writeValueAsString(mockSessionDto);
        this.mockSession.setName(mockSessionDto.getName());
        this.mockSession.setDescription(mockSessionDto.getDescription());
        Long id = 3L;
        // WHEN
        Mockito.when(sessionService.update(id, this.mockSession)).thenReturn(this.mockSession);
        // THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/api/session/{id}", id)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(mockSessionDto.getName()))
            .andExpect(jsonPath("$.description").value(mockSessionDto.getDescription())
        );
        //throw an error when session id is not integer
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/api/session/{id}", "id")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest()
        );
    }

    @Test
    public void shouldDeleteSession() throws Exception {
        //GIVEN
        Long id = 3L;
        //WHEN
        Mockito.when(sessionService.getById(id)).thenReturn(this.mockSession);
        //THEN
        this.mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/session/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk()
        );
        //return 404 when session not fount
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", 1))
            .andExpect(status().isNotFound()
        );

        //throw an error when session id is not integer
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", "id"))
            .andExpect(status().isBadRequest()
        );
    }

    @Test
    public void shouldParticipateSession() throws Exception {
        //GIVEN
        Long userId = user.getId();
        Long sessionId = this.mockSession.getId();
        //WHEN
        sessionService.participate(sessionId, userId);
        //THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk()
        );
        //throw an error when user id not integer
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/session/{sessionId}/participate/{userId}", sessionId, "id")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest()
        );
    }

    @Test
    public void shouldDeleteParticipateSession() throws Exception {
        //GIVEN
        Long userId = user.getId();
        Long sessionId = this.mockSession.getId();
        // WHEN
        sessionService.noLongerParticipate(sessionId, userId);
        // THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk()
        );
        //throw an error when session id not integer
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/session/{sessionId}/participate/{userId}", "id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest()
        );
    }
}