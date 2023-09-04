package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDetailsServiceImplTest {
    @InjectMocks
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    UserRepository userRepository;

    @Mock
    User user;

    @Test
    public void loadUserByUsername() {
        String fakeUserName = "my name is";
        try {
            userDetailsServiceImpl.loadUserByUsername(fakeUserName);
        } catch (UsernameNotFoundException expectedException) {
            fail("User Not Found with email: " + fakeUserName);
        }

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
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThat(userDetailsServiceImpl.loadUserByUsername(user.getEmail()).getUsername()).isEqualTo(user.getEmail());

    }

}
