package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class SessionTest {
    @Mock
    User user;
    @Mock
    Teacher teacher;
    private Session mockSession;
    private Session mockSession1;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 13, 17);
        mockSession = createSession(1L, "name", "description", date, date);
        mockSession1 = createSession(1L, "name", "description", date, date);
    }

    @Test
    public void testEquals() {
        Session mockEmptySession = new Session();
        // equality of two session instance
        assertThat(mockSession.equals(mockSession1)).isTrue();
        // equality session instance with him self
        assertThat(mockSession.equals(mockSession)).isTrue();
        // equality of two session instance
        assertThat(mockEmptySession.equals(mockEmptySession)).isTrue();
        // equality of empty session instance with user instance
        assertThat(mockSession.equals(mockEmptySession)).isFalse();
        // equality of empty session instance null
        assertThat(mockEmptySession.equals(null)).isFalse();
        // equality of session instance null
        assertThat(mockEmptySession.equals(mockSession)).isFalse();
    }

    @Test
    public void testToString() {
        Session.SessionBuilder mockEmptySession1 = new Session.SessionBuilder();

        assertThat(mockEmptySession1.toString()).isEqualTo(mockEmptySession1.toString());
        assertThat(mockSession.toString()).isEqualTo(mockSession.toString());
    }

    @Test
    public void testHashCodes() {
        assertThat(mockSession.hashCode()).isEqualTo(mockSession.hashCode());
    }

    private Session createSession(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        Session session = new Session();
        session.setId(id);
        session.setName(name);
        session.setDate(new Date());
        session.setDescription(description);
        session.setUsers(Arrays.asList(user));
        session.setTeacher(teacher);
        session.setCreatedAt(createdAt);
        session.setUpdatedAt(updatedAt);
        return session;
    }
}
