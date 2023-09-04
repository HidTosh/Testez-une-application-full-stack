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
    @InjectMocks
    SessionService sessionService;
    @Mock
    SessionRepository sessionRepository;
    @Mock
    UserRepository userRepository;

    private Session mockSession;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.now();
        mockUser = getMockUser(1L);
        Teacher mockTeacher = new Teacher(2L, "last_name", "first_name", date, date);
        ArrayList<User> arrayListUser = new ArrayList<User>(1);
        arrayListUser.add(getMockUser(2L));
        mockSession = new Session(
            3L, "session 1", new Date(), "my description", mockTeacher, arrayListUser, date, date
        );
    }
    @Test
    public void shouldReturnAllSession() {
        List<Session> listSession = Arrays.asList(mockSession, mockSession);

        when(sessionRepository.findAll()).thenReturn(listSession);

        assertThat(sessionService.findAll().size()).isEqualTo(listSession.size());
        assertThat(sessionService.findAll().get(0).getName()).isEqualTo(mockSession.getName());
    }

    @Test
    public void shouldReturnSession() {
        Long idSession = mockSession.getId();

        // Id Session Hwo Does Not Exist
        assertThat(sessionService.getById(idSession)).isEqualTo(null);

        when(sessionRepository.findById(idSession)).thenReturn(Optional.of(mockSession));

        assertThat(sessionService.getById(idSession).getName()).isEqualTo(mockSession.getName());
        assertThat(sessionService.getById(idSession).getDescription()).isEqualTo(mockSession.getDescription());
    }

    @Test
    public void shouldReturnSessionAfterCreate() {
        when(sessionRepository.save(mockSession)).thenReturn(mockSession);

        assertThat(sessionService.create(mockSession)).isEqualTo(mockSession);
    }

    @Test
    public void shouldNotReturnSessionAfterDelete() {
        sessionService.delete(mockSession.getId());

        assertThat(sessionService.getById(mockSession.getId())).isNull();
        verify(sessionRepository).deleteById(mockSession.getId()); // check that the method was called
    }

    @Test
    public void shouldReturnDataUpdated() {
        Long idUpdate = 6L;
        mockSession.setId(idUpdate);
        when(sessionRepository.save(mockSession)).thenReturn(mockSession);

        Session sessionUpdated = sessionService.update(idUpdate, mockSession);
        assertThat(sessionUpdated.getId()).isEqualTo(mockSession.getId());
        assertThat(sessionUpdated.getName()).isEqualTo(mockSession.getName());
    }

    @Test
    public void shouldAddUserToSession() {
        when(sessionRepository.findById(mockSession.getId())).thenReturn(Optional.of(mockSession));
        //WHEN user is null NotFoundException
        assertThrows(NotFoundException.class, () ->
            sessionService.participate(mockSession.getId(), mockUser.getId())
        );
        //WHEN participate new user
        assertThat(mockSession.getUsers().size()).isEqualTo(1);
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        sessionService.participate(mockSession.getId(), mockUser.getId());
        verify(sessionRepository).save(mockSession);
        assertThat(mockSession.getUsers().size()).isEqualTo(2);
        //WHEN user already participate throw BadRequestException
        mockSession.getUsers().add(mockUser);
        assertThrows(BadRequestException.class, () ->
            sessionService.participate(mockSession.getId(), mockUser.getId())
        );
    }

    @Test
    public void noLongerParticipate() {
        //When session does not exists
        assertThrows(NotFoundException.class, () ->
            sessionService.noLongerParticipate(mockSession.getId(), mockUser.getId())
        );
        //When user is not in participate list
        when(sessionRepository.findById(mockSession.getId())).thenReturn(Optional.of(mockSession));
        assertThrows(BadRequestException.class, () ->
            sessionService.noLongerParticipate(mockSession.getId(), mockUser.getId())
        );
        // When user already participate
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