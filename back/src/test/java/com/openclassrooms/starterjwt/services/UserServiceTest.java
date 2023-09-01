package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        mockUser = new User(
            1L,
            "test@test.io",
            "last",
            "first",
            "***",
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    public void delete() {
        Long idUserExist = mockUser.getId();

        userService.delete(idUserExist);

        verify(userRepository).deleteById(idUserExist);
    }

    @Test
    public void findById() {
        Long idUserExist = mockUser.getId();

        when(userRepository.findById(idUserExist)).thenReturn(Optional.of(mockUser));

        assertThat(userService.findById(idUserExist).getFirstName()).isEqualTo(mockUser.getFirstName());
        assertThat(userService.findById(idUserExist).getLastName()).isEqualTo(mockUser.getLastName());

        // Id user How Does Not Exist
        assertThat(userService.findById(5L)).isEqualTo(null);
    }
}
