package com.marcofaccani.awssqs.integration;

import com.marcofaccani.awssqs.channel.inbound.AppSqsListener;
import com.marcofaccani.awssqs.integration.config.SqsLocalStackInitializer;
import com.marcofaccani.awssqs.model.WireTransferMessage;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static com.marcofaccani.awssqs.TestUtils.readFileAsObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SqsWriterServiceImplIT extends SqsLocalStackInitializer {

  @LocalServerPort
  private int appPort;

  /*
    Since this app writes and reads from the same queue, it is necessary to mock the listener so that it does not consume the message.
    Normally, an app does not read and write to the same queue, so it is not normally necessary to mock the listener
   */
  @MockBean
  AppSqsListener appSqsListener;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private SqsTemplate sqsTemplate;

  @Test
  @SneakyThrows
  void shouldWriteMessageToQueue() {
    // Assert queue is empty
    final var receivedMessage = sqsTemplate.receive(request -> request.queue(SqsLocalStackInitializer.QUEUE_NAME));
    assertTrue(receivedMessage.isEmpty());

    // Write message to the queue
    final var requestBody = readFileAsObject("json/sample-message.json", WireTransferMessage.class);
    final var response = testRestTemplate.postForEntity("http://localhost:" + appPort + "/sqs/send", requestBody, String.class);
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());

    // Retrieve message from Queue and assert the payload is as expected
    final var message = sqsTemplate.receive(request -> request.queue(SqsLocalStackInitializer.QUEUE_NAME));
    assertTrue(message.isPresent());
    assertEquals(requestBody, message.get().getPayload());
  }

}
