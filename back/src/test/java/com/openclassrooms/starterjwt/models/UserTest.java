package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserTest {
    private User mockUser;
    private User mockUser1;

    @BeforeEach
    public void setUp() {
        LocalDateTime date = LocalDateTime.of(2023, 8, 31, 13, 17);
        mockUser = createUser(
            1L, "test@test.io", "last", "first", "**", false, date, date
        );
        mockUser1 = createUser(
            1L, "test@test.io", "last", "first", "**", false, date, date
        );
    }
    @Test
    public void testEquals() {
        User mockEmptyUser = new User();
        User mockEmptyUser1 = new User();
        // equality user instance with him self
        assertThat(mockUser.equals(mockUser)).isTrue();
        // equality of two user instance
        assertThat(mockUser.equals(mockUser1)).isTrue();
        // equality of two user instance
        assertThat(mockEmptyUser.equals(mockEmptyUser1)).isTrue();
        // equality of empty user instance with user instance
        assertThat(mockUser.equals(mockEmptyUser)).isFalse();
        // equality of empty user instance null
        assertThat(mockEmptyUser.equals(null)).isFalse();
        // equality of user instance null
        assertThat(mockEmptyUser.equals(mockUser)).isFalse();
    }

    @Test
    public void testToString() {
        User.UserBuilder user = new User.UserBuilder();
        assertThat(user.toString()).isEqualTo(user.toString());
        assertThat(mockUser.toString()).isEqualTo(mockUser.toString());
    }

    @Test
    public void testHashCodes() {
        User mockEmptyUser = new User();
        assertThat(mockEmptyUser.hashCode()).isEqualTo(mockEmptyUser.hashCode());
        assertThat(mockUser.hashCode()).isEqualTo(mockUser.hashCode());
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
