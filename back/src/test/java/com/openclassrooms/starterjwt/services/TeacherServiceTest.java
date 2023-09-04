package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TeacherServiceTest {
    @InjectMocks
    TeacherService teacherService;
    @Mock
    TeacherRepository teacherRepository;

    private Teacher mockTeacher;

    @BeforeEach
    public void setUp() {
        mockTeacher = new Teacher(
            2L,
            "last_name",
            "first_name",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }
    @Test public void findAll() {
        List<Teacher> listTeacher = Arrays.asList(mockTeacher, mockTeacher);

        when(teacherRepository.findAll()).thenReturn(listTeacher);

        assertThat(teacherService.findAll().size()).isEqualTo(2);
        assertThat(teacherService.findAll().get(0).getFirstName()).isEqualTo(mockTeacher.getFirstName());
    }

    @Test public void findById() {
        Long idSession = mockTeacher.getId();
        // Id Session How Does Not Exist
        assertThat(teacherService.findById(idSession)).isEqualTo(null);

        when(teacherRepository.findById(idSession)).thenReturn(Optional.of(mockTeacher));

        assertThat(teacherService.findById(idSession).getFirstName()).isEqualTo(mockTeacher.getFirstName());
        assertThat(teacherService.findById(idSession).getLastName()).isEqualTo(mockTeacher.getLastName());
    }
}