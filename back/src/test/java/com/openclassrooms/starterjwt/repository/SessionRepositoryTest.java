package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private User user;
    private Teacher teacher;
    private Session session;

    @BeforeEach
    public void setup(){
        user = new User(
                1L,
                "test@test.io",
                "last",
                "first",
                "***",
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        teacher = new Teacher(
                2L,
                "last_name",
                "first_name",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        session = Session.builder()
            .name("session 1")
            .date(new Date())
            .description("my description")
            .teacher(teacher)
            .users(Arrays.asList(user))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    // JUnit test for get all employees operation
    @DisplayName("JUnit test for get all session operation")
    @Test
    public void givenSessionsList_whenFindAll_thenSessionsList(){
        // given - precondition or setup
        sessionRepository.save(session);
        // when -  action or the behaviour that we are going test
        List<Session> sessionList = sessionRepository.findAll();
        // then - verify the output
        assertThat(sessionList).isNotNull();
        Assert.assertTrue(sessionList.size()  >= 1 );
    }

    // JUnit test for save employee operation
    //@DisplayName("JUnit test for save session operation")
    @Test
    public void givenSessionObject_whenSave_thenReturnSavedSession(){

        //given - precondition or setup
        // when - action or the behaviour that we are going test
        Session savedSession = sessionRepository.save(session);

        // then - verify the output
        assertThat(savedSession).isNotNull();
        assertThat(savedSession.getId()).isGreaterThan(0);
    }

    // JUnit test for get employee by id operation
    @DisplayName("JUnit test for get session by id")
    @Test
    public void givenTeacherObject_whenFindById_thenReturnTeacherObject(){
        sessionRepository.save(session);
        // when -  action or the behaviour that we are going test
        Session sessionDB = sessionRepository.findById(session.getId()).get();
        // then - verify the output
        assertThat(sessionDB).isNotNull();
    }

    // JUnit test for delete employee operation
    @DisplayName("JUnit test for delete session operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        sessionRepository.save(session);
        // when -  action or the behaviour that we are going test
        sessionRepository.deleteById(session.getId());
        //sessionRepository.findById(session.getId());
        Optional<Session> sessionOptional = sessionRepository.findById(session.getId());
        // then - verify the output
        assertThat(sessionOptional).isEmpty();
    }

    // JUnit test for update employee operation
    @DisplayName("JUnit test for update session operation")
    @Test
    public void givenSessionObject_whenUpdateSession_thenReturnUpdatedSession(){
        sessionRepository.save(session);

        // when -  action or the behaviour that we are going test
        Session savedSession = sessionRepository.findById(session.getId()).get();
        savedSession.setName("ram");
        savedSession.setDescription("my desc");
        Session updatedSession =  sessionRepository.save(savedSession);

        // then - verify the output
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
