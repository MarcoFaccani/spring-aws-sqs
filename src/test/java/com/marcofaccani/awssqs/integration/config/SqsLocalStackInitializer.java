package com.marcofaccani.awssqs.integration.config;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@Testcontainers
public abstract class SqsLocalStackInitializer {

  public static final String QUEUE_NAME = "DummyQueueName";

  @Container
  static LocalStackContainer localStack;

  static {
    final var dockerImageName = DockerImageName.parse("localstack/localstack:2.1");
    localStack = new LocalStackContainer(dockerImageName).withServices(SQS).withReuse(true);
    localStack.start();
  }

  @DynamicPropertySource
  static void overrideConfiguration(DynamicPropertyRegistry registry) {
    registry.add("app.aws.sqs.queue-name", () -> QUEUE_NAME);
    registry.add("spring.cloud.aws.endpoint", () -> localStack.getEndpointOverride(SQS));
    registry.add("spring.cloud.aws.region.static", localStack::getRegion);
    registry.add("spring.cloud.aws.credentials.access-key", localStack::getAccessKey);
    registry.add("spring.cloud.aws.credentials.secret-key", localStack::getSecretKey);
  }

  @BeforeAll
  static void beforeAll() throws IOException, InterruptedException {
    // otherwise can be done by using sqsAsyncClient.createQueue()
    localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", QUEUE_NAME);
  }
}