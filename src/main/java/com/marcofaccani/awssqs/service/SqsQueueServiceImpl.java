package com.marcofaccani.awssqs.service;

import java.util.Map;
import java.util.Optional;

import com.marcofaccani.awssqs.config.props.AwsSqsConfigProperties;
import com.marcofaccani.awssqs.service.interfaces.SqsQueueService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;

@Service
@RequiredArgsConstructor
@Log4j2
public class SqsQueueServiceImpl implements SqsQueueService {

  private final AbstractApplicationContext abstractApplicationContext;
  private final AwsSqsConfigProperties awsSqsConfigProperties;
  private final SqsAsyncClient sqsAsyncClient;


  @PostConstruct
  void postConstruct() {
    abstractApplicationContext.registerShutdownHook();
    createQueueIfNotExists(awsSqsConfigProperties.getQueueName());
  }

  @PreDestroy
  private void deleteQueueOnShutdown() {
    log.debug("executing deleteQueueOnShutdown");
    try {
      final var queueUrlOptional = getQueueUrl(awsSqsConfigProperties.getQueueName());
      queueUrlOptional.ifPresent(s -> sqsAsyncClient.deleteQueue(request -> request.queueUrl(s)));
      abstractApplicationContext.close();
    } catch (Exception ex) {
      log.error("Error deleting queue on shutdown. Error message: {}", ex.getMessage());
    }
  }

  @Override
  public void createQueueIfNotExists(final String awsSqsQueueName) {
    if (getQueueUrl(awsSqsQueueName).isEmpty()) {
      try {
        log.info("Creating Queue with name: {}", awsSqsQueueName);

        // check out available attributes and their meaning at https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_CreateQueue.html
        final var queueAttributes = Map.of(QueueAttributeName.MESSAGE_RETENTION_PERIOD, "120");
        sqsAsyncClient.createQueue(request -> request
            .queueName(awsSqsQueueName)
            .attributes(queueAttributes)
        ).join();

        log.info("Queue {} successfully created", awsSqsQueueName);
      } catch (Exception ex) {
        final var errMsg = String.format(ERR_MSG_CREATING_QUEUE, awsSqsQueueName, ex.getMessage());
        throw new RuntimeException(errMsg);
      }
    }
    log.info("Queue {} already exists", awsSqsQueueName);
  }

  @Override
  public Optional<String> getQueueUrl(final String awsSqsQueueName) {
    try {
      final var queueUrl = sqsAsyncClient.getQueueUrl(request -> request.queueName(awsSqsQueueName)).join().queueUrl();
      return Optional.of(queueUrl);
    } catch (Exception ex) {
      if (ex.getCause() != null && ex.getCause() instanceof QueueDoesNotExistException) {
        final var logMessage = String.format(ERR_MSG_QUEUE_NOT_EXISTS, awsSqsQueueName);
        log.info(logMessage);
        return Optional.empty();
      }
      final var logMessage = String.format(ERR_MSG_GET_QUEUE_URL_GENERAL_EX, awsSqsQueueName, ex.getMessage());
      log.error(logMessage);
      return Optional.empty();
    }
  }

}
