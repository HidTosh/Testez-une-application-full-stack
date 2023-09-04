package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
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

    @Test
    public void givenUserWhenFindByIdThenReturnUser(){
        userRepository.save(user);
        //When
        User userDB = userRepository.findById(user.getId()).get();
        //Then
        assertThat(userDB).isNotNull();
    }

    @Test
    public void givenUserWhenDeleteThenUserDeleted(){
        userRepository.save(user);
        //When
        userRepository.deleteById(user.getId());
        Optional<User> userOptional = userRepository.findById(user.getId());
        //Then - verify the output
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
