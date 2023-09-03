package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthEntryPointJwtTest {

    @InjectMocks
    AuthEntryPointJwt authEntryPointJwt;
    @Test
    public void commence() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("firstName", "Spring");
        request.setParameter("lastName", "Test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException e = mock(AuthenticationException.class);

        authEntryPointJwt.commence(request, response, e);
    }
}
