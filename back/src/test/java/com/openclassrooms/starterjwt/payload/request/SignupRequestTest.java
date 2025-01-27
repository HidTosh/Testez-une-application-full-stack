package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class SignupRequestTest {
    private SignupRequest mockSignupRequest;
    private SignupRequest mockSignupRequest1;
    private SignupRequest mockSignupRequest2;

    @BeforeEach
    public void setUp() {
        mockSignupRequest = signupRequest("test@test.io", "last", "first", "**");
        mockSignupRequest1 = signupRequest("test@test.io", "last", "first", "**");
        mockSignupRequest2 = new SignupRequest();
    }

    @Test
    public void testEquals() {
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isTrue();

        assertThat(mockSignupRequest.equals(mockSignupRequest)).isTrue();

        assertThat(mockSignupRequest2.equals(mockSignupRequest2)).isTrue();

        assertThat(mockSignupRequest.equals(mockSignupRequest2)).isFalse();

        assertThat(mockSignupRequest2.equals(null)).isFalse();

        assertThat(mockSignupRequest2.equals(mockSignupRequest)).isFalse();

        mockSignupRequest1.setEmail("test2@test.io");
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isFalse();

        mockSignupRequest1.setEmail("test@test.io");
        mockSignupRequest1.setLastName("last2");
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isFalse();

        mockSignupRequest1.setLastName("last2");
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isFalse();

        mockSignupRequest1.setLastName("last");
        mockSignupRequest1.setFirstName("first2");
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isFalse();

        mockSignupRequest1.setFirstName("first");
        mockSignupRequest1.setPassword("****");
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isFalse();
        mockSignupRequest1.setPassword("**");

        mockSignupRequest1.setEmail(null);
        mockSignupRequest.setEmail(null);
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isTrue();

        mockSignupRequest1.setLastName(null);
        mockSignupRequest.setLastName(null);
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isTrue();

        mockSignupRequest1.setFirstName(null);
        mockSignupRequest.setFirstName(null);
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isTrue();

        mockSignupRequest1.setPassword(null);
        mockSignupRequest.setPassword(null);
        assertThat(mockSignupRequest.equals(mockSignupRequest1)).isTrue();
    }

    @Test
    public void testToString() {
        Object object = new Object();

        assertThat(mockSignupRequest).isEqualTo(mockSignupRequest);
        assertThat(mockSignupRequest.toString()).isEqualTo(mockSignupRequest.toString());

        assertThat(object.equals(mockSignupRequest)).isFalse();
        assertThat(object.toString().equals(mockSignupRequest.toString())).isFalse();
    }

    @Test
    public void testHashCodes() {
        assertThat(mockSignupRequest).isEqualTo(mockSignupRequest);

        assertThat(mockSignupRequest.hashCode()).isEqualTo(mockSignupRequest.hashCode());
        assertThat(mockSignupRequest2.hashCode()).isNotEqualTo(mockSignupRequest.hashCode());
    }

    private SignupRequest signupRequest(String email, String lastName, String firstName, String password) {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setLastName(lastName);
        signupRequest.setFirstName(firstName);
        signupRequest.setPassword(password);
        return signupRequest;
    }
}
