package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class SessionMapperTest {
    @InjectMocks
    SessionMapperImpl sessionMapperImpl;

    private SessionDto mockSessionDto;

    private SessionDto mockSessionDtoNull;

    private Session mockSession;

    private Session mockSessionNull;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.now();
        mockSessionDto = createSessionDto(1L, "my name", date, date);
        mockSession = createSession(1L, "my name", date, date);
        mockSessionDtoNull = null;
        mockSessionNull = null;
    }

    @Test
    public void shouldTestToEntityListTest() {
        List<SessionDto> listSessionDto = Arrays.asList(mockSessionDto, mockSessionDto);
        List<Session> listSession = Arrays.asList(mockSession, mockSession);
        List<SessionDto> listSessionDtoNull = null;

        assertThat(sessionMapperImpl.toEntity(listSessionDtoNull)).isNull();
        assertThat(sessionMapperImpl.toEntity(listSessionDto)).isEqualTo(listSession);
    }

    @Test
    public void shouldTestToDtoListTest() {
        mockSessionDto.setUsers(Arrays.asList());
        List<SessionDto> listSessionDto = Arrays.asList(mockSessionDto, mockSessionDto);
        List<Session> listSession = Arrays.asList(mockSession, mockSession);
        List<Session> listSessionNull = null;

        assertThat(sessionMapperImpl.toDto(listSessionNull)).isNull();
        assertThat(sessionMapperImpl.toDto(listSession)).isEqualTo(listSessionDto);
    }

    @Test
    public void shouldTestToEntityTest() {
        assertThat(sessionMapperImpl.toEntity(mockSessionDtoNull)).isNull();

        assertThat(sessionMapperImpl.toEntity(mockSessionDto)).isNotNull();
    }

    @Test
    public void shouldTestToDtoTest() {
        LocalDateTime date = LocalDateTime.now();
        assertThat(sessionMapperImpl.toDto(mockSessionNull)).isNull();

        mockSessionDto.setUsers(Arrays.asList());
        assertThat(sessionMapperImpl.toDto(mockSession)).isEqualTo(mockSessionDto);

        Teacher teacher = new Teacher(null, "test", "test", date, date);
        mockSession.setTeacher(teacher);
        assertThat(sessionMapperImpl.toDto(mockSession)).isEqualTo(mockSessionDto);

        Teacher teacher2 = new Teacher(1L, "test", "test", date, date);
        mockSession.setTeacher(teacher2);
        mockSessionDto.setTeacher_id(teacher2.getId());
        assertThat(sessionMapperImpl.toDto(mockSession)).isEqualTo(mockSessionDto);
    }

    private SessionDto createSessionDto(
            Long id,
            String name,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName(name);
        sessionDto.setDescription(null);
        sessionDto.setDate(new Date());
        sessionDto.setCreatedAt(createdAt);
        sessionDto.setUpdatedAt(updatedAt);
        return sessionDto;
    }

    private Session createSession(
            Long id,
            String name,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        Session session = new Session();
        session.setId(id);
        session.setName(name);
        session.setDescription(null);
        session.setDate(new Date());
        session.setCreatedAt(createdAt);
        session.setUpdatedAt(updatedAt);
        return session;
    }
}
