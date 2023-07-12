package com.marcofaccani.awssqs.channel.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcofaccani.awssqs.config.props.AwsSqsConfigProperties;
import com.marcofaccani.awssqs.model.WireTransferMessage;
import com.marcofaccani.awssqs.service.interfaces.SqsWriterService;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.marcofaccani.awssqs.util.CodeUtils.executeWithHandling;

@Service
@RequiredArgsConstructor
@Log4j2
public class SqsWriterServiceImpl implements SqsWriterService {
  private final AwsSqsConfigProperties awsSqsConfigProperties;
  private final SqsTemplate sqsTemplate;
  private final ObjectMapper objectMapper;


  @SneakyThrows
  @Override
  public void sendMessage(WireTransferMessage message) {
    var stringPayload = objectMapper.writeValueAsString(message);
    log.debug("Outbound Message: {}", stringPayload);

    executeWithHandling(() ->
        sqsTemplate.send(to -> to
            .queue(awsSqsConfigProperties.getQueueName())
            .payload(stringPayload)
            .delaySeconds(1)),
        ERR_MSG_SEND_MESSAGE_TO_SQS
    );
    log.info("Message sent successfully");

  }

}
