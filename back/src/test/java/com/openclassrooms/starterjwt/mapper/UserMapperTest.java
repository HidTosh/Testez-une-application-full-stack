package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserMapperTest {
    @InjectMocks
    UserMapperImpl userMapperImpl;

    private UserDto mockUserDto;

    private UserDto mockUserDtoNull;

    private User mockUser;

    private User mockUserNull;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.now();
        mockUserDto = createUserDto(
            1L, "test@test.io", "last", "first", "**", false, date, date
        );
        mockUser = createUser(
            1L, "test@test.io", "last", "first", "**", false, date, date
        );
        mockUserDtoNull = null;
        mockUserNull = null;
    }

    @Test
    public void toEntity() {
        User.UserBuilder user = User.builder();
        user.id(mockUserDto.getId());
        user.email(mockUserDto.getEmail());
        user.lastName(mockUserDto.getLastName());
        user.firstName(mockUserDto.getFirstName());
        user.password(mockUserDto.getPassword());
        user.admin(mockUserDto.isAdmin());
        user.createdAt(mockUserDto.getCreatedAt());
        user.updatedAt(mockUserDto.getUpdatedAt());

        assertThat(userMapperImpl.toEntity(mockUserDtoNull)).isNull();

        assertThat(userMapperImpl.toEntity(mockUserDto)).isEqualTo(user.build());
    }

    @Test
    public void toDtoList() {
        List<UserDto> listUserDto = Arrays.asList(mockUserDto, mockUserDto);
        List<User> listUser = Arrays.asList(mockUser, mockUser);
        List<User> listUserDtoNull = null;

        assertThat(userMapperImpl.toDto(listUserDtoNull)).isNull();

        assertThat(userMapperImpl.toDto(listUser)).isEqualTo(listUserDto);
    }

    @Test
    public void toDo() {
        assertThat(userMapperImpl.toDto(mockUserNull)).isNull();

        assertThat(userMapperImpl.toDto(mockUser)).isEqualTo(mockUserDto);
    }

    @Test
    public void toEntityList() {
        List<UserDto> listUserDto = Arrays.asList(mockUserDto, mockUserDto);
        List<User> listUser = Arrays.asList(mockUser, mockUser);
        List<UserDto> listUserDtoNull = null;

        assertThat(userMapperImpl.toEntity(listUserDtoNull)).isNull();

        assertThat(userMapperImpl.toEntity(listUserDto)).isEqualTo(listUser);
    }

    private UserDto createUserDto(
            Long id,
            String email,
            String lastName,
            String firstName,
            String password,
            Boolean isAdmin,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        UserDto user = new UserDto();
        user.setId(id);
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setAdmin(isAdmin);
        user.setPassword(password);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);
        return user;
    }

    private User createUser(
            Long id,
            String email,
            String lastName,
            String firstName,
            String password,
            Boolean isAdmin,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setAdmin(isAdmin);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);
        return user;
    }
}
