package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    public void setup(){
        teacher = Teacher.builder()
            .firstName("test first name")
            .lastName("test last name")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    public void givenTeacherWhenFindAllThenTeachersList(){
        //Given
        teacherRepository.save(teacher);
        //When
        List<Teacher> teachersList = teacherRepository.findAll();
        //Then
        assertThat(teachersList).isNotNull();
        assertThat(teachersList.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void givenTeacherWhenFindByIdThenReturnTeacher(){
        teacherRepository.save(teacher);
        //When
        Teacher TeacherDB = teacherRepository.findById(teacher.getId()).get();
        //Then
        assertThat(TeacherDB).isNotNull();
    }

    @AfterEach
    public void cleanUpEach(){
        try {
            teacherRepository.deleteById(teacher.getId());
        }
        catch (Exception e) {
            fail("Exception " + e);
        }
    }
}
