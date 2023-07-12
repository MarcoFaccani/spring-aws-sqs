package com.marcofaccani.awssqs.channel.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcofaccani.awssqs.model.WireTransferMessage;
import com.marcofaccani.awssqs.service.interfaces.MailService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class AppSqsListener {

  public static final String MESSAGE_RECEIVED = "Received new message: %s";

  private final ObjectMapper objectMapper;
  private final MailService mailService;

  @SqsListener("${app.aws.sqs.queue-name}")
  @SneakyThrows
  public void listen(String message) {
    // here we should read the transactionId, verify if it's already present in the DB and if positive discard the message
    // to avoid consuming twice the same message

    final var payload = objectMapper.readValue(message, WireTransferMessage.class);
    mailService.sendMail(payload.recipientEmail());
  }

}
