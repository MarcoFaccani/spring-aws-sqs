package com.marcofaccani.awssqs.channel.inbound;

import java.util.Optional;

import io.awspring.cloud.sqs.listener.interceptor.MessageInterceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class SqsInterceptor implements MessageInterceptor<Object> {

  public static final String MSG_SUCCESS = "Message processed successfully";
  public static final String MSG_RECEIVED = "New message received: %s";
  public static final String ERR_MSG = "Error processing message with id %s. Error message: %s";

  @Override
  public Message<Object> intercept(Message<Object> message) {
    final var logMsg = String.format(MSG_RECEIVED, message);
    log.debug(logMsg);
    return message;
  }

  @Override
  public void afterProcessing(Message<Object> message, @Nullable Throwable t) {
    if (t == null) {
      log.info(MSG_SUCCESS);
    }
    else {
      final var errMsg = String.format(ERR_MSG, message.getHeaders().getId(), getRootCause(t).getMessage());
      log.error(errMsg);
    }
  }

  private Throwable getRootCause(Throwable throwable) {
    return Optional.ofNullable(throwable)
        .map(Throwable::getCause)
        .map(this::getRootCause)
        .orElse(throwable);
  }


}