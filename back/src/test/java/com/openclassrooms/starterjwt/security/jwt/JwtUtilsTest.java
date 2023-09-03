package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JwtUtilsTest {
    @InjectMocks
    JwtUtils jwtUtils;

    private Authentication authentication;

    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setup() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "openclassrooms");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600);

        userDetails = userDetails();
        TestingAuthenticationProvider provider = new TestingAuthenticationProvider();
        TestingAuthenticationToken token = new TestingAuthenticationToken(
                userDetails,
                "ROLE_USER"
        );
        authentication = provider.authenticate(token);
    }

    @Test
    public void generateJwtToken() {
        assertThat(jwtUtils.generateJwtToken(authentication)).isNotNull();
    }

    @Test
    public void getUserNameFromJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);

        assertThat(jwtUtils.getUserNameFromJwtToken(token)).isEqualTo(userDetails.getUsername());
    }

    @Test
    public void validateJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);

        assertThat(jwtUtils.validateJwtToken(token)).isEqualTo(true);

        assertThat(jwtUtils.validateJwtToken(token + "545")).isEqualTo(false); //SignatureException

        assertThat(jwtUtils.validateJwtToken("string")).isEqualTo(false); // MalformedJwtException

        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 0);
        String myToken = jwtUtils.generateJwtToken(authentication);
        assertThat(jwtUtils.validateJwtToken(myToken)).isEqualTo(false); //ExpiredJwtException

        String mockJwt = Jwts.builder().setIssuedAt(new Date()).compact();
        assertThat(jwtUtils.validateJwtToken(mockJwt)).isEqualTo(false);// UnsupportedJwtException

        assertThat(jwtUtils.validateJwtToken("")).isEqualTo(false); //IllegalArgumentException
    }

    private UserDetailsImpl userDetails() {
        return new UserDetailsImpl(
            1L,
            "yogauser",
            "first name",
            "last name",
            true,
            "test!1234"
        );
    }
}
