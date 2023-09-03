package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest
public class JwtResponseTest {

    private JwtResponse jwtResponse;

    @BeforeEach
    public void setUp() {
        jwtResponse = new JwtResponse(
            "access-token",
            1L,
            "username",
            "first name",
            "last name",
            false
        );
    }
    @Test
    public void testGetSetToken() {
        assertThat(jwtResponse.getToken()).isEqualTo("access-token");

        jwtResponse.setToken("new access-token");
        assertThat(jwtResponse.getToken()).isEqualTo("new access-token");
    }

    @Test
    public void testGetSetIsAdmin() {
        assertThat(jwtResponse.getAdmin()).isEqualTo(false);

        jwtResponse.setAdmin(true);
        assertThat(jwtResponse.getAdmin()).isEqualTo(true);
    }

    @Test
    public void testGetSetLastName() {
        assertThat(jwtResponse.getLastName()).isEqualTo("last name");

        jwtResponse.setLastName("new last name");
        assertThat(jwtResponse.getLastName()).isEqualTo("new last name");
    }

    @Test
    public void testGetSetFirstName() {
        assertThat(jwtResponse.getFirstName()).isEqualTo("first name");

        jwtResponse.setFirstName("new first name");
        assertThat(jwtResponse.getFirstName()).isEqualTo("new first name");
    }

    @Test
    public void testGetSetUserName() {
        assertThat(jwtResponse.getUsername()).isEqualTo("username");

        jwtResponse.setUsername("new username");
        assertThat(jwtResponse.getUsername()).isEqualTo("new username");
    }

    @Test
    public void testGetSetId() {
        assertThat(jwtResponse.getId()).isEqualTo(1L);

        jwtResponse.setId(2L);
        assertThat(jwtResponse.getId()).isEqualTo(2L);
    }

    @Test
    public void testGetSetType() {
        assertThat(jwtResponse.getType()).isEqualTo("Bearer");

        jwtResponse.setType("Clear");
        assertThat(jwtResponse.getType()).isEqualTo("Clear");
    }
}
