package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    User mockUser;

    @Mock
    Teacher mockTeacher;

    private SessionDto mockSessionDto;

    private SessionDto mockSessionDtoNull;

    private Session mockSession;

    private Session mockSessionNull;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 13, 17);
        mockSessionDto = createSessionDto(1L, "my name", date, date);
        mockSession = createSession(1L, "my name", date, date);
        mockSessionDtoNull = null;
        mockSessionNull = null;
    }

    @Test
    public void toEntityListTest() {
        List<SessionDto> listSessionDto = Arrays.asList(mockSessionDto);
        List<Session> listSession = Arrays.asList(mockSession);
        List<SessionDto> listSessionDtoNull = null;

        assertThat(sessionMapperImpl.toEntity(listSessionDtoNull)).isNull();

        assertThat(sessionMapperImpl.toEntity(listSessionDto)).isEqualTo(listSession);
    }

    @Test
    public void toDtoListTest() {
        List<Session> listSessionNull = null;

        assertThat(sessionMapperImpl.toDto(listSessionNull)).isNull();
    }

    @Test
    public void toEntityTest() {
        assertThat(sessionMapperImpl.toEntity(mockSessionDtoNull)).isNull();
    }
    @Test
    public void toDtoTest() {
        assertThat(sessionMapperImpl.toDto(mockSessionNull)).isNull();

        mockSessionDto.setUsers(Arrays.asList());
        assertThat(sessionMapperImpl.toDto(mockSession)).isEqualTo(mockSessionDto);

        Teacher teacher = new Teacher(null, "test", "test", LocalDateTime.now(), LocalDateTime.now());

        mockSession.setTeacher(teacher);
        assertThat(sessionMapperImpl.toDto(mockSession)).isEqualTo(mockSessionDto);

        Teacher teacher2 = new Teacher(1L, "test", "test", LocalDateTime.now(), LocalDateTime.now());

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
