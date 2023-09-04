package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TeacherMapperTest {
    @InjectMocks
    TeacherMapperImpl teacherMapperImpl;

    private TeacherDto mockTeacherDto;

    private TeacherDto mockTeacherDtoNull;

    private Teacher mockTeacher;

    private Teacher mockTeacherNull;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.now();
        mockTeacherDto = createTeacherDto(1L, "last", "first", date, date);
        mockTeacher = createTeacher(1L, "last", "first", date, date);
        mockTeacherDtoNull = null;
        mockTeacherNull = null;
    }

    @Test
    public void toEntity() {
        Teacher.TeacherBuilder teacher = Teacher.builder();
        teacher.id(mockTeacherDto.getId());
        teacher.lastName(mockTeacherDto.getLastName());
        teacher.firstName(mockTeacherDto.getFirstName());
        teacher.createdAt(mockTeacherDto.getCreatedAt());
        teacher.updatedAt(mockTeacherDto.getUpdatedAt());

        assertThat(teacherMapperImpl.toEntity(mockTeacherDtoNull)).isNull();

        assertThat(teacherMapperImpl.toEntity(mockTeacherDto)).isEqualTo(teacher.build());
    }

    @Test
    public void toDtoList() {
        List<TeacherDto> listTeacherDto = Arrays.asList(mockTeacherDto, mockTeacherDto);
        List<Teacher> listTeacher = Arrays.asList(mockTeacher, mockTeacher);
        List<Teacher> listTeacherNull = null;

        assertThat(teacherMapperImpl.toDto(listTeacherNull)).isNull();

        assertThat(teacherMapperImpl.toDto(listTeacher)).isEqualTo(listTeacherDto);
    }

    @Test
    public void toDo() {
        assertThat(teacherMapperImpl.toDto(mockTeacherNull)).isNull();

        assertThat(teacherMapperImpl.toDto(mockTeacher)).isEqualTo(mockTeacherDto);
    }

    @Test
    public void toEntityList() {
        List<TeacherDto> listTeacherDto = Arrays.asList(mockTeacherDto,mockTeacherDto);
        List<Teacher> listTeacher = Arrays.asList(mockTeacher, mockTeacher);
        List<TeacherDto> listTeacherDtoNull = null;

        assertThat(teacherMapperImpl.toEntity(listTeacherDtoNull)).isNull();

        assertThat(teacherMapperImpl.toEntity(listTeacherDto)).isEqualTo(listTeacher);
    }

    private TeacherDto createTeacherDto(
        Long id,
        String lastName,
        String firstName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(id);
        teacherDto.setLastName(lastName);
        teacherDto.setFirstName(firstName);
        teacherDto.setCreatedAt(createdAt);
        teacherDto.setUpdatedAt(updatedAt);
        return teacherDto;
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
