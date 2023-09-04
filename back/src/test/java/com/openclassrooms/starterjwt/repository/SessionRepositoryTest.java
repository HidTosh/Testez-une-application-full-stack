package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;

    private Session session;

    @BeforeEach
    public void setup(){
        LocalDateTime date = LocalDateTime.now();
        User user = new User(
            1L, "test@test.io", "last", "first", "***", false, date, date
        );
        Teacher teacher = new Teacher(
            2L, "last_name", "first_name", date, date
        );
        session = Session.builder()
            .name("session 1")
            .date(new Date())
            .description("my description")
            .teacher(teacher)
            .users(Arrays.asList(user, user))
            .createdAt(date)
            .updatedAt(date)
            .build();
    }

    @Test
    public void givenSessionWhenFindAllThenSessionsList(){
        //Given
        sessionRepository.save(session);
        //When find all
        List<Session> sessionList = sessionRepository.findAll();
        //Then
        assertThat(sessionList).isNotNull();
        assertThat(sessionList.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void givenSessionWhenSaveThenReturnSavedSession(){
        //When save
        Session savedSession = sessionRepository.save(session);
        //Then - verify the output
        assertThat(savedSession).isNotNull();
        assertThat(savedSession.getId()).isEqualTo(session.getId());
    }

    @Test
    public void givenSessionWhenFindByIdThenReturnSession(){
        sessionRepository.save(session);
        //When
        Session sessionDb = sessionRepository.findById(session.getId()).get();
        //Then
        assertThat(sessionDb).isNotNull();
        assertThat(sessionDb.getId()).isEqualTo(session.getId());
    }

    @Test
    public void givenSessionWhenDeleteThenSessionDeleted(){
        sessionRepository.save(session);
        //When
        sessionRepository.deleteById(session.getId());
        Optional<Session> sessionOptional = sessionRepository.findById(session.getId());
        //Then
        assertThat(sessionOptional).isEmpty();
    }

    @Test
    public void givenSessionWhenUpdateThenReturnUpdatedSession(){
        sessionRepository.save(session);
        //When
        Session savedSession = sessionRepository.findById(session.getId()).get();
        savedSession.setName("ram");
        savedSession.setDescription("my desc");
        Session updatedSession =  sessionRepository.save(savedSession);
        //Then
        assertThat(updatedSession.getName()).isEqualTo("ram");
        assertThat(updatedSession.getDescription()).isEqualTo("my desc");
    }

    @AfterEach
    public void cleanUpEach(){
        try {
            sessionRepository.deleteById(session.getId());
        }
        catch (Exception e) {
            fail("Exception " + e);
        }
    }
}
