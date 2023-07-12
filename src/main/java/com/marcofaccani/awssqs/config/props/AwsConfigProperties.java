package com.marcofaccani.awssqs.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.cloud.aws")
public class AwsConfigProperties {

  private final AwsSqsConfigProperties awsSqsConfigProps;
  private final AwsSesConfigProperties awsSesConfigProps;

}
