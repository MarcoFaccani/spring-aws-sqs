package com.marcofaccani.awssqs.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.aws.sqs")
public class AwsSqsConfigProperties {

  private final String queueName;

}
