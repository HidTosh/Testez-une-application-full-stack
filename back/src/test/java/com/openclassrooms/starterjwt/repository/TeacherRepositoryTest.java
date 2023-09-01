package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

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
            .firstName("Ramesh")
            .lastName("Fadatare")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    // JUnit test for get all employees operation
    @DisplayName("JUnit test for get all Teachers operation")
    @Test
    public void givenTeachersList_whenFindAll_thenTeachersList(){
        // given - precondition or setup
        teacherRepository.save(teacher);
        // when -  action or the behaviour that we are going test
        List<Teacher> teachersList = teacherRepository.findAll();
        // then - verify the output
        assertThat(teachersList).isNotNull();
        Assert.assertTrue(teachersList.size()  >= 1 );
    }

    // JUnit test for get employee by id operation
    @DisplayName("JUnit test for get teacher by id")
    @Test
    public void givenTeacherObject_whenFindById_thenReturnTeacherObject(){
        teacherRepository.save(teacher);
        // when -  action or the behaviour that we are going test
        Teacher TeacherDB = teacherRepository.findById(teacher.getId()).get();
        // then - verify the output
        assertThat(TeacherDB).isNotNull();
    }

    @AfterEach
    public void cleanUpEach(){
        teacherRepository.deleteById(teacher.getId());
    }
}
