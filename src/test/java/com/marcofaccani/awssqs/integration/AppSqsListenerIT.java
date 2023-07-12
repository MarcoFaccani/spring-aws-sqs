package com.marcofaccani.awssqs.integration;

import com.marcofaccani.awssqs.channel.inbound.AppSqsListener;
import com.marcofaccani.awssqs.integration.config.SqsLocalStackInitializer;
import com.marcofaccani.awssqs.model.WireTransferMessage;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static com.marcofaccani.awssqs.TestUtils.readFileAsObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class AppSqsListenerIT extends SqsLocalStackInitializer {

  @Autowired
  private SqsTemplate sqsTemplate;

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void shouldConsumeMessageFromQueue(CapturedOutput logCapturer) {
    final var message = readFileAsObject("json/sample-message.json", WireTransferMessage.class);

    assertDoesNotThrow(() -> sqsTemplate.send(to -> to.queue(QUEUE_NAME).payload(message)));

    final var expectedLogMsg = String.format(AppSqsListener.MESSAGE_RECEIVED, message);
    assertThat(logCapturer).contains(expectedLogMsg);
  }

}
