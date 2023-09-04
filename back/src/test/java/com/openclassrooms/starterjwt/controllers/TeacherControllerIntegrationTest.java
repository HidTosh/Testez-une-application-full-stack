package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class TeacherControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TeacherRepository teacherRepository;

    private Teacher mockTeacher;

    private Teacher mockTeacher1;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.now();
        mockTeacher = new Teacher(1L, "last_name", "first_name", date, date);
        mockTeacher1 = new Teacher(2L, "last_name2", "first_name2", date, date);
    }
    @Test
    public void shouldReturnTeacherList() throws Exception {
        //GIVEN
        List<Teacher> listTeachers = Arrays.asList(mockTeacher, mockTeacher1);
        //WHEN
        Mockito.when(teacherRepository.findAll()).thenReturn(listTeachers);
        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/teacher")
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(listTeachers.size()))
                .andExpect(jsonPath("$[0].lastName").value(mockTeacher.getLastName()))
                .andExpect(jsonPath("$[0].firstName").value(mockTeacher.getFirstName())
            );
    }

    @Test
    public void shouldReturnTeacher() throws Exception {
        //GIVEN
        Long id = Long.valueOf(3L);
        // WHEN teacher does not exist
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/teacher/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound()
            );
        //WHEN
        Mockito.when(teacherRepository.findById(id)).thenReturn(Optional.of(mockTeacher));
        //THEN
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/teacher/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value(mockTeacher.getLastName()))
                .andExpect(jsonPath("$.firstName").value(mockTeacher.getFirstName())
            );
        //WHEN teacher id not integer
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/teacher/{id}", "sdsd")
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest()
            );
    }
}
