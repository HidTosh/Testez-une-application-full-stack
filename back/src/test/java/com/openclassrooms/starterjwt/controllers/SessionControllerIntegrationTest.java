package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
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
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class SessionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    SessionRepository sessionRepository;
    private Session mockSession;
    private Teacher mockTeacher;
    private LocalDateTime date;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        date = LocalDateTime.now();
        mockTeacher = new Teacher(
                1L, "last name", "first name", date, date
            );
        mockUser = new User(
                1L, "test@test.io", "last", "first", "***", false, date, date
            );

        mockSession = new Session(
                3L,
                "session 1",
                new Date(),
                "my description",
                mockTeacher,
                Arrays.asList(mockUser, mockUser),
                date,
                date
            );
    }

    @Test
    public void shouldReturnSession() throws Exception {
        //GIVEN
        Long id = mockSession.getId();
        //WHEN session does not exist
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/session/{id}", id))
                .andExpect(status().isNotFound()
            );
        //WHEN session exists
        Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(mockSession));
        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/session/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockSession.getId()))
                .andExpect(jsonPath("$.name").value(mockSession.getName())
            );
        //WHEN session id is not integer
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", "id"))
                .andExpect(status().isBadRequest()
            );
    }

    @Test
    public void shouldReturnSessionList() throws Exception {
        //GIVEN
        List<Session> listSession = Arrays.asList(mockSession, mockSession);
        //WHEN
        Mockito.when(sessionRepository.findAll()).thenReturn(listSession);
        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/session")
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(listSession.size()))
                .andExpect(jsonPath("$[0].id").value(mockSession.getId()))
                .andExpect(jsonPath("$[0].description").value(mockSession.getDescription())
            );
    }

    @Test
    public void shouldCreateSession() throws Exception {
        //GIVEN
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<Long> listUserId = Arrays.asList(mockUser.getId(), mockUser.getId());
        SessionDto mockSessionDto = new SessionDto(
            3L, "session 1", new Date(), mockTeacher.getId(), "my description", listUserId, date, date
        );
        String requestBody = objectMapper.writeValueAsString(mockSessionDto);
        // WHEN
        Mockito.when(sessionRepository.save(mockSession)).thenReturn(mockSession);
        // THEN
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/session")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").value(mockSession.getId()))
                .andExpect(jsonPath("$.name").value(mockSession.getName()))
                .andExpect(jsonPath("$.description").value(mockSession.getDescription())
            );
    }

    @Test
    public void shouldUpdateSession() throws Exception {
        //GIVEN
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<Long> listUserId = Arrays.asList(mockUser.getId(), mockUser.getId());
        SessionDto mockSessionDto = new SessionDto(
            3L, "session 5", new Date(), mockTeacher.getId(), "description text", listUserId, date, date
        );
        String requestBody = objectMapper.writeValueAsString(mockSessionDto);
        mockSession.setName(mockSessionDto.getName());
        mockSession.setDescription(mockSessionDto.getDescription());
        Long id = mockSession.getId();
        // WHEN
        Mockito.when(sessionRepository.save(mockSession)).thenReturn(mockSession);
        // THEN
        mockMvc.perform(MockMvcRequestBuilders
                    .put("/api/session/{id}", id)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockSessionDto.getName()))
                .andExpect(jsonPath("$.description").value(mockSessionDto.getDescription())
            );
        //WHEN session id is not integer
        mockMvc.perform(MockMvcRequestBuilders
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
        Long id = mockSession.getId();
        //WHEN session does not exists
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/api/session/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound()
            );
        //WHEN
        Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(mockSession));
        //THEN
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/session/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk()
        );
        //WHEN session id not integer
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", "id"))
                .andExpect(status().isBadRequest()
            );
    }

    @Test
    public void shouldParticipateSession() throws Exception {
        //GIVEN
        User mockUser2 = new User(
            2L, "test@test.io", "last", "first", "***", false, date, date
        );
        ArrayList<User> userArrayList = new ArrayList<User>(1);
        userArrayList.add(mockUser2);
        Long userId = mockUser.getId();
        Long sessionId = mockSession.getId();
        mockSession.setUsers(userArrayList);
        //WHEN
        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        //THEN
        assertThat(mockSession.getUsers().size()).isEqualTo(1);
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()
            );
        assertThat(mockSession.getUsers().size()).isEqualTo(2);
        //Throw an error when user id not integer
        mockMvc.perform(MockMvcRequestBuilders
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
        User mockUser2 = new User(
            2L, "test@test.io", "last", "first", "***", false, date, date
        );
        ArrayList<User> userArrayList = new ArrayList<User>(1);
        userArrayList.add(mockUser);
        userArrayList.add(mockUser2);
        Long userId = mockUser.getId();
        Long sessionId = mockSession.getId();
        mockSession.setUsers(userArrayList);
        // WHEN
        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        // THEN
        assertThat(mockSession.getUsers().size()).isEqualTo(2);
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/api/session/{sessionId}/participate/{userId}", sessionId, userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()
            );
        assertThat(mockSession.getUsers().size()).isEqualTo(1);
        //throw an error when session id not integer
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/api/session/{sessionId}/participate/{userId}", "id", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest()
            );
    }
}