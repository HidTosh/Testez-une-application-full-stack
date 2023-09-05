package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import nl.altindag.log.LogCaptor;
import nl.altindag.log.model.LogEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

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
        TestingAuthenticationToken token = new TestingAuthenticationToken(userDetails, "ROLE_USER");
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

        assertThat(jwtUtils.validateJwtToken(token)).isEqualTo(true); //No Exception throw

        LogCaptor logCaptor = LogCaptor.forClass(JwtUtils.class);
        assertThat(jwtUtils.validateJwtToken(token + "545")).isEqualTo(false); //SignatureException
        List<LogEvent> logEvents = logCaptor.getLogEvents();
        assertThat(logEvents.size()).isEqualTo(1);
        LogEvent logEvent = logEvents.get(0);
        assertThat(logEvent.getMessage()).isEqualTo("Invalid JWT signature: {}");
        assertThat(logEvent.getLevel()).isEqualTo("ERROR");

        LogCaptor logCaptor1 = LogCaptor.forClass(JwtUtils.class);
        assertThat(jwtUtils.validateJwtToken("string")).isEqualTo(false); //MalformedJwtException
        List<LogEvent> logEvents1 = logCaptor1.getLogEvents();
        assertThat(logEvents1.size()).isEqualTo(1);
        LogEvent logEvent1 = logEvents1.get(0);
        assertThat(logEvent1.getMessage()).isEqualTo("Invalid JWT token: {}");
        assertThat(logEvent1.getLevel()).isEqualTo("ERROR");

        LogCaptor logCaptor2 = LogCaptor.forClass(JwtUtils.class);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 0);
        String myToken = jwtUtils.generateJwtToken(authentication);
        assertThat(jwtUtils.validateJwtToken(myToken)).isEqualTo(false); //ExpiredJwtException
        List<LogEvent> logEvents2 = logCaptor2.getLogEvents();
        assertThat(logEvents2.size()).isEqualTo(1);
        LogEvent logEvent2 = logEvents2.get(0);
        assertThat(logEvent2.getMessage()).isEqualTo("JWT token is expired: {}");
        assertThat(logEvent2.getLevel()).isEqualTo("ERROR");

        LogCaptor logCaptor3 = LogCaptor.forClass(JwtUtils.class);
        String mockJwt = Jwts.builder().setIssuedAt(new Date()).compact();
        assertThat(jwtUtils.validateJwtToken(mockJwt)).isEqualTo(false); //UnsupportedJwtException
        List<LogEvent> logEvents3 = logCaptor3.getLogEvents();
        assertThat(logEvents3.size()).isEqualTo(1);
        LogEvent logEvent3 = logEvents3.get(0);
        assertThat(logEvent3.getMessage()).isEqualTo("JWT token is unsupported: {}");
        assertThat(logEvent3.getLevel()).isEqualTo("ERROR");

        LogCaptor logCaptor4 = LogCaptor.forClass(JwtUtils.class);
        assertThat(jwtUtils.validateJwtToken("")).isEqualTo(false); //IllegalArgumentException
        List<LogEvent> logEvents4 = logCaptor4.getLogEvents();
        assertThat(logEvents4.size()).isEqualTo(1);
        LogEvent logEvent4 = logEvents4.get(0);
        assertThat(logEvent4.getMessage()).isEqualTo("JWT claims string is empty: {}");
        assertThat(logEvent4.getLevel()).isEqualTo("ERROR");
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
