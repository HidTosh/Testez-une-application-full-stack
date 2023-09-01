package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup(){
        user = User.builder()
            .email("test@test.io")
            .lastName("last")
            .firstName("first")
            .password("******")
            .admin(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    // JUnit test for get employee by id operation
    @DisplayName("JUnit test for get teacher by id")
    @Test
    public void givenUserObject_whenFindById_thenReturnUserObject(){
        userRepository.save(user);
        // when -  action or the behaviour that we are going test
        User userDB = userRepository.findById(user.getId()).get();
        // then - verify the output
        assertThat(userDB).isNotNull();
    }


    // JUnit test for delete employee operation
    @DisplayName("JUnit test for delete user operation")
    @Test
    public void givenUserObject_whenDelete_thenRemoveUser(){
        userRepository.save(user);
        // when -  action or the behaviour that we are going test
        userRepository.deleteById(user.getId());
        //sessionRepository.findById(session.getId());
        Optional<User> userOptional = userRepository.findById(user.getId());
        // then - verify the output
        assertThat(userOptional).isEmpty();
    }

    @AfterEach
    public void cleanUpEach(){
        try {
            userRepository.deleteById(user.getId());
        }
        catch (Exception e) {
            fail("Exception " + e);
        }
    }
}
