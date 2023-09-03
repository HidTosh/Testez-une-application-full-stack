package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TeacherService teacherService;

    private Teacher mockTeacher;

    private Teacher mockTeacher1;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 13, 17);
        mockTeacher = createTeacher(1L, "last_name", "first_name", date, date);
        mockTeacher1 = createTeacher(2L, "last_name2", "first_name2", date, date);
    }
    @Test
    public void shouldReturnTeacherList() throws Exception {
        //GIVEN
        List<Teacher> listTeachers = Arrays.asList(this.mockTeacher, this.mockTeacher1);
        //WHEN
        Mockito.when(teacherService.findAll()).thenReturn(listTeachers);
        //THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/teacher")
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(listTeachers.size()))
            .andExpect(jsonPath("$[0].lastName").value(this.mockTeacher.getLastName()))
            .andExpect(jsonPath("$[0].firstName").value(this.mockTeacher.getFirstName())
        );
    }

    @Test
    public void shouldReturnTeacher() throws Exception {
        //GIVEN
        Long id = Long.valueOf(3L);
        //WHEN
        Mockito.when(teacherService.findById(id)).thenReturn(this.mockTeacher);
        //THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/teacher/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastName").value(this.mockTeacher.getLastName()))
            .andExpect(jsonPath("$.firstName").value(this.mockTeacher.getFirstName())
        );

        //THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/teacher/{id}", 5L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        //THEN
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/teacher/{id}", "sdsd")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    private Teacher createTeacher(Long id, String lastName, String firstName, LocalDateTime createdAt,  LocalDateTime updatedAt) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setLastName(lastName);
        teacher.setFirstName(firstName);
        teacher.setCreatedAt(createdAt);
        teacher.setUpdatedAt(updatedAt);
        return teacher;
    }
}
