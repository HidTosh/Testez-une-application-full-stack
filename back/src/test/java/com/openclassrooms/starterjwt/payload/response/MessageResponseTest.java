package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MessageResponseTest {

    private MessageResponse messageResponse;

    @BeforeEach
    public void setUp() {
        messageResponse = new MessageResponse("you message");
    }
    @Test
    public void testGetSetMessage() {
        assertThat(messageResponse.getMessage()).isEqualTo("you message");

        messageResponse.setMessage("my new message");
        assertThat(messageResponse.getMessage()).isEqualTo("my new message");
    }
}
