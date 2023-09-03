package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserDetailsImplTest {

    @InjectMocks
    UserDetailsImpl userDetailsImpl;

    @Test
    public void isAccountNonExpired() {
        assertThat(userDetailsImpl.isAccountNonExpired()).isTrue();
    }


    @Test
    public void isAccountNonLocked() {
        assertThat(userDetailsImpl.isAccountNonLocked()).isTrue();
    }

    @Test
    public void isCredentialsNonExpired() {
        assertThat(userDetailsImpl.isCredentialsNonExpired()).isTrue();
    }

    @Test
    public void isEnabled() {
        assertThat(userDetailsImpl.isEnabled()).isTrue();
    }

    @Test
    public void equals() {
        Object object = userDetails();
        assertThat(userDetailsImpl.equals(object)).isFalse();

        assertThat(userDetailsImpl.equals(null)).isFalse();

        userDetailsImpl = new UserDetailsImpl(
                1L,
                "yogauser",
                "first name",
                "last name",
                true,
                "test!1234");
        assertThat(userDetailsImpl.equals(userDetailsImpl)).isTrue();

        ReflectionTestUtils.setField(userDetailsImpl, "id", 1L);

        assertThat(userDetailsImpl.equals(object)).isTrue();

        assertThat(object.equals(object)).isTrue();

        assertThat(object.toString().equals(object.toString())).isTrue();

        assertThat(UserDetailsImpl.builder().toString().equals(UserDetailsImpl.builder().toString())).isTrue();
    }

    @Test
    public void getsTest() {
        userDetailsImpl = new UserDetailsImpl(
                1L,
                "yogauser",
                "first name",
                "last name",
                true,
                "test!1234");

        assertThat(userDetailsImpl.getAuthorities()).isEqualTo(new HashSet<GrantedAuthority>());
        assertThat(userDetailsImpl.getAdmin()).isEqualTo(true);
        assertThat(userDetailsImpl.getPassword()).isEqualTo("test!1234");
    }
    private UserDetailsImpl userDetails() {
        UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder()
                .id(1L)
                .username("yogauser")
                .firstName("first name")
                .lastName("last name")
                .password("test!1234")
                .admin(true)
                .build();
        return userDetailsImpl;
    }
}
