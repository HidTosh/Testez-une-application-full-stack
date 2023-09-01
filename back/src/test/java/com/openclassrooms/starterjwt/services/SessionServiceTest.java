package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SessionServiceTest {
    @Mock
    SessionRepository sessionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    Teacher teacher;

    @InjectMocks
    SessionService sessionService;

    private Session mockSession;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        mockUser = getMockUser(1L);
        User mockUser2 = getMockUser(2L);
        User mockUser3 = getMockUser(3L);
        LocalDateTime date = LocalDateTime.now();
        ArrayList<User> arrlistUser = new ArrayList<User>(5);
        arrlistUser.add(mockUser2);
        arrlistUser.add(mockUser2);
        mockSession = new Session(
            3L,
            "session 1",
            new Date(),
            "my description",
            teacher,
            arrlistUser,
            date,
            date
        );
    }
    @Test
    public void findAllUsers() {
        List<Session> listSession = Arrays.asList(mockSession, mockSession);

        when(sessionRepository.findAll()).thenReturn(listSession);

        assertThat(sessionService.findAll().size()).isEqualTo(2);
        assertThat(sessionService.findAll().get(0).getName()).isEqualTo(mockSession.getName());
    }

    @Test
    public void getById() {
        Long idSessionExist = mockSession.getId();
        when(sessionRepository.findById(idSessionExist)).thenReturn(Optional.of(mockSession));

        assertThat(sessionService.getById(idSessionExist).getName()).isEqualTo(mockSession.getName());
        assertThat(sessionService.getById(idSessionExist).getDescription()).isEqualTo(mockSession.getDescription());

        // Id Session How Does Not Exist
        assertThat(sessionService.getById(5L)).isEqualTo(null);
    }

    @Test
    public void create() {
        when(sessionRepository.save(mockSession)).thenReturn(mockSession);

        assertThat(sessionService.create(mockSession)).isEqualTo(mockSession);
    }

    @Test
    public void delete() {
        sessionService.delete(mockSession.getId());

        verify(sessionRepository).deleteById(mockSession.getId()); // check that the method was called
    }

    @Test
    public void update() {
        Long idUpdate = 6L;
        mockSession.setId(idUpdate);
        when(sessionRepository.save(mockSession)).thenReturn(mockSession);

        Session sessionUpdated = sessionService.update(idUpdate, mockSession);
        assertThat(sessionUpdated.getId()).isEqualTo(mockSession.getId());
        assertThat(sessionUpdated.getName()).isEqualTo(mockSession.getName());
    }

    @Test
    public void participate() {
        when(sessionRepository.findById(mockSession.getId())).thenReturn(Optional.of(mockSession));
        // when user is null NotFoundException
        assertThrows(NotFoundException.class, () -> sessionService.participate(mockSession.getId(), mockUser.getId()));

        // when participate new user
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        sessionService.participate(mockSession.getId(), mockUser.getId());
        verify(sessionRepository).save(mockSession);

        // when user already participate throw BadRequestException
        mockSession.getUsers().add(mockUser);
        assertThrows(BadRequestException.class, () -> sessionService.participate(mockSession.getId(), mockUser.getId()));
    }

    @Test
    public void noLongerParticipate() {
        when(sessionRepository.findById(mockSession.getId())).thenReturn(Optional.of(mockSession));
        //When session does not exists
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(5L, mockUser.getId()));

        // when user is not in participate list
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(mockSession.getId(), mockUser.getId()));

        // when user already participate
        mockSession.getUsers().add(mockUser);
        sessionService.noLongerParticipate(mockSession.getId(), mockUser.getId());
        verify(sessionRepository).save(mockSession);
    }

    private User getMockUser(Long id) {
        return new User(
            id,
            "test@test.io",
            "last",
            "first",
            "***",
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }
}