package com.openclassrooms.starterjwt.security.jwt;

import nl.altindag.log.LogCaptor;
import nl.altindag.log.model.LogEvent;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class AuthEntryPointJwtTest {

    @InjectMocks
    AuthEntryPointJwt authEntryPointJwt;
    @Test
    public void commence() throws Exception {
        LogCaptor logCaptor = LogCaptor.forClass(AuthEntryPointJwt.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException e = mock(AuthenticationException.class);

        authEntryPointJwt.commence(request, response, e);

        List<LogEvent> logEvents = logCaptor.getLogEvents();
        assertThat(logEvents.size()).isEqualTo(1);
        LogEvent logEvent = logEvents.get(0);
        assertThat(logEvent.getMessage()).isEqualTo("Unauthorized error: {}");
        assertThat(logEvent.getLevel()).isEqualTo("ERROR");
    }
}
