package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TeacherTest { ;
    private Teacher mockTeacher;
    private Teacher mockTeacher1;
    private Teacher mockTeacher2;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 13, 17);
        mockTeacher = createTeacher(1L, "last_name", "first_name", date, date);
        mockTeacher1 = createTeacher(2L, "last_name2", "first_name2", date, date);

        mockTeacher2 = Teacher.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .createdAt(date)
                .updatedAt(date)
                .build();
    }
    @Test
    public void testEquals() {
        Teacher mockEmptyTeacher = new Teacher();
        // equality of two user instance
        assertThat(mockTeacher.equals(mockTeacher1)).isFalse();
        // equality user instance with him self
        assertThat(mockTeacher.equals(mockTeacher)).isTrue();
        // equality of two user instance
        assertThat(mockEmptyTeacher.equals(mockEmptyTeacher)).isTrue();
        // equality of empty user instance with user instance
        assertThat(mockTeacher.equals(mockEmptyTeacher)).isFalse();
        // equality of empty user instance null
        assertThat(mockEmptyTeacher.equals(null)).isFalse();
        // equality of user instance null
        assertThat(mockEmptyTeacher.equals(mockTeacher)).isFalse();

        Object object = new Object();
        assertThat(mockEmptyTeacher.equals(object)).isFalse();

    }

    @Test
    public void testToString() {
        assertThat(mockTeacher2).isEqualTo(mockTeacher);
        assertThat(mockTeacher2.toString()).isEqualTo(mockTeacher.toString());

        Teacher.TeacherBuilder mockEmptyTeacher1 = new Teacher.TeacherBuilder();
        assertThat(mockEmptyTeacher1.toString()).isEqualTo(mockEmptyTeacher1.toString());
    }

    @Test
    public void testHashCodes() {
        assertThat(mockTeacher.hashCode()).isEqualTo(mockTeacher.hashCode());
    }

    private Teacher createTeacher(
        Long id,
        String lastName,
        String firstName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setLastName(lastName);
        teacher.setFirstName(firstName);
        teacher.setCreatedAt(createdAt);
        teacher.setUpdatedAt(updatedAt);
        return teacher;
    }
}
