package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthTokenFilterTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    AuthTokenFilter authTokenFilter;

    @InjectMocks
    JwtUtils jwtUtils;

    @InjectMocks
    UserDetailsServiceImpl userDetailsServiceImpl;

    private Authentication authentication;

    private UserDetailsImpl userDetails;

    private User mockUser;

    @BeforeEach
    public void setup() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "openclassrooms");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 360000);


        authTokenFilter = new AuthTokenFilter();
        ReflectionTestUtils.setField(authTokenFilter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(authTokenFilter, "userDetailsService", userDetailsServiceImpl);

        userDetails = userDetails();
        TestingAuthenticationProvider provider = new TestingAuthenticationProvider();
        TestingAuthenticationToken token = new TestingAuthenticationToken(
                userDetails,
                "ROLE_USER"
        );
        authentication = provider.authenticate(token);
    }

    @Test
    public void doFilterInternal() throws Exception{
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String myNewToken = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 360000))
                .signWith(SignatureAlgorithm.HS512, "openclassrooms")
                .compact();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + myNewToken);
        request.setParameter("firstName", "Spring");
        request.setParameter("lastName", "Test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        authTokenFilter.doFilterInternal(request, response, filterChain);



        MockFilterChain filterChain2 = new MockFilterChain();
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(mockUser()));
        authTokenFilter.doFilterInternal(request, response, filterChain2);
    }

    private UserDetailsImpl userDetails() {
        return new UserDetailsImpl(
            1L,
            "test@test.io",
            "first name",
            "last name",
            true,
            "test!1234"
        );
    }

    private User mockUser() {
        return new User(
            1L,
            "test@test.io",
            "last",
            "first",
            "***",
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }
}
